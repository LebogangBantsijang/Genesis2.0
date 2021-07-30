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

package com.lebogang.genesis.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.ActivityHistoryBinding
import com.lebogang.genesis.ui.history.adapters.PagerAdapter

class HistoryActivity : AppCompatActivity() {
    private val bind:ActivityHistoryBinding by lazy{ActivityHistoryBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        initToolbar()
        initViewPager()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initViewPager(){
        bind.viewpager.adapter = PagerAdapter(this)
        TabLayoutMediator(bind.tabLayout,bind.viewpager){tab,position->
            when(position){
                0->{
                    tab.text = getString(R.string.local)
                    tab.icon = getDrawable(R.drawable.ic_musical_notes_ios)
                }
                1->{
                    tab.text = getString(R.string.charts)
                    tab.icon = getDrawable(R.drawable.ic_world_ios)
                }
                2->{
                    tab.text = getString(R.string.stream)
                    tab.icon = getDrawable(R.drawable.ic_stream_ios)
                }
            }
        }.attach()
    }
}
