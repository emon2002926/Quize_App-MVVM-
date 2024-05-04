package com.example.bcsprokotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
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
            practice.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_questionFragment) }
            btnShowAllSubject.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment2_to_subjectsFragment)
            }
            scrollView.isVerticalScrollBarEnabled = false
            scrollView.isHorizontalScrollBarEnabled = false;
        }

        return binding.root
    }





}