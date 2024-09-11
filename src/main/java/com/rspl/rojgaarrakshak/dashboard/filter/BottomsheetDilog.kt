package com.rspl.rojgaarrakshak.dashboard.filter

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.databinding.BottomSheetLayoutBinding

open class BottomsheetDilog(
    val title: String, val companyname: String,
    val salary: String, val jobdescreption: String, val experience: String,
    val destination: String, val location: String,
    val expdate: String, val skilltitle: String,
    val isApplied: Boolean = false,
    val clickListener: BtnClickListenr
) : BottomSheetDialogFragment() {


//    companion object {
//        fun newInstance(clickListener : BtnClickListenr) = BottomsheetDilog(clickListener,)
//    }

    lateinit var binding: BottomSheetLayoutBinding
    var jobid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetLayoutBinding.bind(view)

        binding.jobtitle.text = title
        binding.experience.text = experience
        binding.location.text = location
        binding.Salary.text = salary
        binding.CompanyName.text = companyname
        binding.skillTitle.text = skilltitle
        binding.descreption.text = jobdescreption
        binding.expDate.text = expdate
        binding.designation.text = destination

        if (isApplied) {
            binding.Applyjob.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.button_background))
            binding.Applyjob.text = "APPLIED"
            binding.Applyjob.setTextColor(resources.getColor(R.color.white))
        }

        binding.Applyjob.setOnClickListener {
            if (!isApplied)
                clickListener.onApplybtnclick()
            else {
                Toast.makeText(context, "Already applied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}