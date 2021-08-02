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

package com.lebogang.vibe.ui.stream.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lebogang.vibe.online.stream.models.AbstractDetails
import com.lebogang.vibe.ui.stream.fragments.DetailsInfoFragment
import com.lebogang.vibe.ui.stream.fragments.DetailsVisualFragment

class ViewpagerAdapter(item:AbstractDetails?, activity:FragmentActivity):FragmentStateAdapter(activity) {
    private val list = listOf(DetailsVisualFragment(item),DetailsInfoFragment(item))

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]

}
