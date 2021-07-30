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
import com.lebogang.genesis.R
import com.lebogang.genesis.database.local.models.Artist
import com.lebogang.genesis.databinding.ItemArtistPreviewsBinding
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.Type

class ArtistPreviewAdapter:RecyclerView.Adapter<ArtistPreviewAdapter.Holder>() {
    private val asyncListDiffer = AsyncListDiffer(this, DiffCallback)
    var showMore:Boolean = true
    lateinit var imageLoader: ImageLoader
    lateinit var itemClickInterface: ItemClickInterface

    fun setData(list: MutableList<Artist>) = asyncListDiffer.submitList(list)

    companion object DiffCallback: DiffUtil.ItemCallback<Artist>(){
        override fun areItemsTheSame(o: Artist, n: Artist): Boolean = o.id == n.id

        override fun areContentsTheSame(o: Artist, n: Artist): Boolean = o == n
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemArtistPreviewsBinding.inflate(inflater, parent, false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val artist = asyncListDiffer.currentList[position]
        if (showMore){
            if (position < 5){
                holder.bind.titleTextView.visibility = View.VISIBLE
                holder.bind.titleTextView.text = artist.title
                imageLoader.loadImage(artist.artUri, Type.ARTIST,holder.bind.imageView)
            }else{
                holder.bind.titleTextView.visibility = View.GONE
                holder.bind.imageView.setImageResource(R.drawable.more_drawable)
            }
        }else{
            holder.bind.titleTextView.visibility = View.VISIBLE
            holder.bind.titleTextView.text = artist.title
            imageLoader.loadImage(artist.artUri, Type.ARTIST,holder.bind.imageView)
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class Holder(val bind:ItemArtistPreviewsBinding):RecyclerView.ViewHolder(bind.root){
        init {
            bind.root.setOnClickListener {
                if (showMore){
                    if (bindingAdapterPosition < 5)
                        itemClickInterface.onItemClick(itemView,
                            asyncListDiffer.currentList[bindingAdapterPosition],Type.ARTIST)
                    else
                        itemClickInterface.onItemClick(itemView,null,Type.ARTIST)
                }else{
                    itemClickInterface.onItemClick(itemView,
                        asyncListDiffer.currentList[bindingAdapterPosition],Type.ARTIST)
                }
            }
        }
    }
}
