package com.rspl.rojgaarrakshak.dashboard.search_job.Adapter


import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.interfaces.SalarySlipClickListner
import com.rspl.rojgaarrakshak.models.GetSalarySlipResponse.SalarySlipData


class SalarySlipAdapter(var context:Context,val locList: ArrayList<SalarySlipData>, val itemClick: SalarySlipClickListner):
    RecyclerView.Adapter<SalarySlipAdapter.viewholder>(){

    class viewholder(ItemView: View) :RecyclerView.ViewHolder(ItemView){

        val month:TextView=itemView.findViewById(R.id.month)

        val download:ImageView=itemView.findViewById(R.id.download)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_salary_slip, parent, false)
        return viewholder(view)

    }

    override fun getItemCount(): Int {
        return locList.size

    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {

        var salarydata=locList[position]

        holder.month.text=salarydata.month


        holder.download.setOnClickListener {

           itemClick.onItemClicked(position,salarydata.salarySlip!!)

        }
    }
}