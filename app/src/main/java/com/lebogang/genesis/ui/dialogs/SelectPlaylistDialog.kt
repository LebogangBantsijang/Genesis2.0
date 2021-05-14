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

package com.lebogang.genesis.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.DialogAddAudioToPlaylistBinding
import com.lebogang.genesis.room.models.Playlist
import com.lebogang.genesis.ui.adapters.ItemSelectPlaylistAdapter
import com.lebogang.genesis.ui.adapters.utils.OnSelectPlaylistListener
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.PlaylistViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class SelectPlaylistDialog:DialogFragment(),OnSelectPlaylistListener {
    private val viewBinding:DialogAddAudioToPlaylistBinding by lazy {
        DialogAddAudioToPlaylistBinding.inflate(layoutInflater) }
    private val playlistViewModel:PlaylistViewModel by lazy { ViewModelFactory(requireActivity().application)
            .getPlaylistViewModel() }
    private val adapter = ItemSelectPlaylistAdapter().apply {
        listener = this@SelectPlaylistDialog }
    private lateinit var audio:Audio

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View {
        audio = requireArguments().getParcelable(Keys.SONG_KEY)!!
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
        playlistViewModel.liveData.observe(viewLifecycleOwner, { adapter.setData(it) })
    }

    override fun onPlaylistClick(playlist: Playlist) {
        playlistViewModel.insertPlaylistAudio(playlist.id, audio.id)
        dismiss()
    }
}
