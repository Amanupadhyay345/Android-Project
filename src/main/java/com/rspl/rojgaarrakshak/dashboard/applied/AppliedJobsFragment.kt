package com.rspl.rojgaarrakshak.dashboard.applied

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.peerpicks.core.interfaces.ItemClickListener
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentAppliedJobsBinding
import com.rspl.rojgaarrakshak.databinding.ItemAppliedJobBinding
import com.rspl.rojgaarrakshak.models.GetappliedJobResponse.AppliedJobs
import dagger.hilt.android.AndroidEntryPoint

import kotlin.Exception
@AndroidEntryPoint
class AppliedJobsFragment : Fragment() {

    companion object {
        fun newInstance() = AppliedJobsFragment()
    }

    private lateinit var viewModel: AppliedJobsViewModel
    private lateinit var binding: FragmentAppliedJobsBinding
    private lateinit var Searchitemadapter:SearchItemAdapter
    private var progressDialog : Dialog? = null
    var itemid:Int=0

    val arrlist:ArrayList<Int> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_applied_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=  FragmentAppliedJobsBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AppliedJobsViewModel::class.java)



            Searchitemadapter= SearchItemAdapter(viewModel.appliedjobresult,object : ItemClickListener{
                override fun onItemClicked(pos: Int) {
//               requireActivity().printToast(pos.toString())
                }

            })
        try {
            var accesstoken=requireActivity().getPrefeb("UserToken")

            val ApplyToken = "Bearer $accesstoken"

            viewModel.getapppliedjob(ApplyToken)



        }catch (e:Exception)
        {
            println(e)
        }

        progressDialog = requireActivity().getProgressDialog()

        viewModel.getappliedjob.observe(viewLifecycleOwner){

            progressDialog?.let {
                it.dismiss()
            }
            when (it)
            {
                is NetworkResult.Loading ->{
                    progressDialog?.let {
                        it.show()
                    }

                }
                is NetworkResult.Success ->{

                    it.data!!.appliedJobs?.let { jobList ->
                        jobList.forEach { job ->

                            viewModel.appliedjobresult.add(job)

                            arrlist.add(job.id!!)

                            Log.e("viewmodelappliedresult",viewModel.appliedjobresult.toString())

//                            binding.pageTitle.visibility=View.INVISIBLE
                            Searchitemadapter.notifyDataSetChanged()

                            binding.searchResult.apply {
                                adapter = Searchitemadapter

                            }

                        }
                    }

                    if (viewModel.appliedjobresult.isEmpty())
                    {
                        binding.pageTitle.visibility=View.INVISIBLE
                        binding.Alert.visibility=View.VISIBLE
                    }

                }
                is NetworkResult.Error ->{


                    requireActivity().showalertdilog(it.message!!)
                    if (viewModel.appliedjobresult.isEmpty())
                    {
                        binding.pageTitle.visibility=View.INVISIBLE
                        binding.Alert.visibility=View.VISIBLE
                    }

                }

            }
        }



        binding.backBtn.setOnClickListener {
            (requireActivity() as DashboardActivity).onBackBtnPressed()

        }

    }

    class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemAppliedJobBinding.bind(itemView)

    }

    class SearchItemAdapter(private val appliedlist: ArrayList<AppliedJobs>, val itemClick: ItemClickListener) :
        RecyclerView.Adapter<SearchItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
            return SearchItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_applied_job, parent, false)
            )
        }

        override fun getItemCount(): Int {
          return  appliedlist.size
        }

        override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {

            val getappliedjob = appliedlist[position]

            holder.binding.jobname.text=getappliedjob.jobTitle

            holder.binding.applyBtn.setOnClickListener {
                itemClick.onItemClicked(position)
            }
        }

    }

}