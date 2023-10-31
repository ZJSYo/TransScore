package com.example.myapplication.ui.collection

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.logic.model.MediaFile
import com.example.myapplication.logic.network.FileService
import com.example.myapplication.logic.network.ServiceCreator
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FileAdapter(private val mediaFileList: List<MediaFile>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class AudioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val audioName = view.findViewById<TextView>(R.id.fileName)
        val audioImage = view.findViewById<ImageView>(R.id.fileImage)
    }

    inner class SheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sheetName = view.findViewById<TextView>(R.id.fileName)
        val sheetImage = view.findViewById<ImageView>(R.id.fileImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        return when (viewType) {
            MediaFile.MUSIC -> {
                val viewHolder = AudioViewHolder(view)
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    val file = mediaFileList[position]
                    Log.d("FileAdapter", "MUSIC${file.name}被点击")
                }
                viewHolder.itemView.setOnLongClickListener {
                    val popupMenu =
                        androidx.appcompat.widget.PopupMenu(parent.context, viewHolder.itemView)
                    popupMenu.menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        val position = viewHolder.adapterPosition
                        val file = mediaFileList[position]
                        Log.d("FileAdapter", "${file.name}被长按")
                        Toast.makeText(parent.context, "删除${file.name}", Toast.LENGTH_SHORT)
                            .show()
                        val fileSevice = ServiceCreator.create(FileService::class.java)
                        val call = fileSevice.deleteFile(file.id)
                        call.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                Log.d("FileAdapter", "onResponse: $response")
                                val responseBody = response.body()
                                val results = response.body()?.string()
                                Log.d("FileAdapter", "results: $results")
                                if (responseBody != null) {
                                    val jsonObject: JSONObject = JSONObject(results)
                                    var status = jsonObject.getString("status")
                                    if (status == "ok") {
                                        Toast.makeText(
                                            parent.context,
                                            "删除成功",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        mediaFileList.toMutableList().removeAt(position)
                                        notifyItemRemoved(position)
                                        notifyItemRangeChanged(position, mediaFileList.size)
                                        Log.d("FileAdapter", "删除成功")
                                    } else {
                                        Log.d("FileAdapter", "删除失败")
                                    }
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                // 处理请求失败
                                Toast.makeText(parent.context, "请检查网络连接", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                        true
                    }
                    true
                }
                viewHolder.itemView.setOnLongClickListener {
                    val popupMenu =
                        androidx.appcompat.widget.PopupMenu(parent.context, viewHolder.itemView)
                    popupMenu.menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        val position = viewHolder.adapterPosition
                        val file = mediaFileList[position]
                        Log.d("FileAdapter", "${file.name}被长按")
                        Toast.makeText(parent.context, "删除${file.name}", Toast.LENGTH_SHORT)
                            .show()
                        val fileSevice = ServiceCreator.create(FileService::class.java)
                        val call = fileSevice.deleteFile(file.id)
                        call.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                Log.d("FileAdapter", "onResponse: $response")
                                val responseBody = response.body()
                                val results = response.body()?.string()
                                Log.d("FileAdapter", "results: $results")
                                if (responseBody != null) {
                                    val jsonObject: JSONObject = JSONObject(results)
                                    var status = jsonObject.getString("status")
                                    if (status == "ok") {
                                        Toast.makeText(
                                            parent.context,
                                            "删除成功",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        mediaFileList.toMutableList().removeAt(position)
                                        notifyItemRemoved(position)
                                        notifyItemRangeChanged(position, mediaFileList.size)
                                        Log.d("FileAdapter", "删除成功")
                                    } else {
                                        Log.d("FileAdapter", "删除失败")
                                    }
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                // 处理请求失败
                                Toast.makeText(parent.context, "请检查网络连接", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })

                        true
                    }
                    popupMenu.show()
                    true
                }
                viewHolder
            }

            MediaFile.SHEET -> {
                val viewHolder = SheetViewHolder(view)
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    val file = mediaFileList[position]
                    Log.d("FileAdapter", "SHEET${file.name}被点击")
                }

                viewHolder
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val file = mediaFileList[position]
        when (holder) {
            is AudioViewHolder -> {
                holder.audioName.text = file.name
                holder.audioImage.setImageResource(R.drawable.audio)
            }

            is SheetViewHolder -> {
                holder.sheetName.text = file.name
                holder.sheetImage.setImageResource(R.drawable.sheet)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount() = mediaFileList.size

    override fun getItemViewType(position: Int): Int {
        val file = mediaFileList[position]
        return file.type
    }
}