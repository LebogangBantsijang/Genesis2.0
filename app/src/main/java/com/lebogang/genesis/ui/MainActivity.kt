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
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.ActivityMainBinding
import com.lebogang.genesis.interfaces.OnStateChangedListener
import com.lebogang.genesis.service.ManageServiceConnection
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.settings.PlayerBackgroundType
import com.lebogang.genesis.ui.helpers.ThemeHelper
import com.lebogang.genesis.utils.SeekBarThreader

class MainActivity : ThemeHelper() {
    private val viewBinding: ActivityMainBinding by lazy{ ActivityMainBinding.inflate(layoutInflater) }
    lateinit var musicService: MusicService
    private lateinit var appBarConfiguration:AppBarConfiguration
    private lateinit var player: Player

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ManageServiceConnection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initNavigation()
        initBanner()
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
        return controller.navigateUp(appBarConfiguration) || player.isSheetShowing() ||super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val controller = findNavController(R.id.navFragmentContainer)
        if (!controller.navigateUp() && !player.isSheetShowing())
            moveTaskToBack(true)
    }

    fun getStateChangedListener(): OnStateChangedListener {
        return player.getStateListener()
    }

    fun changePlayerBackground(type:PlayerBackgroundType){
        if (type == PlayerBackgroundType.GIF)
            player.loadGif()
        else
            player.changeBackground()
    }

    fun onServiceReady(musicService: MusicService) {
        this.musicService = musicService
        player = Player(this, viewBinding).also {
            it.musicService = musicService
            it.seekBarThreader = SeekBarThreader(this, musicService)
        }
    }

    private fun initBanner(){
        MobileAds.initialize(this)
        viewBinding.adView.loadAd(AdRequest.Builder()
                .build())
    }

}
