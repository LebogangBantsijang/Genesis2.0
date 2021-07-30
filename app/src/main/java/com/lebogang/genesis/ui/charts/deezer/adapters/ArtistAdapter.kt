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

package com.lebogang.genesis.ui.charts.deezer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.databinding.ItemArtistOnlineBinding
import com.lebogang.genesis.online.deezer.models.Artist
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.Type

class ArtistAdapter:RecyclerView.Adapter<ArtistAdapter.Holder>() {
    private var list = listOf<Artist>()
    lateinit var imageLoader: ImageLoader
    lateinit var itemClickInterface: ItemClickInterface

    fun setData(list: List<Artist>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemArtistOnlineBinding.inflate(inflater, parent, false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val artist = list[position]
        holder.bind.titleView.text = artist.title
        imageLoader.loadImage(artist.coverMedium,Type.ARTIST,holder.bind.imageView)
    }

    override fun getItemCount(): Int = list.size

    inner class Holder(val bind:ItemArtistOnlineBinding):RecyclerView.ViewHolder(bind.root){
        init {
            itemView.setOnClickListener { itemClickInterface
                .onItemClick(itemView,list[bindingAdapterPosition],Type.ARTIST) }
        }
    }
}
