package com.example.myapplication.ui.music

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTextTransferBinding
import com.example.myapplication.logic.network.ServiceCreator
import com.example.myapplication.logic.network.TransferService
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class TextTransferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextTransferBinding
    private var inputText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_transfer)
        binding = ActivityTextTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.transferTextBtn.setOnClickListener {
            inputText = binding.inputText.text.toString()
            if (inputText == "") {
                Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "正在转换，请耐心等待", Toast.LENGTH_LONG).show()
                //发送网络请求，进行转换
                val transferService = ServiceCreator.create(TransferService::class.java)
                val call = transferService.transfer2audio(inputText)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        // 处理响应,得到音频
                        Log.d("TextTransferActivity", "Response from server: $response")
                        val responseBody = response.body()
                        val results = response.body()?.string()
                        Log.d("TextTransferActivity", "results: $results")
                        if (responseBody != null) {
                            Log.d("TextTransferActivity", "responseBody is not null")
                            val jsonObject: JSONObject = JSONObject(results)
                            var status = jsonObject.getString("status")
                            var data = jsonObject.getString("data")
                            val audioBytes = decodeBase64(data)
                            // 创建临时文件来存储解码后的音频数据，名字为audio_+当前时间戳+.mp3，用于播放
                            val prefix = "audio_" + System.currentTimeMillis()
                            val tempAudioFile = File.createTempFile(prefix, ".mp3", cacheDir)
                            val fos = FileOutputStream(tempAudioFile)
                            fos.write(audioBytes)
                            fos.close()
                            if (status == "ok") {
                                Intent(
                                    this@TextTransferActivity,
                                    ResultAudioActivity::class.java
                                ).apply {
                                    putExtra("audio_path", tempAudioFile.absolutePath)
                                    startActivity(this)
                                }
                            } else {
                                Log.d("ResultActivity", "onResponse: status is not ok")
                            }

                        } else {
                            Log.d("TextTransferActivity", "responseBody is null")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 处理请求失败
                        Log.d("TextTransferActivity", "onFailure: $t")
                    }
                })
            }

        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decodeBase64(base64Str: String): ByteArray {
        return Base64.decode(base64Str, 0, base64Str.length)
    }
}