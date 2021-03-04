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

package com.lebogang.kxgenesis.ui.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.service.Queue
import com.lebogang.kxgenesis.settings.PlayerSettings
import com.lebogang.kxgenesis.ui.adapters.PlayerViewPagerAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.utils.GlobalGlide

class PlayerFragment:BottomSheetDialogFragment(),OnAudioClickListener{
    private val playerSettings:PlayerSettings by lazy{
        PlayerSettings(context!!)
    }
    private val adapter = PlayerViewPagerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(playerSettings.getPlayerResource(), container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStandardViews()
        initViewPager()
        initDetails()
    }

    private fun initDetails(){
        Queue.currentAudio.observe(viewLifecycleOwner,{
            GlobalGlide.loadAudioCover(this,view!!.findViewById(R.id.backView),it.albumArtUri)
            GlobalGlide.loadAudioCover(this,view!!.findViewById(R.id.coverView),it.albumArtUri)
            view?.findViewById<TextView>(R.id.titleView)?.text = it.title
            view?.findViewById<TextView>(R.id.subtitleView)?.text = it.artist
            view?.findViewById<TextView>(R.id.durationView)?.text = it.durationFormatted
            adapter.setAudioData(Queue.audioQueue)
            val index = adapter.setNowPlaying(it.id)
            view?.findViewById<ViewPager2>(R.id.viewPager)?.currentItem = index
        })
    }

    private fun initViewPager(){
        adapter.listener = this
        view?.findViewById<ViewPager2>(R.id.viewPager)?.adapter = adapter
    }

    private fun initStandardViews(){
        view?.findViewById<ImageButton>(R.id.minimiseView)?.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onAudioClick(audio: Audio) {
        Queue.setCurrentAudio(audio)
    }

    override fun onAudioClickOptions(audio: Audio) {
        //not needed
    }
}
