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

package com.lebogang.kxgenesis.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Artist
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ActivityArtistViewBinding
import com.lebogang.kxgenesis.service.ManageServiceConnection
import com.lebogang.kxgenesis.service.Queue
import com.lebogang.kxgenesis.service.utils.PlaybackState
import com.lebogang.kxgenesis.service.utils.RepeatSate
import com.lebogang.kxgenesis.service.utils.ShuffleSate
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.adapters.ItemSongAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.ui.dialogs.AudioOptionsDialog
import com.lebogang.kxgenesis.ui.dialogs.QueueDialog
import com.lebogang.kxgenesis.ui.helpers.PlayerHelper
import com.lebogang.kxgenesis.ui.helpers.ThemeHelper
import com.lebogang.kxgenesis.utils.GlobalGlide
import com.lebogang.kxgenesis.viewmodels.ArtistViewModel
import com.lebogang.kxgenesis.viewmodels.AudioViewModel

class ArtistViewActivity : ThemeHelper(), OnAudioClickListener , PlayerHelper {
    private val viewBinding: ActivityArtistViewBinding by lazy{
        ActivityArtistViewBinding.inflate(layoutInflater)
    }
    private val audioViewModel:AudioViewModel by lazy {
        AudioViewModel.Factory((application as GenesisApplication).audioRepo)
            .create(AudioViewModel::class.java)
    }
    private val artistViewModel:ArtistViewModel by lazy {
        ArtistViewModel.Factory((application as GenesisApplication).artistRepo)
            .create(ArtistViewModel::class.java)
    }
    private val adapter = ItemSongAdapter()
    private var artist:Artist? = null
    private lateinit var manageServiceConnection: ManageServiceConnection

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        manageServiceConnection = ManageServiceConnection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        artist = artistViewModel.getArtists(intent.getStringExtra("Artist")!!)
        initToolbar()
        initArtistDetails()
        initRecyclerView()
        initOtherView()
        observeAudioData()
        observeAudio()
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initArtistDetails(){
        viewBinding.titleView.text = artist?.title
        title = artist?.title
        GlobalGlide.loadArtistCover(this,viewBinding.artView, artist?.coverUri)
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.itemAnimator?.addDuration = 450
        viewBinding.recyclerView.adapter = adapter
    }

    private fun initOtherView(){
        viewBinding.launcherView.root.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        viewBinding.launcherView.queueView.setOnClickListener {
            QueueDialog().show(supportFragmentManager,"") }
        viewBinding.launcherView.playPauseView.setOnClickListener {
            //not finished
        }
    }

    private fun observeAudioData(){
        artist?.let {
            audioViewModel.getArtistAudio(it.title)
            audioViewModel.liveData.observe(this, {list->
                adapter.setAudioData(list)
            })
        }
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

    override fun onAudioClick(audio: Audio) {
        Queue.setCurrentAudio(audio, adapter.getList())
        manageServiceConnection.musicService.play(audio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        AudioOptionsDialog(audio, false).show(supportFragmentManager, "")
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
}
