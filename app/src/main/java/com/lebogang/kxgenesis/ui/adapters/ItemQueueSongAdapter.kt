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

package com.lebogang.kxgenesis.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ItemLocalQueueSongBinding
import com.lebogang.kxgenesis.ui.adapters.utils.AdapterInterface
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener

class ItemQueueSongAdapter :RecyclerView.Adapter<ItemQueueSongAdapter.ViewHolder>(),AdapterInterface{
    var listener: OnAudioClickListener? = null
    var listAudio = mutableListOf<Audio>()
    var audioId:Long = -1

    override fun setAudioData(mutableList: MutableList<Audio>){
        listAudio.clear()
        notifyDataSetChanged()
        for (x in 0 until mutableList.size){
            listAudio.add(mutableList[x])
            notifyItemInserted(x)
        }
    }

    override fun setNowPlaying(audioId:Long):Int{
        this.audioId = audioId
        notifyDataSetChanged()
        listAudio.forEach {
            if (it.id == audioId){
                return listAudio.indexOf(it)
            }
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalQueueSongBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio = listAudio[position]
        val subtitle = audio.artist + "-" + audio.album
        val counter = (1+position).toString()
        holder.viewBinding.titleView.text = audio.title
        holder.viewBinding.subtitleView.text = subtitle
        holder.viewBinding.counterView.text = counter
        holder.viewBinding.durationView.text = audio.durationFormatted
        updateNowPlaying(holder, audio)
    }

    private fun updateNowPlaying(holder: ViewHolder, audio: Audio){
        if (audio.id == audioId)
            holder.viewBinding.lottieView.visibility = View.VISIBLE
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
                listener?.onAudioClickOptions(listAudio[adapterPosition]) }
        }
    }
}
