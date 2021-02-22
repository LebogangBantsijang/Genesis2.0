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
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.ActivityPlaylistViewBinding
import com.lebogang.kxgenesis.room.models.Playlist
import com.lebogang.kxgenesis.viewmodels.PlaylistViewModel

class PlaylistViewActivity : AppCompatActivity() {
    private lateinit var viewBinding:ActivityPlaylistViewBinding
    private val playlistViewModel:PlaylistViewModel by lazy {
        PlaylistViewModel.Factory((application as GenesisApplication).playlistRepo)
            .create(PlaylistViewModel::class.java)
    }
    private var playlist:Playlist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlaylistViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        //playlist = playlistViewModel.getPlaylists(intent.getLongExtra("Playlist",0))
    }

    private fun initRecyclerView(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

}
