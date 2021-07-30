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

package com.lebogang.genesis.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lebogang.genesis.R
import com.lebogang.genesis.ui.stream.StreamActivity

abstract class GoogleHelper :AppCompatActivity(){
    private lateinit var contract: ActivityResultLauncher<Intent>
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("293584344261-nijn3t1oteib9mbs4cfls8gve82fvded.apps.googleusercontent.com")
        .requestEmail()
        .build()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        contract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data->
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    signIn(account?.idToken)
                }catch (e: ApiException){
                    Log.e("Google",e.toString())
                }
            }
        }
    }

    internal fun signIn(){
        val client = GoogleSignIn.getClient(this,options)
        contract.launch(client.signInIntent)
    }

    private fun signIn(accessTokenId:String?){
        val credential = GoogleAuthProvider.getCredential(accessTokenId,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful)
                startActivity(Intent(this, StreamActivity::class.java))
            else
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
        }
    }
}
