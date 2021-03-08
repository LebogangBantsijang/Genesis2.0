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
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.service.ManageServiceConnection
import com.lebogang.kxgenesis.service.Queue
import com.lebogang.kxgenesis.service.utils.PlaybackState
import com.lebogang.kxgenesis.service.utils.RepeatSate
import com.lebogang.kxgenesis.service.utils.ShuffleSate
import com.lebogang.kxgenesis.settings.PlayerSettings
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.adapters.PlayerViewPagerAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.ui.dialogs.QueueDialog
import com.lebogang.kxgenesis.ui.helpers.PlayerHelper
import com.lebogang.kxgenesis.ui.helpers.ThemeHelper
import com.lebogang.kxgenesis.utils.GlobalBlurry

class PlayerActivity: ThemeHelper(), OnAudioClickListener, PlayerHelper {
    private val playerSettings: PlayerSettings by lazy {
        PlayerSettings(applicationContext)
    }
    private val adapter = PlayerViewPagerAdapter()
    private lateinit var manageServiceConnection: ManageServiceConnection

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        manageServiceConnection = ManageServiceConnection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerSettings.getPlayerResource())
        initToolbar()
        initViewPager()
        initControls()
        observeAudio()
    }

    private fun initToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {onBackPressed()}
    }

    private fun initViewPager(){
        findViewById<ViewPager2>(R.id.viewPager)?.let{
            adapter.listener = this
            it.adapter = adapter
        }
    }

    private fun initControls(){
        findViewById<ImageButton>(R.id.previousView).setOnClickListener {
            manageServiceConnection.musicService.skipToPrevious()
        }
        findViewById<ImageButton>(R.id.playPauseView).setOnClickListener {
            manageServiceConnection.musicService.togglePlayPause()
        }
        findViewById<ImageButton>(R.id.nextView).setOnClickListener {
            manageServiceConnection.musicService.skipToNext()
        }
        findViewById<ImageButton>(R.id.repeatView).setOnClickListener {
            manageServiceConnection.musicService.changeRepeatMode()
        }
        findViewById<ImageButton>(R.id.shuffleView).setOnClickListener {
            manageServiceConnection.musicService.changeShuffleMode()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.player_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.queueMenu -> {
                QueueDialog().show(supportFragmentManager,"")
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        manageServiceConnection.musicService.play(audio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        //not needed
    }

    override fun onPlaybackChanged(playbackState: PlaybackState) {
        if (playbackState == PlaybackState.PLAYING)
            findViewById<ImageButton>(R.id.playPauseView).setImageResource(R.drawable.ic_pause)
        else
            findViewById<ImageButton>(R.id.playPauseView).setImageResource(R.drawable.ic_play)
        Toast.makeText(this, "playback", Toast.LENGTH_SHORT).show()
    }

    override fun onRepeatModeChange(repeatSate: RepeatSate) {
        when(repeatSate){
            RepeatSate.REPEAT_NONE ->
                findViewById<ImageButton>(R.id.repeatView).setImageResource(R.drawable.ic_repeat_none)
            RepeatSate.REPEAT_ALL ->
                findViewById<ImageButton>(R.id.repeatView).setImageResource(R.drawable.ic_repeat)
            RepeatSate.REPEAT_ONE ->
                findViewById<ImageButton>(R.id.repeatView).setImageResource(R.drawable.ic_repeat_one)
        }
    }

    override fun onShuffleModeChange(shuffleSate: ShuffleSate) {
        when(shuffleSate){
            ShuffleSate.SHUFFLE_NONE ->
                findViewById<ImageButton>(R.id.shuffleView).setImageResource(R.drawable.ic_shuffle_none)
            ShuffleSate.SHUFFLE_ALL ->
                findViewById<ImageButton>(R.id.shuffleView).setImageResource(R.drawable.ic_shuffle)
        }
    }

}
