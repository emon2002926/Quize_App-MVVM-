package com.example.bcsprokotlin.ui.fragment.SubjectsFragment

import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.SubjectAdapter
import com.example.bcsprokotlin.databinding.FragmentSubjectsBinding
import com.example.bcsprokotlin.databinding.SubjectBasedExamSubmitionBinding
import com.example.bcsprokotlin.model.SharedData
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.GeneralUtils.isEmpty
import com.example.bcsprokotlin.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubjectsFragment : BaseFragment<FragmentSubjectsBinding>(FragmentSubjectsBinding::inflate),
    SubjectAdapter.HandleClickListener {

    private val viewModel: SubjectViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var subjectAdapter: SubjectAdapter


    override fun onCreateView() {

        subjectAdapter = SubjectAdapter(this)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.viewModelScope.launch {
            viewModel.getSubjectName(3)
        }


    }

    private fun setupRecyclerView() {
        binding.rvSubjects.apply {
            adapter = subjectAdapter
        }
    }

    private fun setupObservers() {

        viewModel.subjects.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvSubjects)
                    response.data?.let { subjectAdapter.submitList(it) }
                }

                is Resource.Error -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvSubjects)
                    response.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    GeneralUtils.showShimmerLayout(binding.shimmerLayout, binding.rvSubjects)

                }
            }
        }
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    override fun onClick(subjectName: SubjectName) {

        sharedViewModel.sharedString.observe(viewLifecycleOwner, { data ->
            when (data) {
                "subjectBasedQuestions" -> showSubjectBasedQuestion(subjectName)
                "subjectBasedExamQuestions" -> subjectBasedExamSubmission(
                    subjectName.subject_name,
                    subjectName.subject_code
                )
            }
        })
    }


    fun showSubjectBasedQuestion(subjectName: SubjectName) {

        val data = SharedData(
            subjectName.subject_name,
            "subjectBasedQuestions",
            200,
            "",
            subjectName.subject_code,
            0
        )
        sharedViewModel.setSharedData(data)
        findNavController().navigate(R.id.action_subjectsFragment_to_questionFragment)
    }

    private fun subjectBasedExamSubmission(subjectName: String, subjectCode: String) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingExamOption =
            SubjectBasedExamSubmitionBinding.inflate(LayoutInflater.from(context))

        with(bindingExamOption) {

            tvSubjectName.text = subjectName

            btnSubmit.setOnClickListener {
                val time = etTime.text.toString().trim()
                val question = etNumOfQuestion.text.toString().trim()

                if (time.isNotEmpty() && question.isNotEmpty()) {

                    val data = SharedData(
                        subjectCode,
                        "subjectBasedQuestions",
                        question.toInt(),
                        "",
                        subjectCode,
                        time.toInt()
                    )
                    sharedViewModel.setSharedData(data)
                    findNavController().navigate(R.id.action_subjectsFragment_to_questionFragment)
                    bottomSheetDialog.dismiss()

                } else {
                    etTime.isEmpty("please enter time")
                    etNumOfQuestion.isEmpty("please enter the amount of question")
                }

            }
            btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        }

        bottomSheetDialog.setContentView(bindingExamOption.root)
        bottomSheetDialog.show()

    }

}