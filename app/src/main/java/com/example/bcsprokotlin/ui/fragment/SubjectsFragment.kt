package com.example.bcsprokotlin.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper.UP
import com.example.bcsprokotlin.adapter.SubjectAdapter
import com.example.bcsprokotlin.databinding.FragmentSubjectsBinding
import com.example.bcsprokotlin.util.Resource
import com.example.bcsprokotlin.viewModel.SubjectViewModel
import kotlinx.coroutines.launch

class SubjectsFragment : Fragment() {

    lateinit var binding: FragmentSubjectsBinding
    private val viewModel: SubjectViewModel by viewModels()
    private var subjectAdapter= SubjectAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = FragmentSubjectsBinding.inflate(inflater,container,false)

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