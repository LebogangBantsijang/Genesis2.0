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
import android.provider.MediaStore
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.repositories.LocalAudio
import com.lebogang.kxgenesis.data.repositories.SORT_BY_TITLE

private const val THEME_MODE = "THEME_MODE"
private const val AUDIO_SORT = "SORT_ORDER"
private const val AUDIO_DURATION = "AUDIO_DURATION"


class AppPreferences(private val context: Context) : ThemeInterface, AudioInterface{
    private val primaryColorPrefKey = "PrimaryColor"

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name), Context.MODE_PRIVATE)

    @ColorInt
    private val primaryColor = ContextCompat.getColor(context, R.color.primaryColor)

    override fun getThemeMode(): Int {
        return sharedPreferences.getInt(THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    override fun setThemeMode(mode: Int) {
        sharedPreferences.edit().putInt(THEME_MODE, mode).apply()
    }

    override fun setPrimaryColor(@ColorInt colorRes: Int) {
        sharedPreferences.edit().putInt(primaryColorPrefKey, colorRes).apply()
    }

    override fun getPrimaryColor(): Int {
        return sharedPreferences.getInt(primaryColorPrefKey, primaryColor)
    }

    override fun setSortOrder(order: String) {
        sharedPreferences.edit().putString(AUDIO_SORT, order).apply()
    }

    override fun getSortOrder(): String {
        return sharedPreferences.getString(AUDIO_SORT, SORT_BY_TITLE)!!
    }

    override fun setAudioDurationFilter(duration: Long) {
        sharedPreferences.edit().putLong(AUDIO_DURATION, duration).apply()
    }

    override fun getAudioDurationFilter(): Long {
        return sharedPreferences.getLong(AUDIO_DURATION, 0)
    }


}