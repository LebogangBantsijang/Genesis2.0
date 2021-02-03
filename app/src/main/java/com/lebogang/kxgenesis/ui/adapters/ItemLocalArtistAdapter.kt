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
import com.lebogang.kxgenesis.data.models.Artist
import com.lebogang.kxgenesis.databinding.ItemLocalArtistBinding
import com.lebogang.kxgenesis.network.model.DeezerArtistModel
import com.lebogang.kxgenesis.ui.adapters.utils.OnArtistClickListener

class ItemLocalArtistAdapter:RecyclerView.Adapter<ItemLocalArtistAdapter.ViewHolder>(){
    var listener:OnArtistClickListener? = null
    private var listArtist = arrayListOf<Artist>()

    fun setArtistData(artistMap:LinkedHashMap<String, Artist>){
        artistMap.asIterable().forEach {
            listArtist.add(it.value)
            val index = listArtist.indexOf(it.value)
            notifyItemInserted(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalArtistBinding.inflate(inflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = listArtist[position]
        val deezerArtist:DeezerArtistModel? = artist.deezerArtistModel
        deezerArtist?.let {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                Glide.with(holder.viewBinding.root)
                    .asBitmap()
                    .load(it.pictureSmall)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(holder.viewBinding.imageView.width, holder.viewBinding.imageView.height)
                    .error(R.drawable.ic_music_24dp)
                    .into(holder.viewBinding.imageView)
                    .clearOnDetach()
            }else{
                Glide.with(holder.viewBinding.root)
                    .asBitmap()
                    .load(it.pictureSmall)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(holder.viewBinding.imageView.width, holder.viewBinding.imageView.height)
                    .error(R.drawable.ic_music_24dp)
                    .into(holder.viewBinding.imageView)
                    .clearOnDetach()
            }
        }
    }

    override fun getItemCount(): Int {
        return listArtist.size
    }

    inner class ViewHolder(val viewBinding:ItemLocalArtistBinding):RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener {
                listener?.onArtistClick(listArtist[adapterPosition])
            }
        }
    }
}
