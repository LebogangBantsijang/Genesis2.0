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

package com.lebogang.vibe.utils

import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.ui.local.Sort

object SortHelper {
    fun sortList(sort: Sort,list: List<Music>):List<Music> = list.sortedWith { o1, o2 ->
        when(sort) {
            Sort.TITLE-> when {
                o1.title > o2.title -> 1
                o1.title < o2.title -> -1
                else -> 0
            }
            Sort.NEW -> when {
                o1.date < o2.date -> 1
                o1.date > o2.date -> -1
                else -> 0
            }
            Sort.DURATION -> when {
                o1.duration < o2.duration -> 1
                o1.duration > o2.duration -> -1
                else -> 0
            }
        }
    }


}