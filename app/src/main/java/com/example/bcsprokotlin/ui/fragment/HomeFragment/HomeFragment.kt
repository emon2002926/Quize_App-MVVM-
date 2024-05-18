package com.example.bcsprokotlin.ui.fragment.HomeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setListener()

        return binding.root
    }


    fun setListener() = with(binding) {

        practice.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_questionFragment) }
        btnShowAllSubject.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_subjectsFragment) }
        scrollView.isVerticalScrollBarEnabled = false
        scrollView.isHorizontalScrollBarEnabled = false;
        showAll.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_subjectsFragment) }
        btnQuestionBank.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_questionBankFragment) }

    }

}