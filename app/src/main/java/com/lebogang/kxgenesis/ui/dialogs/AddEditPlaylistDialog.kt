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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.databinding.LayoutAddEditPlaylistBinding
import com.lebogang.kxgenesis.room.models.Playlist
import com.lebogang.kxgenesis.utils.Validator
import com.lebogang.kxgenesis.viewmodels.PlaylistViewModel

class AddEditPlaylistDialog(var playlist: Playlist?):BottomSheetDialogFragment() {
    private lateinit var viewBinding:LayoutAddEditPlaylistBinding
    private val playlistViewModel: PlaylistViewModel by lazy{
        PlaylistViewModel.Factory((activity?.application as GenesisApplication).playlistRepo)
                .create(PlaylistViewModel::class.java)
    }
    private var imageUri:Uri? = playlist?.getCoverUri()
    //private lateinit var launcher : ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it
            Glide.with(viewBinding.root)
                    .asBitmap()
                    .load(playlist?.getCoverUri())
                    .override(viewBinding.artView.width, viewBinding.artView.height)
                    .error(R.drawable.ic_round_add_24)
                    .into(viewBinding.artView)
        }*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = LayoutAddEditPlaylistBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAddCoverView()
        initSaveView()
    }

    private fun initAddCoverView(){
        playlist?.let {
            viewBinding.editTextTextPersonName.setText(it.title)
        }
        Glide.with(viewBinding.root)
                .asBitmap()
                .load(playlist?.getCoverUri())
                .override(viewBinding.artView.width, viewBinding.artView.height)
                .error(R.drawable.ic_round_add_24)
                .into(viewBinding.artView)
        viewBinding.artView.setOnClickListener {
            //launcher.launch("image/*")
        }
    }

    private fun initSaveView(){
        viewBinding.saveView.setOnClickListener {
            if (Validator.isValueValid(viewBinding.editTextTextPersonName.text.toString())){
                if (playlist != null){
                    playlist!!.title = viewBinding.editTextTextPersonName.text.toString()
                    playlist!!.coverUriToString = imageUri?.toString()
                }else
                    playlist = Playlist(0,viewBinding.editTextTextPersonName.text.toString()
                            , imageUri?.toString())
                playlistViewModel.insertPlaylist(playlist!!)
                dismiss()
            }else{
                Snackbar.make(viewBinding.root, getString(R.string.no_title_error_msg),Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
