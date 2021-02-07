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

package com.lebogang.kxgenesis.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Artist(
        val id:Long,
        val title:String,
        val albumCount:String,
        @Expose
        @SerializedName("picture_small")
        val pictureSmall:String?,
        @Expose
        @SerializedName("picture_medium")
        val pictureMedium: String?,
        @Expose
        @SerializedName("picture_big")
        val pictureBig: String?
)
