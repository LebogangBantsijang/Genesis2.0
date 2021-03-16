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

package com.lebogang.genesis.ui

import android.os.Bundle
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.ActivityInfoBinding
import com.lebogang.genesis.ui.helpers.ThemeHelper
import com.lebogang.genesis.utils.GlobalBlurry
import com.lebogang.genesis.utils.GlobalGlide
import com.lebogang.genesis.viewmodels.AudioViewModel

class InfoActivity : ThemeHelper() {
    private val viewBinding:ActivityInfoBinding by lazy {
        ActivityInfoBinding.inflate(layoutInflater)
    }
    private var audio:Audio? = null
    private val audioViewModel:AudioViewModel by lazy {
        AudioViewModel.Factory((application as GenesisApplication).audioRepo)
            .create(AudioViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        audio = audioViewModel.getAudio(intent.getLongExtra("Audio",0))
        initToolbar()
        initDetails()
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initDetails(){
        audio?.let {
            viewBinding.titleView.text = it.title
            viewBinding.artistView.text = it.artist
            viewBinding.albumView.text = it.album
            viewBinding.durationView.text = it.durationFormatted
            viewBinding.sizeView.text = it.size
            GlobalGlide.loadAudioCover(this, viewBinding.coverView, it.albumArtUri)
            GlobalBlurry.loadBlurryResource(this,it.albumArtUri,viewBinding.blurView)
        }
    }
}
