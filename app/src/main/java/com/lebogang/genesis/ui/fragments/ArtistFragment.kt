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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Artist
import com.lebogang.genesis.databinding.FragmentArtistsBinding
import com.lebogang.genesis.settings.ThemeSettings
import com.lebogang.genesis.ui.ArtistViewActivity
import com.lebogang.genesis.ui.adapters.ItemArtistAdapter
import com.lebogang.genesis.ui.adapters.utils.OnArtistClickListener
import com.lebogang.genesis.viewmodels.ArtistViewModel

class ArtistFragment: GeneralFragment(), OnArtistClickListener {
    private val viewBinding:FragmentArtistsBinding by lazy {
        FragmentArtistsBinding.inflate(layoutInflater)
    }
    private val adapter = ItemArtistAdapter().apply {
        listener = this@ArtistFragment
    }
    private val genesisApplication:GenesisApplication by lazy{activity?.application as GenesisApplication}
    private val artistViewModel:ArtistViewModel by lazy {
        ArtistViewModel.Factory(genesisApplication.artistRepo)
            .create(ArtistViewModel::class.java)
    }
    private val themeSettings:ThemeSettings by lazy {
        ThemeSettings(context!!)
    }
    private val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

    override fun onSearch(string: String) {
        adapter.filter.filter(string)
    }

    override fun onRefresh() {
        viewBinding.progressBar.visibility = View.VISIBLE
        artistViewModel.getArtists()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View{
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeArtists()
        artistViewModel.getArtists()
        artistViewModel.registerContentObserver()
    }

    private fun initRecyclerView(){
        layoutManager.spanCount = themeSettings.getColumnCount()
        viewBinding.recyclerView.layoutManager = layoutManager
        viewBinding.recyclerView.adapter = adapter
    }

    private fun observeArtists(){
        artistViewModel.liveData.observe(viewLifecycleOwner, {
            adapter.setArtistData(it)
            loadingView(it.isNotEmpty())
        })
    }

    private fun loadingView(hasContent:Boolean){
        viewBinding.progressBar.visibility = View.GONE
        if (hasContent){
            viewBinding.noContentView.text = null
        }else{
            viewBinding.noContentView.text = getString(R.string.no_content)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.artists)
        if (layoutManager.spanCount != themeSettings.getColumnCount()){
            layoutManager.spanCount = themeSettings.getColumnCount()
            viewBinding.recyclerView.layoutManager = layoutManager
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        artistViewModel.unregisterContentContentObserver()
    }

    override fun onArtistClick(artist: Artist) {
        startActivity(Intent(context, ArtistViewActivity::class.java).apply {
            putExtra("Artist", artist.title)
        })
    }

}
