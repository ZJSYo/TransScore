package com.example.myapplication.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.myapplication.logic.model.MediaFile
import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.network.AppNetWork
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject

object FileRepository {

    fun getFileByFolderId(folderId: Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = AppNetWork.getFileByFolderId(folderId)
            if (response.status == "ok") {
                val files = response.data
                Log.d("FileRepository", "files: $files")
                Result.success(files)
            } else {
                Result.failure(RuntimeException("response status is ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<MediaFile>>(e)
        }
        emit(result)
    }

    fun addFile(filename: String, folderId: Int, data: String, type: Int) =
        liveData(Dispatchers.IO) {
            val result = try {
                val response: ResponseBody = AppNetWork.addFile(filename, folderId, type, data)
                val jsonObject: JSONObject = JSONObject(response.string())
                val status = jsonObject.getString("status")
                if (status == "ok") {
                    Result.success(Unit)
                } else {
                    Result.failure(RuntimeException("response status is ${status}"))
                }
            } catch (e: Exception) {
                Result.failure<List<Folder>>(e)
            }
            emit(result)
        }

    fun deleteFile(fileId: Int) =
        liveData(Dispatchers.IO) {
            val result = try {
                val response: ResponseBody = AppNetWork.deleteFile(fileId)
                val jsonObject: JSONObject = JSONObject(response.string())
                val status = jsonObject.getString("status")
                if (status == "ok") {
                    Result.success(Unit)
                } else {
                    Result.failure(RuntimeException("response status is ${status}"))
                }
            } catch (e: Exception) {
                Result.failure<List<Folder>>(e)
            }
            emit(result)
        }

}