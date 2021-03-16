/*
 * Copyright (c) 2021. - Lebogang Bantsijang
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lebogang.genesis.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.ActivityPlaylistViewBinding
import com.lebogang.genesis.service.ManageServiceConnection
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.service.utils.PlaybackState
import com.lebogang.genesis.service.utils.RepeatSate
import com.lebogang.genesis.service.utils.ShuffleSate
import com.lebogang.genesis.ui.adapters.ItemPlaylistSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnPlaylistAudioClickListener
import com.lebogang.genesis.ui.dialogs.QueueDialog
import com.lebogang.genesis.ui.helpers.PlayerHelper
import com.lebogang.genesis.ui.helpers.ThemeHelper
import com.lebogang.genesis.utils.GlobalGlide
import com.lebogang.genesis.viewmodels.AudioViewModel
import com.lebogang.genesis.viewmodels.PlaylistViewModel

class PlaylistViewActivity : ThemeHelper() ,OnPlaylistAudioClickListener , PlayerHelper {
    private val viewBinding:ActivityPlaylistViewBinding by lazy {
        ActivityPlaylistViewBinding.inflate(layoutInflater)
    }
    private val playlistViewModel:PlaylistViewModel by lazy {
        PlaylistViewModel.Factory((application as GenesisApplication).playlistRepo)
            .create(PlaylistViewModel::class.java)
    }
    private val audioViewModel:AudioViewModel by lazy {
        AudioViewModel.Factory((application as GenesisApplication).audioRepo)
                .create(AudioViewModel::class.java)
    }
    private val adapter = ItemPlaylistSongAdapter().apply {
        listener = this@PlaylistViewActivity
    }
    private var playlistId:Long = -1
    private lateinit var musicService: MusicService

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ManageServiceConnection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        playlistId = intent.getLongExtra("Playlist", 0)
        initToolbar()
        initRecyclerView()
        initOtherView()
        observePlaylist()
        observeAudioData()
        observeAudio()
    }

    private fun initRecyclerView(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.playlist_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.clearAllMenu ->{
                playlistViewModel.clearAudioData(playlistId)
                playlistViewModel.getPlaylistAudio(playlistId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initOtherView(){
        viewBinding.launcherView.root.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        viewBinding.launcherView.queueView.setOnClickListener {
            QueueDialog().show(supportFragmentManager,"") }
        viewBinding.launcherView.playPauseView.setOnClickListener {
            musicService.togglePlayPause()
        }
    }

    private fun observePlaylist(){
        playlistViewModel.getPlaylists(playlistId)
        playlistViewModel.livePlaylist.observe(this, {
            observePlaylistAudio(it.id)
            title = it.title
        })
    }

    private fun observePlaylistAudio(id:Long){
        playlistViewModel.getPlaylistAudio(id).observe(this, {
            audioViewModel.getAudio(it)
        })
    }

    private fun observeAudioData(){
        audioViewModel.liveData.observe(this, {
            adapter.setAudioData(it)
            loadingView(it.size > 0)
        })
    }

    private fun observeAudio(){
        Queue.currentAudio.observe(this, {
            if (viewBinding.launcherView.root.visibility == View.GONE)
                viewBinding.launcherView.root.visibility = View.VISIBLE
            viewBinding.launcherView.titleView.text = it.title
            viewBinding.launcherView.subtitleView.text = it.artist
            GlobalGlide.loadAudioCover(this,viewBinding.launcherView.imageView, it.albumArtUri)
            adapter.setNowPlaying(it.id)
        })
    }

    private fun loadingView(hasContent:Boolean){
        viewBinding.progressBar.visibility = View.GONE
        if (hasContent){
            viewBinding.noContentView.text = null
        }else{
            viewBinding.noContentView.text = getString(R.string.no_content)
        }
    }

    override fun onAudioClick(audio: Audio) {
        Queue.setCurrentAudio(audio,adapter.listAudio)
        musicService.play(audio)
    }

    override fun onAudioDeleteClick(audio: Audio) {
        playlistViewModel.deletePlaylistAudio(playlistId, audio.id)
        playlistViewModel.getPlaylistAudio(playlistId)
    }

    override fun onPlaybackChanged(playbackState: PlaybackState){
        if (playbackState == PlaybackState.PLAYING)
            viewBinding.launcherView.playPauseView.setImageResource(R.drawable.ic_pause)
        else
            viewBinding.launcherView.playPauseView.setImageResource(R.drawable.ic_play)
    }

    override fun onRepeatModeChange(repeatSate: RepeatSate) {
        //not needed
    }

    override fun onShuffleModeChange(shuffleSate: ShuffleSate) {
        //not needed
    }

    override fun onServiceReady(musicService: MusicService) {
        this.musicService = musicService
    }

}
