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

package com.lebogang.kxgenesis.data.repositories.local

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
import android.provider.MediaStore.Audio.Artists.ARTIST
import android.provider.MediaStore.Audio.Artists._ID
import android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
import android.util.Log
import com.lebogang.kxgenesis.data.models.Artist

private const val SORT_ARTIST_BY_TITLE = "$ARTIST ASC"

class LocalArtists(val context: Context) {
    val contentResolver = context.applicationContext.contentResolver
    private val projection = arrayOf(_ID, ARTIST, NUMBER_OF_ALBUMS)

    /**
     * Get all artists
     * */
    fun getArtists():LinkedHashMap<String, Artist>{
        val cursor = contentResolver.query(EXTERNAL_CONTENT_URI, projection,
                null, null, SORT_ARTIST_BY_TITLE)
        return loopCursor(cursor)
    }

    /**
     * Get all artists with the specified name
     * @param artistName
     * */
    fun getArtists(artistName:String):LinkedHashMap<String, Artist> {
        val selection = "$ARTIST =?"
        val cursor = contentResolver.query(EXTERNAL_CONTENT_URI, projection,
                selection, arrayOf(artistName), SORT_ARTIST_BY_TITLE)
        return loopCursor(cursor)
    }

    /**
     * Loop through the cursor and build media objects
     * @param cursor]
     * */
    private fun loopCursor(cursor: Cursor?):LinkedHashMap<String, Artist>{
        val linkedHashMap = LinkedHashMap<String, Artist>()
        cursor?.let {
            if (cursor.moveToFirst()){
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(_ID))
                    val title = cursor.getString(cursor.getColumnIndex(ARTIST))
                    val albumCount = cursor.getString(cursor.getColumnIndex(NUMBER_OF_ALBUMS))
                    val artist = Artist(id, title, albumCount, null,null,null)
                    linkedHashMap[title] = artist
                }while (cursor.moveToNext())
            }
            cursor.close()
        }
        return linkedHashMap
    }

}
