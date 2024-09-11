package com.rspl.rojgaarrakshak.bottom_sheet

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rspl.rojgaarrakshak.AutoOtpReciver.SmsBroadCastReciver
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.interfaces.ResendOtpListner
import com.rspl.rojgaarrakshak.core.interfaces.VerifyOtpListner
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.databinding.CheckInBottomsheetBinding
import java.util.regex.Pattern

class VerifyOtpBottomSheet(val clickListener : VerifyOtpListner, var resendButtonclick : ResendOtpListner) : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(verifyotp:VerifyOtpListner,resendButton:ResendOtpListner) = VerifyOtpBottomSheet(verifyotp,resendButton)
    }

    lateinit var binding : CheckInBottomsheetBinding
    lateinit var PhoneNumber:String

    private val REQUEST_USER_CONSENT =200
    var smsBroadCastReciver: SmsBroadCastReciver?=null
    private var countdownTimer: CountDownTimer? = null
    private val countdownInterval = 1000
    private var totalTime = 120000
    val Signupmessage:ArrayList<String> = arrayListOf()

    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.check_in_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = CheckInBottomsheetBinding.bind(view)

        PhoneNumber=""

        binding.resendBtn.setOnClickListener {

            totalTime +=120000
            initializeTimer()
            startTimer()

            resendButtonclick.onResendButtonClick()
        }

        binding.CancleButton.setOnClickListener {
            dismiss()
        }


        try {
            PhoneNumber = requireContext().getPrefeb("Userphone")
        }catch (e:Exception)
        {
            println(e)
        }


        initializeTimer()
        startTimer()
        startsmsUserConsent()


        binding.otp1.doOnTextChanged { text, start, before, count ->
            Log.e("otp1", "text:$text, start:$start, before:$before, count:$count")

            if (text!!.isNotEmpty())
            {
                binding.otp1.setSelection(binding.otp1.text!!.length)

            }

            if (count == 1)
            {
                binding.otp2.requestFocus()
            }

            if (text!!.length>1 && text.length<6)
            {
                binding.otp1.text!!.clear()
                binding.otp1.requestFocus()
            }
            else if(count==6){
                var otpCharArray = text.toString().toCharArray()
                binding.otp1.setText(otpCharArray[0].toString())
                binding.otp2.setText(otpCharArray[1].toString())
                binding.otp3.setText(otpCharArray[2].toString())
                binding.otp4.setText(otpCharArray[3].toString())
                binding.otp5.setText(otpCharArray[4].toString())
                binding.otp6.setText(otpCharArray[5].toString())

            }
        }

        binding.otp2.doOnTextChanged { text, start, before, count ->

            if (text!!.isNotEmpty())
            {
                binding.otp2.setSelection(binding.otp2.text!!.length)
            }

            if (count ==1)
            {
                binding.otp3.requestFocus()
            }

        }

        binding.otp3.doOnTextChanged { text, start, before, count ->

            if (text!!.isNotEmpty())
            {
                binding.otp3.setSelection(binding.otp3.text!!.length)
            }

            if (count ==1)
            {
                binding.otp4.requestFocus()
            }

        }

        binding.otp4.doOnTextChanged { text, start, before, count ->


            if (count ==1)
            {
                binding.otp5.requestFocus()
            }

            if (text!!.isNotEmpty())
            {
                binding.otp4.setSelection(binding.otp4.text!!.length)
            }

        }

        binding.otp5.doOnTextChanged { text, start, before, count ->


            if (count ==1)
            {
                binding.otp6.requestFocus()
            }

            if (text!!.isNotEmpty())
            {
                binding.otp5.setSelection(binding.otp5.text!!.length)
            }

        }

        binding.otp6.doOnTextChanged { text, start, before, count ->

            if (text!!.isNotEmpty())
            {
                binding.otp6.setSelection(binding.otp6.text!!.length)
            }



        }

        binding.otp2.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {



                binding.otp2.text!!.clear()

                binding.otp1.requestFocus()

                true
            } else {
                false
            }
        }
        binding.otp3.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {



                binding.otp3.text!!.clear()

                binding.otp2.requestFocus()

                true
            } else {
                false
            }
        }
        binding.otp4.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {



                binding.otp4.text!!.clear()

                binding.otp3.requestFocus()

                true
            } else {
                false
            }
        }
        binding.otp5.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {



                binding.otp5.text!!.clear()

                binding.otp4.requestFocus()

                true
            } else {
                false
            }
        }
        binding.otp6.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {



                binding.otp6.text!!.clear()

                binding.otp5.requestFocus()

                true
            } else {
                false
            }
        }


        binding.verifyOtp.setOnClickListener {

            var otp =
                "${binding.otp1.text}${binding.otp2.text}${binding.otp3.text}${binding.otp4.text}${binding.otp5.text}${binding.otp6.text}"

            when {
                otp.length < 6 -> {
                    requireActivity().showalertdilog(
                        "Please provide a valid otp."
                    )
                    return@setOnClickListener
                }

                else -> {
                   clickListener.onVerifyButtonClick(otp)


                }

            }

        }
    }

    private fun startsmsUserConsent() {
        var client= SmsRetriever.getClient(requireContext())

        client.startSmsUserConsent(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode==REQUEST_USER_CONSENT)
        {
            if (data!=null)
            {
                var message=data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)

                getotpfrommessage(message)
            }
        }
    }

    private fun getotpfrommessage(message: String?) {

        val otppattern= Pattern.compile("(|^)\\d{6}")

        var mattchar=otppattern.matcher(message)

        if (mattchar.find())
        {

            var otp=mattchar.group(0)

            var otp1= otp[0]
            var otp2=otp[1]
            var otp3=otp[2]
            var otp4=otp[3]
            var otp5=otp[4]
            var otp6=otp[5]

            binding.otp1.setText(otp1.toString())
            binding.otp2.setText(otp2.toString())
            binding.otp3.setText(otp3.toString())
            binding.otp4.setText(otp4.toString())
            binding.otp5.setText(otp5.toString())
            binding.otp6.setText(otp6.toString())
        }

    }


    private fun RegisterBrodcastReciver(){
        smsBroadCastReciver = SmsBroadCastReciver()

        smsBroadCastReciver!!.smsBroadCastReciverListner = object :SmsBroadCastReciver.SmsBroadCastReciverListner{
            override fun onSuccess(intent: Intent?) {
                startActivityForResult(intent!!,REQUEST_USER_CONSENT)

            }

            override fun onFailure() {

//                requireContext().printToast("faield to get otp")

            }

        }

        var intentfilter= IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireContext().registerReceiver(smsBroadCastReciver,intentfilter)
    }

    override fun onStart() {
        super.onStart()

        RegisterBrodcastReciver()
    }

    override fun onStop() {
        super.onStop()

        requireContext().unregisterReceiver(smsBroadCastReciver)
    }
    // auto otp reciver method close

    // timer method start
    private fun startTimer() {
        binding.resendBtn.isEnabled = false

        val textColor = "#787272"

        binding.resendBtn.setTextColor(Color.parseColor(textColor))
        binding.timeLeft.setTextColor(Color.parseColor("#FFFFFFFF"))


        countdownTimer?.start()

    }

    override fun onDestroy() {
        super.onDestroy()

        countdownTimer?.cancel()
    }

    private fun initializeTimer() {
        countdownTimer = object :CountDownTimer(totalTime.toLong(),countdownInterval.toLong())
        {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val formattedTime = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60)
                binding.timeLeft.text =  formattedTime
            }

            override fun onFinish() {
                binding.resendBtn.isEnabled = true

                val timertextcolor = "#9F9A9A"

                binding.timeLeft.setTextColor(Color.parseColor(timertextcolor))

                val textColor = "#FFFFFFFF"

                binding.resendBtn.setTextColor(Color.parseColor(textColor))
            }

        }

    }

}