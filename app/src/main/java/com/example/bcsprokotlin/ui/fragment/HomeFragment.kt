package com.example.bcsprokotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        

        with(binding){
            practice.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_questionFragment) }
            btnQuestionBank.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
            }
        }


        return binding.root
    }



}