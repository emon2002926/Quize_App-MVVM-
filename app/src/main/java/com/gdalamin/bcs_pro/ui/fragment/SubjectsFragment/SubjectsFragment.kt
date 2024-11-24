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
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.SubjectAdapter
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.common.SharedViewModel
import com.gdalamin.bcs_pro.ui.common.observer.InterstitialAdObserver
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_INTERSTITIAL_SUBJECT_AD_ID
import com.gdalamin.bcs_pro.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils
import com.gdalamin.bcs_pro.utilities.GeneralUtils.isEmpty
import com.gdalamin.bcs_pro.utilities.GeneralUtils.isInternetAvailable
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubjectsFragment : BaseFragment<FragmentSubjectsBinding>(FragmentSubjectsBinding::inflate),
    SubjectAdapter.HandleClickListener,
    NetworkReceiverManager.ConnectivityChangeListener {
    
    private val subjectViewModel: SubjectViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val adViewModel: AdViewModel by activityViewModels()
    private lateinit var subjectAdapter: SubjectAdapter
//    private lateinit var subjectAdapterAD: SubjectWithAdsAdapter
    
    private var sharedString = ""
    lateinit var interstitialAdObserver: InterstitialAdObserver<SubjectsFragment, FragmentSubjectsBinding>
    
    override fun loadUi() {
        subjectAdapter = SubjectAdapter(this)
        setupRecyclerView()
        setupListeners()
        observer()
        observeSubjectName()
        observeSharedData()
        
        interstitialAdObserver = InterstitialAdObserver(
            fragment = this,
            adViewModel = adViewModel,
            binding = binding,
            adUnitId = ADMOB_INTERSTITIAL_SUBJECT_AD_ID,
            navigateAction = {
                findNavController().navigate(R.id.action_subjectsFragment_to_questionFragment) // Navigation action
            }
        )
//        setupRecyclerViewWithAd()
    }

//    private fun setupRecyclerViewWithAd() {
//        subjectAdapterAD = SubjectWithAdsAdapter(this)  // Pass the click listener (this fragment)
//        binding.rvSubjects.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvSubjects.adapter = subjectAdapterAD
//    }
//
    
    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            subjectViewModel.getSubjectsName()
        }
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
//                        subjectAdapterAD.setSubjectsWithAds(it)
                    }
                }
            }
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
//        findNavController().navigate(R.id.action_subjectsFragment_to_questionFragment)
        interstitialAdObserver.observeInterstitialAd()
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
    
    override fun onConnected() {
        observer()
    }
    
    override fun onDisconnected() {
    }
    
}