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
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.databinding.ItemLocalSelectPlaylistBinding
import com.lebogang.genesis.room.models.Playlist
import com.lebogang.genesis.ui.adapters.utils.OnSelectPlaylistListener

class ItemSelectPlaylistAdapter:RecyclerView.Adapter<ItemSelectPlaylistAdapter.Holder>(){
    private var listPlaylist = listOf<Playlist>()
    var listener:OnSelectPlaylistListener? = null

    fun setData(list: List<Playlist>){
        listPlaylist = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalSelectPlaylistBinding.inflate(inflater, parent, false)
        return Holder(viewBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val playlist = listPlaylist[position]
        holder.viewBinding.titleView.text = playlist.title
    }

    override fun getItemCount(): Int {
        return listPlaylist.size
    }

    inner class Holder(val viewBinding:ItemLocalSelectPlaylistBinding)
        :RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener {
                listener?.onPlaylistClick(listPlaylist[adapterPosition])
            }
        }
    }
}
