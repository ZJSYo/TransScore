package com.example.myapplication.logic

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.liveData
import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.model.User
import com.example.myapplication.logic.network.AppNetWork
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

object UserRepository {
    fun findUser(username: String) = liveData(Dispatchers.IO) {
        val result = try {
            //先检查本地缓存是否有用户数据
            val response = AppNetWork.getUserByName(username)
            if (response.status == "ok") {
                val user = response.user
                Log.d("UserRepository", "findUser Ok,user: $user")
                Result.success(user)
            } else {
                Result.failure(RuntimeException("response status is ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<User>>(e)
        }
        emit(result)
    }

    fun register(username: String, password: String) = liveData(Dispatchers.IO) {
        val result = try {
            val response: ResponseBody = AppNetWork.registerUser(username, password)
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