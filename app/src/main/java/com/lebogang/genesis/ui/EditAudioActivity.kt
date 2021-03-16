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

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import com.google.android.material.snackbar.Snackbar
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.ActivityEditAudioBinding
import com.lebogang.genesis.ui.helpers.ThemeHelper
import com.lebogang.genesis.utils.GlobalBlurry
import com.lebogang.genesis.utils.GlobalGlide
import com.lebogang.genesis.utils.Validator
import com.lebogang.genesis.viewmodels.AudioViewModel

class EditAudioActivity: ThemeHelper() {
    private lateinit var viewBinding:ActivityEditAudioBinding
    private val audioViewModel:AudioViewModel by lazy {
        AudioViewModel.Factory((application as GenesisApplication).audioRepo)
                .create(AudioViewModel::class.java)
    }
    private var audio:Audio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditAudioBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        audio = audioViewModel.getAudio(intent.getLongExtra("Audio",0))
        initToolbar()
        initImageView()
        initUpdateView()
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initImageView(){
        GlobalBlurry.loadBlurryResource(this, audio?.albumArtUri, viewBinding.blurView)
        GlobalGlide.loadAudioCover(this, viewBinding.coverView, audio?.albumArtUri)
    }

    @SuppressLint("InlinedApi")
    private fun initUpdateView(){
        viewBinding.titleView.setText(audio?.title)
        viewBinding.artistView.setText(audio?.artist)
        viewBinding.albumView.setText(audio?.album)
        viewBinding.updateView.setOnClickListener {
            val title = viewBinding.titleView.text?.toString()
            val artist = viewBinding.artistView.text?.toString()
            val album = viewBinding.albumView.text?.toString()
            if (Validator.isValueValid(title) && Validator.isValueValid(artist) &&
                    Validator.isValueValid(album)){
                val values = ContentValues()
                values.put(MediaStore.Audio.Media.TITLE, title)
                values.put(MediaStore.Audio.Media.ARTIST, artist)
                values.put(MediaStore.Audio.Media.ALBUM, album)
                audioViewModel.updateAudio(audio!!, values)
                onBackPressed()
            }else{
                Snackbar.make(viewBinding.root, getString(R.string.null_values), Snackbar.LENGTH_SHORT)
                        .show()
            }
        }
    }
}
