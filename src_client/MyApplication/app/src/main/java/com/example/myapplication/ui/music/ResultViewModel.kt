package com.example.myapplication.ui.music

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel:ViewModel() {
    var image = MutableLiveData<Bitmap>()

}