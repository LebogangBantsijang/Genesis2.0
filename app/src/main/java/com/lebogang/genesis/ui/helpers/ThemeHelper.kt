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

package com.lebogang.genesis.ui.helpers

import android.graphics.Color
import android.graphics.ColorSpace
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.lebogang.genesis.R
import com.lebogang.genesis.settings.ThemeSettings

abstract class ThemeHelper:AppCompatActivity() {
    val themeSettings: ThemeSettings by lazy{
        ThemeSettings(this)
    }
    private var lastTheme:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastTheme = themeSettings.getThemeResource()
        setTheme(lastTheme)
        getColors()
    }

    @Suppress("DEPRECATION")
    private fun getColors(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            PRIMARY_COLOR = getColor(R.color.primaryColor)
            SECONDARY_TEXTCOLOR_NO_DISABLE = getColor(R.color.secondaryTextColorLightNoDisable)
            if (themeSettings.isThemeLight()){
                PRIMARY_TEXTCOLOR = getColor(R.color.primaryTextColorLight)
                SECONDARY_TEXTCOLOR = getColor(R.color.secondaryTextColorLight)
            }else{
                PRIMARY_TEXTCOLOR = getColor(R.color.primaryTextColorDark)
                SECONDARY_TEXTCOLOR = getColor(R.color.secondaryTextColorDark)
            }
        }else{
            PRIMARY_COLOR = resources.getColor(R.color.primaryColor)
            if (themeSettings.isThemeLight()){
                PRIMARY_TEXTCOLOR = resources.getColor(R.color.primaryTextColorLight)
                SECONDARY_TEXTCOLOR = resources.getColor(R.color.secondaryTextColorLight)
            }else{
                PRIMARY_TEXTCOLOR = resources.getColor(R.color.primaryTextColorDark)
                SECONDARY_TEXTCOLOR = resources.getColor(R.color.secondaryTextColorDark)
            }
        }
    }

    companion object ThemeColors{
        @ColorInt var PRIMARY_COLOR:Int = 0
        @ColorInt var PRIMARY_TEXTCOLOR:Int = 0
        @ColorInt var SECONDARY_TEXTCOLOR:Int = 0
        @ColorInt var SECONDARY_TEXTCOLOR_NO_DISABLE:Int = 0
    }
}
