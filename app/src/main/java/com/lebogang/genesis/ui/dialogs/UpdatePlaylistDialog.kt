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
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.databinding.DialogUpdatePlaylistBinding
import com.lebogang.genesis.room.models.Playlist
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.utils.Validator
import com.lebogang.genesis.viewmodels.PlaylistViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class UpdatePlaylistDialog:DialogFragment() {
    private val viewBinding:DialogUpdatePlaylistBinding by lazy {DialogUpdatePlaylistBinding.inflate(layoutInflater)}
    private val playlistViewModel:PlaylistViewModel by lazy { ViewModelFactory(requireActivity().application)
            .getPlaylistViewModel() }
    private lateinit var playlist: Playlist

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        playlist = requireArguments().getParcelable(Keys.PLAYLIST_KEY)!!
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSaveView()
        viewBinding.playlistNameView.setText(playlist.title)
    }

    private fun initSaveView(){
        viewBinding.saveView.setOnClickListener {
            val name = viewBinding.playlistNameView.text.toString()
            if (Validator.isValueValid(name)){
                playlist.title = name
                playlistViewModel.insertPlaylist(playlist)
                dismiss()
            }
        }
    }
}
