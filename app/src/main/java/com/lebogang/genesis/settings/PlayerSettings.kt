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

package com.lebogang.genesis.settings

import android.content.Context
import android.content.SharedPreferences
import com.lebogang.genesis.R
import com.lebogang.genesis.service.utils.RepeatSate
import com.lebogang.genesis.service.utils.ShuffleSate

class PlayerSettings(private val context: Context){
    private val preferences: SharedPreferences = context.applicationContext
        .getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val playerKey = "Player"
    private val repeatKey = "Repeat"
    private val shuffleKey = "Shuffle"

    fun setPlayerResource(resource:Int){
        preferences.edit().putInt(playerKey,resource).apply()
    }

    fun getPlayerResource():Int{
        return preferences.getInt(playerKey,R.layout.player_layout_two)
    }

    fun setRepeatMode(repeatSate: RepeatSate){
        when(repeatSate){
            RepeatSate.REPEAT_NONE-> preferences.edit().putInt(repeatKey, 0).apply()
            RepeatSate.REPEAT_ONE-> preferences.edit().putInt(repeatKey, 1).apply()
            RepeatSate.REPEAT_ALL-> preferences.edit().putInt(repeatKey, 2).apply()
        }
    }

    fun getRepeatMode():RepeatSate{
        return when(preferences.getInt(repeatKey,0)){
            1 -> RepeatSate.REPEAT_ONE
            2 -> RepeatSate.REPEAT_ALL
            else -> RepeatSate.REPEAT_NONE
        }
    }

    fun setShuffleMode(shuffleSate: ShuffleSate){
        when(shuffleSate){
            ShuffleSate.SHUFFLE_NONE -> preferences.edit().putInt(shuffleKey, 0).apply()
            ShuffleSate.SHUFFLE_ALL -> preferences.edit().putInt(shuffleKey, 1).apply()
        }
    }

    fun getShuffleMode():ShuffleSate{
        return when(preferences.getInt(shuffleKey, 0)){
            1 -> ShuffleSate.SHUFFLE_ALL
            else -> ShuffleSate.SHUFFLE_NONE
        }
    }
}
