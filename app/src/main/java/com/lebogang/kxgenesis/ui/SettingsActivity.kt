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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lebogang.kxgenesis.databinding.ActivitySettingsBinding
import com.lebogang.kxgenesis.settings.ThemeSettings
import com.lebogang.kxgenesis.ui.dialogs.FilterDialog
import com.lebogang.kxgenesis.ui.dialogs.ThemeDialog

class SettingsActivity : AppCompatActivity() {
    private val viewBinding:ActivitySettingsBinding by lazy{
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private val themeSettings: ThemeSettings by lazy{
        ThemeSettings(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(themeSettings.getThemeResource())
        setContentView(viewBinding.root)
        initToolbar()
        initThemeView()
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener {onBackPressed()}
    }

    private fun initThemeView(){
        viewBinding.selectThemeView.setOnClickListener {
            ThemeDialog().show(supportFragmentManager, "")
        }
        viewBinding.filterView.setOnClickListener {
            FilterDialog().show(supportFragmentManager,"")
        }
    }
}
