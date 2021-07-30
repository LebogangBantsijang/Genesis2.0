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
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.R
import com.lebogang.genesis.database.local.models.Music
import com.lebogang.genesis.databinding.ItemMusicBinding
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ItemOptionsInterface
import com.lebogang.genesis.ui.Type

class SearchAdapter:RecyclerView.Adapter<SearchAdapter.Holder>(),Filterable{
    private var list = listOf<Music>()
    private var searchResults = mutableListOf<Music>()
    lateinit var imageLoader: ImageLoader
    lateinit var favouriteClickInterface: ItemOptionsInterface
    lateinit var optionsClickInterface: ItemOptionsInterface
    var selectableBackgroundId:Int = 0

    fun setData(list: List<Music>){
        this.list = list
        if (searchResults.isNotEmpty()) searchResults.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemMusicBinding.inflate(inflater,parent,false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = list[position]
        holder.bind.titleTextView.text = music.title
        holder.bind.subtitleView.text = music.artist
        if (music.isFavourite)
            holder.bind.favouriteView.setImageResource(R.drawable.ic_melting_heart_filled_ios)
        else
            holder.bind.favouriteView.setImageResource(R.drawable.ic_melting_heart_ios)
        imageLoader.loadImage(music.artUri, Type.MUSIC,holder.bind.imageView)
    }

    private fun highlight(holder: Holder, isSelected:Boolean){
        if (isSelected)
            holder.bind.root.setBackgroundResource(R.color.primaryColor_light)
        else
            holder.bind.root.setBackgroundResource(selectableBackgroundId)
    }

    override fun getItemCount(): Int = searchResults.size

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchResults.clear()
                constraint?.let {
                    list.forEach { music ->
                        if (music.title.contains(it, false)||music.artist.contains(it, false)){
                            searchResults.add(music)
                        }
                    }
                }
                return FilterResults()
            }
            override fun publishResults(c: CharSequence?, r: FilterResults?) = notifyDataSetChanged()
        }
    }

    inner class Holder(val bind:ItemMusicBinding):RecyclerView.ViewHolder(bind.root){
        init {
            bind.favouriteView.setOnClickListener {
                favouriteClickInterface.onOptionsClick(list[bindingAdapterPosition]) }
            bind.moreView.setOnClickListener {
                optionsClickInterface.onOptionsClick(list[bindingAdapterPosition]) }
        }
    }
}