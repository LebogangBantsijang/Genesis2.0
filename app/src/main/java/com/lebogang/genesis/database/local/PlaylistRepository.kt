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

package com.lebogang.genesis.database.local

import androidx.annotation.WorkerThread
import com.lebogang.genesis.database.local.models.Playlist
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class PlaylistRepository (private val playlistAccess: PlaylistAccess) {

    fun getPlaylist() = playlistAccess.getPlaylist()

    fun getPlaylistPreview() = playlistAccess.getPlaylistPreview(6)

    fun getPlaylistMusic(playlistId:Long) = playlistAccess.getPlaylistMusicIds(playlistId)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addPlaylist(playlist: Playlist){
        playlistAccess.addPlaylist(playlist)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addPlaylistMusicId(playlistMembers: Playlist.Members){
        playlistAccess.addPlaylistMusicId(playlistMembers)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addPlaylistMusicId(playlistMembers: List<Playlist.Members>){
        playlistAccess.addPlaylistMusicId(playlistMembers)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deletePlaylist(playlist: Playlist){
        playlistAccess.deletePlaylist(playlist)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deletePlaylistMusicId(playlistId: Long,musicId:Long){
        playlistAccess.deletePlaylistMusicId(playlistId,musicId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun reset(){
        playlistAccess.resetMembers()
        playlistAccess.reset()
    }

}
