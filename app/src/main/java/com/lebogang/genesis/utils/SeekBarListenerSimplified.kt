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

import android.widget.SeekBar

abstract class SeekBarListenerSimplified : SeekBar.OnSeekBarChangeListener{
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(fromUser){
            progressChanged(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //not needed
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //not needed
    }

    abstract fun progressChanged(progress: Int)
}
