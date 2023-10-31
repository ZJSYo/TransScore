package com.example.myapplication.logic

import android.icu.text.CaseMap.Fold
import android.util.Log
import androidx.lifecycle.liveData
import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.model.User
import com.example.myapplication.logic.network.AppNetWork
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

object FolderRepository {
    private var folders = ArrayList<Folder>().apply {
        //模拟一个文件夹列表
        add(Folder(1, "文件夹1", 1))
        add(Folder(2, "文件夹2", 1))
        add(Folder(3, "文件夹3", 1))
        add(Folder(4, "文件夹4", 1))
        add(Folder(5, "文件夹5", 1))
        add(Folder(6, "文件夹6", 1))
        add(Folder(7, "文件夹7", 1))
    }

    fun getFolderByUserId(userId: Int) = liveData(Dispatchers.IO) {
//        Log.d("FolderRepository", "getFolderByUserId")
//        val result = Result.success(folders)
//        emit(result)
        val result = try {
            //先检查本地缓存是否有用户数据
            val response = AppNetWork.getFolderByUserId(userId)
            if (response.status=="ok"){
                val folders = response.folders
                Result.success(folders)
            }else{
                Result.failure(RuntimeException("response status is ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Folder>>(e)
        }
        emit(result)
    }

    fun addFolder(foldername: String, userId: Int) = liveData(Dispatchers.IO) {
//        Log.d("FolderRepository", "addFolder")
//        folders.add(Folder(20, foldername, userId))
//        emit(Result.success(Unit))
        val result = try {
            Log.d("FolderRepository", "addFolder")
            val response: ResponseBody = AppNetWork.addFolder(foldername,userId)
            val jsonObject: JSONObject = JSONObject(response.string())
            val status = jsonObject.getString("status")
            if (status == "ok") {
                Log.d("FolderRepository", "addFolder success")
                Result.success(Unit)
            } else {
                Log.d("FolderRepository", "addFolder fail")
                Result.failure(RuntimeException("response status is ${status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Folder>>(e)
        }
        emit(result)
    }

    fun deleteFolder(folderId: Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response: ResponseBody = AppNetWork.deleteFolder(folderId)
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