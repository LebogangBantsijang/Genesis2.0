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

import androidx.room.*
import com.lebogang.vibe.room.models.Statistics
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao {

    @Query("SELECT * FROM Statistics ORDER BY playCount DESC LIMIT 50")
    fun getStats():Flow<List<Statistics>>

    @Query("SELECT * FROM Statistics WHERE audioId = :id")
    suspend fun getStat(id:Long):Statistics?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStat(statistics: Statistics)

    @Delete
    suspend fun deleteStat(statistics: Statistics)

    @Query("DELETE FROM Statistics")
    suspend fun clearAll()
}
