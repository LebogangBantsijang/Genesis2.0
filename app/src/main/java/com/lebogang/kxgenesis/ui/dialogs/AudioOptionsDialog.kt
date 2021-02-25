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

package com.lebogang.kxgenesis.ui.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.DialogAudioOptionsBinding
import com.lebogang.kxgenesis.ui.AlbumViewActivity
import com.lebogang.kxgenesis.ui.ArtistViewActivity

class AudioOptionsDialog(private val audio:Audio) :BottomSheetDialogFragment(){
    private lateinit var viewBinding:DialogAudioOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = DialogAudioOptionsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){
        Glide.with(this)
                .asBitmap()
                .load(audio.albumArtUri)
                .error(R.drawable.ic_music_24dp)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(viewBinding.coverView.width, viewBinding.coverView.height)
                .centerCrop()
                .into(viewBinding.coverView)
        viewBinding.titleView.text = audio.title
        viewBinding.playView.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.addToListView.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.artistView.setOnClickListener {
            startActivity(Intent(context, ArtistViewActivity::class.java).apply {
                putExtra("Artist", audio.artist)
            })
            dismissAllowingStateLoss()
        }
        viewBinding.albumView.setOnClickListener {
            startActivity(Intent(requireContext(), AlbumViewActivity::class.java)
                    .apply { putExtra("Album",audio.album) })
            dismissAllowingStateLoss()
        }
        viewBinding.editView.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.infoView.setOnClickListener {
            dismissAllowingStateLoss()
        }
        viewBinding.shareView.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

}
