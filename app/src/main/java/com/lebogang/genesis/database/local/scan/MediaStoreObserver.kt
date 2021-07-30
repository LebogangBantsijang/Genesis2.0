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

package com.lebogang.genesis.database.local.scan

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper

abstract class MediaStoreObserver: ContentObserver(Handler(Looper.getMainLooper())) {

    override fun onChange(selfChange: Boolean) {
        onContentChanged()
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        onContentChanged()
    }

    override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
        onContentChanged()
    }

    override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
        onContentChanged()
    }

    abstract fun onContentChanged()
}
