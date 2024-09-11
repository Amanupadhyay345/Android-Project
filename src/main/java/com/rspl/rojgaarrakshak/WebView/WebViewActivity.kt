package com.rspl.rojgaarrakshak.WebView

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.bottom_sheet.PaymentDoneBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.PaymentStausBtnLisner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.ActivityWebViewBinding
import com.rspl.rojgaarrakshak.landing.pay_and_complete.PayAndCompleteViewModel
import com.rspl.rojgaarrakshak.landing.pay_and_complete.WebViewVIewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity() : AppCompatActivity() {

    lateinit var binding:ActivityWebViewBinding
    private var progressDialog : Dialog? = null

    lateinit var paydonebottomseet:PaymentDoneBottomSheet
    private lateinit var viewModel: WebViewVIewModel
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        viewModel = ViewModelProvider(this)[WebViewVIewModel::class.java]


        progressDialog = this.getProgressDialog()

        val webSettings: WebSettings = binding.WebView.settings
                    webSettings.javaScriptEnabled = true


          val url = intent.getStringExtra("url")
                    binding.WebView.loadUrl(url!!)

                    binding.WebView.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                            Log.e("shouldOverrideUrlLoading","$url")
//                            if(url== "rojgarraskak://payment"){
//                                finish()
//                            }
                            return false  // Let the WebView load the URL
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            Log.e("onPageFinished","$url")

                            if(url== "rojgarraskak://payment"){

                                setResult(Activity.RESULT_OK)
                                finish()
                                binding.WebView.visibility=View.GONE
                            }
                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)

                            Log.e("onPageFinished","$url")
                        }

                        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                           Log.e("onError", "Reciveerrorview$view")
                           Log.e("onError", "ReciveerrorerrorCode$errorCode")
                           Log.e("onError", "Reciveerrordescription$description")
                           Log.e("onError", "ReciveerrorfailingUrl$failingUrl")

                        }
                    }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
        super.onBackPressed()
    }
}