package com.example.bcsprokotlin.ui.fragment.HomeFragment

import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.LiveExamAdapter
import com.example.bcsprokotlin.adapter.SubjectAdapterHomeScreen
import com.example.bcsprokotlin.databinding.FragmentHomeBinding
import com.example.bcsprokotlin.databinding.LayoutShowExamOptionBinding
import com.example.bcsprokotlin.model.LiveExam
import com.example.bcsprokotlin.model.SharedData
import com.example.bcsprokotlin.model.SubjectName
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.base.BaseFragment
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectViewModel
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    LiveExamAdapter.HandleClickListener, SubjectAdapterHomeScreen.HandleClickListener {

    private val subjectViewModel: SubjectViewModel by viewModels()
    private val liveExamViewModel: HomeFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val subjectAdapter = SubjectAdapterHomeScreen(this)
    private val liveExamAdapter = LiveExamAdapter(this)
    private var questionAmount = 0
    private var time = 0
    private var examType = ""


    override fun loadUi() {

//        subjectViewModel.viewModelScope.launch { subjectViewModel.getSubjectName(3) }

        binding.horizontalScrollView.isHorizontalScrollBarEnabled = false

        observeLiveExamInfo()
        observeSubjectName()
        // Setting up RecyclerViews
        setupRecyclerView(binding.rvSubjects, subjectAdapter)
        setupRecyclerView(binding.rvLiveExam, liveExamAdapter)
        setListeners()


    }

    private fun observeLiveExamInfo() = binding.apply {

        // Observe network call results
        liveExamViewModel.liveExamInfo.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    Log.d("HomeFragment", response.message.toString())
                }

                is Resource.Loading -> {
                    GeneralUtils.showShimmerLayout(binding.shimmerLiveExam, binding.rvLiveExam)
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(binding.shimmerLiveExam, binding.rvLiveExam)
                    response.data?.let {
                        liveExamAdapter.submitList(it)
                    }
                }
            }
        }

        // Observe data from Room database
        liveExamViewModel.getExamsFromDatabase().observe(viewLifecycleOwner) { exams ->
            liveExamAdapter.submitList(exams)
        }
        // Check for data and decide to fetch from network or not
        viewLifecycleOwner.lifecycleScope.launch {
            liveExamViewModel.getExamInfo(apiNumber = 2)
        }

    }

    private fun observeSubjectName() = binding.apply {
        // Observe network call results
        subjectViewModel.subjectName.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
                    GeneralUtils.showShimmerLayout(shimmerSubject, rvSubjects)
                    Log.d("HomeFragment", response.message.toString())
                }

                is Resource.Loading -> {
                    GeneralUtils.showShimmerLayout(shimmerSubject, rvSubjects)
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(shimmerSubject, rvSubjects)
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
            subjectViewModel.getSubjectNameN(apiNumber = 3)
        }

    }


    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setListeners() = with(binding) {
        practice.setOnClickListener {
            sharedViewModel.setStringData("subjectBasedPractise")
            findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
        }

        btnShowAllSubject.setOnClickListener {
            sharedViewModel.setStringData("subjectBasedPractise")
            findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
        }

        btnQuestionBank.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_questionBankFragment) }

        exams.setOnClickListener { showExamOptions() }

        subjectBasedExam.setOnClickListener {
            sharedViewModel.setStringData("subjectBasedExam")
            findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
        }
    }

    private fun showExamOptions() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingExamOption = LayoutShowExamOptionBinding.inflate(LayoutInflater.from(context))

        bindingExamOption.apply {
            layout25Min.setOnClickListener {
                questionAmount = 50
                time = 1500
                selectIcon(option25Icon)
                eraseIcon(option50Icon, option100Icon)
                examType = "50QuestionExam"
            }
            layout50Min.setOnClickListener {
                questionAmount = 100
                time = 3000
                selectIcon(option50Icon)
                eraseIcon(option25Icon, option100Icon)
                examType = "100QuestionExam"
            }
            layout100Min.setOnClickListener {
                questionAmount = 200
                time = 6000
                selectIcon(option100Icon)
                eraseIcon(option25Icon, option50Icon)
                examType = "200QuestionExam"
            }
            btnExamStart.setOnClickListener {
                if (questionAmount != 0) {
                    val data = SharedData(
                        "সামগ্রিক পরীক্ষা", "normalExam", questionAmount, examType, "", time
                    )
                    sharedViewModel.setSharedData(data)
                    findNavController().navigate(R.id.action_homeFragment_to_examFragment)
                    bottomSheetDialog.dismiss()
                } else {
                    Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()

                }
            }
            btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }

        }


        bottomSheetDialog.setContentView(bindingExamOption.root)
        bottomSheetDialog.show()


    }

    private fun selectIcon(imageView: ImageView) {
        imageView.setImageResource(R.drawable.black_dot)
    }

    fun eraseIcon(imageView: ImageView, imageView2: ImageView) {
        imageView.setImageResource(R.drawable.round_back_white50_100)
        imageView2.setImageResource(R.drawable.round_back_white50_100)
    }

    override fun onClickLiveExam(item: LiveExam) {

        val data = SharedData(
            "ডেইলি মডেল টেস্ট ", "liveModelTest", item.totalQc, "normal", "", 0
        )
        sharedViewModel.setSharedData(data)
        findNavController().navigate(R.id.action_homeFragment_to_examFragment)
    }

    override fun onSubjectClick(subjectName: SubjectName) {
        showSubjectBasedQuestion(subjectName)
    }

    fun showSubjectBasedQuestion(subjectName: SubjectName) {
        val data = SharedData(
            subjectName.subject_name,
            "subjectBasedQuestions",
            20,
            "",
            subjectName.subject_code,
            0
        )
        sharedViewModel.setSharedData(data)
        findNavController().navigate(R.id.action_homeFragment_to_questionFragment)
    }


}
