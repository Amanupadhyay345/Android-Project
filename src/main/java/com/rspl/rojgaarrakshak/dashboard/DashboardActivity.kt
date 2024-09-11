package com.rspl.rojgaarrakshak.dashboard

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.aqube.ram.extension.makeVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.bottom_sheet.PaymentDoneBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.interfaces.LocationListener
import com.rspl.rojgaarrakshak.core.interfaces.PaymentStausBtnLisner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var binding : ActivityDashboardBinding
    lateinit var paydonebottomseet:PaymentDoneBottomSheet
    lateinit var bottomNavigationView : BottomNavigationView

    var isUserLoggedIn = false

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0

    var progressDialog : Dialog?= null

    private var locListener: LocationListener? = null
    private var stringBuilder: StringBuilder? = null


    fun requestCurrentLocation(listener: LocationListener){
        locListener = listener
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                fetchCurrentLocation()
            }
        }

    private fun fetchCurrentLocation() {
        locationRequest = LocationRequest.Builder(600000).setMaxUpdates(1).setPriority(
            LocationRequest.PRIORITY_HIGH_ACCURACY).setWaitForAccurateLocation(false).build()
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.isWaitForAccurateLocation = false
        stringBuilder = StringBuilder()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    location?.let {
                        wayLatitude = location.latitude
                        wayLongitude = location.longitude
                        Log.e("Location", "LatLng:->$wayLatitude:$wayLongitude")

                        fetchAddressUsingLatLng(LatLng(wayLatitude, wayLongitude))
                    }
                }
            }
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@DashboardActivity)

        if (ActivityCompat.checkSelfPermission(
                this@DashboardActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@DashboardActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            Toast.makeText(this@DashboardActivity, "Please allow location permission.", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchAddressUsingLatLng(latLng: LatLng) {
        try {
            val geocoder = Geocoder(this@DashboardActivity, Locale.getDefault())
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            locListener?.onLatLngFetched(latLng)
            addresses?.let {
                Log.e("Location", "data:->$addresses")
                locListener?.let {
                    if (addresses.isEmpty())
                        it.onAddressFetched("Address couldn't be determined.")
                    else{
                        it.onAddressFetched(addresses[0].locality)
                    }
//                        it.onAddressFetched(addresses[0].getAddressLine(0))

                    it.onLatLngFetched(latLng)
                }
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.nav_host_fragment)






//        binding.backBtn.setOnClickListener {
//            navController.popBackStack()
//        }

        bottomNavigationView = binding.bottomNav
        bottomNavigationView.setupWithNavController(navController)




        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ticketFragment -> {
                    // Load Fragment 1
                    if (navController.currentDestination?.id != R.id.ticketFragment) {
                        // Load Fragment 1
                        navController.navigate(R.id.ticketFragment)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.raiseConcernFragment -> {
                    // Load Fragment 2
                    navController.navigate(R.id.raiseConcernFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profileFragment -> {
                    // Load Fragment 3
                    navController.navigate(R.id.profileFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.settingsFragment ->{
                    navController.navigate(R.id.settingsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

       navController.navigate(R.id.ticketFragment)



        try {

            val paymentStatus = intent.getBooleanExtra("PaymentResponseSuccess", false)

            if (paymentStatus)
            {
                this.gotoPage(R.id.searchJobFragment,null)
            }

        }catch (e:Exception)
        {
            println(e)
        }





    }

    fun gotoPage(@IdRes pageId : Int, bundle: Bundle?){
        if(bundle!=null)
            navController.navigate(pageId,bundle)
        else
            navController.navigate(pageId)
    }

    fun onBackBtnPressed(){
        navController.popBackStack()
    }
}