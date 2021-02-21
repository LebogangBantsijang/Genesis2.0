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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.LayoutNavigationDrawerBinding
import com.lebogang.kxgenesis.ui.adapters.LocalContentActivityViewPagerAdapter

class LocalContentActivity : AppCompatActivity() {
    private val viewBinding: LayoutNavigationDrawerBinding by lazy{
        LayoutNavigationDrawerBinding.inflate(layoutInflater)
    }
    private val localContentActivityViewPagerAdapter:LocalContentActivityViewPagerAdapter by lazy{
        LocalContentActivityViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar()
        initNavigationView()
        checkPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.local_content_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_search ->{
                startActivity(Intent(this, SearchActivity::class.java))
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
        viewBinding.navigationView.setNavigationItemSelectedListener {
            viewBinding.drawerLayout.closeDrawers()
            true
        }
    }


    private fun checkPermissions(){
        when(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PackageManager.PERMISSION_GRANTED -> initViewPager()
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
        if (granted)
            initViewPager()
        else
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.permission_error))
                .setPositiveButton(getString(R.string.close), null)
                .setMessage(getString(R.string.permission_error_message))
                .setOnDismissListener { finish()}
    }

    private fun initViewPager(){
        viewBinding.content.viewPager.adapter = localContentActivityViewPagerAdapter
        TabLayoutMediator(viewBinding.content.tabLayout, viewBinding.content.viewPager
        ) { tab, pos ->
            when(pos){
                0 -> {
                    tab.icon =
                        ResourcesCompat.getDrawable(resources,R.drawable.ic_music_24dp, theme)
                }
                1 -> {
                    tab.icon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_music_record_24dp, theme)
                }
                2 -> {
                    tab.icon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_round_person_24, theme)
                }
                3 -> {
                    tab.icon =
                        ResourcesCompat.getDrawable(resources,R.drawable.ic_music_folder_24dp, theme)
                }
            }
        }.attach()
    }

}
