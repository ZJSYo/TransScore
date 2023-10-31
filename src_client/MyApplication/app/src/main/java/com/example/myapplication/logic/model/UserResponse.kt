package com.example.myapplication.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponse(val status:String,@SerializedName("data")val user:List<User>){

}
data class User(val id:Int,@SerializedName("username") val username:String,val password:String):Serializable{

}