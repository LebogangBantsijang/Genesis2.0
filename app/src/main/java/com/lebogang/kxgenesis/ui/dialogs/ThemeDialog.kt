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

package com.lebogang.kxgenesis.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.DialogThemeBinding
import com.lebogang.kxgenesis.settings.ThemeSettings

class ThemeDialog: DialogFragment() {
    private lateinit var viewBinding:DialogThemeBinding
    private val themeSettings:ThemeSettings by lazy {
        ThemeSettings(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View {
        viewBinding = DialogThemeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRadioViews()
    }

    private fun initRadioViews(){
        viewBinding.radioGroup.check(getCheckId())
        viewBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.lightButton ->{
                    themeSettings.setThemeResource(R.style.Theme_Genesis20_System)
                }
                R.id.darkButton ->{
                    themeSettings.setThemeResource(R.style.Theme_Genesis20_System_Dark)
                }
            }
        }
    }

    private fun getCheckId():Int{
        return when(themeSettings.getThemeResource()){
            R.style.Theme_Genesis20_System_Dark -> R.id.darkButton
            else -> R.id.lightButton
        }
    }
}