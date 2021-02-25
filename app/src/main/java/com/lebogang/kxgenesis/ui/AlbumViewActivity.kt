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

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Album
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ActivityAlbumViewBinding
import com.lebogang.kxgenesis.ui.adapters.ItemAlbumSongAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.ui.dialogs.AudioOptionsDialog
import com.lebogang.kxgenesis.utils.GlobalGlide
import com.lebogang.kxgenesis.viewmodels.AlbumViewModel
import com.lebogang.kxgenesis.viewmodels.AudioViewModel
import jp.wasabeef.blurry.Blurry

class AlbumViewActivity : AppCompatActivity(),OnAudioClickListener{

    private val viewBinding:ActivityAlbumViewBinding by lazy{
        ActivityAlbumViewBinding.inflate(layoutInflater)
    }
    private val albumViewModel:AlbumViewModel by lazy{
        AlbumViewModel.Factory((application as GenesisApplication).albumRepo)
                .create(AlbumViewModel::class.java)
    }
    private val audioViewModel:AudioViewModel by lazy {
        AudioViewModel.Factory((application as GenesisApplication).audioRepo)
                .create(AudioViewModel::class.java)
    }
    private var album:Album? = null
    private val adapter = ItemAlbumSongAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        album = albumViewModel.getAlbums(intent.getStringExtra("Album")!!)
        initToolbar()
        iniAlbumDetails()
        initRecyclerView()
        observeAudio()
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener {onBackPressed()}
    }

    private fun iniAlbumDetails(){
        album?.let {
            viewBinding.titleView.text = album!!.title
            title = album!!.artist
            Glide.with(this)
                .asBitmap()
                .load(album!!.albumArtUri)
                    .addListener(object :RequestListener<Bitmap>{
                        override fun onLoadFailed(e: GlideException?, model: Any?
                                                  , target: Target<Bitmap>?, isFirstResource
                                                  : Boolean): Boolean {
                            return e != null
                        }
                        override fun onResourceReady(resource: Bitmap?, model: Any?,
                                                     target: Target<Bitmap>?, dataSource: DataSource?
                                                     , isFirstResource: Boolean): Boolean {
                            if (resource!=null)
                                Blurry.with(baseContext).async()
                                        .radius(10)
                                        .sampling(4)
                                        .from(resource)
                                        .into(viewBinding.blurView)
                            return true
                        }
                    })
                    .submit()
            GlobalGlide.loadAlbumCover(viewBinding.root, viewBinding.artView,album?.albumArtUri)
        }
    }

    private fun initRecyclerView(){
        adapter.listener = this
        adapter.fallbackPrimaryTextColor = ResourcesCompat.getColor(resources, R.color.primaryTextColor, null)
        adapter.fallbackSecondaryTextColor = ResourcesCompat.getColor(resources, R.color.secondaryTextColor, null)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.itemAnimator?.addDuration = 450
        viewBinding.recyclerView.adapter = adapter
    }

    private fun observeAudio(){
        album?.let {
            audioViewModel.getAlbumAudio(it.title)
            audioViewModel.liveData.observe(this,{list->
                adapter.setAudioData(list)
            })
        }
    }

    override fun onAudioClick(audio: Audio) {
        //Not
    }

    override fun onAudioClickOptions(audio: Audio) {
        AudioOptionsDialog(audio).show(supportFragmentManager, "")
    }
}
