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

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.R
import com.lebogang.vibe.databinding.ActivitySearchBinding
import com.lebogang.vibe.ui.ModelFactory
import com.lebogang.vibe.ui.local.adapters.SearchAdapter
import com.lebogang.vibe.ui.local.viewmodel.MusicViewModel

class SearchActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private val bind:ActivitySearchBinding by lazy{ActivitySearchBinding.inflate(layoutInflater)}
    private val app: GApplication by lazy { application as GApplication }
    private val musicViewModel: MusicViewModel by lazy{ ModelFactory(app).getMusicViewModel()}
    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.application_search_widget, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as SearchView?)?.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
            isIconified = false
            setOnQueryTextListener(this@SearchActivity)
        }
        return true
    }

    private fun initRecyclerView(){
        musicViewModel.getMusic().observe(this,{adapter.setData(it)})
        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = adapter
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

}
