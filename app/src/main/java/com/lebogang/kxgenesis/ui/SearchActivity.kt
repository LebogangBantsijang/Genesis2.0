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

package com.lebogang.kxgenesis.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.kxgenesis.GenesisApplication
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.databinding.ActivitySearchBinding
import com.lebogang.kxgenesis.ui.adapters.ItemSearchAdapter
import com.lebogang.kxgenesis.ui.adapters.utils.OnAudioClickListener
import com.lebogang.kxgenesis.viewmodels.AudioViewModel

class SearchActivity : AppCompatActivity(), OnAudioClickListener {
    private lateinit var viewBinding: ActivitySearchBinding
    private val audioViewModel: AudioViewModel by lazy {
        AudioViewModel.Factory((application as GenesisApplication).audioRepo)
            .create(AudioViewModel::class.java)
    }
    private val adapter = ItemSearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initRecyclerView()
        initSearchView()
        observeData()
    }

    private fun initRecyclerView(){
        adapter.listener = this
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun initSearchView(){
        viewBinding.backView.setOnClickListener { onBackPressed() }
        viewBinding.searchView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun observeData(){
        audioViewModel.liveData.observe(this, {
            adapter.setAudioData(it)
        })
    }

    override fun onAudioClick(audio: Audio) {
    }

    override fun onAudioClickOptions(audio: Audio) {
        //nothing here
    }

}