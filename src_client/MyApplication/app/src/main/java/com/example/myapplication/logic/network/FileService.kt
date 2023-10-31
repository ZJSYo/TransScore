package com.example.myapplication.logic.network

import com.example.myapplication.logic.model.FileRequest
import com.example.myapplication.logic.model.FileResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FileService {
    @GET("/file")
    fun getFileByFolderId(@Query("folderid")folderId:Int): Call<FileResponse>

    @POST("/file")
    fun addFile(@Body request: FileRequest):Call<ResponseBody>

    @DELETE("file")
    fun deleteFile(@Query("fileid") fileid:Int):Call<ResponseBody>
}