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
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ItemLocalSongBinding
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.utils.GlobalGlide

class ItemSongAdapter:RecyclerView.Adapter<ItemSongAdapter.ViewHolder>(), Filterable {
    var listener:OnAudioClickListener? = null
    private var listAudio = mutableListOf<Audio>()
    private val filteredList = mutableListOf<Audio>()
    var fallbackPrimaryTextColor:Int = 0
    var fallbackSecondaryTextColor:Int = 0
    var color:Int = -1
    var audioId:Long = -1
    private var isUserSearching = false

    fun setAudioData(mutableList: MutableList<Audio>){
        isUserSearching = false
        filteredList.clear()
        listAudio.clear()
        notifyDataSetChanged()
        for (x in 0 until mutableList.size){
            listAudio.add(mutableList[x])
            notifyItemInserted(x)
        }
    }

    fun setNowPlaying(audioId:Long, color:Int){
        this.audioId = audioId
        this.color = color
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalSongBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val audio = if(!isUserSearching) listAudio[position] else filteredList[position]
        val subtitle = audio.artist + "-" + audio.album
        holder.viewBinding.titleView.text = audio.title
        holder.viewBinding.subtitleView.text = subtitle
        holder.viewBinding.durationView.text = audio.durationFormatted
        GlobalGlide.loadAudioCover(holder.viewBinding.root, holder.viewBinding.imageView
                , audio.albumArtUri)
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
        if (isUserSearching)
            return filteredList.size
        return listAudio.size
    }

    inner class ViewHolder(val viewBinding:ItemLocalSongBinding)
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

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                isUserSearching = false
                constraint?.let {
                    isUserSearching = true
                    filteredList.clear()
                    for (x in 0 until listAudio.size){
                        val audio = listAudio[x]
                        if (audio.title.toLowerCase().contains(it.toString().toLowerCase())){
                            filteredList.add(audio)
                        }
                    }
                }
                return FilterResults()
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }
}
