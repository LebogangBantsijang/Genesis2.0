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
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.gson.GsonBuilder
import com.lebogang.genesis.data.repositories.AlbumRepo
import com.lebogang.genesis.data.repositories.ArtistRepo
import com.lebogang.genesis.data.repositories.AudioRepo
import com.lebogang.genesis.database.history.HistoryRepository
import com.lebogang.genesis.database.local.*
import com.lebogang.genesis.database.local.scan.LocalContent
import com.lebogang.genesis.network.DeezerRepo
import com.lebogang.genesis.network.dao.DeezerService
import com.lebogang.genesis.room.GenesisDatabase
import com.lebogang.genesis.room.PlaylistRepo
import com.lebogang.genesis.room.StatisticsRepo
import com.lebogang.genesis.database.user.UserRepository
import com.lebogang.genesis.online.deezer.DeezerAccess
import com.lebogang.genesis.online.deezer.DeezerRepository
import com.lebogang.genesis.online.spotify.SpotifyAccess
import com.lebogang.genesis.online.spotify.SpotifyAuthentication
import com.lebogang.genesis.online.spotify.SpotifyAuthenticationAccess
import com.lebogang.genesis.online.spotify.SpotifyRepository
import com.lebogang.genesis.utils.Keys
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GenesisApplication:Application() {
    //new
    private val appDatabase:GensysDatabase by lazy { GensysDatabase.getDatabase(this) }
    val musicRepository:MusicRepository by lazy {MusicRepository(appDatabase.getMusicAccess())}
    val albumRepository:AlbumRepository by lazy {AlbumRepository(appDatabase.getAlbumAccess())}
    val artistRepository:ArtistRepository by lazy {ArtistRepository(appDatabase.getArtistAccess())}
    val genreRepository:GenreRepository by lazy {GenreRepository(appDatabase.getGenreAccess())}
    val playlistRepository:PlaylistRepository by lazy {PlaylistRepository(appDatabase.getPlaylistAccess())}
    val historyRepository:HistoryRepository by lazy{HistoryRepository(appDatabase.getHistoryAccess())}
    val userRepository:UserRepository by lazy{UserRepository(appDatabase.getUserAccess())}
    val localContent:LocalContent by lazy {
        LocalContent(this,musicRepository, albumRepository, artistRepository,genreRepository) }
    val deezerRepository:DeezerRepository by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping()
                .create()))
            .client(OkHttpClient())
            .build()
        val deezerAccess = retrofit.create(DeezerAccess::class.java)
        DeezerRepository(deezerAccess)
    }
    val spotifyAuthentication:SpotifyAuthentication by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping()
                .create()))
            .client(OkHttpClient())
            .build()
        val authenticationAccess = retrofit.create(SpotifyAuthenticationAccess::class.java)
        SpotifyAuthentication(authenticationAccess)
    }
    val spotifyRepository:SpotifyRepository by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping()
                .create()))
            .client(OkHttpClient())
            .build()
        val spotifyAccess = retrofit.create(SpotifyAccess::class.java)
        SpotifyRepository(spotifyAccess,spotifyAuthentication)
    }


    override fun onCreate() {
        super.onCreate()
        val theme = getSharedPreferences(Keys.PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getInt(Keys.THEME_KEY,AppCompatDelegate.MODE_NIGHT_YES)
        AppCompatDelegate.setDefaultNightMode(theme)
    }

    //old
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
            .baseUrl("https://api.deezer.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping()
                .create()))
            .client(OkHttpClient())
            .build()
    }

    private val deezerService:DeezerService by lazy{
        deezerRetrofit.create(DeezerService::class.java)
    }

    val deezerRepo:DeezerRepo by lazy {
        DeezerRepo(deezerService)
    }

}
