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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Artist
import com.lebogang.kxgenesis.databinding.FragmentArtistsBinding
import com.lebogang.kxgenesis.ui.adapters.ItemLocalArtistAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnArtistClickListener
import com.lebogang.kxgenesis.viewmodels.ArtistViewModel

class ArtistFragment(): Fragment(), OnArtistClickListener {
    private lateinit var viewBinding:FragmentArtistsBinding
    private val adapter = ItemLocalArtistAdapter()
    private val genesisApplication:GenesisApplication by lazy{activity?.application as GenesisApplication}
    private val artistViewModel:ArtistViewModel by lazy {
        ArtistViewModel.Factory(genesisApplication.artistRepo, genesisApplication.deezerService)
            .create(ArtistViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentArtistsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeArtists()
        artistViewModel.getArtists()
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun observeArtists(){
        artistViewModel.liveData.observe(viewLifecycleOwner, {
            adapter.setArtistData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        artistViewModel.registerContentObserver()
        activity?.title = getString(R.string.artists)
    }

    override fun onPause() {
        super.onPause()
        artistViewModel.unregisterContentContentObserver()
    }

    override fun onArtistClick(artist: Artist) {
        //Not
    }

}
