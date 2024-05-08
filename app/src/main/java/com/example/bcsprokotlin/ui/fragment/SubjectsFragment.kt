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

        return binding.root
    }

    private fun setupRecyclerView() = binding.rvSubjects.apply {
        adapter = subjectAdapter

    }



    private fun showShimmer() = binding.apply{
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
       rvSubjects.visibility = View.GONE
    }

        private fun hideShimmer() = binding.apply{
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility= View.GONE
             rvSubjects.visibility=View.VISIBLE
    }

}