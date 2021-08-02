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

package com.lebogang.vibe.settings

import android.content.Context

class Preferences (context: Context){
    private val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    fun isFirstInstall():Boolean = sharedPreferences.getBoolean(PreferenceKeys.ON_INSTALL, true)

    fun changeInstallValue() = sharedPreferences.edit().putBoolean(PreferenceKeys.ON_INSTALL, false).apply()


    object PreferenceKeys{
        const val ON_INSTALL = "install"
    }
}
