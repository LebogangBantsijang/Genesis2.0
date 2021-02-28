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
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ItemLocalPlaylistSongBinding
import com.lebogang.kxgenesis.ui.adapters.utils.OnPlaylistAudioClickListener
import com.lebogang.kxgenesis.utils.GlobalGlide

class ItemPlaylistSongAdapter:RecyclerView.Adapter<ItemPlaylistSongAdapter.Holder>(){
    private var listAudio = mutableListOf<Audio>()
    var listener:OnPlaylistAudioClickListener? = null

    fun setAudioData(mutableList: MutableList<Audio>){
        listAudio.clear()
        notifyDataSetChanged()
        for (x in 0 until mutableList.size){
            listAudio.add(mutableList[x])
            notifyItemInserted(x)
        }
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
        GlobalGlide.loadAudioCover(holder.viewBinding.root, holder.viewBinding.imageView
                , audio.albumArtUri)
    }

    override fun getItemCount(): Int {
        return listAudio.size
    }

    inner class Holder(val viewBinding:ItemLocalPlaylistSongBinding):RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener {
                listener?.onAudioClick(listAudio[adapterPosition]) }
            viewBinding.deleteView.setOnClickListener {
                listener?.onAudioDeleteClick(listAudio[adapterPosition]) }
        }
    }
}
