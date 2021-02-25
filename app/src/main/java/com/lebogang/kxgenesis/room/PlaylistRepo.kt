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

package com.lebogang.kxgenesis.room

import androidx.annotation.WorkerThread
import com.lebogang.kxgenesis.room.models.Playlist
import com.lebogang.kxgenesis.room.dao.PlaylistAudioBridgeDao
import com.lebogang.kxgenesis.room.dao.PlaylistDao
import com.lebogang.kxgenesis.room.models.PlaylistBridgeTable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class PlaylistRepo(private val playlistDao: PlaylistDao,
                   private val playlistAudioBridgeDao: PlaylistAudioBridgeDao) {

    suspend fun getPlaylists(): List<Playlist> = coroutineScope{
        val deff = async {
            playlistDao.getPlaylist()
        }
        deff.await()
    }

    suspend fun getPlaylist(id:Long): Playlist = coroutineScope{
        val deff = async {
            playlistDao.getPlaylist(id)
        }
        deff.await()
    }

    suspend fun getPlaylistAudioIds(playlistId:Long):List<Long> = coroutineScope{
        val deff = async {
            playlistAudioBridgeDao.getAudioIds(playlistId)
        }
        deff.await()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertPlaylist(playlist: Playlist){
        playlistDao.insert(playlist)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAudioPlaylist(id:Long, audioId:Long){
        playlistAudioBridgeDao.insertAudio(PlaylistBridgeTable(0, id, audioId))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deletePlaylist(playlist: Playlist){
        playlistAudioBridgeDao.delete(playlist.id)
        playlistDao.delete(playlist)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun clearData(){
        playlistDao.clearData()
        playlistAudioBridgeDao.clearData()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deletePlaylistAudio(playlistId:Long, audioId:Long){
        playlistAudioBridgeDao.delete(playlistId, audioId)
    }

}
