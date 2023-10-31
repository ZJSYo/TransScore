package com.example.myapplication.logic.network


import com.example.myapplication.logic.model.User
import com.example.myapplication.logic.model.UserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("user")
    fun getUserByName(@Query("username") username:String):Call<UserResponse>

    @POST("user")
    fun registerUser(@Body user: User):Call<ResponseBody>

}