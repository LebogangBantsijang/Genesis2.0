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

package com.lebogang.genesis.online.stream.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id:Long,
    val title:String,
    val album:String,
    val coverSmall:String,
    val coverMedium:String,
    val coverLarge:String,
    val releaseDate:Long,
    val releaseType:String,
    val nbTracks:Int,
    val artistIds:List<Long>,
    val genre:List<String>):AbstractDetails() {

    override fun getImagePath(): String = coverMedium
}