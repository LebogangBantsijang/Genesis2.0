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

package com.lebogang.genesis.ui.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.DialogAudioOptionsBinding
import com.lebogang.genesis.ui.AlbumViewActivity
import com.lebogang.genesis.ui.ArtistViewActivity
import com.lebogang.genesis.ui.EditAudioActivity
import com.lebogang.genesis.ui.InfoActivity
import com.lebogang.genesis.utils.GlobalGlide

class AudioOptionsDialog(private val audio:Audio, private val enableModify:Boolean) :BottomSheetDialogFragment(){
    private lateinit var viewBinding:DialogAudioOptionsBinding

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
        GlobalGlide.loadAudioCover(viewBinding.root, viewBinding.coverView, audio.albumArtUri)
        viewBinding.titleView.text = audio.title
        viewBinding.addToListView.setOnClickListener {
            SelectPlaylistDialog(audio).show(activity?.supportFragmentManager!!, "")
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
        if(enableModify)
            viewBinding.editView.setOnClickListener {
                startActivity(Intent(requireContext(), EditAudioActivity::class.java).apply {
                    putExtra("Audio", audio.id)
                })
                dismissAllowingStateLoss()
            }
        else
            viewBinding.editView.visibility = View.GONE

        viewBinding.infoView.setOnClickListener {
            startActivity(Intent(requireContext(), InfoActivity::class.java).apply {
                putExtra("Audio", audio.id)
            })
            dismissAllowingStateLoss()
        }
        viewBinding.shareView.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

}
