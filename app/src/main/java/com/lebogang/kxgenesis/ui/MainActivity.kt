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

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.LayoutNavigationDrawerBinding
import com.lebogang.kxgenesis.service.ManageServiceConnection
import com.lebogang.kxgenesis.service.Queue
import com.lebogang.kxgenesis.service.utils.PlaybackState
import com.lebogang.kxgenesis.service.utils.RepeatSate
import com.lebogang.kxgenesis.service.utils.ShuffleSate
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.adapters.LocalContentActivityViewPagerAdapter
import com.lebogang.kxgenesis.ui.dialogs.QueueDialog
import com.lebogang.kxgenesis.ui.helpers.PlayerHelper
import com.lebogang.kxgenesis.ui.helpers.ThemeHelper
import com.lebogang.kxgenesis.utils.GlobalBlurry
import com.lebogang.kxgenesis.utils.GlobalGlide
import com.lebogang.kxgenesis.utils.PermissionManager
import com.lebogang.kxgenesis.utils.TextWatcherSimplifier

class MainActivity : ThemeHelper(), PlayerHelper {
    private val viewBinding: LayoutNavigationDrawerBinding by lazy{
        LayoutNavigationDrawerBinding.inflate(layoutInflater)
    }
    private val adapter:LocalContentActivityViewPagerAdapter by lazy {
        LocalContentActivityViewPagerAdapter(this)
    }
    private lateinit var manageServiceConnection:ManageServiceConnection

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        manageServiceConnection = ManageServiceConnection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar()
        initNavigationView()
        checkPermissions()
    }

    @SuppressLint("RtlHardcoded")
    private fun initToolbar(){
        setSupportActionBar(viewBinding.content.toolbar)
        viewBinding.content.toolbar.setNavigationOnClickListener {
            viewBinding.drawerLayout.openDrawer(Gravity.LEFT)
        }
    }

    private fun initNavigationView(){
        viewBinding.content.viewPager.currentItem
        viewBinding.navigationView.setNavigationItemSelectedListener {
            viewBinding.drawerLayout.closeDrawers()
            true
        }
        viewBinding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_settings->{
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.menu_about->{
                    startActivity(Intent(this,AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun initViewPager(){
        viewBinding.content.viewPager.adapter = adapter
        viewBinding.content.viewPager.offscreenPageLimit = 4
        TabLayoutMediator(viewBinding.content.tabLayout, viewBinding.content.viewPager
        ) { tab, pos ->
            when(pos){
                0 -> tab.icon =
                        ResourcesCompat.getDrawable(resources,R.drawable.ic_music_note_24dp, theme)
                1 -> tab.icon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_music_record_24dp, theme)
                2 -> tab.icon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_round_person_24, theme)
                3 -> tab.icon =
                        ResourcesCompat.getDrawable(resources,R.drawable.ic_music_folder_24dp, theme)
            }
        }.attach()
    }

    private fun initOtherView(){
        viewBinding.content.searchView.addTextChangedListener(object :TextWatcherSimplifier(){
            override fun textChanged(string: String) {
                adapter.onSearch(string, viewBinding.content.viewPager.currentItem)
            }
        })
        viewBinding.content.launcherView.root.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
        viewBinding.content.launcherView.queueView.setOnClickListener {
            QueueDialog().show(supportFragmentManager,"") }
        viewBinding.content.launcherView.playPauseView.setOnClickListener {
            manageServiceConnection.musicService.togglePlayPause()
        }
    }

    private fun observeAudio(){
        Queue.currentAudio.observe(this, {
            if (themeSettings.isBackgroundImageVisible()){
                GlobalBlurry.loadBlurryResource(this, it.albumArtUri, viewBinding.content.backView)
            }
            if (viewBinding.content.launcherView.root.visibility == View.GONE)
                viewBinding.content.launcherView.root.visibility = View.VISIBLE
            viewBinding.content.launcherView.titleView.text = it.title
            viewBinding.content.launcherView.subtitleView.text = it.artist
            GlobalGlide.loadAudioCover(this,viewBinding.content.launcherView.imageView, it.albumArtUri)
        })
    }

    private fun checkPermissions(){
        if (PermissionManager.checkWritePermissionGranted(this)){
            initViewPager()
            initOtherView()
            observeAudio()
        }else
            PermissionManager.requestWritePermission(this)
    }

    fun playAudio(audio:Audio){
        manageServiceConnection.musicService.play(audio)
    }

    override fun onPlaybackChanged(playbackState: PlaybackState){
        if (playbackState == PlaybackState.PLAYING)
            viewBinding.content.launcherView.playPauseView.setImageResource(R.drawable.ic_pause)
        else
            viewBinding.content.launcherView.playPauseView.setImageResource(R.drawable.ic_play)
    }

    override fun onRepeatModeChange(repeatSate: RepeatSate) {
        //not needed
    }

    override fun onShuffleModeChange(shuffleSate: ShuffleSate) {
        //not needed
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initViewPager()
            initOtherView()
            observeAudio()
        }else
            showPermissionDialog()
    }

    private fun showPermissionDialog(){
        MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.permission_error))
                .setPositiveButton(getString(R.string.close), null)
                .setMessage(getString(R.string.permission_error_message))
                .setOnDismissListener { finishAndRemoveTask()}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.local_content_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_refresh ->{
                adapter.onRefresh(viewBinding.content.viewPager.currentItem)
                true
            }
            R.id.menu_settings ->{
                startActivity(Intent(this,SettingsActivity::class.java))
                true
            }
            R.id.menu_about ->{
                startActivity(Intent(this,AboutActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        if (!themeSettings.isBackgroundImageVisible())
            viewBinding.content.backView.visibility = View.INVISIBLE
        else
            viewBinding.content.backView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
