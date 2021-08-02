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

package com.lebogang.vibe.ui.local.playlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.databinding.ItemMusicPlaylistAddBinding
import com.lebogang.vibe.ui.local.Sort
import com.lebogang.vibe.utils.SortHelper

class MusicAddAdapter:RecyclerView.Adapter<MusicAddAdapter.Holder>(){
    private val asyncListDiffer = AsyncListDiffer(this,DiffCallback)
    var selectedItems = mutableListOf<Long>()
    var selectableBackground:Int = 0

    fun sort(sort: Sort) {
        val list = SortHelper.sortList(sort,asyncListDiffer.currentList)
        asyncListDiffer.submitList(list)
    }

    fun setData(list:MutableList<Music>) = asyncListDiffer.submitList(list)

    companion object DiffCallback: DiffUtil.ItemCallback<Music>(){
        override fun areItemsTheSame(o: Music, n: Music):Boolean = o.id == n.id

        override fun areContentsTheSame(o: Music, n: Music): Boolean = o == n
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemMusicPlaylistAddBinding.inflate(inflater, parent, false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = asyncListDiffer.currentList[position]
        holder.bind.titleTextView.text = music.title
        holder.bind.subtitleView.text = music.artist
        highlight(holder,selectedItems.contains(music.id))
    }

    private fun highlight(holder: Holder, isSelected:Boolean){
        if (isSelected){
            holder.bind.checkBox.setImageResource(R.drawable.ic_checked_ios)
            holder.bind.root.setBackgroundResource(R.color.primaryColor_light)
        }
        else{
            holder.bind.checkBox.setImageResource(R.drawable.ic_unchecked_box)
            holder.bind.root.setBackgroundResource(selectableBackground)
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class Holder(val bind:ItemMusicPlaylistAddBinding):RecyclerView.ViewHolder(bind.root){
        init {
            itemView.setOnClickListener {
                val music = asyncListDiffer.currentList[bindingAdapterPosition]
                val alreadyAdded = selectedItems.contains(music.id)
                if (alreadyAdded)
                    selectedItems.remove(music.id)
                else
                    selectedItems.add(music.id)
                onBindViewHolder(this,bindingAdapterPosition)
            }
        }
    }
}
