package com.example.myapplication.logic.model

import com.google.gson.annotations.SerializedName

data class FolderResponse (val status:String,@SerializedName("data")val folders:List<Folder>) {}
data class Folder(val id:Int,@SerializedName("name")val foldername:String,val userId:Int){}