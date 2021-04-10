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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lebogang.genesis.databinding.DialogDeezerPreviewBinding
import com.lebogang.genesis.network.models.TrackDeezer
import com.lebogang.genesis.ui.helpers.ThemeHelper
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.utils.glide.GlideManager

class DialogDeezerPreview : BottomSheetDialogFragment(){
    private val viewBinding: DialogDeezerPreviewBinding by lazy { DialogDeezerPreviewBinding.inflate(layoutInflater) }
    private lateinit var audio:TrackDeezer
    @ColorInt
    private var explicitColor:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        audio = requireArguments().getParcelable(Keys.DEEZER_SONG_KEY)!!
        explicitColor = if (audio.hasExplicitLyrics) ThemeHelper.PRIMARY_COLOR else
            ThemeHelper.SECONDARY_TEXTCOLOR_NO_DISABLE
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.titleView.text = audio.title
        viewBinding.subtitleView.text = audio.artist.title
        viewBinding.explicitView.setTextColor(explicitColor)
        try{
            GlideManager(this).loadOnline(audio.album.coverMedium, viewBinding.imageView)
        }catch(e:NullPointerException){
            //not needed
        }
    }
}
