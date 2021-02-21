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

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.ItemLayoutPlaylistBinding
import com.lebogang.kxgenesis.room.models.Playlist
import com.lebogang.kxgenesis.ui.adapters.utils.OnPlaylistClickListener

class ItemLocalPlaylist:RecyclerView.Adapter<ItemLocalPlaylist.ViewHolder>(){
    var listener:OnPlaylistClickListener? = null
    private var listPlaylist = mutableListOf<Playlist>()

    fun setPlaylistData(mutableList: MutableList<Playlist>){
        for (x in 0 until mutableList.size){
            listPlaylist.add(mutableList[x])
            notifyItemInserted(x)
        }
    }

    inner class ViewHolder(val viewBinding:ItemLayoutPlaylistBinding):RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener { listener?.onPlaylistClick(listPlaylist[adapterPosition]) }
            viewBinding.editView.setOnClickListener {
                listener?.onPlaylistEditClick(listPlaylist[adapterPosition]) }
            viewBinding.deleteView.setOnClickListener {
                listener?.onPlaylistDeleteClick(listPlaylist[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLayoutPlaylistBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = listPlaylist[position]
        holder.viewBinding.titleView.text = playlist.title
    }

    override fun getItemCount(): Int {
        return listPlaylist.size
    }
}
