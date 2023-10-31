package com.example.myapplication.ui.collection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.myapplication.logic.FileRepository

import com.example.myapplication.logic.model.MediaFile


class FileViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<Int>()
    val mediaFileList =  ArrayList<MediaFile>()
    private val userIdLiveData = MutableLiveData<Int>()

    val fileLiveData = searchLiveData.switchMap {

        folderId -> FileRepository.getFileByFolderId(folderId)

    }

    fun getFileByFolderId(folderId:Int){
        searchLiveData.value = folderId
    }


}