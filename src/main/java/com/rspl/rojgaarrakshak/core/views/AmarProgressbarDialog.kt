package com.rspl.rojgaarrakshak.core.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.ActionBar
import com.rspl.rojgaarrakshak.databinding.CustomProgressLayoutBinding

class AmarProgressbarDialog(val ctx : Context) {


    fun Context.getProgressDialog() : Dialog {
        val dialog = Dialog(this)
        val binding = CustomProgressLayoutBinding.inflate(LayoutInflater.from(this))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        try {
            dialog.show()
        }catch (e : Exception){
            e.printStackTrace()
        }

        return dialog
    }

}