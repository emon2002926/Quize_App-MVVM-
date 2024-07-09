package com.gdalamin.bcs_pro.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<viewBinding : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> viewBinding
) : Fragment() {

    private var _binding: viewBinding? = null
    val binding: viewBinding get() = _binding as viewBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = bindingInflater.invoke(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUi()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun loadUi()


}