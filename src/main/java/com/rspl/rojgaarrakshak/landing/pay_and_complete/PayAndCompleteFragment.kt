package com.rspl.rojgaarrakshak.landing.pay_and_complete

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.WebView.WebViewActivity
import com.rspl.rojgaarrakshak.bottom_sheet.PaymentDoneBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.PaymentStausBtnLisner
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentPayAndCompleteBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayAndCompleteFragment : Fragment() {

    companion object {
        fun newInstance() = PayAndCompleteFragment()
    }

    private lateinit var viewModel: PayAndCompleteViewModel
    private lateinit var binding: FragmentPayAndCompleteBinding
    var paymentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                try {
                    var accesstoken = requireActivity().getPrefeb("UserToken")
                    var trangectionid = requireActivity().getPrefeb("marchenttrangetionid")

                    if (accesstoken.isNotEmpty() && trangectionid.isNotEmpty())
                    {
                        val ApplyToken = "Bearer $accesstoken"

                        viewModel.PaymentStatus(ApplyToken,trangectionid)
                    }

                }catch (e:Exception)
                {
                    println(e)
                }
            }
        }

    private lateinit var messageTV: TextView
    private var uri: Uri? = null
    lateinit var UserName: String
    private var progressDialog: Dialog? = null
    lateinit var UserEmail: String
    lateinit var UserPhone: String
    lateinit var UserDOB: String
    lateinit var UserSkill: String
    lateinit var UserLocation: String
    lateinit var Document: String
    lateinit var accesstoken: String
    lateinit var paydonebottomseet: PaymentDoneBottomSheet
    lateinit var Deeplink: String
    lateinit var NewAccesstoken: String
    lateinit var MarchentTragectionid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pay_and_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPayAndCompleteBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PayAndCompleteViewModel::class.java)

        accesstoken = ""
        Deeplink = ""
        MarchentTragectionid = ""
        NewAccesstoken = ""

        UserName = ""
        UserDOB = ""
        UserEmail = ""
        UserLocation = ""
        UserPhone = ""
        UserSkill = ""
        Document = ""

        val text = "Terms and Conditions"

        val spannableString = SpannableString(text)

        spannableString.setSpan(UnderlineSpan(), 0, text.length, 0)
        binding.termsandCondision.text = spannableString

        (requireActivity() as LandingPage).enableBackBtn(false)

        progressDialog = requireActivity().getProgressDialog()



        binding.termsandCondision.setOnClickListener {

            if (isNetworkAvabile()) {
                val pdfUrl = "https://mobileapp.rakshaksecuritas.com/Terms-of-Service.pdf"
                openPdfInExternalViewer(pdfUrl)

            } else {
                shownoInternatedilog()
            }

        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

//                (requireActivity() as LandingPage).gotoPage(R.id.uploadImage,null,true)

                val intent = Intent(requireActivity(), DashboardActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        try {
            UserName = requireContext().getPrefeb("UserName")
            UserEmail = requireContext().getPrefeb("Useremail")
            UserPhone = requireContext().getPrefeb("Userphone")
            UserDOB = requireContext().getPrefeb("UserDOB")
            UserSkill = requireContext().getPrefeb("Userskill")
            UserLocation = requireContext().getPrefeb("Userlocation")
            Document = requireContext().getPrefeb("UserUploadedDocument")

            binding.Username.text = UserName

            if (UserEmail.isNotEmpty())
            {
                binding.Useremail.text = UserEmail
            }
            else
            {
                binding.Useremail.text = "Email"
            }

            binding.UserPhone.text = UserPhone
            binding.UserSkills.text = UserSkill
            binding.Userlocation.text = UserLocation
            binding.Userdateofbirth.text = UserDOB
            binding.uploadeddocument.text = Document

        } catch (e: Exception) {
            Log.e("null", "null")
        }

        viewModel.paymentinisilalResponse.observe(viewLifecycleOwner) {

            progressDialog?.let {
                it.dismiss()
            }
            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.let {
                        it.show()
                    }
                }

                is NetworkResult.Success -> {

                    val url = it.data!!.paymentData!!.data!!.instrumentResponse!!.redirectInfo!!.url

                    var marchenttrangetionid: String =
                        it.data.paymentData!!.data!!.merchantTransactionId!!

                    Log.e("marchentid", marchenttrangetionid)

                    requireActivity().putPrefeb("marchenttrangetionid", marchenttrangetionid)

                    val intent = Intent(requireContext(), WebViewActivity::class.java)
                    intent.putExtra("url", url)
//                    startActivity(intent)

                    paymentLauncher.launch(intent)

                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)
                }

                else -> {

                }
            }
        }

        viewModel.paymentStatusResponse.observe(viewLifecycleOwner){

            progressDialog?.let {
                it.dismiss()
            }
            when(it){
                is NetworkResult.Loading -> {
                    progressDialog?.let {
                        it.show()
                    }



                }
                is NetworkResult.Success -> {

                    var message=it.data!!.paymentStatus!!.message
                    val paymentstatus= it.data.paymentStatus!!.success

                    Log.e("PaymentStatusMessage",message.toString())

                    if (message!!.isNotEmpty())
                    {
                        paydonebottomseet=
                            PaymentDoneBottomSheet(message!!,object : PaymentStausBtnLisner {
                                override fun onApplybtnclick() {

                                       if (paymentstatus==true)
                                       {
                                           val intent = Intent(requireActivity(), DashboardActivity::class.java)
                                           intent.putExtra("PaymentResponseSuccess",true)
                                           startActivity(intent)
                                           requireActivity().finish()
                                       }
                                      else
                                       {
                                           val intent = Intent(requireActivity(), DashboardActivity::class.java)
                                           startActivity(intent)
                                           requireActivity().finish()
                                       }




                                }
                            })

                        paydonebottomseet.isCancelable=false
                        paydonebottomseet.show(childFragmentManager,"paydonebottomseet")

                    }

                }
                is NetworkResult.Error -> {

                }
            }
        }

        binding.payAndCompleteBtn.setOnClickListener {

            try {

                accesstoken = requireActivity().getPrefeb("UserToken")

                if (accesstoken.isNotEmpty()) {
                    val ApplyToken = "Bearer $accesstoken"

                    Log.e("UserApplyToken", ApplyToken)
                    viewModel.InisialPayment(ApplyToken)
                } else {
                    Log.e("null", "null")

                }
            } catch (e: Exception) {
                println(e)
            }



        }
    }


    private fun isNetworkAvabile(): Boolean {

        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected


    }

    private fun openPdfInExternalViewer(pdfUrl: String) {

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Handle exceptions, e.g., if no PDF viewer app is available on the device
        }

    }

    private fun shownoInternatedilog() {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.single_button_custom_dilog, null)

        builder.setView(dialogView)

        val alertDialog = builder.create()

        val message: TextView = dialogView.findViewById(R.id.responsemessage)

        message.text = "Please check your internet connection and try again."

        val okbutton: Button = dialogView.findViewById(R.id.Ok)

        okbutton.text = "OK"


        okbutton.setOnClickListener {

            alertDialog.dismiss()

        }
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.setCancelable(false)

        alertDialog.show()

    }

}