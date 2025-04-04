package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdalamin.bcs_pro.R
import com.gdalamin.bcs_pro.data.model.ExamInfoTest
import com.gdalamin.bcs_pro.data.model.Question
import com.gdalamin.bcs_pro.data.model.SharedData
import com.gdalamin.bcs_pro.databinding.FragmentExamBinding
import com.gdalamin.bcs_pro.databinding.ResultViewBinding
import com.gdalamin.bcs_pro.databinding.SubmitAnswerOptionBinding
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.ExamQuestionAdapterPaging
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.QuestionAdapter
import com.gdalamin.bcs_pro.ui.adapter.specificadapters.ResultAdapterTest
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import com.gdalamin.bcs_pro.ui.common.AdViewModel
import com.gdalamin.bcs_pro.ui.common.LoadingStateAdapter
import com.gdalamin.bcs_pro.ui.common.SharedViewModel
import com.gdalamin.bcs_pro.ui.common.observer.RewardedAdObserver
import com.gdalamin.bcs_pro.ui.network.NetworkReceiverManager
import com.gdalamin.bcs_pro.utilities.Constants.Companion.ADMOB_REWARDED_AD_ID
import com.gdalamin.bcs_pro.utilities.DataState
import com.gdalamin.bcs_pro.utilities.GeneralUtils.convertEnglishToBangla
import com.gdalamin.bcs_pro.utilities.GeneralUtils.hideShimmerLayout
import com.gdalamin.bcs_pro.utilities.GeneralUtils.showShimmerLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExamFragment : BaseFragment<FragmentExamBinding>(FragmentExamBinding::inflate),
    ExamQuestionAdapterPaging.OnItemSelectedListenerPaging, QuestionAdapter.OnItemSelectedListener,
    NetworkReceiverManager.ConnectivityChangeListener, RewardedAdObserver.RewardedAdListener {
    
    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val examQuestionAdapterPaging by lazy { ExamQuestionAdapterPaging(this) }
    
    
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModelTest: ExamViewModel by viewModels()
    private val resultViewmodel: ResultViewModel by viewModels()
    private val adViewModel: AdViewModel by activityViewModels()
    
    private lateinit var networkReceiverManager: NetworkReceiverManager
    private lateinit var rewardedAdObserver: RewardedAdObserver<ExamFragment>
    
    private var title = ""
    private var examType = ""
    private var subjectName = ""
    private var answeredQuestions = 0
    private var totalQuestion = 0
    private var stringQuestion = ""
    private var isResultSubmitted = false
    private var individualSubResultTest: List<ExamInfoTest> = emptyList()
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "results_for_statistics"
    
    private var isQuestionLoaded = false
    
    
    override fun loadUi() {
        
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        networkReceiverManager = NetworkReceiverManager(requireContext(), this)
        
        binding.apply {
            fabShowResult.setOnClickListener {
                setUpFabIcon()
            }
        }
        setupRecyclerViewPaging()
        localObserver()
        observeTime()
        handleBackPress()
        
        rewardedAdObserver =
            RewardedAdObserver(requireActivity(), ADMOB_REWARDED_AD_ID, this)
        rewardedAdObserver.preloadRewardedAd()
        
        
    }
    
    
    private fun localObserver() = with(resultViewmodel) {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            viewModelTest.viewModelScope.launch {
                handleAction(data)
            }
        }
        overallResult.observe(viewLifecycleOwner) {
            stringQuestion = it.totalSelectedAnswer.toString()
            answeredQuestions = it.totalSelectedAnswer
        }
        resultSubmission.observe(viewLifecycleOwner) {
            isResultSubmitted = it
            
            if (it == true) {
                examQuestionAdapterPaging.showAnswer(true)
                binding.fabShowResult.setImageResource(R.drawable.baseline_keyboard_double_arrow_up_24)
            }
        }
        
        liveDataExamResults.observe(viewLifecycleOwner) {
            individualSubResultTest = it
        }
        
    }
    
    private suspend fun handleAction(data: SharedData) = binding.apply {
        examType = data.action
        title = data.title
        tvTitle.text = title
        totalQuestion = data.totalQuestion
        resultViewmodel.startCountDown(data.time)
        when (data.action) {
            "liveExam" -> {
                viewModelTest.getExamQuestions(
                    questionAmount = data.totalQuestion,
                    "liveExam",
                    data.questionType
                )
                observePagingQuestion()
                
            }
            
            "normalExam" -> {
                viewModelTest.getExamQuestions(questionAmount = data.totalQuestion)
                observePagingQuestion()
            }
            
            "subjectBasedExam" -> {
                setupRecyclerView()
                observeQuestions()
                viewModelTest.getSubjectExamQuestions(
                    data.batchOrSubjectName,
                    data.totalQuestion
                )
            }
        }
    }
    
    private fun observePagingQuestion() = with(binding) {
        lifecycleScope.launch {
            viewModelTest.questionsPaging.collectLatest { pagingData ->
                examQuestionAdapterPaging.submitData(pagingData)
            }
        }
        examQuestionAdapterPaging.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Error -> {}
                LoadState.Loading -> {
                    showShimmerLayout(
                        shimmerLayout,
                        rvExamQuestion
                    )
                    binding.fabShowResult.visibility = View.GONE
                }
                
                is LoadState.NotLoading -> {
                    isQuestionLoaded = true
                    hideShimmerLayout(shimmerLayout, rvExamQuestion)
                    binding.fabShowResult.visibility = View.VISIBLE
                    
                    if (!isResultSubmitted) {
                        binding.tvTimer.visibility = View.VISIBLE
                    } else {
                        binding.tvTimer.visibility = View.GONE
                    }
                }
            }
        }
    }
    
    private fun observeQuestions() = binding.apply {
        viewModelTest.questions.observe(viewLifecycleOwner) { response ->
            when (response) {
                is DataState.Loading -> {
                    showShimmerLayout(shimmerLayout, rvExamQuestion)
                    fabShowResult.visibility = View.GONE
                }
                
                is DataState.Success -> {
                    isQuestionLoaded = true
                    hideShimmerLayout(shimmerLayout, rvExamQuestion)
                    response.data?.let {
                        questionAdapter.submitList(it)
                        
                        
                    }
                    fabShowResult.visibility = View.VISIBLE
                    tvTimer.visibility = View.VISIBLE
                }
                
                is DataState.Error -> {
                    hideShimmerLayout(shimmerLayout, rvExamQuestion)
                }
            }
        }
    }
    
    
    private fun showResultView() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingResult = ResultViewBinding.inflate(LayoutInflater.from(context))
        resultViewmodel.setBooleanValue(true)
        val resultAdapter by lazy { ResultAdapterTest() }
        bindingResult.apply {
            tvAnsweredQuestion.text = convertEnglishToBangla(totalQuestion.toString())
            
            resultViewmodel.overallResult.observe(viewLifecycleOwner) {
                tvMarks.text = convertEnglishToBangla(it.totalMark.toString())
                tvCorrectAnswer.text = convertEnglishToBangla(it.totalCorrectAnswer.toString())
                tvWrongAnswer.text = convertEnglishToBangla(it.totalWrongAnswer.toString())
            }
            when (examType) {
                "liveExam", "normalExam" -> resultAdapter.submitList(individualSubResultTest)
                "subjectBasedExam" -> {
                    questionAdapter.showAnswer(true)
                    resultViewmodel.overallResult.observe(viewLifecycleOwner) {
                        val overallResult = ExamInfoTest(
                            subjectName = title,
                            totalSelectedAnswer = it.totalSelectedAnswer,
                            totalCorrectAnswer = it.totalCorrectAnswer,
                            totalWrongAnswer = it.totalWrongAnswer,
                            totalMark = it.totalMark
                        )
                        resultAdapter.submitList(listOf(overallResult))
                    }
                }
            }
            rvResultView.adapter = resultAdapter
            rvResultView.layoutManager = LinearLayoutManager(context)
        }
        bottomSheetDialog.setContentView(bindingResult.root)
        bottomSheetDialog.show()
    }
    
    
    @SuppressLint("SetTextI18n")
    fun submitAnswer() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDailogTheme)
        val bindingResult = SubmitAnswerOptionBinding.inflate(LayoutInflater.from(context))
        
        bindingResult.tvDis.text =
            "আপনি ${convertEnglishToBangla(totalQuestion.toString())}" +
                    " প্রশ্নের মধ্যে ${
                        convertEnglishToBangla(
                            answeredQuestions.toString()
                        )
                    } টি প্রশ্নের উত্তর দিয়েছেন"
        
        bindingResult.apply {
            btnSubmit.setOnClickListener {
                
                if (isResultAvailableToShow(answeredQuestions)) {
                    resultViewmodel.overallResult.observe(viewLifecycleOwner) {
                        saveResultForStatic(it)
                    }
                    bottomSheetDialog.dismiss()
                    binding.tvTimer.visibility = View.GONE
                    rewardedAdObserver.showAd()
                } else {
                    Toast.makeText(
                        context,
                        "Please answer Question to see result",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                
            }
            btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        }
        bottomSheetDialog.setContentView(bindingResult.root)
        bottomSheetDialog.show()
    }
    
    private fun isResultAvailableToShow(answeredQuestions: Int): Boolean {
        
        return answeredQuestions != 0
    }
    
    private fun setUpFabIcon() = binding.apply {
        when (isResultSubmitted) {
            true -> showResultView()
            false -> submitAnswer()
        }
    }
    
    private fun handleBackPress() {
        binding.backButton.setOnClickListener {
            if (isResultSubmitted) {
                findNavController().navigateUp()
                
            } else {
                if (isQuestionLoaded) {
                    submitAnswer()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
        
        val backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isResultSubmitted) {
                    findNavController().navigateUp()
                } else {
                    if (isQuestionLoaded) {
                        submitAnswer()
                        
                    } else {
                        findNavController().navigateUp()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback)
        
    }
    
    private fun observeTime() = resultViewmodel.apply {
        timeLeft.observe(viewLifecycleOwner) {
            binding.apply {
                tvTimer.text = convertEnglishToBangla(it)
            }
        }
        isTimerFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                if (!isResultSubmitted) {
                    if (isResultAvailableToShow(answeredQuestions)) {
                        rewardedAdObserver.showAd()
                    }
                }
                
            }
        }
    }
    
    override fun onItemSelectedPaging(item: Question) {
        resultViewmodel.overallResult.observe(viewLifecycleOwner) {
            answeredQuestions = it.totalSelectedAnswer
        }
        resultViewmodel.processQuestion(item)
        
    }
    
    override fun onItemSelected(item: Question) {
        resultViewmodel.overallResult.observe(viewLifecycleOwner) {
            answeredQuestions = it.totalSelectedAnswer
        }
        resultViewmodel.processQuestion(item)
    }
    
    
    private fun setupRecyclerViewPaging() = binding.rvExamQuestion.apply {
        adapter = examQuestionAdapterPaging.withLoadStateFooter(
            footer = LoadingStateAdapter { examQuestionAdapterPaging.retry() }
        )
        layoutManager = LinearLayoutManager(context)
    }
    
    private fun setupRecyclerView() = binding.rvExamQuestion.apply {
        adapter = questionAdapter
        layoutManager = LinearLayoutManager(context)
    }
    
    
    private fun saveResultForStatic(overallResult: ExamInfoTest) {
        val overallNotAnswered: Int = totalQuestion - overallResult.totalSelectedAnswer
        
        if (getInt("totalQuestions", 0) > 1) {
            
            val totalExam: Int = ((getInt("totalExam", 0)) + 1)
            val totalQuestion11: Int = ((getInt("totalQuestions", 0)) + totalQuestion)
            val overAllCorrectAnswer: Int =
                ((getInt("overAllCorrectAnswer", 0)) + overallResult.totalCorrectAnswer)
            val overAllWrongAnswer: Int =
                ((getInt("overAllWrongAnswer", 0)) + overallResult.totalWrongAnswer)
            val overAllNotAnswered: Int =
                ((getInt("overAllNotAnswered", 0)) + overallNotAnswered)
            
            saveInt("totalExam", totalExam)
            saveInt("totalQuestions", totalQuestion11)
            saveInt("overAllCorrectAnswer", overAllCorrectAnswer)
            saveInt("overAllWrongAnswer", overAllWrongAnswer)
            saveInt("overAllNotAnswered", overAllNotAnswered)
            
        } else {
            saveInt("totalExam", 1)
            saveInt("totalQuestions", totalQuestion)
            saveInt("overAllCorrectAnswer", overallResult.totalCorrectAnswer)
            saveInt("overAllWrongAnswer", overallResult.totalWrongAnswer)
            saveInt("overAllNotAnswered", overallNotAnswered)
        }
    }
    
    private fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }
    
    private fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
    
    override fun onConnected() {
        examQuestionAdapterPaging.retry()
    }
    
    override fun onDisconnected() {
    }
    
    override fun onAdClosed() {
        if (isResultAvailableToShow(answeredQuestions)) {
            showResultView()
            
        }
    }
    
    override fun onAdFailedToShow(adError: AdError) {
        if (isResultAvailableToShow(answeredQuestions)) {
            showResultView()
            
        }
    }
    
    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
        if (isResultAvailableToShow(answeredQuestions)) {
            showResultView()
            
        }
    }
    
}
