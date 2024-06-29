package com.example.bcsprokotlin.ui.fragment.ExamFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.bcsprokotlin.R
import com.example.bcsprokotlin.adapter.QuestionAdapter
import com.example.bcsprokotlin.model.Question
import com.example.bcsprokotlin.ui.SharedViewModel
import com.example.bcsprokotlin.ui.fragment.QuestionFragment.QuestionViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TestExamFragment : Fragment(), QuestionAdapter.OnItemSelectedListener {

    private val questionAdapter by lazy { QuestionAdapter(this) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: QuestionViewModel by viewModels()
    private val resultViewmodel: ResultViewModel by viewModels()

    override fun onCreate(
        savedInstanceState: Bundle?

    ) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_exam, container, false)
    }


    override fun onItemSelected(item: Question) {

    }
}