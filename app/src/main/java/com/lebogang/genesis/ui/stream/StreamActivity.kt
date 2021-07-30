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

package com.lebogang.genesis.ui.stream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.ActivityStreamBinding
import com.lebogang.genesis.ui.SettingsActivity
import com.lebogang.genesis.ui.charts.ChartsActivity
import com.lebogang.genesis.ui.history.HistoryActivity
import com.lebogang.genesis.ui.local.HomeActivity
import com.lebogang.genesis.ui.user.SignInActivity
import com.lebogang.genesis.ui.user.UserActivity

class StreamActivity : AppCompatActivity() {
    private val bind:ActivityStreamBinding by lazy{ ActivityStreamBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        title = getString(R.string.app_name)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar)
        initBottomNavigation()
    }

    private fun initBottomNavigation(){
        bind.bottomNavigation.selectedItemId = R.id.stream
        bind.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.charts -> startActivity(Intent(this, ChartsActivity::class.java))
                R.id.settings-> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.local -> startActivity(Intent(this, HomeActivity::class.java))
            }
            false
        }
    }
}