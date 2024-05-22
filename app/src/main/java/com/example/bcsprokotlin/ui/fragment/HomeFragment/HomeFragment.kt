package com.example.bcsprokotlin.ui.fragment.HomeFragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.SubjectAdapterHomeScreen
import com.example.bcsprokotlin.databinding.FragmentHomeBinding
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectViewModel
import com.example.bcsprokotlin.ui.fragment.base.BaseFragment
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val subjectViewModel: SubjectViewModel by viewModels()

    private var subjectAdapter = SubjectAdapterHomeScreen()


    override fun onCreateView() {

        subjectViewModel.viewModelScope.launch {
            subjectViewModel.getSubjectName(3)
        }



        subjectViewModel.subjects.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
//                    GeneralUtils.hideShimmerLayout()
                }


                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    response.data?.let {
                        subjectAdapter.differ.submitList(it)
                    }
                }
            }

        }


        setUpRecylerView()

        setListener()
    }


    private fun setListener() = with(binding) {

        practice.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_questionFragment) }
//        btnShowAllSubject.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_subjectsFragment) }
        btnShowAllSubject.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_subjectsFragment) }
        btnQuestionBank.setOnClickListener { findNavController().navigate(R.id.action_homeFragment2_to_questionBankFragment) }

    }

    fun setUpRecylerView() = binding.rvSubjects.apply {
        adapter = subjectAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

}