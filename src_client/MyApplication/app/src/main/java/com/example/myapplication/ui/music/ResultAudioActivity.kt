package com.example.myapplication.ui.music

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.StaticApplication
import com.example.myapplication.databinding.ActivityResultAudioBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class ResultAudioActivity : AppCompatActivity() {
    private val mediaPlayer = MediaPlayer()
    private lateinit var binding: ActivityResultAudioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            //获得文件路径
            val tempAudioFile = intent.getStringExtra("audio_path")?.let { File(it) }!!
            val prefix = tempAudioFile.nameWithoutExtension
            // 将MediaPlayer的数据源设置为临时音频文件
            mediaPlayer.setDataSource(tempAudioFile.path)
            mediaPlayer.prepare()

            // 设置按钮点击监听器
            binding.playBtn.setOnClickListener {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }

            binding.pauseBtn.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }

            binding.stopBtn.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(tempAudioFile.path)
                    mediaPlayer.prepare()
                }
            }

            binding.saveBtn.setOnClickListener {
//                //将音频文件保存到手机文件夹
                Toast.makeText(this, "请求下载中", Toast.LENGTH_SHORT).show()
                val audioFileName = "$prefix.mp3" // 文件名字为prefix.mp3
//                val audioFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), audioFileName)
                val audioFile = File("/storage/emulated/0/Documents/识音", audioFileName)
                try {
                    //将tempAudioFile复制到audioFile
                    tempAudioFile.copyTo(audioFile)
                    // 音频保存成功，显示提示信息
                    Toast.makeText(
                        this,
                        "音频保存成功，路径: ${audioFile.absolutePath}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("ResultAudioActivity", "音频保存成功，路径: ${audioFile.absolutePath}")
                } catch (e: IOException) {
                    e.printStackTrace()
                    // 音频保存失败，显示错误信息
                    Toast.makeText(this, "音频保存失败,请检查权限", Toast.LENGTH_SHORT).show()
                }
            }
            binding.exitBtn.setOnClickListener {
                finish()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "播放音频时出现错误", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decodeBase64(base64Str: String): ByteArray {
        return Base64.decode(base64Str, 0, base64Str.length)
    }
}