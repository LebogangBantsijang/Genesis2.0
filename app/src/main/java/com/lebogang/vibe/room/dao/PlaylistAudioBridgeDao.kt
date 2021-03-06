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

package com.lebogang.vibe.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lebogang.vibe.room.models.PlaylistBridgeTable

@Dao
interface PlaylistAudioBridgeDao {

    @Query("SELECT audioId FROM PlaylistAudioBridge WHERE playlistId =:playlistId")
    suspend fun getAudioIds(playlistId:Long): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(playlistBridgeTable: PlaylistBridgeTable)

    /**Remove one audio entry*/
    @Query("DELETE FROM PlaylistAudioBridge WHERE playlistId =:playlistId AND audioId=:audioId")
    suspend fun delete(playlistId:Long,audioId:Long)

    /**Remove all playlist audio*/
    @Query("DELETE FROM PlaylistAudioBridge WHERE playlistId =:playlistId")
    suspend fun delete(playlistId: Long)

    @Query("DELETE FROM PlaylistAudioBridge")
    suspend fun clearData()
}
