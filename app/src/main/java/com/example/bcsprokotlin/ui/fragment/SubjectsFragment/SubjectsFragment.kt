package com.example.bcsprokotlin.ui.fragment.SubjectsFragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.bcsprokotlin.adapter.SubjectAdapter
import com.example.bcsprokotlin.databinding.FragmentSubjectsBinding
import com.example.bcsprokotlin.ui.fragment.base.BaseFragment
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubjectsFragment : BaseFragment<FragmentSubjectsBinding>(FragmentSubjectsBinding::inflate) {

    private val viewModel: SubjectViewModel by viewModels()
    private var subjectAdapter = SubjectAdapter()


    override fun onCreateView() {

        viewModel.viewModelScope.launch {
            viewModel.getSubjectName(3)

        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        subjectAdapter = SubjectAdapter()
        viewModel.subjects.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideShimmer()
                    response.data?.let { questionList ->
                        subjectAdapter.differ.submitList(questionList)

                    }
                }

                is Resource.Error -> {
                    hideShimmer()
                    response.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showShimmer()
                }
            }
        }


        setupRecyclerView()

    }

    private fun setupRecyclerView() = binding.rvSubjects.apply {
        adapter = subjectAdapter

    }


    private fun showShimmer() = binding.apply {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        rvSubjects.visibility = View.GONE
    }

    private fun hideShimmer() = binding.apply {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        rvSubjects.visibility = View.VISIBLE
    }

}