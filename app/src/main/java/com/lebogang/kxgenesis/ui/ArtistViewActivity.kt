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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Artist
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ActivityArtistViewBinding
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.adapters.ItemSongAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.ui.dialogs.AudioOptionsDialog
import com.lebogang.kxgenesis.utils.GlobalGlide
import com.lebogang.kxgenesis.viewmodels.ArtistViewModel
import com.lebogang.kxgenesis.viewmodels.AudioViewModel

class ArtistViewActivity : AppCompatActivity(), OnAudioClickListener {
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
    private val themeSettings: ThemeSettings by lazy{
        ThemeSettings(this)
    }
    private val adapter = ItemSongAdapter()
    private var artist:Artist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(themeSettings.getThemeResource())
        setContentView(viewBinding.root)
        artist = artistViewModel.getArtists(intent.getStringExtra("Artist")!!)
        initToolbar()
        initArtistDetails()
        initRecyclerView()
        observeData()
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initArtistDetails(){
        viewBinding.titleView.text = artist?.title
        viewBinding.subtitleView.text = artist?.albumCount
        GlobalGlide.loadArtistCover(viewBinding.root,viewBinding.artView, artist?.coverUri)
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.itemAnimator?.addDuration = 450
        viewBinding.recyclerView.adapter = adapter
    }

    private fun observeData(){
        artist?.let {
            audioViewModel.getArtistAudio(it.title)
            audioViewModel.liveData.observe(this, {list->
                adapter.setAudioData(list)
            })
        }
    }

    override fun onAudioClick(audio: Audio) {
        //not
    }

    override fun onAudioClickOptions(audio: Audio) {
        AudioOptionsDialog(audio, false).show(supportFragmentManager, "")
    }
}
