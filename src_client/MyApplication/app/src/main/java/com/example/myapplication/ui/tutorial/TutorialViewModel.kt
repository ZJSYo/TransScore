package com.example.myapplication.ui.tutorial

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R

class TutorialViewModel : ViewModel() {
    val uri1: Uri = Uri.parse("android.resource://com.example.myapplication/${R.raw.segment_2}")
    val uri2: Uri = Uri.parse("android.resource://com.example.myapplication/${R.raw.segment_3}")
    val uri3: Uri = Uri.parse("android.resource://com.example.myapplication/${R.raw.segment_4}")

}