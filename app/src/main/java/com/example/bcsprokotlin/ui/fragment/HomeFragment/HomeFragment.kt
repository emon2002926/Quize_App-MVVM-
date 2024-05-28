package com.example.bcsprokotlin.ui.fragment.HomeFragment

import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.LiveExamAdapter
import com.example.bcsprokotlin.adapter.SubjectAdapterHomeScreen
import com.example.bcsprokotlin.databinding.FragmentHomeBinding
import com.example.bcsprokotlin.databinding.LayoutShowExamOptionBinding
import com.example.bcsprokotlin.model.SharedData
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectViewModel
import com.example.bcsprokotlin.ui.fragment.base.BaseFragment
import com.example.bcsprokotlin.util.GeneralUtils
import com.example.bcsprokotlin.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val subjectViewModel: SubjectViewModel by viewModels()
    private val liveExamViewModel: HomeFragmentViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val subjectAdapter = SubjectAdapterHomeScreen()
    private val liveExamAdapter = LiveExamAdapter()
    private var questionAmount = 0


    override fun onCreateView() {


        // Launching coroutines in the appropriate ViewModel scope
        subjectViewModel.viewModelScope.launch { subjectViewModel.getSubjectName(3) }
        liveExamViewModel.viewModelScope.launch { liveExamViewModel.getLiveExamInfo(2) }

        // Disable horizontal scroll bar
        binding.horizontalScrollView.isHorizontalScrollBarEnabled = false

        // Observing LiveData
        observeViewModel()

        // Setting up RecyclerViews
        setupRecyclerView(binding.rvSubjects, subjectAdapter)
        setupRecyclerView(binding.rvLiveExam, liveExamAdapter)

        // Setting up listeners
        setListeners()
    }

    private fun observeViewModel() = binding.apply {
        subjectViewModel.subjects.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {
//                    GeneralUtils.hideShimmerLayout(shimmerSubject, rvSubjects)
                }

                is Resource.Loading -> {
                    GeneralUtils.showShimmerLayout(shimmerSubject, rvSubjects)
                }

                is Resource.Success -> {
                    GeneralUtils.hideShimmerLayout(shimmerSubject, rvSubjects)
                    response.data?.let { subjectAdapter.differ.submitList(it) }
                }
            }
        }

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
                    response.data?.let { liveExamAdapter.differ.submitList(it) }
                }
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setListeners() = with(binding) {
        practice.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_questionFragment) }
        btnShowAllSubject.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment) }
        btnQuestionBank.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_questionBankFragment) }
        exams.setOnClickListener { showExamOptions() }
    }


    private fun showExamOptions() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingExamOption = LayoutShowExamOptionBinding.inflate(LayoutInflater.from(context))

        bindingExamOption.apply {
            layout25Min.setOnClickListener {
                questionAmount = 50
                selectIcon(option25Icon)
                eraseIcon(option50Icon, option100Icon)
            }
            layout50Min.setOnClickListener {
                questionAmount = 100
                selectIcon(option50Icon)
                eraseIcon(option25Icon, option100Icon)
            }
            layout100Min.setOnClickListener {
                questionAmount = 200
                selectIcon(option100Icon)
                eraseIcon(option25Icon, option50Icon)
            }
            btnExamStart.setOnClickListener {
                if (questionAmount != 0) {
                    val data = SharedData(
                        "", questionAmount, "normal",
                    )
                    sharedViewModel.setSharedData(data)
                    findNavController().navigate(R.id.action_homeFragment_to_questionFragment)
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
}
