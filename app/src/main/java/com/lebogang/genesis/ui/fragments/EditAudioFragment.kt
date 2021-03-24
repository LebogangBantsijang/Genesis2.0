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

package com.lebogang.genesis.ui.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.databinding.FragmentEditAudioBinding
import com.lebogang.genesis.utils.GlobalBlurry
import com.lebogang.genesis.utils.GlobalGlide
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.utils.Validator
import com.lebogang.genesis.viewmodels.AudioViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

class EditAudioFragment: Fragment() {
    private val viewBinding:FragmentEditAudioBinding by lazy { FragmentEditAudioBinding.inflate(layoutInflater) }
    private val viewModel:AudioViewModel by lazy { ViewModelFactory(requireActivity().application).getAudioViewModel()}
    private lateinit var audio:Audio

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        audio = requireArguments().getParcelable(Keys.SONG_KEY)!!
        return viewBinding.root
    }

    @SuppressLint("InlinedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalBlurry.loadBlurryResource(this, audio.getArtUri(), viewBinding.blurView)
        GlobalGlide.loadAudioCover(this, viewBinding.coverView, audio.getArtUri())
        viewBinding.titleView.setText(audio.title)
        viewBinding.artistView.setText(audio.artist)
        viewBinding.albumView.setText(audio.album)
        viewBinding.updateView.setOnClickListener {
            val title = viewBinding.titleView.text?.toString()
            val artist = viewBinding.artistView.text?.toString()
            val album = viewBinding.albumView.text?.toString()
            if (Validator.isValueValid(title) && Validator.isValueValid(artist) &&
                Validator.isValueValid(album)){
                val values = ContentValues()
                values.put(MediaStore.Audio.Media.TITLE, title)
                values.put(MediaStore.Audio.Media.ARTIST, artist)
                values.put(MediaStore.Audio.Media.ALBUM, album)
                viewModel.updateAudio(audio, values)
                Snackbar.make(view, getString(R.string.update_successful), Snackbar.LENGTH_SHORT)
                    .show()
            }else{
                Snackbar.make(view, getString(R.string.null_values), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

}
