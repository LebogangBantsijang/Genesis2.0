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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ContextMenu
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.NavigationMenu
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.FragmentSongsBinding
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.settings.ContentSettings
import com.lebogang.genesis.ui.MainActivity
import com.lebogang.genesis.ui.adapters.ItemSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.genesis.ui.dialogs.AudioOptionsDialog
import com.lebogang.genesis.viewmodels.AudioViewModel
import io.github.yavski.fabspeeddial.FabSpeedDial

class SongsFragment: GeneralFragment(),OnAudioClickListener {
    private val viewBinding:FragmentSongsBinding by lazy {
        FragmentSongsBinding.inflate(layoutInflater)
    }
    private val adapter = ItemSongAdapter().apply {
        listener= this@SongsFragment
    }
    private val genesisApplication:GenesisApplication by lazy{activity?.application as GenesisApplication}
    private val audioViewModel:AudioViewModel by lazy {
        AudioViewModel.Factory(genesisApplication.audioRepo).create(AudioViewModel::class.java)
    }
    private val contentSettings:ContentSettings by lazy {
        ContentSettings(context!!)
    }

    override fun onSearch(string: String) {
        adapter.filter.filter(string)
    }

    override fun onRefresh() {
        viewBinding.progressBar.visibility = View.VISIBLE
        audioViewModel.getAudio()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeAudioData()
        initSortView()
        observeCurrentAudio()
        audioViewModel.getAudio()
        audioViewModel.registerContentObserver()
    }

    private fun initRecyclerView(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun initSortView(){
        viewBinding.sortView.setMenuListener(object:FabSpeedDial.MenuListener{
            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                return when(menuItem!!.itemId){
                    R.id.titleAsc->{
                        contentSettings.setSortOrder(contentSettings.titleAsc)
                        audioViewModel.getAudio()
                        return true
                    }
                    R.id.titleDesc->{
                        contentSettings.setSortOrder(contentSettings.titleDesc)
                        audioViewModel.getAudio()
                        return true
                    }
                    R.id.dateAsc->{
                        contentSettings.setSortOrder(contentSettings.dateAsc)
                        audioViewModel.getAudio()
                        return true
                    }
                    R.id.dateDesc->{
                        contentSettings.setSortOrder(contentSettings.dateDesc)
                        audioViewModel.getAudio()
                        return true
                    }
                    else -> false
                }
            }

            override fun onPrepareMenu(p0: NavigationMenu?): Boolean {
                return true
            }

            override fun onMenuClosed() {
                //not needed
            }
        })
    }

    private fun observeAudioData(){
        audioViewModel.liveData.observe(viewLifecycleOwner,{
            adapter.setAudioData(it)
            loadingView(it.isNotEmpty())
        })
    }

    private fun observeCurrentAudio(){
        Queue.currentAudio.observe(viewLifecycleOwner, {
            adapter.setNowPlaying(it.id)
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
        activity?.title = getString(R.string.music)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioViewModel.unregisterContentContentObserver()
    }

    override fun onAudioClick(audio: Audio) {
        Queue.setCurrentAudio(audio, adapter.getList())
        activity?.let {
            (it as MainActivity).playAudio(audio)
        }
    }

    override fun onAudioClickOptions(audio: Audio) {
        AudioOptionsDialog(audio, true).show(fragmentManager!!, "")
    }

}
