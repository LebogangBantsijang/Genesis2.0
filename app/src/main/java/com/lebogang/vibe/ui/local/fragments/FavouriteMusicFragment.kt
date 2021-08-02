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

package com.lebogang.vibe.ui.local.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.databinding.FragmentFavouriteLayoutBinding
import com.lebogang.vibe.ui.ImageLoader
import com.lebogang.vibe.ui.ItemOptionsInterface
import com.lebogang.vibe.ui.local.adapters.MusicAdapter
import com.lebogang.vibe.ui.local.dialogs.MusicOptionsDialog
import com.lebogang.vibe.ui.ModelFactory
import com.lebogang.vibe.ui.local.viewmodel.MusicViewModel

class FavouriteMusicFragment:Fragment() {
    private lateinit var bind:FragmentFavouriteLayoutBinding
    private val app:GApplication by lazy { requireActivity().application as GApplication }
    private val musicViewModel:MusicViewModel by lazy { ModelFactory(app).getMusicViewModel()}
    private val adapter = MusicAdapter()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        bind = FragmentFavouriteLayoutBinding.inflate(inflater, parent, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter.imageLoader = ImageLoader(requireActivity())
        adapter.favouriteClickInterface = getFavouriteInterface()
        adapter.optionsClickInterface = getOptionClickInterface()
        musicViewModel.getFavourites().observe(viewLifecycleOwner,{adapter.setData(it)})
        bind.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        bind.recyclerView.adapter = adapter
    }

    private fun getFavouriteInterface() = object : ItemOptionsInterface{
        override fun onOptionsClick(item: Any) {
            val music = item as Music
            music.isFavourite = !music.isFavourite
            musicViewModel.addMusic(music)
        }
    }

    private fun getOptionClickInterface() = object :ItemOptionsInterface{
        override fun onOptionsClick(item: Any) {
            val musicOptionsDialog = MusicOptionsDialog()
            musicOptionsDialog.music = item as Music
            musicOptionsDialog.showNow(requireActivity().supportFragmentManager,null)
        }
    }
}
