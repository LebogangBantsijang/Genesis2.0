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

package com.lebogang.vibe.ui.local.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Album
import com.lebogang.vibe.databinding.ItemAlbumBinding
import com.lebogang.vibe.ui.ImageLoader
import com.lebogang.vibe.ui.ItemClickInterface
import com.lebogang.vibe.ui.ItemOptionsInterface
import com.lebogang.vibe.ui.Type

class AlbumAdapter:RecyclerView.Adapter<AlbumAdapter.Holder>() {
    private val asyncListDiffer = AsyncListDiffer(this,DiffCallback)
    lateinit var imageLoader: ImageLoader
    lateinit var itemOptionsInterface: ItemOptionsInterface
    lateinit var itemClickInterface: ItemClickInterface

    fun setData(list: MutableList<Album>) = asyncListDiffer.submitList(list)

    companion object DiffCallback:DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(o: Album, n: Album): Boolean = o.id == n.id

        override fun areContentsTheSame(o: Album, n: Album): Boolean = o == n
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemAlbumBinding.inflate(inflater, parent, false)
        return Holder(bind)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val album = asyncListDiffer.currentList[position]
        holder.bind.titleTextView.text = album.title
        holder.bind.artistTextView.text = album.artist
        holder.bind.durationTextView.text = album.duration
        holder.bind.sizeTextView.text = album.size
        imageLoader.loadImage(album.artUri, Type.ALBUM, holder.bind.imageView)
        if (album.isFavourite)
            holder.bind.favouriteImageView.setImageResource(R.drawable.ic_melting_heart_filled_ios)
        else
            holder.bind.favouriteImageView.setImageResource(R.drawable.ic_melting_heart_ios)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class Holder(val bind:ItemAlbumBinding):RecyclerView.ViewHolder(bind.root){
        init {
            bind.favouriteImageView.setOnClickListener { itemOptionsInterface
                .onOptionsClick(asyncListDiffer.currentList[bindingAdapterPosition]) }
            bind.root.setOnClickListener { itemClickInterface.onItemClick(itemView
                ,asyncListDiffer.currentList[bindingAdapterPosition],Type.ALBUM) }
        }
    }
}
