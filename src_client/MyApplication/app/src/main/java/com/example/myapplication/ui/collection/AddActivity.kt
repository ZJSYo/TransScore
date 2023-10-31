package com.example.myapplication.ui.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddBinding
import com.example.myapplication.logic.FolderRepository
import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.network.FolderService
import com.example.myapplication.logic.network.ServiceCreator
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("AddActivity","onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addBtn.setOnClickListener(){
            Log.d("AddActivity","addBtn")
            val folderName = binding.textDir.text.toString()
            var userId = intent.getIntExtra("userId",0)
            val folderService: FolderService = ServiceCreator.create(FolderService::class.java)
            val call = folderService.addFolder(Folder(0,folderName,userId))
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("AddActivity", "onResponse: $response")
                    val responseBody = response.body()
                    val results = response.body()?.string()
                    Log.d("AddActivity", "results: $results")
                    if (responseBody != null) {
                        Log.d("AddActivity", "responseBody: $responseBody")
                        val jsonObject: JSONObject = JSONObject(results)
                        var status = jsonObject.getString("status")
                        if (status == "ok") {
                           Toast.makeText(this@AddActivity, "添加成功", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@AddActivity, "添加失败", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AddActivity, "添加失败", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 处理请求失败
                    Log.d("AddActivity", "onFailure: $t")
                    Toast.makeText(this@AddActivity, "添加失败", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }
}