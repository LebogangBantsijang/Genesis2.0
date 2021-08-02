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

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Genre
import com.lebogang.vibe.databinding.ItemGenreBinding
import com.lebogang.vibe.ui.ItemClickInterface
import com.lebogang.vibe.ui.Type

class GenreAdapter :RecyclerView.Adapter<GenreAdapter.Holder>(){
    private val asyncListDiffer = AsyncListDiffer(this,DiffCallback)
    private var colorList = mutableListOf<Int>()
    lateinit var itemClickInterface: ItemClickInterface

    fun setData(list: List<Genre>) = asyncListDiffer.submitList(list)

    companion object DiffCallback:DiffUtil.ItemCallback<Genre>(){
        override fun areItemsTheSame(o: Genre, n: Genre): Boolean = o.id == n.id

        override fun areContentsTheSame(o: Genre, n: Genre): Boolean = o.equals(n)
    }

    private fun getRandomColors(context: Context){
        colorList.add(context.getColor(R.color.blue_light))
        colorList.add(context.getColor(R.color.red_light))
        colorList.add(context.getColor(R.color.green_light))
        colorList.add(context.getColor(R.color.gray_light))
        colorList.add(context.getColor(R.color.orange_light))
        colorList.add(context.getColor(R.color.purple_light))
        colorList.add(context.getColor(R.color.red))
        colorList.add(context.getColor(R.color.green))
        colorList.add(context.getColor(R.color.blue))
        colorList.add(context.getColor(R.color.brown))
        colorList.add(context.getColor(R.color.yellow))
        colorList.add(context.getColor(R.color.darkColor3))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        if (colorList.size <= 0)
            getRandomColors(parent.context)
        val inflater = LayoutInflater.from(parent.context)
        val bind = ItemGenreBinding.inflate(inflater, parent, false)
        bind.titleTextView.setBackgroundColor(colorList.random())
        return Holder(bind)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val genre = asyncListDiffer.currentList[position]
        holder.bind.titleTextView.text = genre.title
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class Holder(val bind:ItemGenreBinding):RecyclerView.ViewHolder(bind.root){
        init {
            itemView.setOnClickListener { itemClickInterface
                .onItemClick(itemView,asyncListDiffer.currentList[bindingAdapterPosition],Type.GENRE) }
        }
    }

}
