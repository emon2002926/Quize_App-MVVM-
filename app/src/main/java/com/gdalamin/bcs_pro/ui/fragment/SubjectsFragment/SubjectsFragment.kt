package com.gdalamin.bcs_pro.ui.fragment.SubjectsFragment

import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.databinding.FragmentSubjectsBinding
import com.gdalamin.bcs_pro.databinding.SubjectBasedExamSubmitionBinding
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.SubjectAdapter
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.ui.utilities.DataState
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.isEmpty
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.isInternetAvailable
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubjectsFragment : BaseFragment<FragmentSubjectsBinding>(FragmentSubjectsBinding::inflate),
    SubjectAdapter.HandleClickListener {
    
    private val subjectViewModel: SubjectViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var subjectAdapter: SubjectAdapter
    private var sharedString = ""
    
    override fun loadUi() {
        subjectAdapter = SubjectAdapter(this)
        setupRecyclerView()
        setupListeners()
        observeSubjectName()
        observeSharedData()
    }
    
    
    private fun observeSubjectName() = binding.apply {
        // Observe network call results
        subjectViewModel.subjectName.observe(viewLifecycleOwner) { response ->
            when (response) {
                is DataState.Error -> {
//                    Log.d("HomeFragment", response.message.toString())
                    if (isInternetAvailable(requireContext())) {
                        Toast.makeText(
                            context,
                            CHECK_INTERNET_CONNECTION_MESSAGE,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                
                is DataState.Loading -> {
                    GeneralUtils.showShimmerLayout(binding.shimmerLayout, binding.rvSubjects)
                }
                
                is DataState.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLayout, binding.rvSubjects)
                    response.data?.let {
                        subjectAdapter.submitList(it)
                    }
                }
            }
        }
        // Observe data from Room database
        subjectViewModel.getSubjectNameDatabase().observe(viewLifecycleOwner) { exams ->
            subjectAdapter.submitList(exams)
        }
        // Check for data and decide to fetch from network or not
        viewLifecycleOwner.lifecycleScope.launch {
            subjectViewModel.getSubjectsName(apiNumber = 3)
        }
        
    }
    
    private fun setupRecyclerView() {
        binding.rvSubjects.apply {
            adapter = subjectAdapter
        }
    }
    
    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    
    private fun observeSharedData() {
        sharedViewModel.sharedString.observe(viewLifecycleOwner) {
            sharedString = it
            when (sharedString) {
                "subjectBasedPractise" -> binding.tvTitle.text = "অনুশীলন"
                "subjectBasedExam" -> binding.tvTitle.text = "বিষয়ভিত্তিক পরীক্ষা"
            }
        }
    }
    
    
    override fun onClick(subjectName: SubjectName) {
        when (sharedString) {
            "subjectBasedPractise" -> showSubjectBasedQuestion(subjectName)
            "subjectBasedExam" -> subjectBasedExamSubmission(
                subjectName.subject_name,
                subjectName.subject_code
            )
        }
        
    }
    
    
    private fun showSubjectBasedQuestion(subjectName: SubjectName) {
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
                        title = subjectName,
                        action = "subjectBasedExam",
                        totalQuestion = question.toInt(),
                        questionType = "",
                        batchOrSubjectName = subjectCode,
                        time = time.toInt() * 60
                    )
                    sharedViewModel.setSharedData(data)
                    findNavController().navigate(R.id.action_subjectsFragment_to_examFragment)
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