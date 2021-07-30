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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.FragmentDeezerAlbumsBinding
import com.lebogang.genesis.network.models.AlbumDeezer
import com.lebogang.genesis.ui.adapters.ItemDeezerAlbumAdapter
import com.lebogang.genesis.ui.adapters.utils.OnDeezerAlbumClickListener
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.DeezerViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class DeezerTopAlbumsFragment : Fragment(), OnDeezerAlbumClickListener {
    private val viewBinding: FragmentDeezerAlbumsBinding by lazy {FragmentDeezerAlbumsBinding.inflate(layoutInflater)}
    private val adapterAlbums = ItemDeezerAlbumAdapter().apply { listener = this@DeezerTopAlbumsFragment }
    private val viewModel: DeezerViewModel by lazy { ViewModelFactory(requireActivity().application)
            .getDeezerViewModel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews(){
        initLoadingView(true)
        viewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
        viewBinding.recyclerView.adapter = adapterAlbums
        viewModel.getChartAlbum()
    }

    private fun initObservers(){
        viewModel.liveDataAlbumChart.observe(viewLifecycleOwner,{
            adapterAlbums.setData(it)
            initLoadingView(false)
        })
    }

    private fun initLoadingView(value:Boolean){
        if (value)
            viewBinding.loadingView.visibility = View.VISIBLE
        else
            viewBinding.loadingView.visibility = View.GONE
    }

    override fun onAlbumClick(album: AlbumDeezer) {
        val controller = findNavController()
        val bundle = Bundle().apply { putParcelable(Keys.DEEZER_ALBUM_KEY, album) }
        controller.navigate(R.id.viewAlbumDeezerFragment, bundle)
    }
}