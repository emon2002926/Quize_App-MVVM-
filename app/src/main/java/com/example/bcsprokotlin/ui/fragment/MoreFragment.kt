package com.example.bcsprokotlin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bcsprokotlin.databinding.FragmentMoreBinding
import com.example.bcsprokotlin.ui.fragment.base.BaseFragment

class MoreFragment : BaseFragment<FragmentMoreBinding>(FragmentMoreBinding::inflate) {

    //    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_more, container, false)
//    }

    fun gg (){
        with(binding){
            text2.text = "Emon"
        }
    }
    override fun taskExecutor() {
        gg()
    }


}