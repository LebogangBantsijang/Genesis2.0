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
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.settings.ThemeSettings
import com.lebogang.genesis.ui.MainActivity

class BackgroundDialog : DialogFragment(){
    private val viewBinding:DialogBackgroundBinding by lazy { DialogBackgroundBinding.inflate(layoutInflater) }
    private val themeSettings:ThemeSettings by lazy {
        ThemeSettings(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRadioViews()
    }

    private fun initRadioViews(){
        val backgroundType = themeSettings.getBackgroundType()
        viewBinding.radioGroup.check(getValueId(backgroundType))
        if (backgroundType == themeSettings.backgroundTypeAdaptive)
            viewBinding.adaptiveTypeView.isEnabled = true

        viewBinding.adaptiveTypeView.isChecked = themeSettings.isAdaptiveBackgroundBlurry()
        viewBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.noneView-> {
                    themeSettings.setBackgroundType(themeSettings.backgroundTypeNone)
                    viewBinding.adaptiveTypeView.isEnabled = false }
                R.id.adaptiveView-> {
                    themeSettings.setBackgroundType(themeSettings.backgroundTypeAdaptive)
                    viewBinding.adaptiveTypeView.isEnabled = true }
            }
            (requireActivity() as MainActivity).changeBackground(Queue.currentAudio.value?.getArtUri())
        }
        viewBinding.adaptiveTypeView.setOnCheckedChangeListener { _, isChecked ->
            themeSettings.setAdaptiveBackgroundBlurry(isChecked)
            (requireActivity() as MainActivity).changeBackground(Queue.currentAudio.value?.getArtUri())
        }
    }

    private fun getValueId(type:String):Int{
        return when(type){
            themeSettings.backgroundTypeNone-> R.id.noneView
            else -> R.id.adaptiveView
        }
    }

}
