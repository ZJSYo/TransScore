package com.example.myapplication.ui.collection

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityFileBinding
import com.example.myapplication.logic.FileRepository
import com.example.myapplication.logic.model.FileRequest
import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.model.MediaFile
import com.example.myapplication.logic.network.FileService
import com.example.myapplication.logic.network.ServiceCreator
import com.example.myapplication.utils.Utils
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileBinding
    private lateinit var adapter: FileAdapter
    val viewModel by lazy {
        ViewModelProvider(this).get(FileViewModel::class.java)
    }
    private var folderId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val layoutManager = LinearLayoutManager(this)
        binding.fileRecyclerView.layoutManager = layoutManager
        adapter = FileAdapter(viewModel.mediaFileList)
        binding.fileRecyclerView.adapter = adapter
        folderId = intent.getIntExtra("folderId", 0)
        Log.d("FileActivity", "folderId: $folderId")
        viewModel.fileLiveData.observe(this, Observer { result ->
            val files = result.getOrNull()
            if (files != null) {
                viewModel.mediaFileList.clear()
                viewModel.mediaFileList.addAll(files)
                adapter.notifyDataSetChanged()
            } else {
                Snackbar.make(binding.root, "未知错误", Snackbar.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.getFileByFolderId(folderId)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            //打开文件导入界面
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val selectedFileUri: Uri? = data?.data
            Log.d("FileActivity", "onActivityResult: $selectedFileUri")
            selectedFileUri?.let {//获得文件的uri，读取其内容，编码为base64，发送给服务器
                val fileType = contentResolver.getType(it)
                val data = Utils.convertAudioToBase64(contentResolver,it)
                val filename = getFileNameFromUri(it)!!
                val type = when (fileType) {
                    "audio/mpeg" -> {
                        MediaFile.MUSIC
                    }
                    "image/jpeg" -> {
                        MediaFile.SHEET
                    }
                    else -> {
                        Snackbar.make(binding.root, "不支持的文件类型", Snackbar.LENGTH_SHORT)
                            .show()
                        return
                    }
                }
                val fileRequest = FileRequest(filename, folderId, type, data)
                val fileService = ServiceCreator.create<FileService>()
                val call = fileService.addFile(fileRequest)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Log.d("FileActivity", "onResponse: $response")
                        val responseBody = response.body()
                        val results = response.body()?.string()
                        if (responseBody != null) {
                            Log.d("FileActivity", "responseBody: $responseBody")
                            val jsonObject: JSONObject = JSONObject(results)
                            var status = jsonObject.getString("status")
                            if (status == "ok") {
                                Toast.makeText(this@FileActivity, "添加成功", Toast.LENGTH_SHORT)
                                    .show()
                                viewModel.getFileByFolderId(folderId)
                            } else {
                                Toast.makeText(this@FileActivity, "添加失败", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 处理请求失败
                        Log.d("FileActivity", "onFailure: $t")
                        Toast.makeText(this@FileActivity, "添加失败", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
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

    fun getRealPathFromUri(uri: Uri): String {
        return uri.path.toString()
    }
}