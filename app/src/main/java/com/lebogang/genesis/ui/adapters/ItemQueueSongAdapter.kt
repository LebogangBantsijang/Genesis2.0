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

package com.lebogang.genesis.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.ItemLocalQueueSongBinding
import com.lebogang.genesis.ui.adapters.utils.AdapterInterface
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener

class ItemQueueSongAdapter :RecyclerView.Adapter<ItemQueueSongAdapter.ViewHolder>(),AdapterInterface{
    var listener: OnAudioClickListener? = null
    var listAudio = mutableListOf<Audio>()
    var audioId:Long = -1
    private var previousSelection = -2
    private var currentSelection = -1;

    override fun setAudioData(mutableList: MutableList<Audio>){
        listAudio = mutableList
        notifyDataSetChanged()
    }

    override fun setNowPlaying(audioId:Long){
        this.audioId = audioId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalQueueSongBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio = listAudio[position]
        val subtitle = audio.artist + "-" + audio.album
        holder.viewBinding.titleView.text = audio.title
        holder.viewBinding.subtitleView.text = subtitle
        updateNowPlaying(holder, audio)
    }

    private fun updateNowPlaying(holder: ViewHolder, audio: Audio){
        if (audio.id == audioId) {
            if(currentSelection < 0){
                currentSelection = holder.adapterPosition
            }
            holder.viewBinding.lottieView.visibility = View.VISIBLE
        }
        else
            holder.viewBinding.lottieView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return listAudio.size
    }

    inner class ViewHolder(val viewBinding:ItemLocalQueueSongBinding)
        :RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.optionsView.setOnClickListener {
                previousSelection = currentSelection
                currentSelection = adapterPosition
                listener?.onAudioClickOptions(listAudio[adapterPosition]) }
            viewBinding.root.setOnClickListener { listener?.onAudioClick(listAudio[adapterPosition]) }
        }
    }
}
