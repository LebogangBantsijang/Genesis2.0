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

package com.lebogang.genesis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Artist
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.FragmentArtistViewBinding
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.ui.MainActivity
import com.lebogang.genesis.ui.adapters.ItemAlbumArtistSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.genesis.ui.dialogs.AudioOptionsDialog
import com.lebogang.genesis.utils.GlobalGlide
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.ArtistViewModel
import com.lebogang.genesis.viewmodels.AudioViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class ViewArtistFragment:Fragment(), OnAudioClickListener {
    private val viewBinding:FragmentArtistViewBinding by lazy{FragmentArtistViewBinding.inflate(layoutInflater)}
    private val viewModelAudio:AudioViewModel by lazy {ViewModelFactory(requireActivity().application).getAudioViewModel()}
    private val adapter = ItemAlbumArtistSongAdapter().apply { listener = this@ViewArtistFragment }
    private lateinit var artist:Artist

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        artist = requireArguments().getParcelable(Keys.ARTIST_KEY)!!
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalGlide.loadArtistCover(this,viewBinding.artView, artist.getArtUri())
        viewBinding.titleView.text = artist.title
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = adapter
        initObservers()
    }

    private fun initObservers(){
        viewModelAudio.getArtistAudio(artist.title)
        viewModelAudio.liveData.observe(viewLifecycleOwner, {list->
            adapter.setAudioData(list)
            viewBinding.subtitleView.text = list.size.toString()
            loadingView(list.isNotEmpty())
        })
        Queue.currentAudio.observe(viewLifecycleOwner, {
            adapter.setNowPlaying(it.id)
        })
    }

    private fun loadingView(hasContent:Boolean){
        viewBinding.loadingView.visibility = View.GONE
        if (hasContent){
            viewBinding.noContentView.text = null
        }else{
            viewBinding.noContentView.text = getString(R.string.no_content)
        }
    }

    override fun onAudioClick(audio: Audio) {
        (requireActivity() as MainActivity).playAudio(audio, adapter.listAudio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        val bundle = Bundle().apply{
            putParcelable(Keys.SONG_KEY, audio)
            putBoolean(Keys.ENABLE_UPDATE_KEY,false) }
        val controller = findNavController()
        controller.navigate(R.id.audioOptionsDialog, bundle)
    }
}
