package com.rspl.rojgaarrakshak.landing.Skill

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.interfaces.SkillItemClickLisner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.models.GetSubSkillModel.PreferenceSubSkillsData
import com.rspl.rojgaarrakshak.models.SkillModel.PreferenceSkillsData

class ItemAdapter(var context: Context, private val mList: List<PreferenceSkillsData>,var itemclick:SkillItemClickLisner) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var list: List<PreferenceSubSkillsData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return ItemViewHolder(view)
    }

    fun updateSubSkillsData(position: Int, subSkills: List<PreferenceSubSkillsData>) {
        mList[position].preferenceSubSkillsData = subSkills as ArrayList<PreferenceSubSkillsData>

        notifyItemChanged(position)
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val model = mList[position]
        holder.mTextView.text = model.preSkillTitle

        val isExpandable: Boolean = model.isExpandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.LineView.visibility = if (isExpandable) View.VISIBLE else View.GONE

        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_back_ios_new_24)
            holder.mArrowImage.rotation=90f


        } else {
            holder.mArrowImage.setImageResource(R.drawable.baseline_arrow_back_ios_new_24)
            holder.mArrowImage.rotation=-90f
        }

            list = model.preferenceSubSkillsData

            val adapter = NestedAdapter(context,list)
            holder.nestedRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
//            holder.nestedRecyclerView.setHasFixedSize(true)
            holder.nestedRecyclerView.adapter = adapter

        holder.linearLayout.setOnClickListener {
            val position = position
            val skillId = model.id.toString()
            var skillname = model.preSkillTitle.toString()

            if (!model.isExpandable) {

                if (model.preferenceSubSkillsData.isEmpty())
                {
                    itemclick.onClick(position, skillId, skillname)
                    model.isExpandable = true
                    notifyItemChanged(holder.bindingAdapterPosition)
                }
                else
                {
                    model.isExpandable = true
                    notifyItemChanged(holder.bindingAdapterPosition)
                }




            } else {
                model.isExpandable = false
                notifyItemChanged(holder.bindingAdapterPosition)


            }
        }




    }

    override fun getItemCount(): Int {
        return mList.size
    }



    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linear_layout)
        val expandableLayout: RelativeLayout = itemView.findViewById(R.id.expandable_layout)
        val mTextView: TextView = itemView.findViewById(R.id.itemTv)
        val LineView:View=itemView.findViewById(R.id.LineView)
        val mArrowImage: ImageView = itemView.findViewById(R.id.arro_imageview)
        val nestedRecyclerView: RecyclerView = itemView.findViewById(R.id.child_rv)
    }
}
