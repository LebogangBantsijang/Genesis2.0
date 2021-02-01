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

package com.lebogang.kxgenesis.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.ActivityLocalContentBinding
import com.lebogang.kxgenesis.ui.adapters.LocalContentActivityViewPagerAdapter

class LocalContentActivity : AppCompatActivity() {
    private val viewBinding:ActivityLocalContentBinding by lazy{
        ActivityLocalContentBinding.inflate(layoutInflater)
    }
    private val localContentActivityViewPagerAdapter = LocalContentActivityViewPagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar()
        initViewPager()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.local_content_toolbar_menu, menu)
        return true
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initViewPager(){
        viewBinding.viewPager.adapter = localContentActivityViewPagerAdapter
        TabLayoutMediator(viewBinding.tablayout, viewBinding.viewPager
        ) { tab, _ ->
            when(tab.id){
                R.id.songsTab -> tab.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_music_24dp, theme)
                R.id.albumsTab -> tab.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_music_record_24dp, theme)
                R.id.artistsTab -> tab.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_microphone_24dp, theme)
                R.id.playlistsTab -> tab.icon = ResourcesCompat.getDrawable(resources,R.drawable.ic_music_folder_24dp, theme)
            }
        }
    }

}
