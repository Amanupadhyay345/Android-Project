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
import com.rspl.rojgaarrakshak.databinding.PaymentDoneBottomsheetBinding
import com.rspl.rojgaarrakshak.databinding.WorkHistoryBottomsheetBinding

class CheckInBottomSheet() : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = CheckInBottomSheet()
    }

    lateinit var binding : CheckInBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.check_in_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CheckInBottomsheetBinding.bind(view)

        binding.verifyOtp.setOnClickListener {
            dismiss()
        }
    }
}