package com.example.myapplication.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentTutorialBinding

class TutorialFragment : Fragment() {

    private var _binding: FragmentTutorialBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tutorialViewModel =
            ViewModelProvider(this).get(TutorialViewModel::class.java)

        _binding = FragmentTutorialBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.videoTransfer2Sheet.setVideoURI(tutorialViewModel.uri1)
        binding.btnPlay1.setOnClickListener {
            if(!binding.videoTransfer2Sheet.isPlaying){
                binding.videoTransfer2Sheet.start()
            }
        }
        binding.btnStop1.setOnClickListener(){
            if(binding.videoTransfer2Sheet.isPlaying){
                binding.videoTransfer2Sheet.pause()
            }
        }
        binding.btnReset1.setOnClickListener(){
            if(binding.videoTransfer2Sheet.isPlaying){
                binding.videoTransfer2Sheet.resume()
            }
        }

        binding.videoTransfer2Audio.setVideoURI(tutorialViewModel.uri2)
        binding.btnPlay2.setOnClickListener {
            if(!binding.videoTransfer2Audio.isPlaying){
                binding.videoTransfer2Audio.start()
            }
        }
        binding.btnStop2.setOnClickListener(){
            if(binding.videoTransfer2Audio.isPlaying){
                binding.videoTransfer2Audio.pause()
            }
        }
        binding.btnReset2.setOnClickListener(){
            if(binding.videoTransfer2Audio.isPlaying){
                binding.videoTransfer2Audio.resume()
            }
        }

        binding.videoCollection.setVideoURI(tutorialViewModel.uri3)
        binding.btnPlay3.setOnClickListener {
            if(!binding.videoCollection.isPlaying){
                binding.videoCollection.start()
            }
        }
        binding.btnStop3.setOnClickListener(){
            if(binding.videoCollection.isPlaying){
                binding.videoCollection.pause()
            }
        }
        binding.btnReset3.setOnClickListener(){
            if(binding.videoCollection.isPlaying){
                binding.videoCollection.resume()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoCollection.suspend()
        binding.videoTransfer2Audio.suspend()
        binding.videoTransfer2Sheet.suspend()
        _binding = null
    }
}