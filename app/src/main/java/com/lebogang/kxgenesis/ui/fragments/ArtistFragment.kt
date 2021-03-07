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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Artist
import com.lebogang.kxgenesis.databinding.FragmentArtistsBinding
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.ArtistViewActivity
import com.lebogang.kxgenesis.ui.adapters.ItemArtistAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnArtistClickListener
import com.lebogang.kxgenesis.viewmodels.ArtistViewModel

class ArtistFragment: GeneralFragment(), OnArtistClickListener {
    private lateinit var viewBinding:FragmentArtistsBinding
    private val adapter = ItemArtistAdapter()
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
        viewBinding = FragmentArtistsBinding.inflate(inflater, container, false)
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
        adapter.listener = this
        layoutManager.spanCount = themeSettings.getColumnCount()
        viewBinding.recyclerView.layoutManager = layoutManager
        viewBinding.recyclerView.itemAnimator?.addDuration = 450
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
