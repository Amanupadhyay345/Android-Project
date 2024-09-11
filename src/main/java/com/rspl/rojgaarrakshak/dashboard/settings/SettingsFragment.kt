package com.rspl.rojgaarrakshak.dashboard.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.rspl.rojgaarrakshak.NetworkLayer.ApiServices
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.WebView.WebViewActivity
import com.rspl.rojgaarrakshak.bottom_sheet.CheckInBottomSheet
import com.rspl.rojgaarrakshak.bottom_sheet.ContactUsBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb

import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentSearchJobBinding
import com.rspl.rojgaarrakshak.databinding.FragmentSettingsBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    companion object {
        fun newInstance() = SettingsFragment()
    }
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding
    var isAlertDialogShown = false
    lateinit var accesstoken:String
//    var string:String
    lateinit var newaccesstoken:String
    lateinit var userlogin:String
    private var progressDialog : Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)
    }
    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        accesstoken=""
        userlogin=""
        newaccesstoken=""
        progressDialog = requireActivity().getProgressDialog()

        try {
            userlogin = requireActivity().getPrefeb("UserToken")

            if (userlogin.isEmpty())
            {
                binding.findJobContainer.visibility=View.GONE
                binding.SalarySlip.visibility=View.GONE
                binding.accountDeactivateContainer.visibility=View.GONE
                binding.Logout.visibility=View.GONE
            }
            else
            {
                var Employeestatus=requireActivity().getPrefeb("EmployeeStatus")

                if (Employeestatus=="Employee")
                {
                    binding.SalarySlip.visibility=View.VISIBLE
                    binding.findJobContainer.visibility=View.GONE
                }
                else
                {
                    binding.SalarySlip.visibility=View.GONE
                    binding.findJobContainer.visibility=View.VISIBLE
                }
            }

        }catch (e:Exception)
        {
//            requireActivity().printToast("null")
        }
        binding.findJobContainer.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.custom_dilog, null)

            builder.setView(dialogView)

            val alertDialog = builder.create()

            val message:TextView= dialogView.findViewById(R.id.messagetext)

            message.text = "Are you sure you want to Request for Refund"

            val okbutton: Button =dialogView.findViewById(R.id.Login)

            okbutton.text = "OK"

            okbutton.setOnClickListener {
                alertDialog.dismiss()

                try {

                    newaccesstoken = requireActivity().getPrefeb("UserToken")

                    if (newaccesstoken.isNotEmpty()) {
                        val ApplyToken = "Bearer $newaccesstoken"
                        viewModel.RequestForRefund(ApplyToken)
                    } else {


                    }
                }catch (e:Exception)
                {
                    println(e)
                }

            }

            val cancelbutton: Button =dialogView.findViewById(R.id.cancel)

            cancelbutton.setOnClickListener {
                alertDialog.dismiss()

            }

            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            alertDialog.setCancelable(false)

            alertDialog.show()
        }

        viewModel.RequestRefundResponce.observe(viewLifecycleOwner) {

            progressDialog?.let {
                it.dismiss()
            }
            when (it) {
                is NetworkResult.Loading ->{
                    progressDialog?.let {
                        it.show()
                    }

                }
                is NetworkResult.Success -> {


                    requireActivity().showalertdilog(it.data!!.message!!)


                    viewModel.RequestRefundResponce.postValue(null)
                }
                is NetworkResult.Error ->{

                    requireActivity().showalertdilog(it.message!!)

                    viewModel.RequestRefundResponce.postValue(null)

                }
                else -> {

                }
            }
        }

        binding.privacypolicies.setOnClickListener {


            if (isNetworkAvabile())
            {
//                val pdfUrl = "https://mobileapp.rakshaksecuritas.com/Privacy-Policy.pdf"
//                openPdfInExternalViewer(pdfUrl)
                val webIntent = Intent(requireActivity(),WebViewActivity::class.java)
                webIntent.putExtra("url",ApiServices.privacy_policy)
                startActivityForResult(webIntent,1234)
            }
            else
            {
                    shownoInternatedilog()
            }

        }

        binding.refundPolicy.setOnClickListener {


            if (isNetworkAvabile())
            {
                val webIntent = Intent(requireActivity(),WebViewActivity::class.java)
                webIntent.putExtra("url",ApiServices.refund_policy)
                startActivityForResult(webIntent,1234)
            }
            else
            {
                    shownoInternatedilog()
            }

        }



        binding.ContactUs.setOnClickListener {
            var Contactusbottomsheet = ContactUsBottomSheet()
            Contactusbottomsheet.show(childFragmentManager,"Contact_us")
        }
        binding.termsofservice.setOnClickListener {

            if (isNetworkAvabile())
            {
                val pdfUrl = "https://mobileapp.rakshaksecuritas.com/Terms-of-Service.pdf"
                openPdfInExternalViewer(pdfUrl)

            }
            else
            {
                shownoInternatedilog()
            }
        }
          binding.Logout.setOnClickListener {

              val builder = AlertDialog.Builder(requireContext())
              val inflater = layoutInflater
              val dialogView = inflater.inflate(R.layout.custom_dilog, null)

              builder.setView(dialogView)

              val alertDialog = builder.create()

              val message:TextView= dialogView.findViewById(R.id.messagetext)

              message.text = "Are you sure you want to Logout"

              val okbutton: Button =dialogView.findViewById(R.id.Login)

              okbutton.text = "OK"


              okbutton.setOnClickListener {

                  alertDialog.dismiss()

                  val preferences = requireContext()
                      .getSharedPreferences("amar_${context?.packageName}", Context.MODE_PRIVATE)
                  var editor=preferences.edit()
                  editor.clear()
                  editor.apply()
                  val intent = Intent(requireContext(), LandingPage::class.java)
                  startActivity(intent)
                  requireActivity().finish()

              }

              val cancelbutton: Button =dialogView.findViewById(R.id.cancel)

              cancelbutton.setOnClickListener {

                  alertDialog.dismiss()

              }
              alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

              alertDialog.setCancelable(false)

              alertDialog.show()

          }
       binding.SalarySlip.setOnClickListener {
           (requireActivity() as DashboardActivity).gotoPage(R.id.salarySlipsFragment,null)
       }

        binding.Apppermission.setOnClickListener {
            openApppermiison(requireContext())
        }
        binding.accountDeactivateContainer.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.custom_dilog, null)

            builder.setView(dialogView)

            val alertDialog = builder.create()

            val message:TextView= dialogView.findViewById(R.id.messagetext)

            message.text = "Are you sure you want to delete your account"

            val okbutton: Button =dialogView.findViewById(R.id.Login)

            okbutton.text = "OK"


            okbutton.setOnClickListener {

                alertDialog.dismiss()

                try {

                    accesstoken = requireActivity().getPrefeb("UserToken")

                    if (accesstoken.isNotEmpty()) {
                        val ApplyToken = "Bearer $accesstoken"
                        Log.e("ApplyToken",ApplyToken)

                        viewModel.DeleteAccount(ApplyToken)
                    } else {
//                        requireActivity().printToast("null")


                    }
                } catch (e: Exception) {
                    println(e)
                }

            }

            val cancelbutton: Button =dialogView.findViewById(R.id.cancel)

            cancelbutton.setOnClickListener {

                alertDialog.dismiss()

            }

            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            alertDialog.setCancelable(false)

            alertDialog.show()

        }

                viewModel.AccountDeactivation.observe(viewLifecycleOwner) {

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

                            if (it.data!!.status == true) {

                                val preferences = requireContext()
                                    .getSharedPreferences("amar_${context?.packageName}", Context.MODE_PRIVATE)
                                var editor=preferences.edit()
                                editor.clear()
                                editor.apply()


                                requireContext().showalertdilog("Deactivation Successfully")
                            }
                           if (it.data.status==false)
                           {
                               requireContext().showalertdilog(it.data.message!!)
                           }


                            viewModel.AccountDeactivation.postValue(null)

                        }

                        is NetworkResult.Error -> {

                            requireActivity().printToast("Account Deactivation Request Already Exist")

                            viewModel.AccountDeactivation.postValue(null)

                        }

                        else -> {

                        }
                    }
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

        val okbutton: Button =dialogView.findViewById(R.id.Ok)

        okbutton.text = "OK"


        okbutton.setOnClickListener {

            alertDialog.dismiss()

        }
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alertDialog.setCancelable(false)

        alertDialog.show()

    }

    private fun isNetworkAvabile(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    private fun openApppermiison(requireContext: Context) {

        val packageName = requireContext.packageName
        val intent = Intent()

        // Check if the Android version is M or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
        } else {
            // For versions lower than M, open the application settings
            intent.action = Settings.ACTION_APPLICATION_SETTINGS
        }

        try {
            requireContext.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext, "Unable to open app settings", Toast.LENGTH_SHORT).show()
        }

    }

}