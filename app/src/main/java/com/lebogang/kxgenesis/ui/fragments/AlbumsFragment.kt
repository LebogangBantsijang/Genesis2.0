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
import com.lebogang.kxgenesis.data.models.Album
import com.lebogang.kxgenesis.databinding.FragmentAlbumsBinding
import com.lebogang.kxgenesis.ui.adapters.ItemLocalAlbumAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAlbumClickListener
import com.lebogang.kxgenesis.viewmodels.AlbumViewModel

class AlbumsFragment(fragmentActivity: FragmentActivity): Fragment(), OnAlbumClickListener {
    private lateinit var viewBinding:FragmentAlbumsBinding
    private val adapter = ItemLocalAlbumAdapter()
    private val genesisApplication = fragmentActivity.application as GenesisApplication
    private val albumViewModel:AlbumViewModel by lazy {
        AlbumViewModel.Factory(genesisApplication.albumRepo).create(AlbumViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeAlbums()
        albumViewModel.getAlbums()
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun observeAlbums(){
        albumViewModel.liveData.observe(viewLifecycleOwner,{
            adapter.setAlbumData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        albumViewModel.registerContentObserver()
    }

    override fun onPause() {
        super.onPause()
        albumViewModel.unregisterContentContentObserver()
    }

    override fun onAlbumClick(album: Album) {
        //not finished here
    }

}
