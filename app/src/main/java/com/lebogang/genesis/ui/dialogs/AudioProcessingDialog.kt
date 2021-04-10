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

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.lebogang.genesis.databinding.DialogAudioFxBinding
import com.lebogang.genesis.interfaces.AudioFxType
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.ui.MainActivity
import com.lebogang.genesis.ui.helpers.SeekBarHelper

class AudioProcessingDialog : DialogFragment(){
    private val viewBinding: DialogAudioFxBinding by lazy { DialogAudioFxBinding.inflate(layoutInflater) }
    private val musicService : MusicService by lazy { (requireActivity() as MainActivity).musicService }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            initView()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initView(){
        val isEnabled = musicService.isAudioFxEnabled()
        viewBinding.enable.isChecked = isEnabled
        if (isEnabled){
            setLevels()
        }
        viewBinding.enable.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                musicService.enableAudioFx()
                setLevels()
            }
            else{
                musicService.disableAudioFx()
                viewBinding.gain.progress = 0
                viewBinding.limiter.progress = 0
                viewBinding.postEq.progress = 0
                viewBinding.compressor.progress = 0
                viewBinding.preEq.progress = 0
            }
        }
        viewBinding.preEq.setOnSeekBarChangeListener(object : SeekBarHelper(){
            override fun progressChanged(progress: Int) {
                if (musicService.isAudioFxEnabled()){
                    musicService.setEffectLevel(progress.toFloat(), AudioFxType.PRE_EQ)
                }
            }
        })
        viewBinding.compressor.setOnSeekBarChangeListener(object : SeekBarHelper(){
            override fun progressChanged(progress: Int) {
                if (musicService.isAudioFxEnabled()){
                    musicService.setEffectLevel(progress.toFloat(), AudioFxType.COMPRESSOR)
                }
            }
        })
        viewBinding.postEq.setOnSeekBarChangeListener(object : SeekBarHelper(){
            override fun progressChanged(progress: Int) {
                if (musicService.isAudioFxEnabled()){
                    musicService.setEffectLevel(progress.toFloat(), AudioFxType.POST_EQ)
                }
            }
        })
        viewBinding.limiter.setOnSeekBarChangeListener(object : SeekBarHelper(){
            override fun progressChanged(progress: Int) {
                if (musicService.isAudioFxEnabled()){
                    musicService.setEffectLevel(progress.toFloat(), AudioFxType.LIMITER)
                }
            }
        })
        viewBinding.gain.setOnSeekBarChangeListener(object : SeekBarHelper(){
            override fun progressChanged(progress: Int) {
                if (musicService.isAudioFxEnabled()){
                    musicService.setEffectLevel(progress.toFloat(), AudioFxType.MASTER_GAIN)
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setLevels(){
        viewBinding.gain.progress = musicService.getEffectLevel(AudioFxType.MASTER_GAIN)
        viewBinding.limiter.progress = musicService.getEffectLevel(AudioFxType.LIMITER)
        viewBinding.postEq.progress = musicService.getEffectLevel(AudioFxType.POST_EQ)
        viewBinding.compressor.progress = musicService.getEffectLevel(AudioFxType.COMPRESSOR)
        viewBinding.preEq.progress = musicService.getEffectLevel(AudioFxType.PRE_EQ)
    }
}