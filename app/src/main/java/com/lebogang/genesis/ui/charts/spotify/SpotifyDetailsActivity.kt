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

package com.lebogang.genesis.ui.charts.spotify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.ActivitySpotifyDetailsBinding
import com.lebogang.genesis.online.spotify.models.Album
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.ModelFactory
import com.lebogang.genesis.ui.Type
import com.lebogang.genesis.ui.charts.spotify.adapters.MusicAdapter
import com.lebogang.genesis.ui.charts.viewmodels.SpotifyViewModel
import com.lebogang.genesis.utils.Keys

class SpotifyDetailsActivity : AppCompatActivity() {
    private val bind:ActivitySpotifyDetailsBinding by lazy{
        ActivitySpotifyDetailsBinding.inflate(layoutInflater)}
    private val app: GenesisApplication by lazy{application as GenesisApplication }
    private val spotifyViewModel: SpotifyViewModel by lazy{ ModelFactory(app).getSpotifyViewModel()}
    private var album:Album? = null
    private val adapter = MusicAdapter()
    private val imageLoader:ImageLoader by lazy{ImageLoader(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        album = intent.getParcelableExtra(Keys.ALBUM_KEY)
        setContentView(bind.root)
        initToolbar()
        initAlbums()
        initRecyclerView()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initAlbums(){
        album?.let{
            var names = ""
            it.artists.forEachIndexed{index, artist ->
                if (index == (it.artists.size-1)){
                    names += artist.title
                    return@forEachIndexed
                }
                names += artist.title + ", "
            }
            bind.artistTextView.text = names
            bind.titleTextView.text = it.title
            bind.dateTextView.text = (getString(R.string.release_date_) + it.date)
            bind.countTextView.text = (getString(R.string.tracks_) + it.numberOfTracks)
            imageLoader.loadImage(it.images.first().url,Type.ALBUM,bind.imageView)
            adapter.artUrl = it.images.last().url
        }
    }

    private fun initRecyclerView(){
        adapter.imageLoader = imageLoader
        adapter.itemClickInterface = getItemClickInterface()
        spotifyViewModel.musicLiveData.observe(this,{
            adapter.setData(it)
            bind.progressBar.visibility = View.GONE
        })
        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = adapter
    }

    private fun getItemClickInterface() = object :ItemClickInterface{
        override fun onItemClick(view: View, item: Any?, type: Type) {
        }
    }

    override fun onResume() {
        super.onResume()
        spotifyViewModel.getAlbumMusic(album!!.id)
    }
}
