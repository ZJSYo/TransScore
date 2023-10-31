package com.example.myapplication.logic.model

import com.google.gson.annotations.SerializedName

data class FileResponse(val status:String,@SerializedName("data")val data:List<MediaFile>){
}
data class MediaFile(val id:Int, val name:String, val filePath:String, val folderId:Int, val type:Int){
    //文件种类，枚举1为音乐,2为乐谱
    companion object{
        const val MUSIC = 1
        const val SHEET = 2
    }

}
data class FileRequest(val name: String,val folderId: Int,val type: Int,val data: String)