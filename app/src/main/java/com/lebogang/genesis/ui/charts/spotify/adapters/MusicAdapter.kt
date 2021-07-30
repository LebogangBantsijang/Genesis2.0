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

package com.lebogang.genesis.ui.charts.spotify.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.ItemMusicOnlineBinding
import com.lebogang.genesis.online.spotify.models.Music
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.Type

class MusicAdapter :RecyclerView.Adapter<MusicAdapter.Holder>(){
    private var list = listOf<Music>()
    lateinit var imageLoader: ImageLoader
    lateinit var itemClickInterface: ItemClickInterface
    var artUrl:String? = null
    @ColorInt
    private var explicitColor:Int = -1

    fun setData(list: List<Music>){
        this.list =  list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        if (explicitColor<0)
            explicitColor = parent.context.getColor(R.color.red)
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemMusicOnlineBinding.inflate(inflater, parent, false)
        return Holder(bind)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = list[position]
        holder.bind.titleView.text = music.title
        holder.bind.subtitleView.text = music.artists.random().title
        holder.bind.albumTitleTextView.text = "Explicit"
        if (music.explicit)
            holder.bind.albumTitleTextView.setTextColor(explicitColor)
        else
            holder.bind.albumTitleTextView.setTextColor(Color.LTGRAY)
        imageLoader.loadImage(artUrl,Type.MUSIC,holder.bind.imageView)
    }

    override fun getItemCount():Int = list.size

    inner class Holder(val bind:ItemMusicOnlineBinding):RecyclerView.ViewHolder(bind.root){
        init {
            itemView.setOnClickListener {
                itemClickInterface.onItemClick(itemView,list[bindingAdapterPosition],Type.MUSIC) }
        }
    }

}
