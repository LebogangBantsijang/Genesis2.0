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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.DialogQueueBinding
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.ui.adapters.ItemQueueSongAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener

class QueueDialog: BottomSheetDialogFragment(), OnAudioClickListener {
    private lateinit var viewBinding:DialogQueueBinding
    private val adapter = ItemQueueSongAdapter().apply {
        listener = this@QueueDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View {
        viewBinding = DialogQueueBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initDismissView()
        observeAudio()
    }

    private fun initRecyclerView(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
        adapter.setAudioData(Queue.audioQueue)
    }

    private fun initDismissView(){
        viewBinding.minimiseView.setOnClickListener{
            dismissAllowingStateLoss()
        }
    }

    private fun observeAudio(){
        Queue.currentAudio.observe(viewLifecycleOwner,{
            adapter.setNowPlaying(it.id)
        })
    }

    override fun onAudioClick(audio: Audio) {
        //Queue.setCurrentAudio(audio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        Queue.removeAudio(audio)
        adapter.setAudioData(Queue.audioQueue)
    }
}
