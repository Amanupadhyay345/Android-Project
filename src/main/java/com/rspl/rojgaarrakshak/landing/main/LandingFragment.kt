package com.rspl.rojgaarrakshak.landing.main

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentLandingBinding
import com.rspl.rojgaarrakshak.landing.LandingPage

class LandingFragment : Fragment() {

    companion object {
        fun newInstance() = LandingFragment()
    }

    private lateinit var viewModel: LandingViewModel
    private lateinit var binding : FragmentLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLandingBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LandingViewModel::class.java)

        binding.signInBtn.setOnClickListener {
            (requireActivity() as LandingPage).gotoPage(R.id.signInFragment,null,true)
        }

        binding.signUpBtn.setOnClickListener {
            (requireActivity() as LandingPage).gotoPage(R.id.signUpFragment,null,true)

//            (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,true)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.backBtn.setOnClickListener {
            val intent = Intent(requireContext(), DashboardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as LandingPage).enableBackBtn(false)
    }

}