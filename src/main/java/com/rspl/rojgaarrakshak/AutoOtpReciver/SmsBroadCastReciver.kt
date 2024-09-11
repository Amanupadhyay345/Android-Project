package com.rspl.rojgaarrakshak.AutoOtpReciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadCastReciver :BroadcastReceiver() {

    var smsBroadCastReciverListner:SmsBroadCastReciverListner ?=null

    override fun onReceive(context: Context?, intent: Intent?) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action)
        {
            val extras=intent.extras
            val SmsRetriverStatus=extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when(SmsRetriverStatus.statusCode)
            {
                CommonStatusCodes.SUCCESS ->{

                    val messageintent=extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)

                    smsBroadCastReciverListner?.onSuccess(messageintent)

                }
                CommonStatusCodes.TIMEOUT ->{

                    smsBroadCastReciverListner?.onFailure()

                }
            }
        }

    }

    interface SmsBroadCastReciverListner{
        fun onSuccess(intent: Intent?)
        fun onFailure()
    }
}