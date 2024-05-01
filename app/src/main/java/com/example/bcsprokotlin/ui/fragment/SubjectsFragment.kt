package com.example.bcsprokotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.adapter.SubjectAdapter
import com.example.bcsprokotlin.databinding.FragmentSubjectsBinding
import com.example.bcsprokotlin.util.Constants.Companion.BANGLA
import com.example.bcsprokotlin.util.Constants.Companion.BANGLADESH_AFFAIRS
import com.example.bcsprokotlin.util.Constants.Companion.ENGLISH
import com.example.bcsprokotlin.util.Constants.Companion.ETHICS
import com.example.bcsprokotlin.util.Constants.Companion.GENERA_SCIENCE
import com.example.bcsprokotlin.util.Constants.Companion.GEOGRAPHY
import com.example.bcsprokotlin.util.Constants.Companion.ICT
import com.example.bcsprokotlin.util.Constants.Companion.INTERNATIONAL_AFFAIRS
import com.example.bcsprokotlin.util.Constants.Companion.MATH
import com.example.bcsprokotlin.util.Constants.Companion.MENTAL_ABILITY
import com.example.bcsprokotlin.util.Resource
import com.example.bcsprokotlin.viewModel.QuestionViewModel
import kotlinx.coroutines.launch
import javax.security.auth.Subject


class SubjectsFragment : Fragment() {

    lateinit var binding: FragmentSubjectsBinding
    private val viewModel: QuestionViewModel by activityViewModels()
    private var subjectAdapter= SubjectAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentSubjectsBinding.inflate(inflater,container,false)

        viewModel.viewModelScope.launch {
            viewModel.getSubjects(3)

        }

        lifecycleScope.launch {
            subjectAdapter = SubjectAdapter()
            viewModel.subjects.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
//                        hidePregressBar()
                        response.data?.let { questionList ->
                            subjectAdapter.differ.submitList(questionList)

                        }
                    }
                    is Resource.Error -> {
//                        hidePregressBar()
                        response.message?.let { message ->
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
//                        showPregressBar()
                    }
                }
            }
        }

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() = binding.rvSubjects.apply {
        adapter = subjectAdapter

    }


//    private fun hidePregressBar() = binding.progressBar.apply{
//        visibility= View.GONE
//        binding.rvQuestion.visibility=View.VISIBLE
//    }
//    private fun showPregressBar() = binding.progressBar.apply{
//        visibility= View.VISIBLE
//        binding.rvQuestion.visibility=View.GONE
//    }

}