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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.DialogAudioOptionsBinding
import com.lebogang.genesis.utils.GlobalGlide
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.AlbumViewModel
import com.lebogang.genesis.viewmodels.ArtistViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class AudioOptionsDialog :BottomSheetDialogFragment(){
    private val viewBinding:DialogAudioOptionsBinding by lazy { DialogAudioOptionsBinding.inflate(layoutInflater) }
    private lateinit var audio:Audio
    private val viewModelAlbum:AlbumViewModel by lazy{ViewModelFactory(requireActivity().application)
            .getAlbumViewModel()}
    private val viewModelArtist:ArtistViewModel by lazy { ViewModelFactory(requireActivity().application)
            .getArtistViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        audio = requireArguments().getParcelable(Keys.SONG_KEY)!!
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){
        GlobalGlide.loadAudioCover(viewBinding.root, viewBinding.coverView, audio.getArtUri())
        viewBinding.titleView.text = audio.title
        viewBinding.subtitleView.text = audio.artist
        viewBinding.addToListView.setOnClickListener {
            val bundle = Bundle().apply { putParcelable(Keys.SONG_KEY, audio) }
            val controller = findNavController()
            controller.navigate(R.id.selectPlaylistDialog, bundle)
        }
        viewBinding.artistView.setOnClickListener {
            val artist = viewModelArtist.getArtists(audio.artist)
            val bundle = Bundle().apply { putParcelable(Keys.ARTIST_KEY, artist) }
            val controller = findNavController()
            controller.navigate(R.id.viewArtistFragment, bundle)
        }
        viewBinding.albumView.setOnClickListener {
            val album = viewModelAlbum.getAlbums(audio.album)
            val bundle = Bundle().apply { putParcelable(Keys.ALBUM_KEY, album) }
            val controller = findNavController()
            controller.navigate(R.id.viewAlbumFragment, bundle)
        }
        if(requireArguments().getBoolean(Keys.ENABLE_UPDATE_KEY))
            viewBinding.editView.setOnClickListener {
                val bundle = Bundle().apply { putParcelable(Keys.SONG_KEY, audio) }
                val controller = findNavController()
                controller.navigate(R.id.editAudioFragment, bundle)
            }
        else
            viewBinding.editView.visibility = View.GONE

        viewBinding.infoView.setOnClickListener {
            val bundle = Bundle().apply { putParcelable(Keys.SONG_KEY, audio) }
            val controller = findNavController()
            controller.navigate(R.id.infoAudioFragment, bundle)
        }
        viewBinding.shareView.setOnClickListener {

        }
    }

}
