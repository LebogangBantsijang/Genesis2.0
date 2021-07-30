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

package com.lebogang.genesis.ui.local.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.database.local.models.Playlist
import com.lebogang.genesis.databinding.ItemPlaylistBinding
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.ItemOptionsInterface
import com.lebogang.genesis.ui.Type

class PlaylistAdapter :RecyclerView.Adapter<PlaylistAdapter.Holder>(){
    private val asyncListDiffer = AsyncListDiffer(this,DiffCallback)
    var showOptions:Boolean = true
    lateinit var itemOptionsInterface: ItemOptionsInterface
    lateinit var itemClickInterface: ItemClickInterface

    fun setData(list: List<Playlist>) = asyncListDiffer.submitList(list)

    companion object DiffCallback:DiffUtil.ItemCallback<Playlist>(){
        override fun areItemsTheSame(o: Playlist, n: Playlist): Boolean = o.id == n.id

        override fun areContentsTheSame(o: Playlist, n: Playlist): Boolean = o.equals(n)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemPlaylistBinding.inflate(inflater,parent,false)
        if (!showOptions)
            bind.imageButton.visibility = View.GONE
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val playlist = asyncListDiffer.currentList[position]
        holder.bind.titleTextView.text = playlist.title
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class Holder(val bind:ItemPlaylistBinding):RecyclerView.ViewHolder(bind.root){
        init {
            if (showOptions){
                bind.imageButton.setOnClickListener {
                    itemOptionsInterface.onOptionsClick(
                        asyncListDiffer.currentList[bindingAdapterPosition])
                }
            }
            itemView.setOnClickListener {
                itemClickInterface.onItemClick(it,
                    asyncListDiffer.currentList[bindingAdapterPosition],Type.PLAYLIST)
            }
        }
    }
}
