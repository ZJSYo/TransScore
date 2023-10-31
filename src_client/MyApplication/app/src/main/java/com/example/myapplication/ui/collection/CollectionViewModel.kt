package com.example.myapplication.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.myapplication.logic.FolderRepository
import com.example.myapplication.logic.model.Folder

class CollectionViewModel : ViewModel() {

    val folderList = ArrayList<Folder>()
    private val userIdLiveData = MutableLiveData<Int>()
    val folderLiveData = userIdLiveData.switchMap {
        userId -> FolderRepository.getFolderByUserId(userId)
    }
    fun getFolderByUserId(userId:Int){
        userIdLiveData.value = userId
    }

}