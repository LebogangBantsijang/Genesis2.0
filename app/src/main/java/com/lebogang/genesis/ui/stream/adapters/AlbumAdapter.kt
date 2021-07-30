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

package com.lebogang.genesis.ui.stream.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.genesis.databinding.ItemAlbumOnlineBinding
import com.lebogang.genesis.online.stream.models.Album
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.Type

class AlbumAdapter:RecyclerView.Adapter<AlbumAdapter.Holder>(){
    private var list = listOf<Album>()
    lateinit var itemClickInterface: ItemClickInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemAlbumOnlineBinding.inflate(inflater,parent,false)
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val album = list[position]
    }

    override fun getItemCount(): Int = list.size

    inner class Holder(val bind:ItemAlbumOnlineBinding):RecyclerView.ViewHolder(bind.root){
        init {
            itemView.setOnClickListener {
                itemClickInterface.onItemClick(itemView,list[bindingAdapterPosition], Type.ALBUM) }
        }
    }
}