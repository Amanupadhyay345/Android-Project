package com.rspl.rojgaarrakshak.dashboard.work_history

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.peerpicks.core.interfaces.ItemClickListener
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.bottom_sheet.WorkHistoryBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.WorkHistoryListener
import com.rspl.rojgaarrakshak.core.printToast

import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentWorkHistoryBinding
import com.rspl.rojgaarrakshak.databinding.ItemWorkHistoryBinding
import com.rspl.rojgaarrakshak.models.WorkHistoryResponse.WorkHistory
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class WorkHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = WorkHistoryFragment()
    }

    private lateinit var viewModel: WorkHistoryViewModel
    private lateinit var binding: FragmentWorkHistoryBinding
    private lateinit var accesstoken:String
    private var progressDialog : Dialog? = null

    lateinit var companyname:String
    lateinit var jobdestination:String
    lateinit var joblocation:String
    lateinit var dateofjoing:String
    lateinit var enddate:String
    lateinit var salary:String
    lateinit var WorkHistroydoj:String
    lateinit var Empname:String
    lateinit var newdateofjoing:String

    lateinit var PresentWorkEnddate:String

    lateinit var WorkhistoryAdapter:ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_work_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkHistoryBinding.bind(view)
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WorkHistoryViewModel::class.java)
        accesstoken=""
        companyname=""
        jobdestination=""
        joblocation=""
        dateofjoing=""
        enddate=""
        salary=""
        Empname=""
        WorkHistroydoj=""
        newdateofjoing=""
        PresentWorkEnddate=""


        try {

            accesstoken=requireActivity().getPrefeb("UserToken")

            if (accesstoken.isNotEmpty())

            {
                val ApplyToken = "Bearer $accesstoken"

                Log.e("ApplyToken",ApplyToken)


                viewModel.GetWorkhistory(ApplyToken)
            }
            else
            {
                requireActivity().printToast("null")
            }
        }catch (e:NullPointerException)
        {
            println(e)
            requireActivity().printToast("null")
        }

        binding.currentWork.setOnClickListener {
            var workHistory = WorkHistoryBottomSheet(companyname,jobdestination,joblocation,newdateofjoing,PresentWorkEnddate,salary,Empname)
            workHistory.show(childFragmentManager,"work_history")

        }

        WorkhistoryAdapter = ItemAdapter(viewModel.WorkHistorydata, object : WorkHistoryListener{
            override fun onItemclick(
                pos: Int,
                comoanyname: String,
                jobdescription: String,
                joblocation: String,
                dateofjoining: String,
                enddate: String,
                Salary: String,
                empname:String
            ) {
                var workHistory = WorkHistoryBottomSheet(comoanyname,jobdescription,joblocation,dateofjoining,enddate,Salary,empname)
                workHistory.show(childFragmentManager,"work_history")

            }

        })

        binding.workHistoryList.apply {

            adapter=WorkhistoryAdapter

            layoutManager = LinearLayoutManager(requireContext())
        }


        progressDialog = requireActivity().getProgressDialog()

            viewModel.workhistoryresponse.observe(viewLifecycleOwner) {

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

                            if (it.data!!.presentWork.isEmpty())
                            {
                                binding.currentWork.visibility=View.INVISIBLE
                                binding.currontworkalert.visibility=View.VISIBLE
                                binding.currontworkalert.text="No Current Work Found"
                            }
                            else
                            {
                                binding.currentWork.visibility=View.VISIBLE
                                binding.currontworkalert.visibility=View.GONE

                                if (it.data.presentWork.isNotEmpty())
                                {
                                    for (i in 0 until it.data.presentWork.size)

                                    {
                                        binding.CompanyName.text= it.data.presentWork[i].empDepartment
                                        binding.location.text= it.data.presentWork[i].empPresentAddress
                                        binding.securityservice.text=it.data.presentWork[i].designation

                                        companyname=it.data.presentWork[i].empDepartment!!
                                        jobdestination=it.data.presentWork[i].designation!!
                                        joblocation=it.data.presentWork[i].empPresentAddress!!
                                        dateofjoing=it.data.presentWork[i].doj!!
                                        enddate=it.data.presentWork[i].endDate!!

                                        if (it.data.presentWork[i].annualSalary.isNullOrEmpty())
                                        {
                                            salary="N/A"
                                        }
                                        else
                                        {
                                            salary=it.data.presentWork[i].annualSalary!!
                                        }

                                        Empname=it.data.presentWork[i].empName!!

                                        if (enddate == "Present")
                                        {
                                            PresentWorkEnddate=enddate
                                        }
                                        else
                                        {
                                            getenddatemonth(enddate)
                                        }

                                        binding.enddate.text=PresentWorkEnddate


                                        getDayMonthYear(dateofjoing)
                                        binding.dateofjoing.text=newdateofjoing

                                    }
                                }


                            }
                            if (it.data.workHistory.isEmpty())

                            {
                                binding.workhistoryalert.visibility=View.VISIBLE
                                binding.workhistoryalert.text="No Work History Found"
                                binding.workHistoryList.visibility=View.INVISIBLE
                            }
                            else
                            {
                                binding.workhistoryalert.visibility=View.GONE
                                binding.workHistoryList.visibility=View.VISIBLE
                            }
                            viewModel.WorkHistorydata.clear()
                            it.data!!.workHistory?.let { jobList ->
                                jobList.forEach { job ->
                                    viewModel.WorkHistorydata.add(job)


                                    WorkhistoryAdapter.notifyDataSetChanged()

                                }

                            }

                        }catch (e:Exception)
                        {
                            e.printStackTrace()
                        }



                    }
                    is NetworkResult.Error ->{

                        try {
                            requireActivity().showalertdilog(it.message!!)

                        }catch (e:Exception)
                        {
                            println(e)
                        }


                    }
                    else -> {

                    }
                }
            }
        binding.backBtn.setOnClickListener {
            (requireActivity() as DashboardActivity).onBackBtnPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getenddatemonth(enddate: String) {

        try {


            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Parse the date string using the specified formatter
            val currentDate = LocalDate.parse(enddate, formatter)

            val month = currentDate.month
            val day = currentDate.dayOfMonth
            val year = currentDate.year

            val monthname: String = month.toString()
            var newmonthname: String = monthname.substring(0, 3)

            val PresentWorkenddate = "$newmonthname  $year"
            PresentWorkEnddate=PresentWorkenddate

            println("Day: $day")
            println("Month: $month")
            println("Year: $year")
            println("PresentWorkenddate: $PresentWorkenddate")

        } catch (e: Exception)
        {
            e.printStackTrace()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayMonthYear(date: String?) {

        try {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Parse the date string using the specified formatter
            val currentDate = LocalDate.parse(date, formatter)

            val month = currentDate.month
            val day = currentDate.dayOfMonth
            val year = currentDate.year

            val monthname: String = month.toString()
            var newmonthname: String = monthname.substring(0, 3)

            val PresentWorkenddate = "$newmonthname  $year -"

            newdateofjoing=PresentWorkenddate

            println("Day: $day")
            println("Month: $month")
            println("Year: $year")
            println("PresentWorkenddate: $PresentWorkenddate")

        }catch (e:Exception)
        {
            e.printStackTrace()
        }

    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemWorkHistoryBinding.bind(itemView)

    }

    class ItemAdapter(val list: List<WorkHistory>, val itemClick: WorkHistoryListener) :
        RecyclerView.Adapter<ItemViewHolder>() {

        lateinit var Workhistorydateofjoing:String
        lateinit var WorkHistoryEndDate:String
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_work_history, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            var WorkHistorydata= list[position]

            holder.binding.CompanyName.text=WorkHistorydata.companyName
            holder.binding.location.text=WorkHistorydata.jobLocation

            holder.binding.securityservice.text=WorkHistorydata.designation

            val dateofjoinng=WorkHistorydata.doj

            var EndDate=WorkHistorydata.endDate

            WorkHistoryEndDate=""

            if (EndDate.equals("Present"))

            {
                WorkHistoryEndDate=EndDate!!
            }
            else
            {
                getmonthenddate(EndDate)
            }


            if (dateofjoinng!!.isNotEmpty())

            {
                getmonth(dateofjoinng)
            }

            holder.binding.Dateofjoining.text=Workhistorydateofjoing
            holder.binding.enddate.text=WorkHistoryEndDate

            holder.itemView.setOnClickListener {
                itemClick.onItemclick(position,WorkHistorydata.companyName!!,WorkHistorydata.designation!!
                    ,WorkHistorydata.location!!,Workhistorydateofjoing,WorkHistoryEndDate,WorkHistorydata.salary!!,WorkHistorydata.empName!!)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getmonthenddate(endDate: String?) {

            try {

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val currentDate = LocalDate.parse(endDate,formatter)

                val month = currentDate.month

                val day = currentDate.dayOfMonth

                val year = currentDate.year

                val monthname:String=month.toString()

                var newmonthname:String=monthname.substring(0,3)

                WorkHistoryEndDate  = "$newmonthname $year"

                println("Day: $day")
                println("Month: $month")
                println("Year: $year")
            }catch (e:Exception)
            {
                println(e)
            }

        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getmonth(dateofjoinng: String) {

            try {

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val currentDate = LocalDate.parse(dateofjoinng,formatter)

                val month = currentDate.month

                val day = currentDate.dayOfMonth

                val year = currentDate.year

                val monthname:String=month.toString()

                var newmonthname:String=monthname.substring(0,3)

                Workhistorydateofjoing=""
                Workhistorydateofjoing   = "$newmonthname $year"



                println("Day: $day")
                println("Month: $month")
                println("Year: $year")

            }catch (e:Exception)
            {
                println(e)
            }




        }

    }

}