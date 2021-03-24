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

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.ActivityMainBinding
import com.lebogang.genesis.service.ManageServiceConnection
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.service.utils.PlaybackState
import com.lebogang.genesis.service.utils.RepeatSate
import com.lebogang.genesis.service.utils.ShuffleSate
import com.lebogang.genesis.settings.ThemeSettings
import com.lebogang.genesis.ui.helpers.PlayerHelper
import com.lebogang.genesis.utils.GlobalBlurry
import com.lebogang.genesis.utils.GlobalGlide

class MainActivity : AppCompatActivity(), PlayerHelper {
    private val viewBinding: ActivityMainBinding by lazy{ ActivityMainBinding.inflate(layoutInflater) }
    private val themeSettings:ThemeSettings by lazy{ ThemeSettings(this)}
    private lateinit var musicService: MusicService
    private lateinit var appBarConfiguration:AppBarConfiguration

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ManageServiceConnection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(themeSettings.getThemeResource())
        setContentView(viewBinding.root)
        initNavigation()
        observeAudio()
        initOtherView()
    }

    @SuppressLint("RtlHardcoded")
    private fun initNavigation(){
        setSupportActionBar(viewBinding.toolbar)
        val navHost = supportFragmentManager.findFragmentById(R.id.navFragmentContainer) as NavHostFragment
        val controller = navHost.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.songsFragment,R.id.albumsFragment
                ,R.id.artistFragment,R.id.playlistFragment), viewBinding.drawerLayout)
        setupActionBarWithNavController(controller,appBarConfiguration)
        viewBinding.navigationView.setupWithNavController(controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        val controller = findNavController(R.id.navFragmentContainer)
        return controller.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initOtherView(){
        viewBinding.launcherView.root.setOnClickListener {
            //bottom sheets
        }
        viewBinding.launcherView.queueView.setOnClickListener {
            val controller = findNavController(R.id.navFragmentContainer)
            controller.navigate(R.id.queueDialog)
        }
        viewBinding.launcherView.playPauseView.setOnClickListener {
            musicService.togglePlayPause()
        }
    }

    private fun observeAudio(){
        Queue.currentAudio.observe(this, {
            if (viewBinding.launcherView.root.visibility == View.GONE)
                viewBinding.launcherView.root.visibility = View.VISIBLE
            viewBinding.launcherView.titleView.text = it.title
            viewBinding.launcherView.subtitleView.text = it.artist
            GlobalGlide.loadAudioCover(this, viewBinding.launcherView.imageView, it.getArtUri())
            changeBackground(it.getArtUri())
        })
    }

    fun changeBackground(uri: Uri?){
        when(themeSettings.getBackgroundType()){
            themeSettings.backgroundTypeNone->{
                viewBinding.backView.setImageBitmap(null)
            }
            themeSettings.backgroundTypeAdaptive->{
                if (themeSettings.getBackgroundType() == themeSettings.backgroundTypeAdaptive){
                    if (themeSettings.isAdaptiveBackgroundBlurry())
                        GlobalBlurry.loadBlurryResource(this, uri, viewBinding.backView)
                    else
                        GlobalGlide.loadAudioBackground(this, viewBinding.backView, uri)
                }
            }
        }
    }

    override fun playAudio(audio:Audio){
        Queue.setCurrentAudio(audio)
        musicService.play(audio)
    }

    override fun playAudio(audio:Audio, audioList:MutableList<Audio>){
        Queue.setCurrentAudio(audio, audioList)
        musicService.play(audio)
    }

    override fun onPlaybackChanged(playbackState: PlaybackState){
        if (playbackState == PlaybackState.PLAYING)
            viewBinding.launcherView.playPauseView.setImageResource(R.drawable.ic_pause)
        else
            viewBinding.launcherView.playPauseView.setImageResource(R.drawable.ic_play)
    }

    override fun onRepeatModeChange(repeatSate: RepeatSate) {
        //not needed
    }

    override fun onShuffleModeChange(shuffleSate: ShuffleSate) {
        //not needed
    }

    override fun onServiceReady(musicService: MusicService) {
        this.musicService = musicService
    }

}
