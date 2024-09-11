package com.rspl.rojgaarrakshak.landing.verify_aadhar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.databinding.FragmentVerifyAdharBinding
import com.rspl.rojgaarrakshak.landing.LandingPage

class VerifyAdharFragment : Fragment() {

    companion object {
        fun newInstance() = VerifyAdharFragment()
    }

    private lateinit var viewModel: VerifyAdharViewModel
    private lateinit var binding: FragmentVerifyAdharBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_adhar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVerifyAdharBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VerifyAdharViewModel::class.java)

        binding.verify.setOnClickListener {
            (requireActivity() as LandingPage).gotoPage(R.id.payAndCompleteFragment,null,true)
        }
    }
}