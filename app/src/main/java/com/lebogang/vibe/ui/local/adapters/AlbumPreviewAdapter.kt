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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.vibe.R
import com.lebogang.vibe.databinding.ItemAlbumPreviewsBinding
import com.lebogang.vibe.database.local.models.Album
import com.lebogang.vibe.ui.ImageLoader
import com.lebogang.vibe.ui.ItemClickInterface
import com.lebogang.vibe.ui.Type

class AlbumPreviewAdapter:RecyclerView.Adapter<AlbumPreviewAdapter.Holder>() {
    private val asyncListDiffer = AsyncListDiffer(this,DiffCallback)
    var showMore:Boolean = true
    lateinit var imageLoader: ImageLoader
    lateinit var itemClickInterface: ItemClickInterface

    fun setData(list: MutableList<Album>) = asyncListDiffer.submitList(list)

    companion object DiffCallback: DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(o: Album, n: Album): Boolean = o.id == n.id

        override fun areContentsTheSame(o: Album, n: Album): Boolean = o == n
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemAlbumPreviewsBinding.inflate(inflater, parent, false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val album = asyncListDiffer.currentList[position]
        if (showMore){
            if (position < 5){
                holder.bind.titleTextView.visibility = View.VISIBLE
                holder.bind.titleTextView.text = album.title
                imageLoader.loadImage(album.artUri, Type.ALBUM, holder.bind.imageView)
            }else{
                holder.bind.titleTextView.visibility = View.GONE
                holder.bind.imageView.setImageResource(R.drawable.more_drawable)
            }
        } else{
            holder.bind.titleTextView.visibility = View.VISIBLE
            holder.bind.titleTextView.text = album.title
            imageLoader.loadImage(album.artUri, Type.ALBUM, holder.bind.imageView)
        }
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class Holder(val bind:ItemAlbumPreviewsBinding):RecyclerView.ViewHolder(bind.root){
        init {
            bind.root.setOnClickListener {
                if (showMore){
                    if (bindingAdapterPosition < 5)
                        itemClickInterface.onItemClick(bind.imageView
                            ,asyncListDiffer.currentList[bindingAdapterPosition],Type.ALBUM)
                    else
                        itemClickInterface.onItemClick(bind.imageView,null,Type.ALBUM)
                }
                else
                    itemClickInterface.onItemClick(bind.imageView
                        ,asyncListDiffer.currentList[bindingAdapterPosition],Type.ALBUM)
            }
        }
    }
}
