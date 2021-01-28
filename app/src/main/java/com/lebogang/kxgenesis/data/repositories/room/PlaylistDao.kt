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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.lebogang.kxgenesis.data.models.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM Playlist")
    fun getPlaylist(): Flow<List<Playlist>>

    @Query("SELECT * FROM Playlist WHERE id =:id")
    fun getPlaylist(id:Long):Flow<Playlist>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: Playlist)

    @Delete
    fun delete(playlist: Playlist)

    @Query("DELETE FROM Playlist")
    fun clearData()
}
