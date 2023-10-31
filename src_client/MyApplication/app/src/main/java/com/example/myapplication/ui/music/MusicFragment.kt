package com.example.myapplication.ui.music

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentMusicBinding

class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val musicViewModel by lazy { ViewModelProvider(this).get(MusicViewModel::class.java) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = binding.uploadFilename
        musicViewModel.filename.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.importMusic.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*"
            startActivityForResult(intent, 0)

        }
        binding.Audio2SheetBtn.setOnClickListener{
            if (musicViewModel.uri != Uri.EMPTY) {
                Log.d("MusicFragment", "onViewCreated: uri is not empty: ${musicViewModel.uri}")
                //处理逻辑
                Intent(requireActivity(), ResultActivity::class.java).apply {
                    putExtra("uri", musicViewModel.uri.toString())
                    startActivity(this)
                }
            } else {
                Log.d("MusicFragment", "onViewCreated: uri is empty")
            }

        }
        binding.text2AudioBtn.setOnClickListener{
            if(binding.editTextDescription.text.toString()!=""){
                musicViewModel.description = binding.editTextDescription.text.toString()
                Log.d("MusicFragment", "onCreateView: ${musicViewModel.description},process to next activity")
            }
        }
        binding.editTextDescription.setOnClickListener{
            Intent(requireContext(), TextTransferActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val selectedFileUri: Uri? = data?.data
            Log.d("MusicFragment", "onActivityResult: $selectedFileUri")
            selectedFileUri?.let {
                val contentResolver = requireActivity().contentResolver
                val fileType = contentResolver.getType(it)
                Log.d("MusicFragment", "onActivityResult: $fileType")
                when(fileType){
                    "audio/mpeg" -> {
                        val displayName = getFileNameFromUri(it)
                        if (displayName != null) {
                            // 设置文件名到ViewModel
                            musicViewModel.filename.value = displayName
                            // 设置Uri到ViewModel
                            musicViewModel.uri = it
                        } else {
                           Log.d("MusicFragment", "onActivityResult: Failed to get file name")
                        }
                    }
                    else -> {
                        Log.d("MusicFragment", "onActivityResult: MediaFile type not supported")
                    }
                }
            }
        }
    }
    private fun getFileNameFromUri(uri: Uri): String? {
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
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