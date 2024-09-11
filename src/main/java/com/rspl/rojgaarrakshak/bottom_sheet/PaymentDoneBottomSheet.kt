package com.rspl.rojgaarrakshak.bottom_sheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.peerpicks.core.interfaces.ItemClickListener
import app.peerpicks.core.interfaces.SingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.interfaces.PaymentStausBtnLisner
import com.rspl.rojgaarrakshak.databinding.PaymentDoneBottomsheetBinding

class PaymentDoneBottomSheet(var message:String,val clickListener : PaymentStausBtnLisner) : BottomSheetDialogFragment() {


    lateinit var binding : PaymentDoneBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_done_bottomsheet, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PaymentDoneBottomsheetBinding.bind(view)

        binding.paymentstatusmessage.text=message

            binding.verify.setOnClickListener{
                clickListener.onApplybtnclick()

            }.toString()
    }
}