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

import androidx.appcompat.app.AppCompatActivity
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.servicehelpers.OnStateChangedListener
import com.lebogang.genesis.settings.PlayerBackgroundType

abstract class CommonActivity : AppCompatActivity(){

    abstract fun getMusicService():MusicService?

    abstract fun onServiceReady(musicService: MusicService)

    abstract fun changePlayerBackground(type: PlayerBackgroundType)

    abstract fun getStateChangedListener(): OnStateChangedListener
}
