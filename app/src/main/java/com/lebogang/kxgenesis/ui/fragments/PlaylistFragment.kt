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

package com.lebogang.kxgenesis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.FragmentPlaylistBinding
import com.lebogang.kxgenesis.room.models.Playlist
import com.lebogang.kxgenesis.ui.adapters.ItemLocalPlaylist
import com.lebogang.kxgenesis.ui.adapters.utils.OnPlaylistClickListener
import com.lebogang.kxgenesis.ui.dialogs.AddPlaylistDialog
import com.lebogang.kxgenesis.ui.dialogs.UpdatePlaylistDialog
import com.lebogang.kxgenesis.viewmodels.PlaylistViewModel

class PlaylistFragment: Fragment(),OnPlaylistClickListener {

    private lateinit var viewBinding:FragmentPlaylistBinding
    private val playlistViewModel:PlaylistViewModel  by lazy{
        PlaylistViewModel.Factory((activity?.application as GenesisApplication).playlistRepo)
                .create(PlaylistViewModel::class.java)
    }
    private val adapter = ItemLocalPlaylist()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observePlaylists()
        initAddView()
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun initAddView(){
        viewBinding.addView.setOnClickListener {
            AddPlaylistDialog().show(fragmentManager!!, "")
        }
    }

    private fun observePlaylists(){
        playlistViewModel.getPlaylists().observe(viewLifecycleOwner,{
            adapter.setPlaylistData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.playlists)
    }

    override fun onPlaylistClick(playlist: Playlist) {
        //em
    }

    override fun onPlaylistEditClick(playlist: Playlist) {
        UpdatePlaylistDialog(playlist).show(fragmentManager!!, "")
    }

    override fun onPlaylistDeleteClick(playlist: Playlist) {
        playlistViewModel.deletePlaylist(playlist)
    }

}
