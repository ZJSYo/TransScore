package com.example.myapplication.logic.network

import android.util.Log
import com.example.myapplication.logic.model.FileRequest
import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.model.MediaFile
import com.example.myapplication.logic.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object AppNetWork {
    private val userService = ServiceCreator.create<UserService>()
    private val folderService = ServiceCreator.create<FolderService>()
    private val fileService = ServiceCreator.create<FileService>()
    private val transferService = ServiceCreator.create<TransferService>()

    //UserService
    suspend fun getUserByName(username:String) = userService.getUserByName(username).await()
    suspend fun registerUser(username:String,password:String) = userService.registerUser(User(0,username,password)).await()

    //FolderService
    suspend fun getFolderByUserId(userId:Int) = folderService.getFolderByUserId(userId).await()
    suspend fun addFolder(foldername:String,userId:Int) = folderService.addFolder(Folder(0,foldername,userId)).await()
    suspend fun deleteFolder(folderId:Int) = folderService.deleteFolder(folderId).await()

    //FileService
    suspend fun getFileByFolderId(folderId:Int) = fileService.getFileByFolderId(folderId).await()
    suspend fun addFile(filename:String,folderId:Int,type:Int,data: String) =
        fileService.addFile(FileRequest(filename,folderId,type,data)).await()
    suspend fun deleteFile(fileId:Int) = fileService.deleteFile(fileId).await()

    //TransferService
    suspend fun transfer2Sheet(data:String) = transferService.transfer2sheet(data ).await()
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {continuation->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body() //获取响应体，json已经被解析为Call中的T
                    if(body!=null){
                        Log.d("AppNetWork","response.body() is not null:\n"+body.toString())
                        continuation.resume(body)

                    }
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}