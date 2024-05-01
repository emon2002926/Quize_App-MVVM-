package com.example.bcsprokotlin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.databinding.FragmentRecordBinding
import com.example.bcsprokotlin.viewModel.QuestionViewModel
import kotlinx.coroutines.launch


class RecordFragment : Fragment() {
    lateinit var binding: FragmentRecordBinding

     private var questionAdapter= QuestionAdapter()

    private val viewModel: QuestionViewModel by activityViewModels()


    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        binding = FragmentRecordBinding.bind(view.rootView)


//        viewModel.viewModelScope.launch {
//            viewModel.getQuestion()
//        }

//        lifecycleScope.launch {
//            questionAdapter = QuestionAdapter()
//            viewModel.questions.observe(viewLifecycleOwner) { response ->
//                when (response) {
//                    is Resource.Success -> {
//                        hidePregressBar()
//                        response.data?.let { questionList ->
//                            questionAdapter.differ.submitList(questionList)
//
//
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        hidePregressBar()
//                        response.message?.let { message ->
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    is Resource.Loading -> {
//                        showPregressBar()
//                    }
//                }
//            }
//
//
//        }


//        setupRecyclerView()


        return binding.root
    }

//




}