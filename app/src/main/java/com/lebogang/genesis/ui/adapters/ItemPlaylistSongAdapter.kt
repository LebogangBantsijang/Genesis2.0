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
import com.lebogang.genesis.databinding.ItemLocalPlaylistSongBinding
import com.lebogang.genesis.ui.adapters.utils.AdapterInterface
import com.lebogang.genesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.genesis.ui.adapters.utils.OnPlaylistAudioClickListener
import com.lebogang.genesis.utils.GlobalGlide
import com.lebogang.genesis.utils.glide.GlideManager

class ItemPlaylistSongAdapter:RecyclerView.Adapter<ItemPlaylistSongAdapter.Holder>(),AdapterInterface{
    private var audioId:Long = -1
    var listAudio = mutableListOf<Audio>()
    var listener:OnAudioClickListener? = null

    override fun setAudioData(mutableList: MutableList<Audio>){
        listAudio = mutableList
        notifyDataSetChanged()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalPlaylistSongBinding.inflate(inflater, parent, false)
        return Holder(viewBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val audio = listAudio[position]
        val subtitle = audio.artist + "-" + audio.album
        holder.viewBinding.titleView.text = audio.title
        holder.viewBinding.subtitleView.text = subtitle
        holder.viewBinding.durationView.text = audio.durationFormatted
        GlideManager(holder.itemView).loadAudioArt(audio.getArtUri(), holder.viewBinding.imageView)
        updateNowPlaying(holder, audio)
    }

    private fun updateNowPlaying(holder: Holder, audio: Audio){
        if (audio.id == audioId)
            holder.viewBinding.lottieView.visibility = View.VISIBLE
        else
            holder.viewBinding.lottieView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return listAudio.size
    }

    inner class Holder(val viewBinding:ItemLocalPlaylistSongBinding):RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener {
                listener?.onAudioClick(listAudio[adapterPosition]) }
            viewBinding.deleteView.setOnClickListener {
                listener?.onAudioClickOptions(listAudio[adapterPosition]) }
        }
    }
}
