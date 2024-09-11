package com.rspl.rojgaarrakshak.dashboard.search_job

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.WebView.WebViewActivity
import com.rspl.rojgaarrakshak.bottom_sheet.PaymentDoneBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.PaymentStausBtnLisner
import com.rspl.rojgaarrakshak.core.interfaces.SearchItemClickListner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.dashboard.filter.BottomsheetDilog
import com.rspl.rojgaarrakshak.dashboard.filter.BtnClickListenr
import com.rspl.rojgaarrakshak.dashboard.search_job.Adapter.FilterAdapter
import com.rspl.rojgaarrakshak.dashboard.search_job.FilterModel.CityModel
import com.rspl.rojgaarrakshak.dashboard.search_job.FilterModel.MainModels
import com.rspl.rojgaarrakshak.databinding.FragmentSearchJobBinding
import com.rspl.rojgaarrakshak.databinding.ItemSearchJobBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import com.rspl.rojgaarrakshak.models.search_response.JobsData
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject


@AndroidEntryPoint
class SearchJobFragment : Fragment(), FilterAdapter.BtnClickListener {

    companion object {
        fun newInstance() = SearchJobFragment()
    }

    private lateinit var viewModel: SearchJobViewModel
    private lateinit var binding: FragmentSearchJobBinding
    private var citymodellist: ArrayList<CityModel>? = null
    private lateinit var filterAdapter: FilterAdapter
    private lateinit var mainmodel: ArrayList<MainModels>
    lateinit var cityid: String
    val applyjobid = arrayListOf<Int>()
    lateinit var Selectcityid: String
    var NewListName = arrayListOf<Int>()
    lateinit var Cityidlist: String
    lateinit var itemposition: String
    lateinit var accesstoken: String
    lateinit var newCityidlist: String
    lateinit var newRemovecityid: String
    var itemid: Int = 0
    lateinit var Useraccesstoken: String
    lateinit var paydonebottomseet: PaymentDoneBottomSheet
    private var progressDialog: Dialog? = null
    private lateinit var bottomSheet: BottomsheetDilog

    val appliedjobid = arrayListOf<Int>()
    var appliedjobresponsesuccess = false

    val findjonb = arrayListOf<Int>()
    var findjobid: Int = 0
    val Newarraylist = arrayListOf<Int>()

    var itempositionint: Int = 0

    val idlist = arrayListOf<String>()
    lateinit var searchAdapter: SearchItemAdapter

    var paymentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    var accesstoken = requireActivity().getPrefeb("UserToken")
                    var trangectionid = requireActivity().getPrefeb("marchenttrangetionid")

                    if (accesstoken.isNotEmpty() && trangectionid.isNotEmpty()) {
                        val ApplyToken = "Bearer $accesstoken"
                        viewModel.PaymentStatus(ApplyToken, trangectionid)
                    }

                } catch (e: Exception) {
                    println(e)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchJobBinding.bind(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchJobViewModel::class.java]

        mainmodel = arrayListOf(MainModels())
        cityid = ""
        Selectcityid = ""
        Cityidlist = ""
        newCityidlist = ""
        newRemovecityid = ""
        itemposition = ""
        accesstoken = ""
        Useraccesstoken = ""





        try {
            Useraccesstoken = requireActivity().getPrefeb("UserToken")

            val ApplyToken = "Bearer $Useraccesstoken"

            if (Useraccesstoken.isNotEmpty()) {
                viewModel.getapppliedjob(ApplyToken)
            }


        } catch (e: Exception) {
            println(e)
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


//        (requireActivity() as DashboardActivity).requestCurrentLocation(object : LocationListener {
//            override fun onAddressFetched(address: String) {
//                if (address.isNotEmpty() && !address.contains("Address couldn't be determined."))
//                    viewModel.selectedLocation = address
//            }
//
//            override fun onLatLngFetched(latLng: LatLng) {
//
//            }
//        })

        binding.backBtn.setOnClickListener {
//            (requireActivity() as DashboardActivity).onBackBtnPressed()

            val intent = Intent(requireContext(), DashboardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        citymodellist = arrayListOf()

        Log.e("citymodellist", citymodellist.toString())

        Loaddata()

        binding.moreBtn.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.moreBtn)

            popupMenu.menuInflater.inflate(R.menu.job_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->

                (requireActivity() as DashboardActivity).gotoPage(R.id.appliedJobsFragment, null)
                true
            }

            popupMenu.show()
        }

        try {
            val jsonArray: String = requireActivity().getPrefeb("filter_data")

            if (jsonArray.isNotEmpty()) {
                var cityArray = JSONArray(jsonArray)

                idlist.clear()
                for (i in 0 until cityArray.length()) {
                    var jsonObj = JSONObject(cityArray[i].toString())

                    cityid = jsonObj.getString("city_id")

                    idlist.add(cityid)

                }
                Cityidlist = idlist.toString()

                Log.e("Cityitemidname", Cityidlist)

                newCityidlist = Cityidlist.replace("[", "")
                newRemovecityid = newCityidlist.replace("]", "")

                Log.e("newRemovecityid", newRemovecityid)

            }
        } catch (e: NullPointerException) {
            println(e)
        }

        viewModel.getappliedjob.observe(viewLifecycleOwner) {

//            progressDialog?.let {
//                it.dismiss()
//            }
            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.let {
                        it.show()
                    }

                }

                is NetworkResult.Success -> {
                    it.data!!.appliedJobs?.let { jobList ->
                        jobList.forEach { job ->
                            appliedjobid.add(job.id!!)

                        }
                    }

                    viewModel.findJobs("", newRemovecityid)

                    appliedjobresponsesuccess = true

                    Log.e("appliedjobitemid", appliedjobid.toString())

                    viewModel.getappliedjob.postValue(null)
                }

                is NetworkResult.Error -> {

                    progressDialog?.let {
                        it.dismiss()
                    }

                    if (it.message.equals("No Internet connection found")) {
                        val builder = AlertDialog.Builder(requireContext())
                        val inflater = layoutInflater
                        val dialogView = inflater.inflate(R.layout.single_button_custom_dilog, null)

                        builder.setView(dialogView)

                        val alertDialog = builder.create()

                        val message: TextView = dialogView.findViewById(R.id.responsemessage)

                        message.text = it.message

                        val okbutton: Button = dialogView.findViewById(R.id.Ok)

                        okbutton.text = "Retry"


                        okbutton.setOnClickListener {

                            alertDialog.dismiss()

                            try {
                                var accesstoken = requireActivity().getPrefeb("UserToken")

                                val ApplyToken = "Bearer $accesstoken"

                                viewModel.getapppliedjob(ApplyToken)

                            } catch (e: Exception) {
                                println(e)
                            }

                        }
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        alertDialog.setCancelable(false)

                        alertDialog.show()
                    } else {
                        requireActivity().showalertdilog(it.message.toString())
                    }

                    viewModel.getappliedjob.postValue(null)

                }
            }
        }

        if (Useraccesstoken.isEmpty()) {
            viewModel.findJobs("", newRemovecityid)
        }



        searchAdapter =
            SearchItemAdapter(
                requireContext(),
                viewModel.searchResultArray,
                appliedjobid,
                object : SearchItemClickListner {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onJobItemClicked(
                        pos: Int,
                        id: Int,
                        title: String,
                        companyName: String,
                        salary: String,
                        jobDescription: String,
                        experience: String,
                        designation: String,
                        location: String,
                        expDate: String,
                        skillTitle: String
                    ) {


                        bottomSheet = BottomsheetDilog(title,
                            companyName,
                            salary,
                            jobDescription,
                            experience,
                            designation,
                            location,
                            expDate,
                            skillTitle,
                            appliedjobid.contains(id),
                            object : BtnClickListenr {
                                override fun onApplybtnclick() {

                                    val Userid = id.toString()
                                    itemid = id
                                    applyjobid.add(itemid)

                                    Log.e("Appliedjoblistitemid", appliedjobid.toString())
                                    Log.e("NewJobitemid", findjonb.toString())
                                    bottomSheet.dismiss()

                                    try {
                                        val Usertoken = requireActivity().getPrefeb("UserToken")

                                        if (Usertoken.isNotEmpty()) {
                                            val ApplyToken = "Bearer $Usertoken"

                                            Log.e("Userid", Userid)

                                            viewModel.ApplyJob(ApplyToken, Userid)
                                        } else {

                                            var intent =
                                                Intent(requireActivity(), LandingPage::class.java)

                                            intent.putExtra(
                                                "NavigateToSignInPage",
                                                "NavigateToSignInPage"
                                            )

                                            startActivity(intent)

                                        }

                                    } catch (e: NullPointerException) {
                                        println(e)
                                    }
                                }
                            })
                        bottomSheet.show(requireActivity().supportFragmentManager, "BottomSheet")

                        requireActivity().putPrefeb("Applyjobid", id.toString())

                    }
                })

        progressDialog = requireActivity().getProgressDialog()

        viewModel.paymentStatusResponse.observe(viewLifecycleOwner) {

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

                    var message = it.data!!.paymentStatus!!.message

                    if (message!!.isNotEmpty()) {
                        paydonebottomseet =
                            PaymentDoneBottomSheet(message!!, object : PaymentStausBtnLisner {
                                override fun onApplybtnclick() {

                                    paydonebottomseet.dismiss()
                                }
                            })

                        paydonebottomseet.isCancelable = false
                        paydonebottomseet.show(childFragmentManager, "paydonebottomseet")

                    }

                }

                is NetworkResult.Error -> {

                }
            }
        }

        viewModel.applyjobresponse.observe(viewLifecycleOwner) {

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

                    requireActivity().showalertdilog(it.data!!.message!!)

                    appliedjobid.add(itemid)
                    searchAdapter.notifyDataSetChanged()
                    Log.e("applyjobresponse", "list size: ${viewModel.searchResultArray.size}")
                    (requireActivity() as DashboardActivity).isUserLoggedIn = true

                    viewModel.applyjobresponse.postValue(null)
                    showRecentSearchesPanel()
                }

                is NetworkResult.Error -> {

                    if (it.message!!.equals("Please make Payment First.")) {
                        val builder = AlertDialog.Builder(requireContext())
                        val inflater = layoutInflater
                        val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                        builder.setView(dialogView)

                        val alertDialog = builder.create()

                        val message: TextView = dialogView.findViewById(R.id.messagetext)

                        message.text = it.message

                        val okbutton: Button = dialogView.findViewById(R.id.Login)

                        okbutton.text = "OK"

                        okbutton.setOnClickListener {
                            alertDialog.dismiss()


                            try {

                                accesstoken = requireActivity().getPrefeb("UserToken")

                                if (accesstoken.isNotEmpty()) {
                                    val ApplyToken = "Bearer $accesstoken"
                                    viewModel.InisialPayment(ApplyToken)
                                } else {
                                    Log.e("null", "null")

                                }
                            } catch (e: Exception) {
                                println(e)
                            }


                        }

                        val cancelbutton: Button = dialogView.findViewById(R.id.cancel)

                        cancelbutton.setOnClickListener {
                            alertDialog.dismiss()

                        }

                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        alertDialog.setCancelable(false)

                        alertDialog.show()
                    } else {
                        requireActivity().showalertdilog(it.message!!)
                    }

                    viewModel.applyjobresponse.postValue(null)
                }

                else -> {

                }
            }
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

                    try {

                        val url = it.data!!.paymentData!!.data!!.instrumentResponse!!.redirectInfo!!.url

                        var marchenttrangetionid: String =
                            it.data.paymentData!!.data!!.merchantTransactionId!!

                        requireActivity().putPrefeb("marchenttrangetionid", marchenttrangetionid)

                        val intent = Intent(requireContext(), WebViewActivity::class.java)
                        intent.putExtra("url", url)
                        paymentLauncher.launch(intent)

                    }catch (e:Exception)
                    {
                        println(e)
                    }



                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)
                }

                else -> {

                }
            }
        }


        binding.filterBtn.setOnClickListener {
            (requireActivity() as DashboardActivity).gotoPage(R.id.filterFragment, null)
        }

        binding.searchResult.apply {

            adapter = searchAdapter
        }


        binding.searchJob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                filterdata(s.toString())

                binding.searchFoundTitle.text = searchAdapter.itemCount.toString() + " Result found"

            }

            override fun afterTextChanged(s: Editable?) {


            }
        })


        progressDialog = requireActivity().getProgressDialog()

        viewModel.jobSearchResponse.observe(viewLifecycleOwner) {

            when (it) {

                is NetworkResult.Loading -> {
                    progressDialog?.let {
                        it.show()
                    }

                }

                is NetworkResult.Success -> {
                    progressDialog?.let {
                        it.dismiss()
                    }

                    var datalist = it.data


                    binding.searchFoundTitle.text =
                        datalist!!.resultsCount.toString() + " Result found"



                    loadAnimation(binding.searchResult)



                    viewModel.jobidlist.clear()
                    findjonb.clear()
                    viewModel.searchResultArray.clear()
                    searchAdapter.notifyDataSetChanged()
                    Log.e("jobSearchResponse", "list size: ${viewModel.searchResultArray.size}")
                    viewModel.jobidlist.addAll(datalist!!.jobsData)
                    viewModel.searchResultArray.addAll(datalist!!.jobsData)



                    for (i in 0 until viewModel.jobidlist.size) {
                        findjobid = viewModel.jobidlist[i].id!!
                        findjonb.add(findjobid)
                    }



                    if (it.data!!.jobsData.isNotEmpty()) {
                        binding.placeHolderText.visibility = View.GONE
                    } else {
                        binding.placeHolderText.visibility = View.VISIBLE
                    }

                    Log.e("viewModelserchresult", viewModel.searchResultArray.toString())

//                    requireActivity().printToast(viewModel.searchResultArray.toString())


                    searchAdapter.notifyDataSetChanged()
                    Log.e("jobSearchResponse", "list size: ${viewModel.searchResultArray.size}")
                    viewModel.jobSearchResponse.postValue(null)
                }

                is NetworkResult.Error -> {
                    progressDialog?.let {
                        it.dismiss()
                    }

                    binding.searchFoundTitle.text = "No Result found"

                    if (it.message.equals("No Internet connection found")) {
                        val builder = AlertDialog.Builder(requireContext())
                        val inflater = layoutInflater
                        val dialogView = inflater.inflate(R.layout.single_button_custom_dilog, null)

                        builder.setView(dialogView)

                        val alertDialog = builder.create()

                        val message: TextView = dialogView.findViewById(R.id.responsemessage)

                        message.text = it.message

                        val okbutton: Button = dialogView.findViewById(R.id.Ok)

                        okbutton.text = "Retry"

                        okbutton.setOnClickListener {

                            alertDialog.dismiss()

                            viewModel.findJobs("", newRemovecityid)

                        }
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        alertDialog.setCancelable(false)

                        alertDialog.show()
                    } else {
                        requireActivity().showalertdilog(it.message.toString())
                    }


                    viewModel.jobidlist.clear()
                    findjonb.clear()
                    searchAdapter.notifyDataSetChanged()
                    Log.e(
                        "jobSearchResponse failed",
                        "list size: ${viewModel.searchResultArray.size}"
                    )
                    viewModel.jobSearchResponse.postValue(null)
                }

                else -> {
                }
            }
        }

        Log.e("findjobid", findjonb.toString())

    }

    fun loadAnimation(recyclearview: RecyclerView) {
        var context: Context = recyclearview.context

        var animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
        recyclearview.layoutAnimation = animation
        recyclearview.adapter!!.notifyDataSetChanged()
        recyclearview.scheduleLayoutAnimation()
    }

    fun filterdata(text: String) {

        var filterlist = ArrayList<JobsData>()
        if (text.isNotEmpty()) {
            for (item in viewModel.searchResultArray) {
                if (item.jobTitle!!.trim().lowercase()
                        .contains(text.lowercase()) || item.location!!.trim().lowercase()
                        .contains(text.lowercase())
                ) {
                    filterlist.add(item)
                }
            }
        } else {
            filterlist = viewModel.searchResultArray
        }
        searchAdapter.filterlist(filterlist)

        if (filterlist.isEmpty()) {
//            Toast.makeText(requireContext(), "No Record Found filter", Toast.LENGTH_SHORT).show()

            Log.e("No record found", "No record found")
        }

    }

    fun Loaddata() {
        try {
            val jsonArray: String = requireActivity().getPrefeb("filter_data")

            if (jsonArray.isNotEmpty()) {
                var cityArray = JSONArray(jsonArray)

                for (i in 0 until cityArray.length()) {
                    var jsonObj = JSONObject(cityArray[i].toString())
                    var model =
                        CityModel(jsonObj.getString("city_name"), jsonObj.getInt("city_id"))
                    citymodellist!!.add(model)


                }
            }
            filterAdapter = FilterAdapter(citymodellist!!, this)

//             mainmodel.add(MainModels(citymodellist!!))
            filterAdapter.notifyDataSetChanged()


            Log.e("MainModeljson", mainmodel.toString())
            binding.filterList.adapter = filterAdapter

            Log.e("citymodellistjson", citymodellist.toString())
        } catch (e: NullPointerException) {
            println(e)
        }
        if (citymodellist == null) {
            citymodellist = ArrayList()
        }
        showRecentSearchesPanel()
    }

    class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSearchJobBinding.bind(itemView)

    }

    class SearchItemAdapter(
        var context: Context,
        var locList: ArrayList<JobsData>,
        val appliedjobid: ArrayList<Int>,
        val itemClick: SearchItemClickListner
    ) :
        RecyclerView.Adapter<SearchItemViewHolder>() {

        private var names = ArrayList<JobsData>()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
            return SearchItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_search_job, parent, false)

            )
        }

        fun filterlist(filterlist: ArrayList<JobsData>) {
            locList = filterlist
            notifyDataSetChanged()
            Log.e("filterlist", "called : ${locList.size}")
        }

        override fun getItemCount(): Int {
            return locList.size
        }

        override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {

            val data = locList[position]

            holder.binding.jobTitle.text = "${data.jobTitle} \n${data.location}"

            for (i in 0 until appliedjobid.size) {
                val itemid = appliedjobid.get(i)

                if (data.id == itemid) {
                    holder.binding.applyBtn.setBackgroundResource(R.drawable.applied_button_color)
                    holder.binding.applyBtn.setText("Applied")
                }
            }
            holder.binding.applyBtn.setOnClickListener {
                itemClick.onJobItemClicked(
                    position,
                    data.id!!,
                    data.jobTitle.toString(),
                    data.companyName!!,
                    data.salary!!,
                    data.jobDescription!!,
                    data.experience!!,
                    data.designation!!,
                    data.location!!,
                    data.expDate!!,
                    data.skillTitle!!
                )
            }
        }
    }


    override fun onBtnClick(position: Int) {

        val preferences =
            context?.getSharedPreferences("amar_${context?.packageName}", Context.MODE_PRIVATE)
        val editor = preferences?.edit()

        val jsonArray: String = requireActivity().getPrefeb("filter_data")
        var updateResult = ""
        if (jsonArray.isNotEmpty()) {
            var cityArray = JSONArray(jsonArray)
            cityArray.remove(position)
            updateResult = cityArray.toString()
        }
        requireActivity().putPrefeb("filter_data", updateResult)

        citymodellist?.removeAt(position)
        filterAdapter.notifyDataSetChanged()



        idlist.removeAt(position)

        val listcityid = idlist.toString()
        val rcityid = listcityid.replace("[", "")
        val recityid = rcityid.replace("]", "")

        viewModel.findJobs("", recityid)
        showRecentSearchesPanel()

    }

    fun showRecentSearchesPanel() {
        try {
            if (binding != null) {
                Log.e(
                    "showRecentSearchesPanel",
                    "citymodellist!!.isNotEmpty() => ${citymodellist!!.isNotEmpty()}"
                );
                binding.recentSerches.isVisible = citymodellist!!.isNotEmpty()
                binding.filterList.isVisible = citymodellist!!.isNotEmpty()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
