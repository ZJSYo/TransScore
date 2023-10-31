package com.example.myapplication.logic.network

import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.model.FolderResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FolderService {
    @GET("folder")
    fun getFolderByUserId(@Query("userid") userId:Int): Call<FolderResponse>

    @POST("folder")
    fun addFolder(@Body folder: Folder):Call<ResponseBody>

    @DELETE("folder")
    fun deleteFolder(@Query("folderid") folderid:Int):Call<ResponseBody>

}