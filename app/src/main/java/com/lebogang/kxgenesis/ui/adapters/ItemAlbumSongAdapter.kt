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
import com.lebogang.kxgenesis.databinding.ItemLocalAlbumSongBinding
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener

class ItemAlbumSongAdapter :RecyclerView.Adapter<ItemAlbumSongAdapter.ViewHolder>(){
    var listener: OnAudioClickListener? = null
    var listAudio = mutableListOf<Audio>()
    var fallbackPrimaryTextColor:Int = 0
    var fallbackSecondaryTextColor:Int = 0
    var color:Int = -1
    var audioId:Long = -1

    fun setAudioData(mutableList: MutableList<Audio>){
        listAudio.clear()
        notifyDataSetChanged()
        for (x in 0 until mutableList.size){
            listAudio.add(mutableList[x])
            notifyItemInserted(x)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalAlbumSongBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio = listAudio[position]
        val subtitle = audio.artist + "-" + audio.album
        val counter = (1+position).toString()
        holder.viewBinding.titleView.text = audio.title
        holder.viewBinding.subtitleView.text = subtitle
        holder.viewBinding.counterView.text = counter
        updateNowPlaying(holder, audio)
    }

    private fun updateNowPlaying(holder: ViewHolder, audio: Audio){
        if (audio.id == audioId){
            holder.viewBinding.titleView.setTextColor(color)
            holder.viewBinding.subtitleView.setTextColor(color)
        }else{
            //holder.viewBinding.titleView.setTextColor(fallbackPrimaryTextColor)
            //holder.viewBinding.subtitleView.setTextColor(fallbackSecondaryTextColor)
        }
    }

    override fun getItemCount(): Int {
        return listAudio.size
    }

    inner class ViewHolder(val viewBinding:ItemLocalAlbumSongBinding)
        :RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener { listener?.onAudioClick(listAudio[adapterPosition]) }
            viewBinding.optionsView.setOnClickListener {
                listener?.onAudioClickOptions(listAudio[adapterPosition]) }
            viewBinding.root.setOnLongClickListener {
                listener?.onAudioClickOptions(listAudio[adapterPosition])
                true
            }
        }
    }
}
