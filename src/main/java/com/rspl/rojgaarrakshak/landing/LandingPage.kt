package com.rspl.rojgaarrakshak.landing

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.aqube.ram.extension.makeVisible
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.databinding.ActivityLandingPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingPage : AppCompatActivity() {
    private lateinit var navController: NavController


    lateinit var binding: ActivityLandingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)

        try {

            val extras = intent.extras
            if (extras != null) {
                val navigateToSignInPage = extras.getString("NavigateToSignInPage")

                if (navigateToSignInPage == "NavigateToSignInPage") {

                    this.gotoPage(R.id.landingFragment,null,false)
                }
            }

        }catch (e:Exception){
            println(e)
        }

        val intent = intent
        if (intent != null && intent.action != null) {
            if (Intent.ACTION_VIEW == intent.action) {
                val data = intent.data

                val url=data.toString()

                this.putPrefeb("DeepLink",url)

                if (url=="rojgarraskak://payment")
                {
                    navController.navigate(R.id.payAndCompleteFragment)
                }

                if (url == "https://api-rakshak.synchsoft.in/api/V1/payment/complete")

                {
                    navController.navigate(R.id.payAndCompleteFragment)
                }

            }
        }


        binding.backBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    fun enableBackBtn(enableBackBtn : Boolean){
        binding.backBtn.makeVisible(enableBackBtn)
    }

    fun gotoPage(@IdRes pageId : Int, bundle: Bundle?,enableBackBtn : Boolean){
        binding.backBtn.makeVisible(enableBackBtn)
        if(bundle!=null)
            navController.navigate(pageId,bundle)
        else
            navController.navigate(pageId)
    }
}