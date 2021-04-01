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

package com.lebogang.genesis.ui.fragments.local

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.FragmentSongsBinding
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.settings.ContentSettings
import com.lebogang.genesis.ui.MainActivity
import com.lebogang.genesis.ui.adapters.ItemSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.AudioViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class SongsFragment: Fragment(), OnAudioClickListener {
    private val viewBinding: FragmentSongsBinding by lazy { FragmentSongsBinding.inflate(layoutInflater) }
    private val adapter = ItemSongAdapter().apply { listener= this@SongsFragment }
    private val viewModel: AudioViewModel by lazy {
        ViewModelFactory(requireActivity().application).getAudioViewModel() }
    private val contentSettings: ContentSettings by lazy { ContentSettings(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
        initObservers()
        viewModel.getAudio()
        viewModel.registerContentObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.audio_sort_menu, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.titleAsc ->{
                contentSettings.setSortOrder(contentSettings.titleAsc)
                viewModel.getAudio()
                true
            }
            R.id.titleDesc ->{
                contentSettings.setSortOrder(contentSettings.titleDesc)
                viewModel.getAudio()
                true
            }
            R.id.dateAsc ->{
                contentSettings.setSortOrder(contentSettings.dateAsc)
                viewModel.getAudio()
                true
            }
            R.id.dateDesc ->{
                contentSettings.setSortOrder(contentSettings.dateDesc)
                viewModel.getAudio()
                true
            }
            R.id.refresh ->{
                viewModel.getAudio()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initObservers(){
        viewModel.liveData.observe(viewLifecycleOwner,{
            adapter.setAudioData(it)
            loadingView(it.isNotEmpty())
            val count = getString(R.string.total) + " " + it.size.toString()
            viewBinding.counterView.text = count
        })
        Queue.currentAudio.observe(viewLifecycleOwner, {
            adapter.setNowPlaying(it.id)
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unregisterContentContentObserver()
    }

    override fun onAudioClick(audio: Audio) {
        (requireActivity() as MainActivity).playAudio(audio, adapter.getList())
    }

    override fun onAudioClickOptions(audio: Audio) {
        val bundle = Bundle().apply{
            putParcelable(Keys.SONG_KEY, audio)
            putBoolean(Keys.ENABLE_UPDATE_KEY,true) }
        val controller = findNavController()
        controller.navigate(R.id.audioOptionsDialog, bundle)
    }

}