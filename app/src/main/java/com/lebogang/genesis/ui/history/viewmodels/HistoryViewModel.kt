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

package com.lebogang.genesis.ui.history.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lebogang.genesis.database.history.HistoryRepository
import com.lebogang.genesis.database.history.models.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

class HistoryViewModel(private val historyRepository: HistoryRepository): ViewModel() {
    fun getLocalHistory() = historyRepository.getLocalHistory().asLiveData()

    fun getChartHistory() = historyRepository.getChartHistory().asLiveData()

    fun getStreamHistory() = historyRepository.getStreamHistory().asLiveData()

    companion object fun addHistory(title:String, artist:String,album:String,
                                    type:String,release:String,artUri:String){
        val date = Calendar.getInstance().time.toString()
        val history = History(0, title, artist, album, date, type, release, artUri)
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            historyRepository.addHistory(history)
        }
    }
}