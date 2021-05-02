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

package com.lebogang.genesis.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.DialogQueueBinding
import com.lebogang.genesis.service.MusicQueue
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.ui.MainActivity
import com.lebogang.genesis.ui.adapters.ItemQueueSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener

class QueueDialog: DialogFragment(), OnAudioClickListener {
    private val viewBinding:DialogQueueBinding by lazy { DialogQueueBinding.inflate(layoutInflater) }
    private val adapter = ItemQueueSongAdapter().apply { listener = this@QueueDialog }
    private val musicService:MusicService by lazy { (requireActivity() as MainActivity).musicService }
    private val musicQueue : MusicQueue by lazy {(requireActivity().application as GenesisApplication).musicQueue}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){
        viewBinding.cancelView.setOnClickListener { dismiss() }
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
        adapter.setAudioData(musicQueue.audioQueue)
        viewBinding.counterView.text = musicQueue.audioQueue.size.toString()
        musicQueue.currentAudio.observe(viewLifecycleOwner,{ adapter.setNowPlaying(it.id) })
        //musicService.getCurrentAudio().observe(viewLifecycleOwner,{adapter.setNowPlaying(it.id)})
    }

    override fun onAudioClick(audio: Audio) {
        musicService.play(audio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        musicQueue.removeAudio(audio)
        adapter.setAudioData(musicQueue.audioQueue)
        viewBinding.counterView.text = musicQueue.audioQueue.size.toString()
    }
}
