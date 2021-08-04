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

package com.lebogang.vibe.ui.stream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.lebogang.vibe.R
import com.lebogang.vibe.databinding.ActivityStreamBinding
import com.lebogang.vibe.ui.SettingsActivity
import com.lebogang.vibe.ui.charts.ChartsActivity
import com.lebogang.vibe.ui.local.HomeActivity
import com.lebogang.vibe.ui.stream.adapters.AlbumAdapter
import com.lebogang.vibe.ui.stream.adapters.ArtistAdapter
import com.lebogang.vibe.ui.utils.ImageLoader
import com.lebogang.vibe.ui.utils.ItemClickInterface
import com.lebogang.vibe.ui.utils.Type

class StreamActivity : AppCompatActivity() {
    private val bind:ActivityStreamBinding by lazy{ ActivityStreamBinding.inflate(layoutInflater)}
    private val imageLoader:ImageLoader by lazy { ImageLoader(this) }
    private val albumAdapter = AlbumAdapter()
    private val artistAdapter = ArtistAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        title = getString(R.string.app_name)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar)
        initBottomNavigation()
    }

    private fun initAlbumRecyclerView(){
        albumAdapter.imageLoader = imageLoader
        albumAdapter.itemClickInterface = getItemClickInterface()
        bind.recyclerViewAlbums.layoutManager = LinearLayoutManager(this
            ,LinearLayoutManager.HORIZONTAL, false)
        bind.recyclerViewAlbums.adapter = albumAdapter
    }

    private fun getItemClickInterface() = object:ItemClickInterface{
        override fun onItemClick(view: View, item: Any?, type: Type) {
            TODO("Not yet implemented")
        }
    }

    private fun initBottomNavigation(){
        bind.bottomNavigation.selectedItemId = R.id.stream
        bind.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.charts -> startActivity(Intent(this, ChartsActivity::class.java))
                R.id.settings-> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.local -> startActivity(Intent(this, HomeActivity::class.java))
            }
            false
        }
    }



    private fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null
}
