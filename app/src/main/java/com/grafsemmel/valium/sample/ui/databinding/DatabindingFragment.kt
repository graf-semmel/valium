package com.grafsemmel.valium.sample.ui.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.grafsemmel.valium.sample.databinding.FragmentDatabindingBinding

class DatabindingFragment : Fragment() {

    private lateinit var binding: FragmentDatabindingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDatabindingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewmodel = ViewModelProviders.of(this).get(DatabindingViewModel::class.java)
    }
}