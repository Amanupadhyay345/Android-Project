package com.rspl.rojgaarrakshak.dashboard.slips

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_VISIBLE_TO_INSTANT_APPS
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.peerpicks.core.interfaces.ItemClickListener
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.SalarySlipClickListner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.showCancelDialog
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.dashboard.filter.FilterFragment
import com.rspl.rojgaarrakshak.dashboard.search_job.Adapter.SalarySlipAdapter
import com.rspl.rojgaarrakshak.dashboard.search_job.SearchJobFragment
import com.rspl.rojgaarrakshak.dashboard.work_history.WorkHistoryFragment
import com.rspl.rojgaarrakshak.databinding.FragmentSalarySlipsBinding
import com.rspl.rojgaarrakshak.databinding.ItemSalarySlipBinding
import com.rspl.rojgaarrakshak.models.GetSalarySlipResponse.SalarySlipData
import com.rspl.rojgaarrakshak.models.search_response.JobsData
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class SalarySlipsFragment : Fragment() {

    companion object {
        fun newInstance() = SalarySlipsFragment()
    }

    private lateinit var viewModel: SalarySlipsViewModel
    private lateinit var binding: FragmentSalarySlipsBinding
    private val READ_WRITE_PERMISSION_REQUEST = 1

    lateinit var SalarySlip:String
    private var downloadID: Long = 0
    private var permission=0
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    lateinit var name: String
    val joblist = arrayListOf<String>()
    val selected_job_id = arrayListOf<Int>()
    lateinit var accesstoken: String
    val yearlist = arrayListOf<String>()
    var yearname: Int = 0
    var Permisssiongranted=false
    lateinit var Salaryslipadapter: SalarySlipAdapter

    var selectedyearname: Int = 0
    var selectempid: Int = 0
    lateinit var Yearadapter: ArrayAdapter<String>
    private var progressDialog : Dialog? = null

    lateinit var JobNameAdapter: ArrayAdapter<String>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                  Permisssiongranted=true
                }
                else
                {

                    val builder = AlertDialog.Builder(requireContext())
                    val inflater = layoutInflater
                    val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                    builder.setView(dialogView)

                    val alertDialog = builder.create()

                    val message: TextView = dialogView.findViewById(R.id.messagetext)

                    message.text = "This Feature not Available Without Storage Permission please Enable Storage Permission"

                    val okbutton: Button =dialogView.findViewById(R.id.Login)

                    okbutton.text = " Settings "

                    okbutton.setTextColor(Color.WHITE)



                    okbutton.setOnClickListener {
                        alertDialog.dismiss()

                        val packageName = requireContext().packageName
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
                            requireContext().startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Unable to open app settings", Toast.LENGTH_SHORT).show()
                        }



                    }

                    val cancelbutton: Button =dialogView.findViewById(R.id.cancel)

                    cancelbutton.text=" Cancel "

                    cancelbutton.setTextColor(Color.WHITE)



                    cancelbutton.setOnClickListener {
                        alertDialog.dismiss()

                    }

                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



                    alertDialog.show()

                }
            }
        return inflater.inflate(R.layout.fragment_salary_slips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSalarySlipsBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SalarySlipsViewModel::class.java)

        name = ""
        accesstoken = ""
        SalarySlip=""




        try {

            accesstoken = requireActivity().getPrefeb("UserToken")

            if (accesstoken.isNotEmpty()) {
                val ApplyToken = "Bearer $accesstoken"

                Log.e("ApplyToken", ApplyToken)
                viewModel.getAlljob(ApplyToken)
            } else {
              // somethig performs

            }
        } catch (e: NullPointerException) {

            println(e)

        }

        progressDialog = requireActivity().getProgressDialog()


        viewModel.alljobresponce.observe(viewLifecycleOwner) {

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

                    if(it.data!=null) {
                        val data = it.data
                        Log.e("responsedata", data.toString())
                        viewModel.joblist.addAll(data.data)

                        Log.e("viewmodeljoblist", viewModel.joblist.toString())
                        Log.e("Statename", viewModel.joblist.toString())

                        joblist.clear()
                        joblist.add("Select-Jobs")
                        for (i in 0 until viewModel.joblist.size) {
                            name = viewModel.joblist.get(i).jobTitle!!
                            joblist.add(name)
                        }

                        Log.e("joblist", joblist.toString())
                        JobNameAdapter.notifyDataSetChanged()

                        binding.noJobFound.visibility=View.INVISIBLE
                        binding.jobMonthContainer.visibility = View.VISIBLE
                    }

                }

                is NetworkResult.Error -> {
                    if(binding!=null) {
                        binding.noJobFound.visibility = View.VISIBLE
                        binding.noJobFound.text = it.message.toString()

                        binding.jobMonthContainer.visibility = View.INVISIBLE
                    }else{
                        requireActivity().showalertdilog(it.message.toString())
                    }
//

                }

                else -> {
                    binding.noJobFound.visibility=View.VISIBLE
                    binding.jobMonthContainer.visibility = View.INVISIBLE
                }
            }
        }


        if (binding.jobnamespiner != null) {
            JobNameAdapter =
                ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, joblist)
            binding.jobnamespiner.adapter = JobNameAdapter
            JobNameAdapter.notifyDataSetChanged()

            binding.jobnamespiner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    if (position == 0)
                        return

                    val itemid = viewModel.joblist[position - 1].id

                    val ApplyToken = "Bearer $accesstoken"

                    viewModel.getjobyear(ApplyToken, itemid.toString())


                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }

        viewModel.GetAllJobYear.observe(viewLifecycleOwner) {
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
                    val data = it.data!!

                    Log.e("datatocitylist", data.toString())
                    viewModel.yearlist.clear()
                    Log.e("CityResponce", viewModel.yearlist.toString())
                    viewModel.yearlist.addAll(data.data)
//                    viewModel.citylist.addAll(data.citiesData!!)

                    Log.e("CityList", viewModel.yearlist.toString())


                    yearlist.clear()
                    yearlist.add("Select-Year")
                    for (i in 0 until viewModel.yearlist.size) {
                        yearname = viewModel.yearlist.get(i).year!!

                        yearlist.add(yearname.toString())
                    }

                    Yearadapter.notifyDataSetChanged()

                    Log.e("citylistforcity", yearlist.toString())

                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message.toString())
                }

                else -> {

                }
            }
        }
        Yearadapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, yearlist)
        binding.yearspiner.adapter = Yearadapter
        Yearadapter.notifyDataSetChanged()

        binding.yearspiner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                if (position == 0)
                    return

                selectedyearname = viewModel.yearlist[position - 1].year!!
                selectempid = viewModel.yearlist[position - 1].empId!!
//
//                requireActivity().printToast(selectedyearname.toString())
//                requireActivity().printToast(selectempid.toString())

                val ApplyToken = "Bearer $accesstoken"

                viewModel.getSalarySlip(
                    ApplyToken,
                    selectempid.toString(),
                    selectedyearname.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        Salaryslipadapter =
            SalarySlipAdapter(requireContext(),viewModel.salaryslpresult, object : SalarySlipClickListner {
                override fun onItemClicked(pos: Int, slip: String) {

                    SalarySlip=slip

                   if (Build.VERSION.SDK_INT >Build.VERSION_CODES.S)
                   {

                       Permisssiongranted=true
                   }
                    else
                   {
                       requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                   }



                    if (Permisssiongranted)
                    {
                        try {

                            var downloadmanger=requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                            var pdflink= Uri.parse(SalarySlip)

                            val request= DownloadManager.Request(pdflink)

                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                                .setMimeType("application/pdf")
                                .setAllowedOverRoaming(false)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setTitle("RojgarRakshak")
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator+"Ropjgarrakshak.pdf")
                            downloadID=  downloadmanger.enqueue(request)

                            progressDialog?.show()

                            val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                            ContextCompat.registerReceiver(
                                requireContext(),
                                downloadReceiver,
                                filter,
                                ContextCompat.RECEIVER_VISIBLE_TO_INSTANT_APPS
                            )

//                            Toast.makeText(requireContext(),"Download Complete",Toast.LENGTH_LONG).show()

                        }catch (e:Exception)
                        {
                            println(e)
                            Toast.makeText(requireContext(),"Download Faild",Toast.LENGTH_LONG).show()
                        }
                    }








                }


            })

        binding.salarySlipList.apply {

            adapter = Salaryslipadapter

            layoutManager = LinearLayoutManager(requireContext())

        }



        viewModel.GetSalaryslip.observe(viewLifecycleOwner) {
            progressDialog?.dismiss()

            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.show()


                }

                is NetworkResult.Success -> {

                    it.data!!.salarySlipData?.let { jobList ->
                        jobList.forEach { job ->
                            viewModel.salaryslpresult.add(job)

                            Salaryslipadapter.notifyDataSetChanged()

                        }

                    }
                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message.toString())

                }

                else -> {

                }
            }
        }

        binding.backBtn.setOnClickListener {
            (requireActivity() as DashboardActivity).onBackBtnPressed()
        }


    }

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                progressDialog?.dismiss()
                // Download completed, open the PDF file
                val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Ropjgarrakshak.pdf")
                val uri = FileProvider.getUriForFile(requireContext(), "${context!!.packageName}.fileprovider", pdfFile)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)




                try {
                    context!!.startActivity(intent)
                }catch (e:Exception)
                {
                    println(e)
                }
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        requireContext().unregisterReceiver(downloadReceiver)
//    }

}