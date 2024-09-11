package com.rspl.rojgaarrakshak.landing.GuestUser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentGuestFregmentBinding
import com.rspl.rojgaarrakshak.landing.LandingPage


class GuestFregment : Fragment() {

    companion object{
        fun newInstance() = GuestFregment()
    }
    private lateinit var binding: FragmentGuestFregmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_fregment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding=FragmentGuestFregmentBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (requireActivity() as LandingPage).enableBackBtn(false)

        binding.ContinueasaGuest.setOnClickListener {
            var intent=Intent(requireActivity(),DashboardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.AlreadyUser.setOnClickListener {
            (requireActivity() as LandingPage).gotoPage(R.id.signInFragment,null,true)
//            (requireActivity() as LandingPage).gotoPage(R.id.userLocation2,null,true)

        }

        binding.Register.setOnClickListener {
            (requireActivity() as LandingPage).gotoPage(R.id.signUpFragment,null,true)
        }


    }


}
