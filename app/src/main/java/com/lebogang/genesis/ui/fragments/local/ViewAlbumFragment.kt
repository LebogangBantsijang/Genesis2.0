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

package com.lebogang.genesis.ui.fragments.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Album
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.FragmentViewAlbumBinding
import com.lebogang.genesis.service.MusicQueue
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.ui.MainActivity
import com.lebogang.genesis.ui.adapters.ItemAlbumArtistSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.utils.glide.GlideManager
import com.lebogang.genesis.viewmodels.AudioViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class ViewAlbumFragment: Fragment(), OnAudioClickListener {
    private val viewBinding: FragmentViewAlbumBinding by lazy{ FragmentViewAlbumBinding.inflate(layoutInflater) }
    private val viewModelAudio: AudioViewModel by lazy { ViewModelFactory(requireActivity().application)
            .getAudioViewModel() }
    private val adapter = ItemAlbumArtistSongAdapter().apply { listener = this@ViewAlbumFragment }
    private lateinit var album: Album
    private val musicService: MusicService by lazy{(requireActivity() as MainActivity).musicService}
    private val musicQueue : MusicQueue by lazy {(requireActivity().application as GenesisApplication).musicQueue}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        album = requireArguments().getParcelable(Keys.ALBUM_KEY)!!
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.titleView.text = album.title
        GlideManager(this).loadAlbumArt(album.getArtUri(),viewBinding.artView)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = adapter
        initObservers()
    }

    private fun initObservers(){
        viewModelAudio.getAlbumAudio(album.title)
        viewModelAudio.liveData.observe(viewLifecycleOwner,{list->
            adapter.setAudioData(list)
            viewBinding.subtitleView.text = list.size.toString()
            loadingView(list.isNotEmpty())
        })
        musicQueue.currentAudio.observe(viewLifecycleOwner,{ adapter.setNowPlaying(it.id) })
        //musicService.getCurrentAudio().observe(viewLifecycleOwner,{adapter.setNowPlaying(it.id)})
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
        musicQueue.audioQueue = adapter.listAudio
        musicService.play(audio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        val bundle = Bundle().apply{
            putParcelable(Keys.SONG_KEY, audio)
            putBoolean(Keys.ENABLE_UPDATE_KEY,false) }
        val controller = findNavController()
        controller.navigate(R.id.audioOptionsDialog, bundle)
    }

}