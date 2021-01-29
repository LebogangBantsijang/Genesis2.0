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

package com.lebogang.kxgenesis.data.repositories.room

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistAudioBridgeDao {

    @Query("SELECT audioId FROM PlaylistAudioBridge WHERE playlistId =:playlistId")
    fun getAudioIds(playlistId:Long): Flow<List<Long>>

    /**Remove one audio entry*/
    @Query("DELETE FROM PlaylistAudioBridge WHERE playlistId =:playlistId AND audioId=:audioId")
    fun delete(playlistId:Long,audioId:Long)

    /**Remove all playlist audio*/
    @Query("DELETE FROM PlaylistAudioBridge WHERE playlistId =:playlistId")
    fun delete(playlistId: Long)

    @Query("DELETE FROM PlaylistAudioBridge")
    fun clearData()
}
