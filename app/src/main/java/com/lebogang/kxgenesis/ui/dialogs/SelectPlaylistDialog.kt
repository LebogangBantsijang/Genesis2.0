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

package com.lebogang.kxgenesis.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.DialogAddAudioToPlaylistBinding
import com.lebogang.kxgenesis.room.models.Playlist
import com.lebogang.kxgenesis.ui.adapters.ItemSelectPlaylistAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnSelectPlaylistListener
import com.lebogang.kxgenesis.viewmodels.PlaylistViewModel

class SelectPlaylistDialog(private val audio:Audio):DialogFragment(),OnSelectPlaylistListener {
    private lateinit var viewBinding:DialogAddAudioToPlaylistBinding
    private val playlistViewModel:PlaylistViewModel by lazy {
        PlaylistViewModel.Factory((activity?.application as GenesisApplication).playlistRepo)
                .create(PlaylistViewModel::class.java)
    }
    private val adapter = ItemSelectPlaylistAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View {
        viewBinding = DialogAddAudioToPlaylistBinding.inflate(inflater, container , false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCancelView()
        initListView()
        observeData()
    }

    private fun initCancelView(){
        viewBinding.cancelView.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    private fun initListView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun observeData(){
        playlistViewModel.liveData.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }

    override fun onPlaylistClick(playlist: Playlist) {
        playlistViewModel.insertPlaylistAudio(playlist.id, audio.id)
        dismissAllowingStateLoss()
    }
}
