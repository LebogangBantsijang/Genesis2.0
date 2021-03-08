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
import com.lebogang.kxgenesis.databinding.ItemPlayerViewPagerAudioBinding
import com.lebogang.kxgenesis.ui.adapters.utils.AdapterInterface
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.utils.GlobalGlide

class PlayerViewPagerAdapter: RecyclerView.Adapter<PlayerViewPagerAdapter.ViewHolder>(),AdapterInterface{
    var listener:OnAudioClickListener? = null
    private var listAudio = mutableListOf<Audio>()
    private var audioId:Long = -1

    override fun setAudioData(mutableList: MutableList<Audio>){
        listAudio = mutableList
        notifyDataSetChanged()
    }

    override fun setNowPlaying(audioId:Long): Int {
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
        val viewBinding = ItemPlayerViewPagerAudioBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio = listAudio[position]
        val counter = (1+position).toString() + " of " + listAudio.size
        holder.viewBinding.counterView.text = counter
        GlobalGlide.loadAudioCover(holder.viewBinding.root, holder.viewBinding.coverView, audio.albumArtUri)
        updateNowPlaying(holder, audio)
    }

    private fun updateNowPlaying(holder:ViewHolder, audio: Audio){
        if (audio.id == audioId){
            holder.viewBinding.playPauseView.visibility = View.GONE
            holder.viewBinding.counterView.text = "Now Playing"
        }else
            holder.viewBinding.playPauseView.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return listAudio.size
    }

    inner class ViewHolder(val viewBinding:ItemPlayerViewPagerAudioBinding)
        :RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener {
                listener?.onAudioClick(listAudio[adapterPosition])
            }
            viewBinding.playPauseView.setOnClickListener {
                listener?.onAudioClick(listAudio[adapterPosition])
            }
        }
    }
}
