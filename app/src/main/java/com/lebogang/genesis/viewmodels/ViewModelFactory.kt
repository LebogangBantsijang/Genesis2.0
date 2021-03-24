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

package com.lebogang.genesis.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lebogang.genesis.GenesisApplication
import java.lang.IllegalArgumentException

class ViewModelFactory(private val application:Application): ViewModelProvider.Factory {
    private val genesisApplication = application as GenesisApplication

    fun getStatisticsViewModel():StatisticsViewModel{
        return create(StatisticsViewModel::class.java)
    }

    fun getPlaylistViewModel():PlaylistViewModel{
        return create(PlaylistViewModel::class.java)
    }

    fun getAudioViewModel():AudioViewModel{
        return create(AudioViewModel::class.java)
    }

    fun getArtistViewModel():ArtistViewModel{
        return create(ArtistViewModel::class.java)
    }

    fun getAlbumViewModel():AlbumViewModel{
        return create(AlbumViewModel::class.java)
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StatisticsViewModel::class.java) ->
                StatisticsViewModel(genesisApplication.statisticsRepo) as T
            modelClass.isAssignableFrom(PlaylistViewModel::class.java) ->
                PlaylistViewModel(genesisApplication.playlistRepo) as T
            modelClass.isAssignableFrom(AudioViewModel::class.java) ->
                AudioViewModel(genesisApplication.audioRepo) as T
            modelClass.isAssignableFrom(ArtistViewModel::class.java) ->
                ArtistViewModel(genesisApplication.artistRepo) as T
            modelClass.isAssignableFrom(AlbumViewModel::class.java) ->
                AlbumViewModel(genesisApplication.albumRepo) as T
            else -> throw IllegalArgumentException()
        }
    }
}
