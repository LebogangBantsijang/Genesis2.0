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

package com.lebogang.genesis.ui.fragments.deezer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.FragmentDeezerBinding
import com.lebogang.genesis.network.models.AlbumDeezer
import com.lebogang.genesis.network.models.ArtistDeezer
import com.lebogang.genesis.network.models.TrackDeezer
import com.lebogang.genesis.ui.adapters.ItemDeezerAlbumAdapter
import com.lebogang.genesis.ui.adapters.ItemDeezerArtistAdapter
import com.lebogang.genesis.ui.adapters.ItemDeezerSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnDeezerAlbumClickListener
import com.lebogang.genesis.ui.adapters.utils.OnDeezerArtistClickListener
import com.lebogang.genesis.ui.adapters.utils.OnDeezerAudioClickListener
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.DeezerViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class DeezerFragment: Fragment(), OnDeezerAudioClickListener, OnDeezerAlbumClickListener, OnDeezerArtistClickListener {
    private val viewBinding: FragmentDeezerBinding by lazy{ FragmentDeezerBinding.inflate(layoutInflater) }
    private val viewModel: DeezerViewModel by lazy { ViewModelFactory(requireActivity().application)
            .getDeezerViewModel() }
    private val adapterSongs = ItemDeezerSongAdapter().apply { listener = this@DeezerFragment }
    private val adapterAlbums = ItemDeezerAlbumAdapter().apply { listener = this@DeezerFragment }
    private val adapterArtists = ItemDeezerArtistAdapter().apply { listener = this@DeezerFragment }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = adapterSongs
        viewModel.getChartAudio()
        viewBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            initLoadingView(true)
            when(it.itemId){
                R.id.deezerSongs -> {
                    viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    viewBinding.recyclerView.adapter = adapterSongs
                    viewModel.getChartAudio()
                    false
                }
                R.id.deezerAlbums -> {
                    viewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2
                            , StaggeredGridLayoutManager.VERTICAL)
                    viewBinding.recyclerView.adapter = adapterAlbums
                    viewModel.getChartAlbum()
                    false
                }
                R.id.deezerArists -> {
                    viewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2
                            , StaggeredGridLayoutManager.VERTICAL)
                    viewBinding.recyclerView.adapter = adapterArtists
                    viewModel.getChartArtist()
                    false
                }
                R.id.deezerSearch -> {
                    val controller = findNavController()
                    controller.navigate(R.id.deezerSearchFragment)
                    false
                }
                else -> false
            }
        }
    }

    private fun initObservers(){
        viewModel.liveDataAudioChart.observe(viewLifecycleOwner,{
            adapterSongs.setData(it)
            initLoadingView(false)
        })
        viewModel.liveDataAlbumChart.observe(viewLifecycleOwner,{
            adapterAlbums.setData(it)
            initLoadingView(false)
        })
        viewModel.liveDataArtistChart.observe(viewLifecycleOwner,{
            adapterArtists.setData(it)
            initLoadingView(false)
        })
    }

    private fun initLoadingView(value:Boolean){
        if (value)
            viewBinding.loadingView.visibility = View.VISIBLE
        else
            viewBinding.loadingView.visibility = View.GONE
    }

    override fun onAudioClick(audio: TrackDeezer) {
        val controller = findNavController()
        val bundle = Bundle().apply { putParcelable(Keys.DEEZER_SONG_KEY, audio) }
        controller.navigate(R.id.dialogDeezerPreview, bundle)
    }

    override fun onAlbumClick(album: AlbumDeezer) {
        val controller = findNavController()
        val bundle = Bundle().apply { putParcelable(Keys.DEEZER_ALBUM_KEY, album) }
        controller.navigate(R.id.viewAlbumDeezerFragment, bundle)
    }

    override fun onArtistClick(artist: ArtistDeezer) {
        val controller = findNavController()
        val bundle = Bundle().apply { putParcelable(Keys.DEEZER_ARTIST_KEY, artist) }
        controller.navigate(R.id.viewArtistDeezerFragment, bundle)
    }
}
