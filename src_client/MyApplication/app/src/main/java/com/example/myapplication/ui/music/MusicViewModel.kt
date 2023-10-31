package com.example.myapplication.ui.music

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {

    val filename = MutableLiveData<String>().apply {
        value = "点击导入音乐"
    }
    var uri:Uri = Uri.EMPTY
    var description:String = ""
//    val text: LiveData<String> = _text
}