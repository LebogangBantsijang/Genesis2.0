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

package com.lebogang.kxgenesis.ui.helpers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lebogang.kxgenesis.settings.ThemeSettings

abstract class ThemeHelper:AppCompatActivity() {
    val themeSettings: ThemeSettings by lazy{
        ThemeSettings(this)
    }
    private var lastTheme:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastTheme = themeSettings.getThemeResource()
        setTheme(lastTheme)
    }

    override fun onResume() {
        super.onResume()
        if (lastTheme != themeSettings.getThemeResource()){
            recreate()
        }
    }

}
