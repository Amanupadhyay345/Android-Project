package com.rspl.rojgaarrakshak.landing.sign_in

import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentSignInBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: FragmentSignInBinding
    private var progressDialog : Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

//                (requireActivity() as LandingPage).gotoPage(R.id.uploadImage,null,true)

                val intent = Intent(requireActivity(), LandingPage::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        progressDialog = requireActivity().getProgressDialog()
        viewModel.getOtpResponse.observe(viewLifecycleOwner){
            progressDialog?.dismiss()
            when(it){
                is NetworkResult.Loading->{
                    Log.e("verifyOtpResponse","loading...")
                    progressDialog?.show()
                }
                is NetworkResult.Success->{

                    if (it.data!!.status==false)
                    {
                        requireContext().showalertdilog(it.data.message!!)
                    }
                    else
                    {
                        viewModel.getOtpResponse.postValue(null)
//                    (requireActivity() as OnBoarding).namePage()
                        val bundle = Bundle()
                        bundle.putString("mobile",binding.mobileNum.text.toString())
                        bundle.putString("source","signin")
                        (requireActivity() as LandingPage).gotoPage(R.id.OTPFragment,bundle,true)
                    }
                }
                is NetworkResult.Error -> {
                    requireContext().showalertdilog(it.message!!)

                }
            }
        }

        binding.signInBtn.setOnClickListener {

           var inputvalue=binding.mobileNum.text.toString()


            val digitPrefixes = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

            val startsWithDigit = digitPrefixes.any { inputvalue.startsWith(it) }

            if (startsWithDigit)
            {
                val isNumeric = inputvalue.all { it.isDigit() }

                if (inputvalue.length == 10 && isNumeric)
                {
                    progressDialog!!.show()
                    viewModel.requestSignIn(requireActivity(), binding.mobileNum.text.toString())

                }
                else
                {
                    requireActivity().showalertdilog(" Please Enter Valid  Mobile Number.")
                }
            }
           else
            {
                if (isValidAlphaNumeric(inputvalue)) {
                    progressDialog!!.show()
                    viewModel.requestSignIn(requireActivity(), binding.mobileNum.text.toString())
                } else {

                    requireActivity().showalertdilog(" Only alphanumeric characters allowed.")
                }
            }


        }

        binding.signUpBtn.setOnClickListener {
            (requireActivity() as LandingPage).gotoPage(R.id.signUpFragment,null,true)
        }
    }

    private fun isValidAlphaNumeric(inputvalue: String): Boolean {

        val pattern = Regex("^[a-zA-Z0-9]+$")

        return pattern.matches(inputvalue)
    }

}