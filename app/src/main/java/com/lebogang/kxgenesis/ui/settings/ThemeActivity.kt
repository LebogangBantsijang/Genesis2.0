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

package com.lebogang.kxgenesis.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.ActivityThemeBinding
import com.lebogang.kxgenesis.settings.AppPreferences

class ThemeActivity : AppCompatActivity() {
    private val binding:ActivityThemeBinding by lazy {
        ActivityThemeBinding.inflate(layoutInflater)
    }
    private val appPreferences : AppPreferences by lazy {
        AppPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSelectThemeViews()
    }

    private fun initSelectThemeViews(){
        binding.selectedThemeView.text = getThemeText(appPreferences.getThemeMode())
        binding.selectThemeView.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                setTitle("Select Theme")
                setNegativeButton("Cancel", null)
                setItems(R.array.ThemeOptions) { _, which ->
                    processThemeSelection(which)
                }
            }.create().show()
        }
    }

    private fun processThemeSelection(selectionIndex:Int){
        when(selectionIndex){
            0 ->{
                appPreferences.setThemeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                binding.selectedThemeView.text = getThemeText(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            1 -> {
                appPreferences.setThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.selectedThemeView.text = getThemeText(AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            2 -> {
                appPreferences.setThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.selectedThemeView.text = getThemeText(AppCompatDelegate.MODE_NIGHT_NO)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun getThemeText(@LayoutRes resource:Int):String{
        return when(resource){
            AppCompatDelegate.MODE_NIGHT_YES -> getString(R.string.dark_theme)
            AppCompatDelegate.MODE_NIGHT_NO -> getString(R.string.light_theme)
            else -> getString(R.string.follow_system)
        }
    }

}
