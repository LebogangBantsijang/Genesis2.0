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

package com.lebogang.kxgenesis.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.FragmentSongsBinding
import com.lebogang.kxgenesis.ui.adapters.ItemLocalSongAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener

class SongsFragment: Fragment(),OnAudioClickListener {
    private lateinit var viewBinding:FragmentSongsBinding
    private val adapter = ItemLocalSongAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter.listener = this
        adapter.fallbackPrimaryTextColor = ResourcesCompat.getColor(resources, R.color.primaryTextColor, null)
        adapter.fallbackSecondaryTextColor = ResourcesCompat.getColor(resources, R.color.secondaryTextColor, null)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
    }

    override fun onAudioClick(audio: Audio) {
        //not finished here
    }

    override fun onAudioClickOptions(audio: Audio) {
        //not finished here
    }

}
