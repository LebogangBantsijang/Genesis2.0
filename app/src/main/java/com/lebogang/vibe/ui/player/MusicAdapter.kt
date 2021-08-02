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

package com.lebogang.vibe.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.databinding.ItemQueueBinding
import com.lebogang.vibe.database.local.models.AbstractMusic
import com.lebogang.vibe.ui.ItemClickInterface
import com.lebogang.vibe.ui.ItemOptionsInterface
import com.lebogang.vibe.ui.Type

class MusicAdapter:RecyclerView.Adapter<MusicAdapter.Holder>() {
    private var list = mutableListOf<AbstractMusic>()
    lateinit var itemClickInterface: ItemClickInterface
    lateinit var itemOptionsInterface: ItemOptionsInterface

    fun removeItem(music: Music){
        list.remove(music)
        notifyDataSetChanged()
    }

    fun setData(list: MutableList<AbstractMusic>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemQueueBinding.inflate(inflater,parent,false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = list[position]
        holder.bind.titleTextView.text = music.getItemTitle()
        holder.bind.subtitleTextView.text = music.getItemArtist()
    }

    override fun getItemCount(): Int = list.size

    inner class Holder(val bind:ItemQueueBinding):RecyclerView.ViewHolder(bind.root){
        init {
            itemView.setOnClickListener {
                itemClickInterface.onItemClick(itemView,list[bindingAdapterPosition],Type.MUSIC) }
            bind.removeButton.setOnClickListener {
                itemOptionsInterface.onOptionsClick(list[bindingAdapterPosition]) }
        }
    }
}
