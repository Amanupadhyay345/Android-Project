package com.rspl.rojgaarrakshak.dashboard.ticket_tab

import android.Manifest
import android.app.Dialog
import android.content.Context
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rspl.rojgaarrakshak.NetworkLayer.ApiServices
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.WebView.WebViewActivity
import com.rspl.rojgaarrakshak.bottom_sheet.CheckInBottomSheet
import com.rspl.rojgaarrakshak.bottom_sheet.CheckOutBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb

import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentTicketBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketFragment : Fragment() {

    companion object {
        fun newInstance() = TicketFragment()
    }

    private lateinit var viewModel: TicketViewModel
    private lateinit var binding: FragmentTicketBinding
    lateinit var accesstoken:String
    lateinit var newuploaddoc:String

    lateinit var CurrentJobname:String

    private var progressDialog : Dialog? = null
    val Skillname= arrayListOf<String>()
    val subSkillid = arrayListOf<Int>()

    val UserStateselectedid = arrayListOf<Int>()
    val UserSelectedCityidResponse = arrayListOf<Int>()
    val idname= arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTicketBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[TicketViewModel::class.java]

        accesstoken=""
        CurrentJobname=""
        newuploaddoc=""

        try {

            accesstoken=requireActivity().getPrefeb("UserToken")

            if (accesstoken.isNotEmpty())

            {
                val ApplyToken = "Bearer $accesstoken"

                Log.e("ApplyToken",ApplyToken)

                Log.e("Applytoken",ApplyToken)
                viewModel.getuserdata(ApplyToken)
            }
            else
            {
                binding.helloGuest.visibility=View.VISIBLE
                binding.backBtn.visibility=View.VISIBLE
                binding.backBtn.setOnClickListener {
                    val intent=Intent(requireActivity(),LandingPage::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
            }

        }catch (e:NullPointerException)
        {
            binding.helloGuest.visibility=View.VISIBLE
            println(e)

        }

        binding.privacyContainer.setOnClickListener {


            if (isNetworkAvabile())
            {
//                val pdfUrl = "https://mobileapp.rakshaksecuritas.com/Privacy-Policy.pdf"
//                openPdfInExternalViewer(pdfUrl)
                val webIntent = Intent(requireActivity(), WebViewActivity::class.java)
                webIntent.putExtra("url", ApiServices.privacy_policy)
                startActivityForResult(webIntent,1234)
            }
            else
            {
                shownoInternatedilog()
            }
        }
        binding.tosContainer.setOnClickListener {

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
        binding.aboutUs.setOnClickListener {
            val url = ApiServices.about_us_url

            openInChrome(url)
        }

        progressDialog = requireActivity().getProgressDialog()

        viewModel.getuserdata.observe(viewLifecycleOwner) {

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

                    try {

                        binding.helloGuest.visibility=View.INVISIBLE
                        binding.userDetailsContainer.visibility=View.VISIBLE
                        binding.username.text=it.data!!.userData!!.name!!.capitalize()
                        var username=it.data.userData!!.name
                        var dateofbirth=it.data.userData!!.dob

                        var userid=it.data.userData!!.id.toString()

                        var gender=it.data.userData!!.gender
                        var maritialstatus=it.data.userData!!.maritalStatus
                        var mothertongue=it.data.userData!!.motherTounge

                        var Userid=it.data.userData!!.id.toString()

                        requireActivity().putPrefeb("UserUniqueId",Userid)

                        var useremail=it.data.userData!!.email
                        var location =it.data.userData!!.location

                        var UserMobilenumber=it.data.userData!!.phoneNumber

                        try {
                            if (it.data.userPreferedJobData.isNotEmpty()) {

                                subSkillid.clear()
                                UserSelectedCityidResponse.clear()
                                UserStateselectedid.clear()

                                for (i in 0 until it.data.userPreferedJobData.size) {
                                    val subskillid = it.data.userPreferedJobData[i].preSubSkills
                                    val location=it.data.userPreferedJobData[i].location

                                    val UserStateId=it.data.userPreferedJobData[i].stateId

                                    if (subskillid != null) {

                                        val subskillidParts = subskillid.split(",")
                                        for (part in subskillidParts) {
                                            val intValue = part.trim().toIntOrNull()

                                            if (intValue != null) {
                                                subSkillid.add(intValue)
                                            }
                                        }
                                    }

                                    if (location!=null)
                                    {
                                        val locationpart=location.split(",")
                                        for (part in locationpart)
                                        {
                                            val intvalue=part.trim().toIntOrNull()

                                            if (intvalue!=null)
                                            {
                                                UserSelectedCityidResponse.add(intvalue)
                                            }
                                        }
                                    }


                                    if (UserStateId != null) {

                                        val SelectedStateid = UserStateId.split(",")
                                        for (part in SelectedStateid) {
                                            val intValue = part.trim().toIntOrNull()

                                            if (intValue != null) {
                                                UserStateselectedid.add(intValue)
                                            }
                                        }
                                    }
                                }

                                if (subSkillid.isNotEmpty()) {
                                    val gson = Gson()
                                    val json = gson.toJson(subSkillid)

                                    requireActivity().putPrefeb("SubSkillUseridList", json)
                                }

                                if (UserSelectedCityidResponse.isNotEmpty())
                                {
                                    val gson = Gson()
                                    val json = gson.toJson(UserSelectedCityidResponse)

                                    requireActivity().putPrefeb("UserSelectedCityResponseid", json)
                                }

                                if (UserStateselectedid.isNotEmpty())
                                {
                                    val gson = Gson()
                                    val json = gson.toJson(UserStateselectedid)

                                    requireActivity().putPrefeb("UserSelectedStateid", json)
                                }

                            }
                        } catch (e: Exception) {
                            println(e)
                        }

                        if (it.data.empData.isNotEmpty())
                        {

                            requireActivity().putPrefeb("EmployeeStatus","Employee")

                            for (i in 0 until it.data.empData.size)
                            {
                                binding.currentjob.text= it.data.empData!![i].designation

                                binding.dateofjoing.text=it.data.empData!![i].empId

                                var EmployeeId=it.data.empData[i].empId

                                requireActivity().putPrefeb("Employeeid",EmployeeId.toString())


                                binding.workHistoryContainer.visibility=View.VISIBLE
                            }

                        }
                        else
                        {
                            requireActivity().putPrefeb("EmployeeStatus","User")
                            binding.currentjob.text="N/A"
                            binding.datejoingtext.visibility=View.VISIBLE
                            binding.dateofjoing.visibility=View.VISIBLE
                            binding.dateofjoing.text= "TEMP/$userid"
                            binding.workHistoryContainer.visibility=View.INVISIBLE
                        }

                        if (it.data!!.userData!!.skills.isNotEmpty())
                        {
                            Skillname.clear()
                            idname.clear()
                            for (i in 0 until it.data.userData!!.skills.size)
                            {
                                var skillname=it.data.userData!!.skills[i].skillTitle
                                Skillname.add(skillname!!)



                                var skillid=it.data.userData!!.skills[i].id
                                idname.add(skillid!!)
                            }

//                        requireActivity().printToast(Skillname.toString())
//                        requireActivity().printToast(idname.toString())

                            var skillid=idname.toString()
                            var   repleaceid=skillid.replace("[","")
                            var newreplace=repleaceid.replace("]","")


                            var skillname=Skillname.toString()
                            var   namereplace=skillname.replace("[","")
                            var newreplacename=namereplace.replace("]","")


                            requireActivity().putPrefeb("UserSkillid",newreplace)
                            requireActivity().putPrefeb("UserSkillname",newreplacename)

                        }

                        if (it.data.profilePicture.isNotEmpty())
                        {
                            for (i in 0 until it.data.profilePicture.size)
                            {
                                var url= it.data.profilePicture[i].profilepicture

                                requireActivity().putPrefeb("UserImageUrl",url!!)

                                Glide.with(requireActivity()).load(url).into(binding.userimage)

                            }
                        }

                        if (it.data.uploadedDocs.isNotEmpty())
                        {
                            for (i in 0 until it.data.uploadedDocs.size)
                            {
                                val uploaddoc=it.data.uploadedDocs[i].resume

                                Log.e("UploadDoc",uploaddoc.toString())

                                if (uploaddoc!!.startsWith("https://mobileapp.rakshaksecuritas.com/resume/"))
                                {
                                    newuploaddoc=uploaddoc!!.replace("https://mobileapp.rakshaksecuritas.com/resume/","")
                                    requireActivity().putPrefeb("UserUploaddoc",newuploaddoc!!)
                                }
                                else
                                {
                                    newuploaddoc=uploaddoc!!.replace("https://api-rakshak.synchsoft.in/resume/","")
                                    requireActivity().putPrefeb("UserUploaddoc",newuploaddoc!!)
                                }


                            }
                        }

                        requireActivity().putPrefeb("Username",username!!)

                        requireActivity().putPrefeb("Usergender",gender!!)
                        requireActivity().putPrefeb("MaritialStatus",maritialstatus!!)
                        requireActivity().putPrefeb("MotherTongue",mothertongue!!)
                        requireActivity().putPrefeb("DateofBirth",dateofbirth!!)

                        requireActivity().putPrefeb("UserDataid",userid)

                        requireActivity().putPrefeb("UserMobilenumber",UserMobilenumber!!)

                        requireActivity().putPrefeb("Useremail",useremail!!)

                        requireActivity().putPrefeb("Userlocation",location!!)

                        if (it.data!!.empData.isEmpty()) {

                            requireActivity().putPrefeb("EmployeeStatus","User")
                            binding.currentjob.text="N/A"
                            binding.datejoingtext.visibility=View.VISIBLE
                            binding.dateofjoing.visibility=View.VISIBLE
                            binding.dateofjoing.text= "TEMP/$userid"
                            binding.workHistoryContainer.visibility=View.INVISIBLE

                        }

                    }catch (e:Exception)
                    {
                        println(e)
                    }



                }
                is NetworkResult.Error ->{

                    if (it.message.equals("No Internet connection found"))
                    {
                        val builder = AlertDialog.Builder(requireContext())
                        val inflater = layoutInflater
                        val dialogView = inflater.inflate(R.layout.single_button_custom_dilog, null)

                        builder.setView(dialogView)

                        val alertDialog = builder.create()

                        val message: TextView = dialogView.findViewById(R.id.responsemessage)

                        message.text = it.message

                        val okbutton: Button =dialogView.findViewById(R.id.Ok)

                        okbutton.text = "Retry"

                        okbutton.setOnClickListener {

                            alertDialog.dismiss()
                            val ApplyToken = "Bearer $accesstoken"
                            viewModel.getuserdata(ApplyToken)

                        }
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        alertDialog.setCancelable(false)

                        alertDialog.show()

                    }
                    else
                    {
                        requireActivity().showalertdilog(it.message.toString())
                    }

                }
                else -> {

                }
            }
        }

        if (accesstoken.isNotEmpty())
        {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    requireActivity().finish()
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }
        else
        {
                 binding.backBtn.visibility=View.VISIBLE
                 binding.backBtn.setOnClickListener {
                     val intent=Intent(requireActivity(),LandingPage::class.java)
                     requireActivity().startActivity(intent)
                     requireActivity().finish()
                 }
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                   val intent=Intent(requireActivity(),LandingPage::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()

                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        }



        binding.findJobContainer.setOnClickListener {
            (requireActivity() as DashboardActivity).gotoPage(R.id.searchJobFragment,null)
        }


        binding.workHistoryContainer.setOnClickListener {
            (requireActivity() as DashboardActivity).gotoPage(R.id.workHistoryFragment,null)
        }

        binding.salarySlipContainer.setOnClickListener {
            (requireActivity() as DashboardActivity).gotoPage(R.id.salarySlipsFragment,null)
        }

        binding.raiseConcernContainer.setOnClickListener {
            (requireActivity() as DashboardActivity).gotoPage(R.id.raiseConcernFragment,null)
        }

        binding.checkIn.setOnClickListener {
            var checkInSheet = CheckInBottomSheet()
            checkInSheet.show(childFragmentManager,"check_in_sheet")
        }

        binding.checkOut.setOnClickListener {
            var checkOutSheet = CheckOutBottomSheet()
            checkOutSheet.show(childFragmentManager,"check_out_sheet")
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

    private fun openInChrome(url: String) {

        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // Set the package name to ensure Chrome is used, if installed
            intent.setPackage("com.android.chrome")

            startActivity(intent)
        } catch (e: Exception) {
            // Handle exceptions (e.g., Chrome is not installed)
            e.printStackTrace()
        }

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

    override fun onResume() {
        super.onResume()
        try {
            if(binding!=null) {
                val previousImageUrl = requireActivity().getPrefeb("UserImageUrl")
                if (previousImageUrl.isNotEmpty()) {
                    Glide.with(requireActivity()).load(previousImageUrl).into(binding.userimage)
                }

            }
        }catch (e:Exception)
        {
            println(e)
        }
    }


}