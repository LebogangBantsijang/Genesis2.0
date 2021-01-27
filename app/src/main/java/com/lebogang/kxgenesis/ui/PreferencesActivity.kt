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

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lebogang.kxgenesis.databinding.ActivityPreferencesBinding
import com.lebogang.kxgenesis.ui.settings.ThemeActivity

class PreferencesActivity : AppCompatActivity() {
    private val binding:ActivityPreferencesBinding by lazy{
        ActivityPreferencesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initThemeClick()
    }

    private fun initThemeClick(){
        binding.themeSettingsView.setOnClickListener {
            startActivity(Intent(this, ThemeActivity::class.java))
        }
    }

}
