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

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.databinding.ActivityMusicBinding
import com.lebogang.vibe.ui.Colors
import com.lebogang.vibe.ui.ImageLoader
import com.lebogang.vibe.ui.ItemOptionsInterface
import com.lebogang.vibe.ui.ModelFactory
import com.lebogang.vibe.ui.local.adapters.MusicAdapter
import com.lebogang.vibe.ui.local.dialogs.MusicOptionsDialog
import com.lebogang.vibe.ui.local.viewmodel.MusicViewModel
import com.lebogang.vibe.utils.SortHelper

class MusicActivity : AppCompatActivity() {
    private val app:GApplication by lazy { application as GApplication }
    private val bind:ActivityMusicBinding by lazy{ActivityMusicBinding.inflate(layoutInflater)}
    private val musicViewModel:MusicViewModel by lazy{ ModelFactory(app).getMusicViewModel()}
    private val adapter = MusicAdapter()
    private var sort = Sort.TITLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.music)
        setContentView(bind.root)
        initToolbar()
        initRecyclerView()
        initChipGroup()
        musicViewModel.registerObserver(app)
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initChipGroup(){
        bind.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.titleChip -> {
                    sort = Sort.TITLE
                    adapter.sort(Sort.TITLE)
                }
                R.id.recentChip -> {
                    sort = Sort.NEW
                    adapter.sort(Sort.NEW)
                }
                R.id.durationChip -> {
                    sort = Sort.DURATION
                    adapter.sort(Sort.DURATION)
                }
            }
        }
    }

    private fun initRecyclerView(){
        adapter.selectableBackground = Colors.getSelectableBackground(theme)
        adapter.imageLoader = ImageLoader(this)
        adapter.favouriteClickInterface = getFavouriteInterface()
        adapter.optionsClickInterface = getOptionClickInterface()
        musicViewModel.getMusic().observe(this,{
            val list = SortHelper.sortList(sort,it)
            adapter.setData(list)
            bind.progressBar.visibility = View.GONE
        })
        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = adapter
        registerForContextMenu(bind.recyclerView)
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
            musicOptionsDialog.showNow(supportFragmentManager,null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.application_search_refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.refresh->{
                bind.progressBar.visibility = View.VISIBLE
                musicViewModel.localContent.reset()
                true
            }
            R.id.search->{
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
            else->false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        musicViewModel.unregisterObserver(app)
    }
}
