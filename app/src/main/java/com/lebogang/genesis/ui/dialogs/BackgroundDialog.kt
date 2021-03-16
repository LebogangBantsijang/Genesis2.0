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

package com.lebogang.genesis.ui.dialogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.DialogBackgroundBinding
import com.lebogang.genesis.settings.ThemeSettings

const val BACKGROUND_REQUEST_CODE = 8796

class BackgroundDialog : DialogFragment(){
    private val viewBinding:DialogBackgroundBinding by lazy {
        DialogBackgroundBinding.inflate(layoutInflater)
    }
    private val themeSettings:ThemeSettings by lazy {
        ThemeSettings(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View? {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRadioViews()
    }

    private fun initRadioViews(){
        viewBinding.radioGroup.check(getValueId(themeSettings.getBackgroundType()))
        viewBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.noneView-> themeSettings.setBackgroundType(themeSettings.backgroundTypeNone)
                R.id.adaptiveView-> themeSettings.setBackgroundType(themeSettings.backgroundTypeAdaptive)
                R.id.customView-> {
                    themeSettings.setBackgroundType(themeSettings.backgroundTypeCustom)
                    startActivityForResult(Intent(Intent.ACTION_PICK).apply {
                        type = "image/*"
                    }, BACKGROUND_REQUEST_CODE)
                }
            }
        }
    }

    private fun getValueId(type:String):Int{
        return when(type){
            themeSettings.backgroundTypeNone-> R.id.noneView
            themeSettings.backgroundTypeCustom -> R.id.customView
            else -> R.id.adaptiveView
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BACKGROUND_REQUEST_CODE){
            Log.println(Log.ERROR, "Request code: ", "Match")
            data?.data?.let {
                Log.println(Log.ERROR, "Image data: ", "Not Null")
                themeSettings.setBackgroundImageResource(it)
            }
        }
    }
}
