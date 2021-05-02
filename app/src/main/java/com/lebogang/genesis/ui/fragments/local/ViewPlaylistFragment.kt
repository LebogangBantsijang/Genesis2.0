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
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.FragmentViewPlaylistBinding
import com.lebogang.genesis.room.models.Playlist
import com.lebogang.genesis.service.MusicQueue
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.ui.MainActivity
import com.lebogang.genesis.ui.adapters.ItemPlaylistSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.AudioViewModel
import com.lebogang.genesis.viewmodels.PlaylistViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class ViewPlaylistFragment : Fragment(), OnAudioClickListener {
    private val viewBinding: FragmentViewPlaylistBinding by lazy{ FragmentViewPlaylistBinding.inflate(layoutInflater) }
    private val viewModelPlaylist: PlaylistViewModel by lazy {
        ViewModelFactory(requireActivity().application).getPlaylistViewModel() }
    private val viewModelAudio: AudioViewModel by lazy{
        ViewModelFactory(requireActivity().application)
        .getAudioViewModel()}
    private val adapter = ItemPlaylistSongAdapter().apply { listener = this@ViewPlaylistFragment }
    private lateinit var playlist: Playlist
    private val musicService: MusicService by lazy{(requireActivity() as MainActivity).musicService}
    private val musicQueue : MusicQueue by lazy {(requireActivity().application as GenesisApplication).musicQueue}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        playlist = requireArguments().getParcelable(Keys.PLAYLIST_KEY)!!
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.titleView.text = playlist.title
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = adapter
        initObservers()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.playlist_audio_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.clearAllMenu ->{
                viewModelPlaylist.clearAudioData(playlist.id)
                true
            }
            else-> super.onOptionsItemSelected(item)
        }
    }

    private fun initObservers(){
        viewModelPlaylist.getPlaylistAudio(playlist.id)
        viewModelPlaylist.liveDataAudioIds.observe(viewLifecycleOwner,{
            viewModelAudio.getAudio(it)
        })
        viewModelAudio.liveData.observe(viewLifecycleOwner,{
            adapter.setAudioData(it)
            loadingView(it.isNotEmpty())
        })
        musicQueue.currentAudio.observe(viewLifecycleOwner,{ adapter.setNowPlaying(it.id) })
        //musicService.getCurrentAudio().observe(viewLifecycleOwner,{ adapter.setNowPlaying(it.id) })
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
        viewModelPlaylist.deletePlaylistAudio(playlist.id, audio.id)
    }
}