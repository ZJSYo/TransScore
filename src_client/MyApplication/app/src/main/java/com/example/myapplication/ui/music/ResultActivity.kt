package com.example.myapplication.ui.music

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityResultBinding
import com.example.myapplication.logic.network.ServiceCreator
import com.example.myapplication.logic.network.TransferService
import com.example.myapplication.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ResultActivity", "onCreate")
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val resultViewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        resultViewModel.image.observe(this, {
            binding.SheetResult.setImageBitmap(it)
            Log.d("ResultActivity", "image changed")

        })
        val uri = intent.getStringExtra("uri") as String
        val filename = getFileNameFromUri(Uri.parse(uri))?.replace(".mp3", ".png")!!
        val file = File("/storage/emulated/0/Documents/识音", filename)

        binding.finishBtn.setOnClickListener {
            Log.d("ResultActivity", "finish")
            finish()
        }
        binding.downloadFab.setOnClickListener {
            Log.d("ResultActivity", "download")
            Toast.makeText(this, "请求下载", Toast.LENGTH_SHORT).show()
            //将转换结果存入手机文件夹--待实现
            if (resultViewModel.image.value != null) {
                //TODO 将bitmap存入手机文件夹
                val outputStream = FileOutputStream(file)
                // 将Bitmap压缩为PNG格式，参数100表示最大质量
                resultViewModel.image.value?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                // 刷新输出流并关闭
                outputStream.flush()
                outputStream.close()
                Toast.makeText(this, "下载成功,文件保存在${file.absolutePath}", Toast.LENGTH_SHORT).show()
                Log.d("ResultActivity", "download success")
            }else{
                Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show()
                Log.d("ResultActivity", "download failed")
            }
        }
        Log.d("ResultActivity", "uri: $uri")
        //增加处理逻辑,进行转换后将转换结果替代ImageView，并且将它可以存入文件夹--暂时略
        process(uri,resultViewModel)
        //处理逻辑
    }

    private fun process(uri: String,resultViewModel: ResultViewModel) {
        val data = Utils.convertAudioToBase64(contentResolver, Uri.parse(uri))
        Log.d("ResultActivity", "audio_data: $data")
        //将base64编码传入后端，转换为图片
        val transferService = ServiceCreator.create<TransferService>()
        val call = transferService.transfer2sheet(data)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // 处理响应 响应格式为 status 和data
                Log.d("ResultActivity", "onResponse: ${response.code()}")
                val responseBody = response.body()
                if (responseBody != null) {
                    val jsonObject = JSONObject(responseBody.string())
                    val status = jsonObject.getString("status")
                    val imgBase64 = jsonObject.getString("data")
                    if (status == "ok") {
                        //TODO 将data(base64编码)转换为图片并显示
                        val imageDataBytes = decodeBase64(imgBase64)
                        val bitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.size)
                        resultViewModel.image.value = bitmap
                        binding.successText.text = ""
                    } else {
                        Log.d("ResultActivity", "onResponse: status is not ok")
                    }
                } else {
                    Log.d("ResultActivity", "onResponse: body is null")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 处理请求失败
                Log.d("ResultActivity", "onFailure: ${t.message}")
            }
        })
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun decodeBase64(base64Str: String): ByteArray {
        return Base64.decode(base64Str, 0, base64Str.length)
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    return it.getString(columnIndex)
                }
            }
        }
        return null
    }

}