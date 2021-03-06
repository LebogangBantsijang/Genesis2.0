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

package com.lebogang.kxgenesis.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.lebogang.kxgenesis.R

class ThemeSettings(private val context: Context) {
    private val preferences: SharedPreferences = context.applicationContext
            .getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val themeKey = "ThemeResource"
    private val imageKey = "BackgroundKey"

    fun getThemeResource():Int{
        return preferences.getInt(themeKey, R.style.Theme_Genesis20_System_Dark)
    }

    fun setThemeResource(resource:Int){
        preferences.edit().putInt(themeKey, resource).apply()
    }

    fun makeBackgroundImageVisible(value:Boolean){
        preferences.edit().putBoolean(imageKey, value).apply()
    }

    fun isBackgroundImageVisible():Boolean{
        return preferences.getBoolean(imageKey, true)
    }
}
