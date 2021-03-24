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

package com.lebogang.genesis.ui.helpers

import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.service.utils.PlaybackState
import com.lebogang.genesis.service.utils.RepeatSate
import com.lebogang.genesis.service.utils.ShuffleSate

interface PlayerHelper {
    fun onPlaybackChanged(playbackState: PlaybackState)

    fun onRepeatModeChange(repeatSate: RepeatSate)

    fun onShuffleModeChange(shuffleSate: ShuffleSate)

    fun onServiceReady(musicService: MusicService)

    fun playAudio(audio: Audio)

    fun playAudio(audio:Audio, audioList:MutableList<Audio>)
}
