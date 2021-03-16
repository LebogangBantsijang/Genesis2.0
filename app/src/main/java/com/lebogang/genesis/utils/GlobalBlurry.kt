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

package com.lebogang.genesis.utils

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.wasabeef.blurry.Blurry

object GlobalBlurry {
    fun loadBlurryResource(activity:AppCompatActivity,imageUri: Uri?, imageView:ImageView){
        Glide.with(activity)
                .asBitmap()
                .load(imageUri)
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?
                                              , target: Target<Bitmap>?, isFirstResource
                                              : Boolean): Boolean {
                        imageView.setImageBitmap(null)
                        return e != null
                    }
                    override fun onResourceReady(resource: Bitmap?, model: Any?,
                                                 target: Target<Bitmap>?, dataSource: DataSource?
                                                 , isFirstResource: Boolean): Boolean {
                        if (resource!=null)
                            Blurry.with(activity).async()
                                    .radius(10)
                                    .sampling(4)
                                    .from(resource)
                                    .into(imageView)
                        else
                            imageView.setImageBitmap(null)
                        return true
                    }
                })
                .submit()
    }

    fun loadBlurryResource(fragment:Fragment,imageUri: Uri?, imageView:ImageView){
        Glide.with(fragment)
                .asBitmap()
                .load(imageUri)
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?
                                              , target: Target<Bitmap>?, isFirstResource
                                              : Boolean): Boolean {
                        imageView.setImageBitmap(null)
                        return e != null
                    }
                    override fun onResourceReady(resource: Bitmap?, model: Any?,
                                                 target: Target<Bitmap>?, dataSource: DataSource?
                                                 , isFirstResource: Boolean): Boolean {
                        if (resource!=null)
                            Blurry.with(fragment.requireContext()).async()
                                    .radius(10)
                                    .sampling(4)
                                    .from(resource)
                                    .into(imageView)
                        else
                            imageView.setImageBitmap(null)
                        return true
                    }
                })
                .submit()
    }
}
