package com.rspl.rojgaarrakshak;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.landing.LandingPage

class SplashActivity : AppCompatActivity() {

    private lateinit var Accesstoken:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startLandingActivity()
        },3000)
    }

    fun startLandingActivity(){

        Accesstoken=""

        try {

            Accesstoken=this.getPrefeb("UserToken")

            if (Accesstoken.isNotEmpty())
            {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                val intent = Intent(this, LandingPage::class.java)
                startActivity(intent)
                finish()
            }

        }catch (e:Exception)
        {
            println(e)
        }

    }
}