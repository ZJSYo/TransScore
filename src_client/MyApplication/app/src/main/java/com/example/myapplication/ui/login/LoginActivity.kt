package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.logic.model.User
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.logic.UserRepository
import com.example.myapplication.logic.model.UserResponse
import com.example.myapplication.logic.network.ServiceCreator
import com.example.myapplication.logic.network.UserService
import com.example.myapplication.ui.music.ResultAudioActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var nameInput: String = ""
        var pwdInput: String = ""
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)

        var btn = binding.loginBtn
        btn.setOnClickListener {
            nameInput = binding.username.text.toString()
            pwdInput = binding.password.text.toString()
            if (nameInput.isEmpty()) {
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pwdInput.isEmpty()) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "登录中", Toast.LENGTH_SHORT).show()
            if (nameInput == "admin") {// 判断是否为管理员
                if (pwdInput == "123456") {
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    Intent(this, MainActivity::class.java).apply {
                        val user = User(1, "admin", "123456")
                        putExtra("userid", user.id)
                        startActivity(this)
                    }
                } else {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show()
                }
            } else {
                //如果不为管理员，则进行网络请求查找用户
                Log.d("LoginActivity", "onCreate: $nameInput")
                loginViewModel.findUser(nameInput)
            }
        }
        loginViewModel.userLiveData.observe(this, Observer { result ->
            Log.d("LoginActivity", "Response from server")
            val userList = result.getOrNull()
            if (userList != null && userList.size != 0) { //用户名存在
                val user = userList[0]
                if (user.password == pwdInput) { //且密码正确
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    Intent(this, MainActivity::class.java).apply {
                        putExtra("userid", user.id)
                        startActivity(this)
                    }
                } else { //密码错误
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show()
                }
            } else {//用户不存在
                Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show()
                //弹出AlertDialog,点击确定后进行注册
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("用户不存在")
                builder.setMessage("是否注册？")
                builder.setPositiveButton("确定") { dialog, which ->
//                    UserRepository.register(nameInput, pwdInput)
                    val userService = ServiceCreator.create<UserService>()
                    val call = userService.registerUser(User(0,nameInput, pwdInput))
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            Log.d("LoginActivity", "Response from server: $response")
                            val responseBody = response.body()
                            val results = response.body()?.string()
                            Log.d("LoginActivity", "results: $results")
                            if (responseBody != null) {
                                Log.d("LoginActivity", "responseBody is not null")
                                val jsonObject: JSONObject = JSONObject(results)
                                var status = jsonObject.getString("status")
                                if (status == "ok") {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "注册成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "注册失败，请检查网络",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Log.d("LoginActivity", "responseBody is null")
                                Toast.makeText(
                                    this@LoginActivity,
                                    "注册失败，请检查网络",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // 处理请求失败
                            Log.d("LoginActivity", "onFailure: $t")
                            Toast.makeText(
                                this@LoginActivity,
                                "注册失败，请检查网络",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
                builder.setNegativeButton("取消") { dialog, which ->
                    Toast.makeText(this, "请重新输入", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
        })
    }
}
