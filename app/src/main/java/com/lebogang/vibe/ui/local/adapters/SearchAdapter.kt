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
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.databinding.ItemMusicBinding
import com.lebogang.vibe.ui.utils.DiffUtilMusic
import com.lebogang.vibe.ui.utils.ImageLoader
import com.lebogang.vibe.ui.utils.ItemOptionsInterface
import com.lebogang.vibe.ui.utils.Type

class SearchAdapter:RecyclerView.Adapter<SearchAdapter.Holder>(),Filterable{
    private val asyncListDiffer = AsyncListDiffer(this, DiffUtilMusic)
    var list = listOf<Music>()
    private var searchResults = mutableListOf<Music>()
    lateinit var imageLoader: ImageLoader
    lateinit var favouriteClickInterface: ItemOptionsInterface
    lateinit var optionsClickInterface: ItemOptionsInterface
    var selectableBackgroundId:Int = 0

    fun setData(list: List<Music>){
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemMusicBinding.inflate(inflater,parent,false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val music = searchResults[position]
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
                    asyncListDiffer.currentList.forEach { music ->
                        if (music.getItemTitle().contains(it, false)
                            ||music.getItemTitle().contains(it, false)){
                            //searchResults.add(music)
                        }
                    }
                }
                return FilterResults()
            }
            override fun publishResults(c: CharSequence?, r: FilterResults?){
                //fix later
            }
        }
    }

    inner class Holder(val bind:ItemMusicBinding):RecyclerView.ViewHolder(bind.root){
        init {
            bind.favouriteView.setOnClickListener {
                favouriteClickInterface.onOptionsClick(asyncListDiffer.currentList[bindingAdapterPosition]) }
            bind.moreView.setOnClickListener {
                optionsClickInterface.onOptionsClick(asyncListDiffer.currentList[bindingAdapterPosition]) }
        }
    }
}
