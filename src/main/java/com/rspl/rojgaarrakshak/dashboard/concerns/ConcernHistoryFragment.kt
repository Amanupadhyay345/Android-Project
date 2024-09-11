package com.rspl.rojgaarrakshak.dashboard.concerns

import android.app.Dialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import app.peerpicks.core.interfaces.ItemClickListener
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentConcernHistoryBinding
import com.rspl.rojgaarrakshak.databinding.ItemConcernHistoryBinding
import com.rspl.rojgaarrakshak.models.ConcernHistoryResponse.ConcernData
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ConcernHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = ConcernHistoryFragment()
    }

    private lateinit var viewModel: ConcernHistoryViewModel
    private lateinit var binding: FragmentConcernHistoryBinding
    private lateinit var ConcernHistoryadapter: SearchItemAdapter
    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_concern_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConcernHistoryBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConcernHistoryViewModel::class.java)

        progressDialog = requireActivity().getProgressDialog()

        ConcernHistoryadapter =
            SearchItemAdapter(viewModel.ConcernHistoryData, object : ItemClickListener {
                override fun onItemClicked(pos: Int) {
//               requireActivity().printToast(pos.toString())
                }

            })

        try {
            val accesstoken = requireActivity().getPrefeb("UserToken")

            if (accesstoken.isNotEmpty()) {
                val Accesstoken = "Bearer $accesstoken"

                viewModel.ConcernHistory(Accesstoken)

            }

        } catch (e: Exception) {
            println(e)
        }



        viewModel.ConcernHistoryResponse.observe(viewLifecycleOwner)
        {

            progressDialog?.dismiss()



            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.show()
                }

                is NetworkResult.Success -> {

                    viewModel.ConcernHistoryData.clear()
                    it.data!!.concernData?.let { cocerndata ->
                        cocerndata.forEach { data ->


                            viewModel.ConcernHistoryData.add(data)
                            ConcernHistoryadapter.notifyDataSetChanged()

                            binding.searchResult.apply {
                                adapter = ConcernHistoryadapter

                            }

                        }
                    }

                }


                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)

//                    binding.pageTitle.text=it.message

                }

                else -> {

                }
            }
        }

        binding.backBtn.setOnClickListener {
            (requireActivity() as DashboardActivity).onBackBtnPressed()
        }

    }

    class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemConcernHistoryBinding.bind(itemView)

    }

    class SearchItemAdapter(
        private val concernlist: ArrayList<ConcernData>,
        val itemClick: ItemClickListener
    ) :
        RecyclerView.Adapter<SearchItemViewHolder>() {

        lateinit var DayofMonth: String
        lateinit var Month: String
        lateinit var yearname: String
        lateinit var Time: String


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
            return SearchItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_concern_history, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return concernlist.size
        }

        override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {

            var Concernhistorydata = concernlist[position]

            holder.binding.caseid.text = Concernhistorydata.caseId
//            holder.binding.date.text=Concernhistorydata.createdAt

            holder.binding.message.text = Concernhistorydata.message

            for (i in 0 until Concernhistorydata.remarks.size) {
                val remark = Concernhistorydata.remarks.get(i).adminRemark

                holder.binding.remark.text = remark
            }

            gettime(Concernhistorydata.createdAt!!)

            holder.binding.date.text = "$DayofMonth $Month $yearname -$Time"


        }

        fun gettime(date: String) {
            try {
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                val date = dateFormat.parse(date)

                val calendar = Calendar.getInstance()
                calendar.time = date

                val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(date)
                val year = calendar.get(Calendar.YEAR)
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val hour = calendar.get(Calendar.HOUR)
                val minute = calendar.get(Calendar.MINUTE)
                val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

                val formattedTime = String.format("%02d:%02d %s", hour, minute, amPm)

                DayofMonth = ""
                Month = ""
                yearname = ""
                Time = ""

                DayofMonth = dayOfMonth.toString()
                val replacemonth = monthName.substring(0, 3)
                Month = replacemonth
                yearname = year.toString()
                Time = formattedTime

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

}