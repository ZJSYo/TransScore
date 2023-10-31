package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityWelcomeBinding
import com.example.myapplication.ui.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)//绑定布局
        setContentView(binding.root)//设置布局
        binding.btn.setOnClickListener {
            Toast.makeText(this,"欢迎来到识音",Toast.LENGTH_SHORT).show()
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}