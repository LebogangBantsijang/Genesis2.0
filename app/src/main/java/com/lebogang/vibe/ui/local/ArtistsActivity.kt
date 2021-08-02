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

package com.lebogang.vibe.ui.local

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Artist
import com.lebogang.vibe.databinding.ActivityArtistsBinding
import com.lebogang.vibe.ui.ImageLoader
import com.lebogang.vibe.ui.ItemClickInterface
import com.lebogang.vibe.ui.ItemOptionsInterface
import com.lebogang.vibe.ui.Type
import com.lebogang.vibe.ui.local.adapters.ArtistAdapter
import com.lebogang.vibe.ui.local.viewmodel.ArtistViewModel
import com.lebogang.vibe.ui.ModelFactory
import com.lebogang.vibe.utils.Keys

class ArtistsActivity : AppCompatActivity() {
    private val app:GApplication by lazy { application as GApplication }
    private val bind:ActivityArtistsBinding by lazy { ActivityArtistsBinding.inflate(layoutInflater) }
    private val artistViewModel:ArtistViewModel by lazy { ModelFactory(app).getArtistViewModel() }
    private val adapter = ArtistAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.artists)
        setContentView(bind.root)
        initToolbar()
        initRecyclerView()
        artistViewModel.registerObserver(app)
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView(){
        artistViewModel.getArtists().observe(this,{
            adapter.setData(it)
            bind.progressBar.visibility = View.GONE
        })
        adapter.imageLoader = ImageLoader(this)
        adapter.itemOptionsInterface = getFavouriteInterface()
        adapter.itemClickInterface = getItemClick()
        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = adapter
    }

    private fun getFavouriteInterface() = object :ItemOptionsInterface{
        override fun onOptionsClick(item: Any) {
            val artist = item as Artist
            artist.isFavourite = !artist.isFavourite
            artistViewModel.addArtist(artist)
        }
    }

    private fun getItemClick() = object :ItemClickInterface{
        override fun onItemClick(view: View, item: Any?, type: Type) {
            val artist = item as Artist
            val intent = Intent(this@ArtistsActivity,ArtistDetailsActivity::class.java)
            intent.putExtra(Keys.ARTIST_KEY,artist.id)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.application_search_refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.refresh->{
                bind.progressBar.visibility = View.VISIBLE
                artistViewModel.localContent.reset()
                true
            }
            else->false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        artistViewModel.unregisterObserver(app)
    }

}
