package com.example.myapplication.logic

import androidx.lifecycle.liveData
import com.example.myapplication.logic.network.AppNetWork
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

object TransferRepository {
    //目前荒废
    fun transfer2sheet(data:String)= liveData(Dispatchers.IO){
        val result = try{
            //TODO 对服务器返回值进行处理，目前这里还要融合服务器
            val response = AppNetWork.transfer2Sheet(data)
            response
        }catch (e:Exception) {
            e.printStackTrace()
        }
        emit(result)
    }
    fun transfer2audio(){

    }
}