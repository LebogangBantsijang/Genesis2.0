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

package com.lebogang.vibe.ui.charts.spotify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.databinding.ActivitySpotifyBinding
import com.lebogang.vibe.online.spotify.models.Album
import com.lebogang.vibe.ui.ImageLoader
import com.lebogang.vibe.ui.ItemClickInterface
import com.lebogang.vibe.ui.Type
import com.lebogang.vibe.ui.charts.spotify.adapters.AlbumAdapter
import com.lebogang.vibe.ui.ModelFactory
import com.lebogang.vibe.ui.charts.viewmodels.SpotifyViewModel
import com.lebogang.vibe.utils.Keys

class SpotifyActivity : AppCompatActivity() {
    private val bind:ActivitySpotifyBinding by lazy{ActivitySpotifyBinding.inflate(layoutInflater)}
    private val app:GApplication by lazy{application as GApplication}
    private val spotifyViewModel: SpotifyViewModel by lazy{ModelFactory(app).getSpotifyViewModel()}
    private val adapter = AlbumAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView(){
        adapter.imageLoader = ImageLoader(this)
        adapter.itemClickInterface = getItemClickInterface()
        spotifyViewModel.albumLiveData.observe(this,{
            adapter.setData(it)
            bind.progressBar.visibility = View.GONE
        })
        bind.recyclerView.layoutManager = StaggeredGridLayoutManager(
            2, StaggeredGridLayoutManager.VERTICAL
        )
        bind.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        spotifyViewModel.getAlbums()
    }

    private fun getItemClickInterface(): ItemClickInterface = object :ItemClickInterface{
        override fun onItemClick(view: View, item: Any?, type: Type) {
            val album = item as Album
            val intent = Intent(this@SpotifyActivity, SpotifyDetailsActivity::class.java)
            intent.putExtra(Keys.ALBUM_KEY,album)
            startActivity(intent)
        }
    }
}
