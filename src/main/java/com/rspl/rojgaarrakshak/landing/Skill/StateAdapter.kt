package com.rspl.rojgaarrakshak.landing.Skill

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.interfaces.StateAdapterLisner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.dashboard.search_job.SearchJobFragment
import com.rspl.rojgaarrakshak.databinding.StateListBinding

class StateAdapter( private var context:Context,private var statelist:List<String>, var clickLisner: StateAdapterLisner):RecyclerView.Adapter<StateAdapter.ViewHolder>()
{

    private var originalList: List<String> = statelist


    fun filter(query: String) {
        statelist = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { it.contains(query,ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemview:View) :RecyclerView.ViewHolder(itemview)
    {
        var binding=StateListBinding.bind(itemview)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.state_list, parent, false)

        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var list=statelist[position]

        holder.binding.stateName.text=list


        holder.itemView.setOnClickListener {

            clickLisner.onclick(position,list)
        }




    }

    override fun getItemCount(): Int {
       return statelist.size
    }

}