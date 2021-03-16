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

package com.lebogang.genesis.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.ActivityStatisticsBinding
import com.lebogang.genesis.room.models.Statistics
import com.lebogang.genesis.ui.adapters.ItemStatsAdapter
import com.lebogang.genesis.ui.adapters.utils.OnStatisticClickListener
import com.lebogang.genesis.ui.dialogs.StatisticsDialog
import com.lebogang.genesis.ui.helpers.ThemeHelper
import com.lebogang.genesis.viewmodels.StatisticsViewModel

class StatisticsActivity : ThemeHelper(),OnStatisticClickListener {
    private val viewBinding: ActivityStatisticsBinding by lazy{
        ActivityStatisticsBinding.inflate(layoutInflater)
    }
    private val statisticsViewModel:StatisticsViewModel by lazy {
        StatisticsViewModel.Factory((application as GenesisApplication).statisticsRepo)
            .create(StatisticsViewModel::class.java)
    }
    private val adapter = ItemStatsAdapter().apply{
        listener = this@StatisticsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar()
        initRecyclerView()
        observeStats()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.statistics_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_delete->{
                statisticsViewModel.clearAll()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView(){
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun observeStats(){
        statisticsViewModel.liveData.observe(this, {
            adapter.setStatData(it)
        })
    }

    override fun onStatisticClick(statistics: Statistics) {
        StatisticsDialog(statistics).show(supportFragmentManager, "")
    }
}
