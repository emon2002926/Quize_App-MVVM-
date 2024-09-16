package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.common.SharedViewModel
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.isInternetAvailable

class HomeFragmentClickListener(
    private val fragment: Fragment,
    private val binding: FragmentHomeBinding,
    private val sharedViewModel: SharedViewModel,
    private val homeFragmentViewModel: HomeFragmentViewModel
) {
    
    init {
        setClickListeners()
    }
    
    private fun setClickListeners() = binding.apply {
        // Navigate to question bank
        btnQuestionBank.setOnClickListener {
            navigateTo(R.id.action_homeFragment_to_questionBankFragment)
        }
        
        // Set listeners for subject-based questions
        setSubjectClickListener(tvInternationalAffairs, "আন্তর্জাতিক বিষয়াবলি", "IA")
        setSubjectClickListener(tvBangladeshAffairs, "বাংলাদেশ বিষয়াবলি", "BA")
        setSubjectClickListener(tvGeography, "ভূগোল", "GEDM")
        
        // Navigate to all subjects
        val subjectNavigateAction = { sharedViewModel.setStringData("subjectBasedPractise") }
        listOf(tvAllSubject, practice, btnShowAllSubject).forEach { view ->
            view.setOnClickListener {
                subjectNavigateAction()
                navigateTo(R.id.action_homeFragment_to_subjectsFragment)
            }
        }
        
        // Handle subject-based exams
        subjectBasedExam.setOnClickListener {
            sharedViewModel.setStringData("subjectBasedExam")
            navigateTo(R.id.action_homeFragment_to_subjectsFragment)
        }
        
        // Handle exams bottom sheet
        exams.setOnClickListener {
            ExamOptionsBottomSheet(fragment, sharedViewModel).show()
        }
        
        // Important questions
        CvImportantQuestion.setOnClickListener {
            val data = SharedData(
                title = fragment.getString(R.string.importantQuestion),
                action = "importantQuestion",
                totalQuestion = 200,
                questionType = "",
                batchOrSubjectName = "",
                time = 0
            )
            sharedViewModel.setSharedData(data)
            navigateTo(R.id.action_homeFragment_to_questionFragment)
        }
        
        // Swipe refresh logic
        swipeRefreshLayout.setOnRefreshListener {
            if (isInternetAvailable(fragment.requireContext())) {
                homeFragmentViewModel.updateDatabase()
            } else {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    
    private fun setSubjectClickListener(view: View, subjectName: String, subjectCode: String) {
        view.setOnClickListener {
            showSubjectBasedQuestion(subjectName, subjectCode)
        }
    }
    
    private fun navigateTo(actionId: Int) {
        fragment.findNavController().navigate(actionId)
    }
    
    private fun showSubjectBasedQuestion(subjectName: String, subjectCode: String) {
        val data = SharedData(
            title = subjectName,
            action = "subjectBasedQuestions",
            totalQuestion = 200,
            questionType = "",
            batchOrSubjectName = subjectCode,
            time = 0
        )
        sharedViewModel.setSharedData(data)
        navigateTo(R.id.action_homeFragment_to_questionFragment)
    }
}