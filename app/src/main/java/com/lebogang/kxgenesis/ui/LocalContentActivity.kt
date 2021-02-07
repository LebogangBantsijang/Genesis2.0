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
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.ActivityLocalContentBinding
import com.lebogang.kxgenesis.ui.adapters.LocalContentActivityViewPagerAdapter

class LocalContentActivity : AppCompatActivity() {
    private val viewBinding:ActivityLocalContentBinding by lazy{
        ActivityLocalContentBinding.inflate(layoutInflater)
    }
    private val localContentActivityViewPagerAdapter:LocalContentActivityViewPagerAdapter by lazy{
        LocalContentActivityViewPagerAdapter(this)
    }
    private val tabOne = 0
    private val tabTwo = 1
    private val tabThree = 2
    private val tabFour = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar()
        checkPermissions()
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

    private fun checkPermissions(){
        when(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PackageManager.PERMISSION_GRANTED -> initViewPager()
            PackageManager.PERMISSION_DENIED -> {
                registerForActivityResult(ActivityResultContracts.RequestPermission()){
                    if (it)
                        initViewPager()
                    else{
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Permission Error")
                            .setPositiveButton("Close", null)
                            .setMessage("The application needs your permission to read your music content."+
                                    " Without access to your files then the application will not function properly.")
                            .setOnDismissListener { finish()}
                    }
                }.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun initViewPager(){
        viewBinding.viewPager.adapter = localContentActivityViewPagerAdapter
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager
        ) { tab, pos ->
            when(pos){
                tabOne -> tab.icon =
                    ResourcesCompat.getDrawable(resources,R.drawable.ic_music_24dp, theme)
                tabTwo -> tab.icon =
                    ResourcesCompat.getDrawable(resources,R.drawable.ic_music_record_24dp, theme)
                tabThree -> tab.icon =
                    ResourcesCompat.getDrawable(resources,R.drawable.ic_microphone_24dp, theme)
                tabFour -> tab.icon =
                    ResourcesCompat.getDrawable(resources,R.drawable.ic_music_folder_24dp, theme)
            }
        }.attach()
    }

}
