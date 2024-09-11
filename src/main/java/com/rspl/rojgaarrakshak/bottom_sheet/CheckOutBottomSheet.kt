package com.rspl.rojgaarrakshak.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.peerpicks.core.interfaces.ItemClickListener
import app.peerpicks.core.interfaces.SingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.databinding.CheckInBottomsheetBinding
import com.rspl.rojgaarrakshak.databinding.CheckOutBottomsheetBinding
import com.rspl.rojgaarrakshak.databinding.PaymentDoneBottomsheetBinding
import com.rspl.rojgaarrakshak.databinding.WorkHistoryBottomsheetBinding

class CheckOutBottomSheet() : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = CheckOutBottomSheet()
    }

    lateinit var binding : CheckOutBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.check_out_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CheckOutBottomsheetBinding.bind(view)

        binding.verifyOtp.setOnClickListener {
            dismiss()
        }
    }
}