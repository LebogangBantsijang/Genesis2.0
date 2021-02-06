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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Album
import com.lebogang.kxgenesis.databinding.ItemLocalAlbumBinding
import com.lebogang.kxgenesis.ui.adapters.utils.OnAlbumClickListener

class ItemLocalAlbumAdapter:RecyclerView.Adapter<ItemLocalAlbumAdapter.ViewHolder>(){
    private var listAlbum = arrayListOf<Album>()
    var listener:OnAlbumClickListener? = null

    fun setAlbumData(albumMap:LinkedHashMap<String, Album>){
        albumMap.asIterable().forEach {
            listAlbum.add(it.value)
            val index = listAlbum.indexOf(it.value)
            notifyItemInserted(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalAlbumBinding.inflate(inflater)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = listAlbum[position]
        holder.viewBinding.titleView.text = album.title
        holder.viewBinding.subtitleView.text = album.artist
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Glide.with(holder.viewBinding.root)
                .asBitmap()
                .load(album.albumArtUri)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(holder.viewBinding.imageView.width, holder.viewBinding.imageView.height)
                .centerCrop()
                .error(R.drawable.ic_music_record_24dp)
                .into(holder.viewBinding.imageView)
                .clearOnDetach()
        }else{
            Glide.with(holder.viewBinding.root)
                .asBitmap()
                .load(album.albumArtUri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(holder.viewBinding.imageView.width, holder.viewBinding.imageView.height)
                .error(R.drawable.ic_music_record_24dp)
                .centerCrop()
                .into(holder.viewBinding.imageView)
                .clearOnDetach()
        }
    }

    override fun getItemCount(): Int {
        return listAlbum.size
    }

    inner class ViewHolder(val viewBinding:ItemLocalAlbumBinding):RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener { listener?.onAlbumClick(listAlbum[adapterPosition]) }
        }
    }
}
