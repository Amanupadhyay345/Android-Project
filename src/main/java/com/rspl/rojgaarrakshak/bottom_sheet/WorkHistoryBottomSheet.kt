package com.rspl.rojgaarrakshak.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.peerpicks.core.interfaces.ItemClickListener
import app.peerpicks.core.interfaces.SingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.databinding.PaymentDoneBottomsheetBinding
import com.rspl.rojgaarrakshak.databinding.WorkHistoryBottomsheetBinding

class WorkHistoryBottomSheet(val companyname:String,val jobdestination:String,val joblocation:String,
                             val dateofjoing:String,val enddate:String,val Salary:String,val empname:String) : BottomSheetDialogFragment() {

//    companion object {
//        fun newInstance() = WorkHistoryBottomSheet()
//    }

    lateinit var binding : WorkHistoryBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.work_history_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WorkHistoryBottomsheetBinding.bind(view)

        binding.companyname.text=companyname
        binding.enddate.text=enddate
        binding.dateofjoing.text=dateofjoing
        binding.securityservice.text=jobdestination
        binding.location.text=joblocation
        binding.salary.text=Salary
        binding.empname.text=empname

    }
}