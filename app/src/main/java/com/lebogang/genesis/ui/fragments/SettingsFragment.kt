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

package com.lebogang.genesis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.FragmentSettingsBinding
import com.lebogang.genesis.settings.ThemeSettings

class SettingsFragment : Fragment(){
    private val viewBinding:FragmentSettingsBinding by lazy{ FragmentSettingsBinding.inflate(layoutInflater)}
    //private val themeSettings:ThemeSettings by lazy{ ThemeSettings(requireContext())}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backgroundView.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.backgroundDialog)
        }
        viewBinding.filterView.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.filterDialog)
        }
        viewBinding.selectThemeView.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.themeDialog)
        }
    }

}
