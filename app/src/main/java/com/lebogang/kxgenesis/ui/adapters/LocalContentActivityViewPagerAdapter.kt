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

package com.lebogang.kxgenesis.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lebogang.kxgenesis.ui.fragments.AlbumsFragment
import com.lebogang.kxgenesis.ui.fragments.ArtistFragment
import com.lebogang.kxgenesis.ui.fragments.PlaylistFragment
import com.lebogang.kxgenesis.ui.fragments.SongsFragment

class LocalContentActivityViewPagerAdapter(fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity){

    private val fragmentList = arrayListOf(SongsFragment(fragmentActivity)
        , AlbumsFragment(fragmentActivity), ArtistFragment(fragmentActivity)
        , PlaylistFragment(fragmentActivity))

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}
