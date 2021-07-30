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

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.R
import com.lebogang.genesis.database.local.models.Artist
import com.lebogang.genesis.databinding.ItemArtistBinding
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.ItemOptionsInterface
import com.lebogang.genesis.ui.Type

class ArtistAdapter:RecyclerView.Adapter<ArtistAdapter.Holder>() {
    private val asyncListDiffer = AsyncListDiffer(this, DiffCallback)
    lateinit var imageLoader: ImageLoader
    lateinit var itemOptionsInterface: ItemOptionsInterface
    lateinit var itemClickInterface: ItemClickInterface

    fun setData(list: List<Artist>) = asyncListDiffer.submitList(list)

    companion object DiffCallback:DiffUtil.ItemCallback<Artist>(){
        override fun areItemsTheSame(o: Artist, n: Artist): Boolean = o.id == n.id

        override fun areContentsTheSame(o: Artist, n: Artist): Boolean = o == n
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemArtistBinding.inflate(inflater, parent, false)
        return Holder(bind)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val artist = asyncListDiffer.currentList[position]
        holder.bind.titleTextView.text = artist.title
        holder.bind.countTextView.text = "songs: " + artist.trackCount
        holder.bind.durationTextView.text = artist.duration + " - " + artist.size
        imageLoader.loadImage(artist.artUri, Type.ARTIST, holder.bind.imageView)
        if (artist.isFavourite)
            holder.bind.favouriteImageView.setImageResource(R.drawable.ic_melting_heart_filled_ios)
        else
            holder.bind.favouriteImageView.setImageResource(R.drawable.ic_melting_heart_ios)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class Holder(val bind:ItemArtistBinding):RecyclerView.ViewHolder(bind.root){
        init {
            bind.favouriteImageView.setOnClickListener { itemOptionsInterface
                .onOptionsClick(asyncListDiffer.currentList[bindingAdapterPosition]) }
            bind.root.setOnClickListener { itemClickInterface.onItemClick(itemView
                ,asyncListDiffer.currentList[bindingAdapterPosition],Type.ALBUM) }
        }
    }
}
