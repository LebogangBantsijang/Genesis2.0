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

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.lebogang.genesis.databinding.ActivitySettingsBinding
import com.lebogang.genesis.ui.dialogs.BackgroundDialog
import com.lebogang.genesis.ui.dialogs.FilterDialog
import com.lebogang.genesis.ui.dialogs.ThemeDialog
import com.lebogang.genesis.ui.helpers.ThemeHelper

class SettingsActivity : ThemeHelper() {
    private val viewBinding:ActivitySettingsBinding by lazy{
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewBinding.backgroundView.setOnClickListener{
            BackgroundDialog().show(supportFragmentManager, "")
        }
        viewBinding.spinner.setSelection((themeSettings.getColumnCount() - 2))
        viewBinding.spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                themeSettings.setColumnCount((position + 2))
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //not needed
            }
        }
    }
}
