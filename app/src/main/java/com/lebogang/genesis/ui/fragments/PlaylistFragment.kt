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
import com.lebogang.genesis.databinding.FragmentPlaylistBinding
import com.lebogang.genesis.room.models.Playlist
import com.lebogang.genesis.ui.adapters.ItemPlaylistAdapter
import com.lebogang.genesis.ui.adapters.utils.OnPlaylistClickListener
import com.lebogang.genesis.ui.dialogs.AddPlaylistDialog
import com.lebogang.genesis.ui.dialogs.UpdatePlaylistDialog
import com.lebogang.genesis.ui.helpers.SpeedDialHelper
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.PlaylistViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class PlaylistFragment: Fragment(),OnPlaylistClickListener {
    private val viewBinding:FragmentPlaylistBinding by lazy{ FragmentPlaylistBinding.inflate(layoutInflater)}
    private val viewModel:PlaylistViewModel  by lazy{
        ViewModelFactory(requireActivity().application).getPlaylistViewModel() }
    private val adapter = ItemPlaylistAdapter().apply { listener = this@PlaylistFragment}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
        viewModel.liveData.observe(viewLifecycleOwner, {
            adapter.setPlaylistData(it)
            loadingView(it.isNotEmpty())
            val count = getString(R.string.total) + " " + it.size.toString()
            viewBinding.counterView.text = count
        })
        initMenuView()
    }

    private fun initMenuView(){
        viewBinding.menuView.setMenuListener(object: SpeedDialHelper(){
            override fun onItemSelected(itemId: Int): Boolean {
                return when(itemId){
                    R.id.addMenu->{
                        val controller = findNavController()
                        controller.navigate(R.id.addPlaylistDialog)
                        true
                    }
                    R.id.clearMenu->{
                        viewModel.clearData()
                        true
                    }
                    else-> false
                }
            }
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

    override fun onPlaylistClick(playlist: Playlist) {
        val bundle = Bundle().apply { putParcelable(Keys.PLAYLIST_KEY, playlist) }
        val controller = findNavController()
        controller.navigate(R.id.viewPlaylistFragment, bundle)
    }

    override fun onPlaylistEditClick(playlist: Playlist) {
        val bundle = Bundle().apply { putParcelable(Keys.PLAYLIST_KEY, playlist) }
        val controller = findNavController()
        controller.navigate(R.id.updatePlaylistDialog, bundle)
    }

    override fun onPlaylistDeleteClick(playlist: Playlist) {
        viewModel.deletePlaylist(playlist)
    }

}
