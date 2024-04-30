package com.example.bcsprokotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.databinding.FragmentQuestionBinding
import com.example.bcsprokotlin.viewModel.QuestionViewModel
import com.example.bcsprokotlin.util.Resource
import kotlinx.coroutines.launch

class QuestionFragment : Fragment() {

    lateinit var binding: FragmentQuestionBinding

    private var questionAdapter= QuestionAdapter()

    private val viewModel: QuestionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentQuestionBinding.inflate(inflater,container,false)

        lifecycleScope.launch {
            questionAdapter = QuestionAdapter()
            viewModel.questions.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        hidePregressBar()
                        response.data?.let { questionList ->
                            questionAdapter.differ.submitList(questionList)

                        }
                    }
                    is Resource.Error -> {
                        hidePregressBar()
                        response.message?.let { message ->
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        showPregressBar()
                    }
                }
            }
        }


        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() = binding.rvQuestion.apply {
        adapter = questionAdapter
        layoutManager = LinearLayoutManager(context)


    }

    private fun hidePregressBar() = binding.progressBar.apply{
        visibility= View.GONE
        binding.rvQuestion.visibility=View.VISIBLE
    }
    private fun showPregressBar() = binding.progressBar.apply{
        visibility= View.VISIBLE
        binding.rvQuestion.visibility=View.GONE
    }


}