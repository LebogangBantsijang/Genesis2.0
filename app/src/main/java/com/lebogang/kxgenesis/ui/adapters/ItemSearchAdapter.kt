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
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ItemLocalSearchSongBinding
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener

class ItemSearchAdapter:RecyclerView.Adapter<ItemSearchAdapter.Holder>(), Filterable{
    private var listAudio:List<Audio> = listOf()
    private val filteredList = arrayListOf<Audio>()
    var listener:OnAudioClickListener? = null

    fun setAudioData(mutableList: MutableList<Audio>){
        listAudio = mutableList.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemLocalSearchSongBinding.inflate(inflater, parent, false)
        return Holder(viewBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val audio = filteredList[position]
        val subtitle = audio.artist + "-" + audio.album
        holder.viewBinding.titleView.text = audio.title
        holder.viewBinding.subtitleView.text = subtitle
        holder.viewBinding.durationView.text = audio.durationFormatted
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class Holder(val viewBinding:ItemLocalSearchSongBinding):RecyclerView.ViewHolder(viewBinding.root){
        init {
            viewBinding.root.setOnClickListener {
                listener?.onAudioClick(listAudio[adapterPosition])
            }
        }
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResult = FilterResults()
                constraint?.let{query->
                    filteredList.clear()
                    listAudio.iterator().forEach {
                        if (it.title.toLowerCase().contains(query.toString().toLowerCase())
                            ||it.artist.toLowerCase().contains(query.toString().toLowerCase())
                            ||it.album.toLowerCase().contains(query.toString().toLowerCase())){
                            filteredList.add(it)
                        }
                    }
                }
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }
}
