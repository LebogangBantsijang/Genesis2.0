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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ActivityPlaylistViewBinding
import com.lebogang.kxgenesis.room.models.Playlist
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.adapters.ItemPlaylistSongAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnPlaylistAudioClickListener
import com.lebogang.kxgenesis.viewmodels.AudioViewModel
import com.lebogang.kxgenesis.viewmodels.PlaylistViewModel

class PlaylistViewActivity : AppCompatActivity(),OnPlaylistAudioClickListener {
    private lateinit var viewBinding:ActivityPlaylistViewBinding
    private val playlistViewModel:PlaylistViewModel by lazy {
        PlaylistViewModel.Factory((application as GenesisApplication).playlistRepo)
            .create(PlaylistViewModel::class.java)
    }
    private val audioViewModel:AudioViewModel by lazy {
        AudioViewModel.Factory((application as GenesisApplication).audioRepo)
                .create(AudioViewModel::class.java)
    }
    private val themeSettings: ThemeSettings by lazy{
        ThemeSettings(this)
    }
    private val adapter = ItemPlaylistSongAdapter()
    private var playlistId:Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlaylistViewBinding.inflate(layoutInflater)
        setTheme(themeSettings.getThemeResource())
        setContentView(viewBinding.root)
        playlistId = intent.getLongExtra("Playlist", 0)
        initToolbar()
        initRecyclerView()
        observePlaylist()
        observeAudioData()
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.itemAnimator?.addDuration = 450
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

    private fun loadingView(hasContent:Boolean){
        viewBinding.progressBar.visibility = View.GONE
        if (hasContent){
            viewBinding.noContentView.text = null
        }else{
            viewBinding.noContentView.text = getString(R.string.no_content)
        }
    }

    override fun onAudioClick(audio: Audio) {
        //play audio
    }

    override fun onAudioDeleteClick(audio: Audio) {
        playlistViewModel.deletePlaylistAudio(playlistId, audio.id)
        playlistViewModel.getPlaylistAudio(playlistId)
    }

}
