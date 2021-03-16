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

package com.lebogang.genesis.settings

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.lebogang.genesis.R

class ThemeSettings(private val context: Context) {
    private val preferences: SharedPreferences = context.applicationContext
            .getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val themeKey = "ThemeResource"
    private val itemDisplay = "ColumnCount"
    private val powerKey = "PowerKey"
    private val backgroundKey = "BackgroundKey"
    private val backgroundImageResourceKey = "ResourcesKey"
    val backgroundTypeNone = "None"
    val backgroundTypeAdaptive = "Adaptive"
    val backgroundTypeCustom = "Custom"

    fun getThemeResource():Int{
        return preferences.getInt(themeKey, R.style.Theme_Genesis20_System_Dark)
    }

    fun setThemeResource(resource:Int){
        preferences.edit().putInt(themeKey, resource).apply()
    }

    fun getColumnCount():Int{
        return preferences.getInt(itemDisplay, 2)
    }

    fun setColumnCount(count:Int){
        preferences.edit().putInt(itemDisplay, count).apply()
    }

    fun setBackgroundType(value:String){
        preferences.edit().putString(backgroundKey, value).apply()
    }

    fun getBackgroundType():String{
        return preferences.getString(backgroundKey, backgroundTypeAdaptive)!!
    }

    fun setBackgroundImageResource(data:Uri){
        preferences.edit().putString(backgroundImageResourceKey, data.toString()).apply()
    }

    fun getBackgroundImageResource():Uri{
        val uriString = preferences.getString(backgroundImageResourceKey, "")
        return Uri.parse(uriString)
    }
}
