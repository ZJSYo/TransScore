package com.example.myapplication.ui.collection

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.StaticApplication
import com.example.myapplication.logic.model.Folder
import com.example.myapplication.logic.network.FolderService
import com.example.myapplication.logic.network.ServiceCreator
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FolderAdapter(private val fragment: Fragment, private val folderList: List<Folder>) :
    RecyclerView.Adapter<FolderAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val folderName: TextView = view.findViewById(R.id.folderName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = fragment.layoutInflater.inflate(R.layout.folder_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val folder = folderList[position]
            Log.d("FolderAdapter", "${folder.foldername}被点击")
            fragment.activity?.let {
                Intent(fragment.activity, FileActivity::class.java).apply {
                    putExtra("folderId", folder.id)
                    startActivity(fragment.requireActivity(), this, null)
                }
            }
        }
        viewHolder.itemView.setOnLongClickListener {
            //TODO 如果被长按，弹出删除文件夹的PopupMenu
            val popupMenu =
                androidx.appcompat.widget.PopupMenu(fragment.requireContext(), viewHolder.itemView)
            popupMenu.menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                val position = viewHolder.adapterPosition
                val folder = folderList[position]
                Log.d("FolderAdapter", "${folder.foldername}被长按")
                Toast.makeText(
                    StaticApplication.context,
                    "删除${folder.foldername}",
                    Toast.LENGTH_SHORT
                ).show()
                val folderService: FolderService = ServiceCreator.create(FolderService::class.java)
                val call = folderService.deleteFolder(folder.id)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Log.d("FolderAdapter", "onResponse: $response")
                        val responseBody = response.body()
                        val results = response.body()?.string()
                        Log.d("FolderAdapter", "results: $results")
                        if (responseBody != null) {
                            val jsonObject: JSONObject = JSONObject(results)
                            var status = jsonObject.getString("status")
                            if (status == "ok") {
                                Toast.makeText(
                                    fragment.requireContext(),
                                    "删除成功",
                                    Toast.LENGTH_SHORT
                                ).show()
                                folderList.toMutableList().removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, folderList.size)
                                Log.d("FolderAdapter", "删除成功")
                            } else {
                                Log.d("FolderAdapter", "删除失败")

                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 处理请求失败
                        Toast.makeText(
                            fragment.requireContext(),
                            "请检查网络连接",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                true
            }
            popupMenu.show()
            true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folderList[position]
        holder.folderName.text = folder.foldername
    }

    override fun getItemCount(): Int {
        return folderList.size
    }
}