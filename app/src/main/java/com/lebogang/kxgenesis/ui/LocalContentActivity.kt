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
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.LayoutNavigationDrawerBinding
import com.lebogang.kxgenesis.service.Queue
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.adapters.LocalContentActivityViewPagerAdapter
import com.lebogang.kxgenesis.ui.dialogs.QueueDialog
import com.lebogang.kxgenesis.ui.players.PlayerFragment
import com.lebogang.kxgenesis.utils.GlobalBlurry
import com.lebogang.kxgenesis.utils.GlobalGlide
import com.lebogang.kxgenesis.utils.TextWatcherSimplifier

class LocalContentActivity : AppCompatActivity() {
    private val viewBinding: LayoutNavigationDrawerBinding by lazy{
        LayoutNavigationDrawerBinding.inflate(layoutInflater)
    }
    private var adapter:LocalContentActivityViewPagerAdapter? = null
    private val themeSettings:ThemeSettings by lazy{
        ThemeSettings(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(themeSettings.getThemeResource())
        setContentView(viewBinding.root)
        initToolbar()
        initNavigationView()
        checkPermissions()
        observeCurrentAudio()
        initNowPlayingViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.local_content_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_refresh ->{
                adapter?.onRefresh(viewBinding.content.viewPager.currentItem)
                true
            }
            R.id.menu_settings ->{
                startActivity(Intent(this,SettingsActivity::class.java))
                true
            }
            else -> false
        }
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
                else -> false
            }
        }
    }

    private fun checkPermissions(){
        when(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PackageManager.PERMISSION_GRANTED -> {
                initViewPager()
                initSearchView()
            }
            PackageManager.PERMISSION_DENIED -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 121)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var granted = false
        grantResults.iterator().forEach {
            granted = it == PackageManager.PERMISSION_GRANTED
        }
        if (granted){
            initViewPager()
            initSearchView()
        }
        else
            showPermissionDialog()
    }

    private fun showPermissionDialog(){
        MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.permission_error))
                .setPositiveButton(getString(R.string.close), null)
                .setMessage(getString(R.string.permission_error_message))
                .setOnDismissListener { finish()}
    }

    private fun initViewPager(){
        adapter = LocalContentActivityViewPagerAdapter(this)
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

    private fun initSearchView(){
        viewBinding.content.searchView.addTextChangedListener(object :TextWatcherSimplifier(){
            override fun textChanged(string: String) {
                adapter?.onSearch(string, viewBinding.content.viewPager.currentItem)
            }
        })
    }

    private fun observeCurrentAudio(){
        Queue.currentAudio.observe(this,{
            if(!viewBinding.content.playingView.launcherView.isVisible)
                viewBinding.content.playingView.launcherView.visibility = View.VISIBLE
            GlobalBlurry.loadBlurryResource(this, it.albumArtUri, viewBinding.content.backView)
            GlobalGlide.loadAudioCover(this,viewBinding.content.playingView.imageView,it.albumArtUri)
            viewBinding.content.playingView.titleView.text = it.title
            viewBinding.content.playingView.subtitleView.text = it.artist
        })
    }

    private fun initNowPlayingViews(){
        viewBinding.content.playingView.queueView.setOnClickListener {
            QueueDialog().show(supportFragmentManager,"")
        }
        /*viewBinding.content.playingView.launcherView.setOnClickListener {
            PlayerFragment().show(supportFragmentManager, "")
        }*/
    }

}
