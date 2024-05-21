package com.example.bcsprokotlin.ui.fragment.HomeFragment

import androidx.navigation.fragment.findNavController
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.databinding.FragmentHomeBinding
import com.example.bcsprokotlin.ui.fragment.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onCreateView() {
        setListener()
    }


    fun setListener() = with(binding) {

        practice.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_questionFragment) }
//        btnShowAllSubject.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_subjectsFragment) }
        scrollView.isVerticalScrollBarEnabled = false
        scrollView.isHorizontalScrollBarEnabled = false;
        btnShowAllSubject.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_subjectsFragment) }
        btnQuestionBank.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_questionBankFragment) }

    }

}