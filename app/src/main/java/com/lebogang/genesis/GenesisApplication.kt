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

package com.lebogang.genesis

import android.app.Application
import com.google.gson.GsonBuilder
import com.lebogang.genesis.data.repositories.AlbumRepo
import com.lebogang.genesis.data.repositories.ArtistRepo
import com.lebogang.genesis.data.repositories.AudioRepo
import com.lebogang.genesis.network.dao.DeezerService
import com.lebogang.genesis.room.GenesisDatabase
import com.lebogang.genesis.room.PlaylistRepo
import com.lebogang.genesis.room.StatisticsRepo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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


    private val deezerRetrofit:Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping()
                .create()))
            .client(OkHttpClient())
            .build()
    }

    val deeserService:DeezerService by lazy{
        deezerRetrofit.create(DeezerService::class.java)
    }

}
