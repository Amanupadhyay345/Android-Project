package com.rspl.rojgaarrakshak.dashboard.search_job.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.dashboard.search_job.FilterModel.CityModel


class FilterAdapter (private val mList: List<CityModel>,val btnlistener: BtnClickListener) : RecyclerView.Adapter<FilterAdapter.viewholder>(){


    companion object {
        var mClickListener: BtnClickListener? = null
    }

    private var listdata: MutableList<CityModel> = mList as MutableList<CityModel>

    class viewholder(ItemView: View) :RecyclerView.ViewHolder(ItemView){

        val cityname:TextView=itemView.findViewById(R.id.Cityname)

        val close:ImageView=itemView.findViewById(R.id.close)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_filter, parent, false)
        return viewholder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {

        mClickListener = btnlistener
        val ItemsViewModel = mList[position]

        holder.cityname.text=ItemsViewModel.cityname

        holder.close.setOnClickListener{

            if (mClickListener != null)
                mClickListener?.onBtnClick(position)

        }
    }
//    fun deleteItem(index: Int){
//        listdata.removeAt(index)
//        notifyDataSetChanged()
//    }
    open interface BtnClickListener {
        fun onBtnClick(position: Int)

}
}