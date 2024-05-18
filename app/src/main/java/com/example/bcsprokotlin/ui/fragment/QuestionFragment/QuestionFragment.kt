package com.example.bcsprokotlin.ui.fragment.QuestionFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.databinding.FragmentQuestionBinding
import com.example.bcsprokotlin.ui.fragment.base.BaseFragment
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionFragment : BaseFragment<FragmentQuestionBinding>(FragmentQuestionBinding::inflate) {


    private var questionAdapter= QuestionAdapter()

    private val viewModel: QuestionViewModel by viewModels()

    override fun onCreateView() {


        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        lifecycleScope.launch {
            viewModel.questions.observe(viewLifecycleOwner) { response ->
                when (response) {

                    is Resource.Loading -> {
                        showShimmerLayout()
                    }
                    is Resource.Success -> {
                        hideShimmerLayout()
                        response.data?.let { questionList ->
                            questionAdapter.differ.submitList(questionList)

                        }
                    }
                    is Resource.Error -> {
                        hideShimmerLayout()
                        response.message?.let { message ->
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    }


                }
            }
        }

        setupRecyclerView()
    }


    private fun setupRecyclerView() = binding.rvQuestion.apply {
        adapter = questionAdapter
        layoutManager = LinearLayoutManager(context)


    }


    private fun hideShimmerLayout()=binding.apply {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        rvQuestion.visibility = View.VISIBLE
    }

    private fun  showShimmerLayout() = binding.apply {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        rvQuestion.visibility = View.GONE
    }



}