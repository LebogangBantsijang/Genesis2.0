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
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.service.Queue
import com.lebogang.kxgenesis.settings.PlayerSettings
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.adapters.PlayerViewPagerAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.utils.GlobalBlurry

class PlayerActivity: AppCompatActivity(), OnAudioClickListener {
    private val themeSettings: ThemeSettings by lazy {
        ThemeSettings(this)
    }
    private val playerSettings: PlayerSettings by lazy {
        PlayerSettings(this)
    }
    private val adapter = PlayerViewPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setTheme(themeSettings.getThemeResource())
        setContentView(R.layout.player_layout_one)
        observeAudio()
        initViewPager()
    }

    private fun initViewPager(){
        findViewById<ViewPager2>(R.id.viewPager)?.let{
            adapter.listener = this
            it.adapter = adapter
        }
    }

    private fun observeAudio(){
        Queue.currentAudio.observe(this, {
            title = it.artist
            findViewById<TextView>(R.id.titleView).text = it.title
            findViewById<TextView>(R.id.subtitleView).text = it.album
            findViewById<TextView>(R.id.durationView).text = it.durationFormatted
            findViewById<ViewPager2>(R.id.viewPager)?.let{ pager->
                adapter.setAudioData(Queue.audioQueue)
                val index = adapter.setNowPlaying(it.id)
                pager.currentItem = index
            }
            findViewById<ImageView>(R.id.backView)?.let { imageView->
                GlobalBlurry.loadBlurryResource(this, it.albumArtUri, imageView)
            }
        })
    }

    override fun onAudioClick(audio: Audio) {
        Queue.setCurrentAudio(audio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        //not needed
    }
}
