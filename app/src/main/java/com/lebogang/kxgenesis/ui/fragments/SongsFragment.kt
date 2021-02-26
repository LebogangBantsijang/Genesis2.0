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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ContextMenu
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.FragmentSongsBinding
import com.lebogang.kxgenesis.settings.ContentSettings
import com.lebogang.kxgenesis.settings.DATE_ASC
import com.lebogang.kxgenesis.settings.DATE_DESC
import com.lebogang.kxgenesis.settings.TITLE_ASC
import com.lebogang.kxgenesis.settings.TITLE_DESC
import com.lebogang.kxgenesis.ui.adapters.ItemSongAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.ui.dialogs.AudioOptionsDialog
import com.lebogang.kxgenesis.viewmodels.AudioViewModel

class SongsFragment: GeneralFragment(),OnAudioClickListener,PopupMenu.OnMenuItemClickListener {
    private lateinit var viewBinding:FragmentSongsBinding
    private val adapter = ItemSongAdapter()
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
        viewBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeAudioData()
        initSortView()
        audioViewModel.getAudio()
        audioViewModel.registerContentObserver()
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.itemAnimator?.addDuration = 450
        viewBinding.recyclerView.adapter = adapter
    }

    private fun initSortView(){
        viewBinding.sortView.setOnClickListener {
            PopupMenu(context!!, it).apply {
                setOnMenuItemClickListener(this@SongsFragment)
                inflate(R.menu.sort_menu)
                show()
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == R.id.sortView)
            activity?.menuInflater?.inflate(R.menu.sort_menu, menu)
    }

    private fun observeAudioData(){
        audioViewModel.liveData.observe(viewLifecycleOwner,{
            adapter.setAudioData(it)
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
        activity?.title = getString(R.string.music)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioViewModel.unregisterContentContentObserver()
    }

    override fun onAudioClick(audio: Audio) {
        //not finished here
    }

    override fun onAudioClickOptions(audio: Audio) {
        AudioOptionsDialog(audio, true).show(fragmentManager!!, "")
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.titleAsc -> {
                contentSettings.setSortOrder(TITLE_ASC)
                audioViewModel.getAudio()
                true
            }
            R.id.titleDesc -> {
                contentSettings.setSortOrder(TITLE_DESC)
                audioViewModel.getAudio()
                true
            }
            R.id.dateAsc -> {
                contentSettings.setSortOrder(DATE_ASC)
                audioViewModel.getAudio()
                true
            }
            R.id.dateDesc -> {
                contentSettings.setSortOrder(DATE_DESC)
                audioViewModel.getAudio()
                true
            }
            else -> false
        }
    }

}
