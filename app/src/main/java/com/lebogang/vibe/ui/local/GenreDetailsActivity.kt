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

package com.lebogang.vibe.ui.local

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Genre
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.databinding.ActivityGenreDetailsBinding
import com.lebogang.vibe.ui.Colors
import com.lebogang.vibe.ui.ImageLoader
import com.lebogang.vibe.ui.ItemOptionsInterface
import com.lebogang.vibe.ui.ModelFactory
import com.lebogang.vibe.ui.local.adapters.MusicAdapter
import com.lebogang.vibe.ui.local.viewmodel.GenreViewModel
import com.lebogang.vibe.ui.local.viewmodel.MusicViewModel
import com.lebogang.vibe.utils.Keys

class GenreDetailsActivity : AppCompatActivity() {
    private val bind:ActivityGenreDetailsBinding by lazy{
        ActivityGenreDetailsBinding.inflate(layoutInflater)}
    private val app:GApplication by lazy { application as GApplication }
    private var genre:Genre? = null
    private val genreViewModel: GenreViewModel by lazy { ModelFactory(app).getGenreViewModel() }
    private val musicViewModel: MusicViewModel by lazy{ ModelFactory(app).getMusicViewModel()}
    private val adapter = MusicAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        genre = intent.extras?.getParcelable(Keys.GENRE_KEY)
        title = genre?.title ?: getString(R.string.genre)
        setContentView(bind.root)
        initToolbar()
        initData()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initData(){
        genre?.let { item->
            genreViewModel.getGenreMusicIds(item.id).observe(this,{
                val idArray = it.toTypedArray()
                initRecyclerView(idArray)
            })
        }
    }

    private fun initRecyclerView(idArray: Array<Long>){
        adapter.selectableBackground = Colors.getSelectableBackground(theme)
        adapter.imageLoader = ImageLoader(this)
        adapter.favouriteClickInterface = getFavouriteInterface()
        adapter.showOptions = false
        musicViewModel.getMusic(idArray).observe(this,{
            adapter.setData(it)
            bind.progressBar.visibility = View.GONE
        })
        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = adapter
    }

    private fun getFavouriteInterface() = object : ItemOptionsInterface {
        override fun onOptionsClick(item: Any) {
            val music = item as Music
            music.isFavourite = !music.isFavourite
            musicViewModel.addMusic(music)
        }
    }

}
