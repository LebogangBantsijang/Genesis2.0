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

package com.lebogang.kxgenesis

import android.app.Application
import com.lebogang.kxgenesis.data.repositories.AlbumRepo
import com.lebogang.kxgenesis.data.repositories.ArtistRepo
import com.lebogang.kxgenesis.data.repositories.AudioRepo
import com.lebogang.kxgenesis.room.GenesisDatabase
import com.lebogang.kxgenesis.room.PlaylistRepo
import com.lebogang.kxgenesis.room.StatisticsRepo

class GenesisApplication:Application() {
    private val genesisDatabase: GenesisDatabase by lazy {
        GenesisDatabase.getDatabase(this)
    }

    val playlistRepo: PlaylistRepo by lazy{
        PlaylistRepo(genesisDatabase.playlistDao(),genesisDatabase.playlistAudioBridge())
    }

    val statisticsRepo:StatisticsRepo by lazy {
        StatisticsRepo(genesisDatabase.statisticsDao())
    }

    val audioRepo:AudioRepo by lazy {
        AudioRepo(this)
    }

    val albumRepo:AlbumRepo by lazy {
        AlbumRepo(this)
    }

    val artistRepo:ArtistRepo by lazy {
        ArtistRepo(this)
    }

}
