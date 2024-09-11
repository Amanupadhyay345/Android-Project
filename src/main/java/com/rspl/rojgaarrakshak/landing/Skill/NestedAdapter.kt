package com.rspl.rojgaarrakshak.landing.Skill

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.RemoveSubSkillid
import com.rspl.rojgaarrakshak.core.SaveSubSkillid
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.models.CityModelList
import com.rspl.rojgaarrakshak.models.GetSubSkillModel.PreferenceSubSkillsData
import com.rspl.rojgaarrakshak.models.SavePrefrencesModel

class NestedAdapter( var context:Context,private val mList: List<PreferenceSubSkillsData>) :
    RecyclerView.Adapter<NestedAdapter.NestedViewHolder>() {



//        var SubSkillid:ArrayList<String> = arrayListOf()

        var GetsubSkillid:ArrayList<Int> = arrayListOf()

       var selectedSkillid:ArrayList<String> = arrayListOf()




//    init {
//        // Load the previous sublist from shared preferences when the adapter is created
//        val savedSubList = context.getPrefeb("UserLocationSubSkillidLIst")
//        if (savedSubList.isNotEmpty()) {
//            SubSkillid.addAll(savedSubList.split(","))
//        }
//    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.nested_item, parent, false)
        return NestedViewHolder(view)


    }
    override fun onBindViewHolder(holder: NestedViewHolder, position: Int) {

        var data=mList[position]

        try {

            val json2 = context.getPrefeb("SubSkillUseridList")

            val gson2=Gson()

            val listType2 = object : TypeToken<ArrayList<Int>>() {}.type
            val list2 = gson2.fromJson<ArrayList<Int>>(json2, listType2)

            GetsubSkillid = list2 ?: ArrayList()

            for (i in 0 until GetsubSkillid.size) {
                val itemid = GetsubSkillid.get(i)

                if (data.id == itemid) {
                    holder.checkbox.isChecked=true
                }
            }



            val json1 = context.getPrefeb("UserLocationSubSkillidLIst")

            val gson1=Gson()

            val listType1 = object : TypeToken<ArrayList<String>>() {}.type
            val list1 = gson1.fromJson<ArrayList<String>>(json1, listType1)

            selectedSkillid = list1 ?: ArrayList()

            for (i in 0 until selectedSkillid.size) {
                val itemid = selectedSkillid.get(i).toInt()

                if (data.id == itemid) {
                    holder.checkbox.isChecked=true
                }
            }
        }catch (e:Exception)
        {
            println(e)
        }




            holder.mTv.text = mList[position].title

            holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                {
                   context.SaveSubSkillid(mList[position].id.toString())

                }
                else
                {
                   context.RemoveSubSkillid(mList[position].id.toString())

                }

//                val gson = Gson()
//                val json = gson.toJson(SubSkillid)
//
//
//
//                context.putPrefeb("UserLocationSubSkillidLIst", json)
//
//                val listcityid = SubSkillid.joinToString(separator = ",")
//
//                val newreplaceid=listcityid.replace("[","")
//                val replaceid=listcityid.replace("]","")
//
//                context.printToast(replaceid)
//
//                context.putPrefeb("UserLocationSubSkillid", replaceid)
            }


    }

    override fun getItemCount(): Int {

        return mList.size
    }

    inner class NestedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val mTv: TextView
       lateinit  var nodatacheck:CheckBox
       lateinit var nodataitem:TextView
      lateinit var   checkbox: CheckBox

        init {
            mTv = itemView.findViewById<TextView>(R.id.nestedItemTv)
            nodatacheck=itemView.findViewById(R.id.NodataCheckbox)
            nodataitem=itemView.findViewById(R.id.nodatanestedItemTv)
            checkbox=itemView.findViewById(R.id.Checbox)

        }
    }
}

