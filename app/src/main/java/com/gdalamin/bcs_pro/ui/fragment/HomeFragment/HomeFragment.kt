package com.gdalamin.bcs_pro.ui.fragment.HomeFragment

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.LiveExam
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.data.model.SubjectName
import com.gdalamin.bcs_pro.databinding.FragmentHomeBinding
import com.gdalamin.bcs_pro.ui.SharedViewModel
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.LiveExamAdapter
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.SubjectAdapterHomeScreen
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.fragment.SubjectsFragment.SubjectViewModel
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.gdalamin.bcs_pro.ui.utilities.DataState
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.isInternetAvailable
import com.gdalamin.bcs_pro.ui.utilities.GeneralUtils.showShimmerLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    LiveExamAdapter.HandleClickListener, SubjectAdapterHomeScreen.HandleClickListener,
    NetworkReceiverManager.ConnectivityChangeListener {
    
    private val subjectViewModel: SubjectViewModel by viewModels()
    private val homeFragmentViewModel: HomeFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val subjectAdapter = SubjectAdapterHomeScreen(this)
    private val liveExamAdapter = LiveExamAdapter(this)
    
    
    private lateinit var networkReceiverManager: NetworkReceiverManager
    
    
    override fun loadUi() {
        
        binding.horizontalScrollView.isHorizontalScrollBarEnabled = false
        
        
        observeSubjectName()
        
        
        setupRecyclerView(binding.rvSubjects, subjectAdapter)
        setupRecyclerView(binding.rvLiveExam, liveExamAdapter)
        setListeners()
        
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
        when (isInternetAvailable(requireContext())) {
            true -> {
                homeFragmentViewModel.clearDatabaseIfNeededTime()
            }
            
            false -> {}
        }
        observeLiveExamInfo()
        observer()
    }
    
    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeFragmentViewModel.getExamInfo(apiNumber = 2)
        }
    }
    
    
    override fun onDisconnected() {
    }
    
    private fun observeLiveExamInfo() = binding.apply {
        // Observe network call results
        homeFragmentViewModel.liveExamInfo.observe(viewLifecycleOwner) { response ->
            when (response) {
                is DataState.Error -> {
                    showShimmerLayout(binding.shimmerLiveExam, binding.rvLiveExam)
//                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                
                is DataState.Loading -> {
                    showShimmerLayout(binding.shimmerLiveExam, binding.rvLiveExam)
                }
                
                is DataState.Success -> {
                    hideShimmerLayout(binding.shimmerLiveExam, binding.rvLiveExam)
                    swipeRefreshLayout.isRefreshing = false
                    response.data?.let {
                        liveExamAdapter.submitList(it)
                    }
                }
            }
        }
    }
    
    private fun observeSubjectName() = binding.apply {
        // Observe data from Room database
        subjectViewModel.subjectName.observe(viewLifecycleOwner) { response ->
            when (response) {
                is DataState.Error -> {
                    showShimmerLayout(shimmerSubject, rvSubjects)
                }
                
                is DataState.Loading -> {
                    showShimmerLayout(shimmerSubject, rvSubjects)
                }
                
                is DataState.Success -> {
                    hideShimmerLayout(shimmerSubject, rvSubjects)
                    swipeRefreshLayout.isRefreshing = false
                    response.data?.let {
                        subjectAdapter.submitList(it)
                    }
                }
            }
        }
        subjectViewModel.getSubjectNameDatabase().observe(viewLifecycleOwner) { exams ->
            subjectAdapter.submitList(exams)
        }
        // Check for data and decide to fetch from network or not
        viewLifecycleOwner.lifecycleScope.launch {
            subjectViewModel.getSubjectsName(apiNumber = 3)
        }
    }
    
    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    
    private fun setListeners() = with(binding) {
        
        swipeRefreshLayout.setOnRefreshListener {
            observeSubjectName()
            when (isInternetAvailable(requireContext())) {
                true -> {
                    homeFragmentViewModel.updateDatabase()
                }
                
                false -> {
                }
            }
            
        }
        practice.setOnClickListener {
            sharedViewModel.setStringData("subjectBasedPractise")
            findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
        }
        CvImportantQuestion.setOnClickListener {
            val data = SharedData(
                title = getString(R.string.importantQuestion),
                action = "importantQuestion",
                totalQuestion = 200,
                questionType = "",
                batchOrSubjectName = "",
                time = 0
            )
            sharedViewModel.setSharedData(data)
            findNavController().navigate(R.id.action_homeFragment_to_questionFragment)
        }
        
        
        btnShowAllSubject.setOnClickListener {
            sharedViewModel.setStringData("subjectBasedPractise")
            findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
        }
        
        btnQuestionBank.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_questionBankFragment) }
        
        val examOptionsDialog = ExamOptionsBottomSheet(this@HomeFragment, sharedViewModel)
        
        exams.setOnClickListener {
            examOptionsDialog.show()
            
        }
        
        subjectBasedExam.setOnClickListener {
            sharedViewModel.setStringData("subjectBasedExam")
            findNavController().navigate(R.id.action_homeFragment_to_subjectsFragment)
        }
    }
    
    override fun onClickLiveExam(item: LiveExam) {
        val data = SharedData(
            title = "ডেইলি মডেল টেস্ট",
            action = "normalExam",
            totalQuestion = item.totalQc,
            questionType = "normal",
            batchOrSubjectName = "",
            time = item.time * 60
        
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
    
    
    override fun onDestroyView() {
        super.onDestroyView()
        networkReceiverManager.unregister()
    }
    
    override fun onConnected() {
        observeSubjectName()
        observeLiveExamInfo()
        observer()
        
    }
    
    
}
