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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.NavigationMenu
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.FragmentPlaylistBinding
import com.lebogang.genesis.room.models.Playlist
import com.lebogang.genesis.ui.PlaylistViewActivity
import com.lebogang.genesis.ui.adapters.ItemPlaylistAdapter
import com.lebogang.genesis.ui.adapters.utils.OnPlaylistClickListener
import com.lebogang.genesis.ui.dialogs.AddPlaylistDialog
import com.lebogang.genesis.ui.dialogs.UpdatePlaylistDialog
import com.lebogang.genesis.viewmodels.PlaylistViewModel
import io.github.yavski.fabspeeddial.FabSpeedDial

class PlaylistFragment: GeneralFragment(),OnPlaylistClickListener {

    private val viewBinding:FragmentPlaylistBinding by lazy{
        FragmentPlaylistBinding.inflate(layoutInflater)
    }
    private val playlistViewModel:PlaylistViewModel  by lazy{
        PlaylistViewModel.Factory((activity?.application as GenesisApplication).playlistRepo)
                .create(PlaylistViewModel::class.java)
    }
    private val adapter = ItemPlaylistAdapter().apply {
        listener = this@PlaylistFragment
    }

    override fun onSearch(string: String) {
        //Not Needed
    }

    override fun onRefresh() {
        //not needed
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observePlaylists()
        initMenuView()
    }

    private fun initRecyclerView(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun initMenuView(){
        viewBinding.menuView.setMenuListener(object: FabSpeedDial.MenuListener{
            override fun onPrepareMenu(p0: NavigationMenu?): Boolean {
                return true
            }

            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                return when(menuItem!!.itemId){
                    R.id.addMenu->{
                        AddPlaylistDialog().show(fragmentManager!!, "")
                        return true
                    }
                    R.id.clearMenu->{
                        playlistViewModel.clearData()
                        return true
                    }
                    else-> false
                }
            }

            override fun onMenuClosed() {
                //not needed
            }
        })
    }

    private fun observePlaylists(){
        playlistViewModel.liveData.observe(viewLifecycleOwner, {
            adapter.setPlaylistData(it)
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
        activity?.title = getString(R.string.playlists)
    }

    override fun onPlaylistClick(playlist: Playlist) {
        startActivity(Intent(context,PlaylistViewActivity::class.java).apply {
            putExtra("Playlist", playlist.id)
        })
    }

    override fun onPlaylistEditClick(playlist: Playlist) {
        UpdatePlaylistDialog(playlist).show(fragmentManager!!, "")
    }

    override fun onPlaylistDeleteClick(playlist: Playlist) {
        playlistViewModel.deletePlaylist(playlist)
    }

}
