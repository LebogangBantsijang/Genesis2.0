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

package com.lebogang.genesis.online.spotify

import com.lebogang.genesis.online.spotify.models.Authentication
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.http.Field
import java.nio.charset.Charset

class SpotifyAuthentication(private val spotifyAuthenticationAccess: SpotifyAuthenticationAccess) {
    private val authentication = Credentials.basic("845adac64a964500ae05d8132be829b8"
        ,"1ea05c91aa3c4c30bdd669796392fbda", Charset.defaultCharset())

    fun authenticate(callBack: Callback<Authentication>){
        spotifyAuthenticationAccess.getAccessToken(authentication,"client_credentials")
            .enqueue(callBack)
    }

}
