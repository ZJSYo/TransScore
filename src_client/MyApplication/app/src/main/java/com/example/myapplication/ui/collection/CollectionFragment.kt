package com.example.myapplication.ui.collection

import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.myapplication.databinding.FragmentCollectionBinding

class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!
    private var userId: Int = 0
    val viewModel by lazy { ViewModelProvider(this).get(CollectionViewModel::class.java) }
    private lateinit var adapter: FolderAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CollectionFragment", "onCreateView")
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.fab.setOnClickListener {
            Intent(activity, AddActivity::class.java).apply {
                putExtra("userId", userId)
                startActivity(this)
            }
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("CollectionFragment", "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        //设置RecyclerView
        val layoutManager = LinearLayoutManager(activity)
        binding.folderRecyclerView.layoutManager = layoutManager
        adapter = FolderAdapter(this, viewModel.folderList)
        binding.folderRecyclerView.adapter = adapter
        //得到Intent传递的user对象
        userId = activity?.intent?.getSerializableExtra("userid") as Int
        //初始化folderList
        viewModel.getFolderByUserId(userId)
        //设置生命周期观察回调
        viewModel.folderLiveData.observe(viewLifecycleOwner, Observer {
            Log.d("CollectionFragment", "refresh RecyclerView")
            val folders = it.getOrNull()
            if (folders != null) {
                viewModel.folderList.clear()
                viewModel.folderList.addAll(folders)//更新folderList
                adapter.notifyDataSetChanged()//刷新RecyclerView
            } else {
                Log.d("CollectionFragment", "无文件")
//                Toast.makeText(activity, "无文件", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFolderByUserId(userId)
    }

}