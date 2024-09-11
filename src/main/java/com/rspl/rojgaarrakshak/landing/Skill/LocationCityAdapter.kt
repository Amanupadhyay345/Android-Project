package com.rspl.rojgaarrakshak.landing.Skill

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.interfaces.StateAdapterLisner
import com.rspl.rojgaarrakshak.databinding.StateListBinding
import com.rspl.rojgaarrakshak.models.CityModelList

class LocationCityAdapter
    (private var context: Context, private var CityList:List<CityModelList>, var clickLisner: StateAdapterLisner)
    :RecyclerView.Adapter<LocationCityAdapter.viewholder>()
{

    private var originalList: List<CityModelList> = CityList


    fun filter(query: String) {
        CityList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { it.cityname.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    class viewholder(itemview:View) :RecyclerView.ViewHolder(itemview)
    {
        var binding= StateListBinding.bind(itemview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

        return viewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.state_list, parent, false)

        )

    }

    override fun getItemCount(): Int {

        return CityList.size

    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {

        var list=CityList[position]

        var cityid=context.getPrefeb("CityLocationUserid")

        if (cityid.contains(list.cityid.toString()))
        {
            holder.binding.checkcircle.visibility=View.VISIBLE
        }
        else
        {
            holder.binding.checkcircle.visibility=View.GONE
        }

        holder.binding.stateName.text=list.cityname


        holder.itemView.setOnClickListener {

            clickLisner.onclick(position,list.cityname)
        }

    }
}