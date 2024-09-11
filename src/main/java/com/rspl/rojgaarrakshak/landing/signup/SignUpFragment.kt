package com.rspl.rojgaarrakshak.landing.signup

import android.Manifest
import android.annotation.SuppressLint

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.bottom_sheet.ChooseFileTypeBottomSheet
import com.rspl.rojgaarrakshak.bottom_sheet.PaymentDoneBottomSheet
import com.rspl.rojgaarrakshak.bottom_sheet.VerifyOtpBottomSheet
import com.rspl.rojgaarrakshak.core.getDate
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.LocationListener
import com.rspl.rojgaarrakshak.core.interfaces.PaymentStausBtnLisner
import com.rspl.rojgaarrakshak.core.interfaces.ResendOtpListner
import com.rspl.rojgaarrakshak.core.interfaces.Signlistener
import com.rspl.rojgaarrakshak.core.interfaces.VerifyOtpListner
import com.rspl.rojgaarrakshak.core.interfaces.genderclickListnerinterface
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.core.showCancelDialog
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity

import com.rspl.rojgaarrakshak.databinding.FragmentSignUpBinding
import com.rspl.rojgaarrakshak.databinding.GenderItemLayoutBinding
import com.rspl.rojgaarrakshak.databinding.ItemSkillsBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import com.rspl.rojgaarrakshak.models.skill_response.SkillsData
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray

import java.util.Locale


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding : FragmentSignUpBinding
    var buttnclick:Boolean=true
    lateinit var veryfyotpBottomSheet: VerifyOtpBottomSheet
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0

    var progressDialog : Dialog?= null

    var ganderlist:ArrayList<String> = arrayListOf()

    val maritalstatus:ArrayList<String> = arrayListOf()

    val Signupmessage:ArrayList<String> = arrayListOf()

    private var locListener: LocationListener? = object : LocationListener{
        override fun onAddressFetched(address: String) {
            try {
                binding.locationText.setText(address)
            }catch (ex : Exception){
                ex.printStackTrace()
            }
        }
        override fun onLatLngFetched(latLng: LatLng) {

        }

    }
    private var stringBuilder: StringBuilder? = null


    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                fetchCurrentLocation()
            }
        }

    private fun fetchCurrentLocation() {
        locationRequest = LocationRequest.Builder(600000).setMaxUpdates(1).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setWaitForAccurateLocation(false).build()
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            Toast.makeText(requireActivity(), "Please allow location permission.", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchAddressUsingLatLng(latLng: LatLng) {
        try {
            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            locListener?.onLatLngFetched(latLng)
            addresses?.let {
                Log.e("Location", "data:->$addresses")
                locListener?.let {
                    if (addresses.isEmpty())
                        it.onAddressFetched("Address couldn't be determined.")
                    else
                        it.onAddressFetched(addresses[0].getAddressLine(0))

                    it.onLatLngFetched(latLng)
                }
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        (requireActivity() as LandingPage).enableBackBtn(false)

        binding.signupbackbutton.setOnClickListener {
            val intent = Intent(requireContext(), LandingPage::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

                val preferences =
               context?.getSharedPreferences("amar_${context?.packageName}", Context.MODE_PRIVATE)
               val editor = preferences?.edit()
               editor!!.remove("MartialList")
               editor!!.remove("genderStatuslist")
               editor.apply()


        binding.phoneNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.length==10)
                {
                    binding.mobileContainer.setBackgroundResource(R.drawable.rect_round_corner)
                }
                else
                {
                    binding.mobileContainer.setBackgroundResource(R.drawable.warning_layout_corner)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.aadharEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.length==12)
                {
                    binding.AadharContainer.setBackgroundResource(R.drawable.rect_round_corner)
                }
                else
                {
                    binding.AadharContainer.setBackgroundResource(R.drawable.warning_layout_corner)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })





        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val intent = Intent(requireContext(), LandingPage::class.java)
                startActivity(intent)
                requireActivity().finish()


            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        progressDialog = requireActivity().getProgressDialog()

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

//        viewModel.getSkills()
        viewModel.getSkillsResponse.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Loading->{

                }

                is NetworkResult.Success->{
                    viewModel.skillList.clear()
                    viewModel.skillList.addAll(it.data!!.skillsData!!)
                    viewModel.getSkillsResponse.postValue(null)
                }

                is NetworkResult.Error->{

                    try {

                        val message= it.data!!.message!!

//                    requireActivity().printToast(message)

                        Log.e("responcemessage",message)

                        requireActivity().printToast(message)

                    }catch (e:Exception)
                    {
                        println(e)
                    }



                }

                else -> {

                }
            }
        }

        viewModel.signUpResponse.observe(viewLifecycleOwner){

            progressDialog?.dismiss()
            when(it){
                is NetworkResult.Loading->{
                    progressDialog?.show()


                }
                is NetworkResult.Success->{


                    val id=it.data?.userId

                    Log.e("UserId",id.toString())

                    requireActivity().putPrefeb("SignupUserid",id.toString())

                    viewModel.uploadImage(requireActivity(),id!!,viewModel.selectedFilePath!!,viewModel.selectedFileUri!!)
                    viewModel.signUpResponse.postValue(null)
                }
                is NetworkResult.Error->{



                   val message=it.message

                    if (message.equals("User is already registered with the provided phone number.Please login!"))
                    {
                        var AlertMessage=message
                        val builder = AlertDialog.Builder(requireContext())
                        val inflater = layoutInflater
                        val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                        builder.setView(dialogView)

                        val alertDialog = builder.create()

                        val message: TextView =dialogView.findViewById(R.id.messagetext)

                        message.text="This mobile number seems to be registered already. Would you like to \n Sign In ?"

                        var okbutton: Button =dialogView.findViewById(R.id.cancel)

                        okbutton.text=" Yes "

                        okbutton.setOnClickListener {
                            alertDialog.dismiss()
                            (requireActivity() as LandingPage).gotoPage(R.id.signInFragment,null,false)


                        }
                        val Canclebutton:Button = dialogView.findViewById(R.id.Login)

                        Canclebutton.text=" No "

                        Canclebutton.setOnClickListener {
                            alertDialog.dismiss()
                        }

                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        alertDialog.setCancelable(false)

                        alertDialog.show()

                    }

                   else if (message!!.startsWith("["))
                    {
                        val jsonArray=   JSONArray(message)

                        Signupmessage.clear()

                        for (i in 0 until jsonArray.length())

                        {
                            Signupmessage.add(jsonArray.getString(i))
                        }
                        Log.e("Signupmessage",Signupmessage.toString())

                        val Signupmessage=Signupmessage.toString()

                        val replace=Signupmessage.replace("[","")

                        val newreplacevalue=replace.replace("]","")

                        val comareplace:String=newreplacevalue.replace(",","\n")

                        requireActivity().showalertdilog(comareplace)

                    }
                    else
                    {
                        requireActivity().showalertdilog(message)
                    }

                    viewModel.signUpResponse.postValue(null)

                }

                else -> {

                }
            }
        }
        viewModel.uploadResponse.observe(viewLifecycleOwner){
            progressDialog?.dismiss()
            when(it){
                is NetworkResult.Loading->{
                    progressDialog?.show()

                }

                is NetworkResult.Success->{
                    viewModel.uploadResponse.postValue(null)

                    requireActivity().putPrefeb("Userphone",binding.phoneNum.text.toString())
                    requireActivity().putPrefeb("UserName",binding.name.text.toString())
                    requireActivity().putPrefeb("UserDOB",binding.dobText.text.toString())
                    requireActivity().putPrefeb("Useremail",binding.email.text.toString())
                    requireActivity().putPrefeb("Userlocation",binding.locationText.text.toString())
                    requireActivity().putPrefeb("Userskill",binding.skills.text.toString())

                    requireActivity().putPrefeb("UserAadharNumber",binding.aadharEdittext.text.toString())
                    requireActivity().putPrefeb("UserUploadedDocument",binding.uploadedFileText.text.toString())

                    requireActivity().putPrefeb("MaritialStatus",binding.skills2.text.toString())
                    requireActivity().putPrefeb("Usergender",binding.skills1.text.toString())
                    requireActivity().putPrefeb("MotherTongue",binding.MotherTongue.text.toString())
                    requireActivity().putPrefeb("Viewmodeldateofbirth",viewModel.dobDate)

                    veryfyotpBottomSheet=
                        VerifyOtpBottomSheet(object :VerifyOtpListner{
                            override fun onVerifyButtonClick(otp: String) {

                              try {

                                  var phoneNumber=requireActivity().getPrefeb("Userphone")
                                  var AadharNumber=requireActivity().getPrefeb("UserAadharNumber")

                                  if (phoneNumber.isNotEmpty() && AadharNumber.isNotEmpty())
                                  {
                                      viewModel.verifyOTP(requireActivity(),phoneNumber,otp,AadharNumber)
                                  }

                              }catch (e:Exception)
                              {
                                  println(e)
                              }

                            }
                        },object:ResendOtpListner{
                            override fun onResendButtonClick() {

                                try {

                                    var phoneNumber=requireActivity().getPrefeb("Userphone")


                                    if (phoneNumber.isNotEmpty())
                                    {
                                        viewModel.SignupResendOtp(phoneNumber)
                                    }

                                }catch (e:Exception)
                                {
                                    println(e)
                                }

                            }

                        })

                    veryfyotpBottomSheet.isCancelable=false
                    veryfyotpBottomSheet.show(childFragmentManager,"verifyotpbottomsheet")

//                    (requireActivity() as LandingPage).gotoPage(R.id.adharAndOTPFragment,null,true)
                }

                is NetworkResult.Error->{

                    val message=it.message

                     if (message!!.startsWith("["))
                    {
                        val jsonArray=   JSONArray(message)

                        Signupmessage.clear()

                        for (i in 0 until jsonArray.length())

                        {
                            Signupmessage.add(jsonArray.getString(i))
                        }
                        Log.e("Signupmessage",Signupmessage.toString())

                        val Signupmessage=Signupmessage.toString()

                        val replace=Signupmessage.replace("[","")

                        val newreplacevalue=replace.replace("]","")

                        val comareplace:String=newreplacevalue.replace(",","\n")

                        requireActivity().showalertdilog(comareplace)

                    }
                    else
                    {
                        requireActivity().showalertdilog(message)
                    }

                    viewModel.uploadResponse.postValue(null)
                }

                else -> {

                }
            }
        }


        viewModel.getOtpResponse.observe(viewLifecycleOwner) {
            progressDialog?.dismiss()
            when (it) {
                is NetworkResult.Loading -> {

                    progressDialog?.show()

                }

                is NetworkResult.Success -> {

                    progressDialog?.dismiss()

                    var token=   it.data!!.authorisation!!.token

                    requireActivity().putPrefeb("UserToken",token!!)

                    (requireActivity() as LandingPage).gotoPage(
                        R.id.skillFregment,
                        null,
                        true
                    )
                }

                is NetworkResult.Error -> {

                    progressDialog?.dismiss()

                    val message=it.message

                    if (message!!.startsWith("["))
                    {
                        val jsonArray=   JSONArray(message)

                        Signupmessage.clear()

                        for (i in 0 until jsonArray.length())

                        {
                            Signupmessage.add(jsonArray.getString(i))
                        }
                        Log.e("Signupmessage",Signupmessage.toString())

                        val Signupmessage=Signupmessage.toString()

                        val replace=Signupmessage.replace("[","")

                        val newreplacevalue=replace.replace("]","")

                        val comareplace:String=newreplacevalue.replace(",","\n")

                        requireActivity().showalertdilog(comareplace)

                    }
                    else
                    {
                        requireActivity().showalertdilog(message)
                    }

                }
            }
        }

        viewModel.resendotpResponse.observe(viewLifecycleOwner) {
            progressDialog?.dismiss()
            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.show()

                }

                is NetworkResult.Success -> {
                    progressDialog?.dismiss()

                    requireActivity().printToast(it.data!!.message.toString())

                }

                is NetworkResult.Error -> {
                    progressDialog?.dismiss()
                    val message=it.message

                    if (message!!.startsWith("["))
                    {
                        val jsonArray=   JSONArray(message)

                        Signupmessage.clear()

                        for (i in 0 until jsonArray.length())

                        {
                            Signupmessage.add(jsonArray.getString(i))
                        }
                        Log.e("Signupmessage",Signupmessage.toString())

                        val Signupmessage=Signupmessage.toString()

                        val replace=Signupmessage.replace("[","")

                        val newreplacevalue=replace.replace("]","")

                        val comareplace:String=newreplacevalue.replace(",","\n")

                        requireActivity().showalertdilog(comareplace)

                    }
                    else
                    {
                        requireActivity().showalertdilog(message)
                    }

                }
            }
        }

        binding.dobBtn.setOnClickListener{
            var datePicker = DatePickerDialog(requireContext())
            datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                var pickedDate = "$dayOfMonth-${String.format(Locale.ENGLISH,"%02d",(month+1))}-$year"
                getDate(pickedDate ,"dd-MM-yyyy")
                viewModel.dobDate = pickedDate//getDate(pickedDate ,"dd/MM/yyyy")!!

                Log.e("viewModel.dobDate",viewModel.dobDate.toString())
                binding.dobText.text= pickedDate
            }

            datePicker.show()
        }



        binding.fileUpload.setOnClickListener {
//            filePickerLauncher.launch(arrayOf("application/pdf","image/*"))
            var docBottomSheet = ChooseFileTypeBottomSheet(object : ChooseFileTypeBottomSheet.FilePickerListener{
                override fun onFilePicked(uri: Uri?,filePath : String) {
                    uri?.let { fileUri->

                        viewModel.selectedFileUri = fileUri
                        viewModel.selectedFilePath = filePath


                        requireActivity().contentResolver.query(fileUri,null,null,null,null)?.use {
                                cursor ->

                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                            cursor.moveToFirst()
                            val fileName = cursor.getString(nameIndex)
                            val fileSizeBytes = cursor.getLong(fileSizeIndex)
                            val fileSizeKB = fileSizeBytes.toDouble() / 1024.0


                            viewModel.fileSize=fileSizeKB


                            binding.uploadedFileText.text = fileName
                        }

                        Log.e("Picked file"," file name : $filePath" )


                    }
                }
            })

            docBottomSheet.show(childFragmentManager,"doc bottom sheet")
        }

        binding.signInBtn.setOnClickListener {

            when{
                binding.name.text.trim().isEmpty()->{
                    requireActivity().showalertdilog("Please enter a valid name.")
                    return@setOnClickListener
                }

                binding.dobText.text.trim().isEmpty()->{
                    requireActivity().showalertdilog("Please enter a valid Date Of Birth.")
                    return@setOnClickListener
                }

//                binding.email.text.isEmpty()->{
//                    requireActivity().showalertdilog("Please enter a valid email.")
//                    return@setOnClickListener
//                }

                binding.locationText.text.trim().isEmpty()->{
                    requireActivity().showalertdilog("Please enter a valid address.")
                    return@setOnClickListener
                }

                binding.phoneNum.text.trim().isEmpty()->{
                    requireActivity().showalertdilog("Please enter a valid phone number.")
                    return@setOnClickListener
                }

                binding.aadharEdittext.text.trim().isEmpty()->{
                    requireActivity().showalertdilog("Please enter a valid Aadhar number.")
                    return@setOnClickListener
                }
                binding.uploadedFileText.text.trim().isEmpty() ->{
                    requireActivity().showalertdilog("Please Upload Your Resume.")
                    return@setOnClickListener
                }
                viewModel.fileSize > 2048 ->{
                    requireActivity().showalertdilog("Please Upload Your Resume Less Than 2 MB.")
                    return@setOnClickListener
                }
                binding.aadharEdittext.text.trim().length < 12 -> {
                    requireActivity().showalertdilog("Please Enter Valid aadhar Number .")
                    return@setOnClickListener
                }
            }

//            progressDialog!!.show()

            viewModel.signUpRequest(binding.name.text.toString().trim(),binding.email.text.toString().trim()
                ,binding.locationText.text.toString().trim(),binding.phoneNum.text.toString().trim(),binding.skills1.text.toString().trim(),binding.skills2.text.toString().trim().lowercase(),binding.MotherTongue.text.toString().trim())


        }

        ganderlist.add("Male")
        ganderlist.add("Female")

        maritalstatus.add("Married")
        maritalstatus.add("Unmarried")

        binding.skillContainer.setOnClickListener {
            binding.skillList.visibility=if(binding.skillList.isVisible) View.GONE else View.VISIBLE
        }

        binding.skillContainer1.setOnClickListener {
            binding.skillList1.visibility=if(binding.skillList1.isVisible) View.GONE else View.VISIBLE
        }
        binding.skillContainer2.setOnClickListener {
            binding.skillList2.visibility=if(binding.skillList2.isVisible) View.GONE else View.VISIBLE
        }

        binding.skillList.apply {
            adapter = ItemAdapter(viewModel.skillList , object : Signlistener{
                override fun onItemClicked(pos: Int) {
//                    binding.skillList.visibility = View.GONE
                    binding.skills.text =""
                    viewModel.skillList.forEach {
                        if(it!!.ischecked) {
                            binding.skills.text = binding.skills.text.toString() + it!!.skillTitle+", "
                        }

                    }

                }

            })
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.skillList1.apply {
            adapter = GenderAdapter( requireContext(),ganderlist, object : genderclickListnerinterface{
                override fun onItemClicked(pos: Int, data: String) {
                    binding.skillList1.visibility = View.GONE
                    binding.skills1.text =""

                    binding.skills1.text = data
                }


            })
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.skillList2.apply {
            adapter = listAdapter(requireContext(), maritalstatus, object : genderclickListnerinterface{
                override fun onItemClicked(pos: Int, data: String) {
                    binding.skillList2.visibility = View.GONE
                    binding.skills2.text =""

                    binding.skills2.text = data
                }


            })
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

//    override fun onResume() {
//        super.onResume()
//
//    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSkillsBinding.bind(itemView)

    }

    class ItemAdapter(val list: ArrayList<SkillsData?>, val itemClick: Signlistener) :
        RecyclerView.Adapter<ItemViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_skills, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            var data = list[position]



            holder.binding.skillName.text = data!!.skillTitle

            holder.binding.skillName.isChecked = data.ischecked

            holder.itemView.setOnClickListener {

                notifyDataSetChanged()

                data!!.ischecked=!data!!.ischecked
                holder.binding.skillName.isChecked=data.ischecked

                itemClick.onItemClicked(position)

            }

        }

    }


    class listviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = GenderItemLayoutBinding.bind(itemView)

    }

    class listAdapter( val context:Context, val list: ArrayList<String>, val itemClick: genderclickListnerinterface) :
        RecyclerView.Adapter<listviewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listviewholder {
            return listviewholder(
                LayoutInflater.from(parent.context).inflate(R.layout.gender_item_layout, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        @SuppressLint("SuspiciousIndentation")
        override fun onBindViewHolder(holder: listviewholder, position: Int) {

            try {
                var data = list[position]

                var adapterpositon=position

             var genderselectedposition=   context.getPrefeb("MartialList")






                if (genderselectedposition.isNotEmpty())
                {
                    var postion=genderselectedposition.toInt()

                    if (postion==adapterpositon)
                    {
                        holder.binding.checkcircle.visibility=View.VISIBLE
                    }
                    else
                    {
                        holder.binding.checkcircle.visibility=View.INVISIBLE
                    }
                }

                holder.binding.skillName.text = data

                holder.itemView.setOnClickListener {


                    context.putPrefeb("MartialList",position.toString())

                    notifyDataSetChanged()



                    itemClick.onItemClicked(position,data)

                }

            }catch (e:Exception)
            {
                println(e)
            }


        }

    }


    class genderviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = GenderItemLayoutBinding.bind(itemView)

    }

    class GenderAdapter( val context:Context, val list: ArrayList<String>, val itemClick: genderclickListnerinterface) :
        RecyclerView.Adapter<genderviewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): genderviewholder {
            return genderviewholder(
                LayoutInflater.from(parent.context).inflate(R.layout.gender_item_layout, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        @SuppressLint("SuspiciousIndentation")
        override fun onBindViewHolder(holder: genderviewholder, position: Int) {

            try {
                var data = list[position]

                var adapterpositon=position

                var genderselectedposition=   context.getPrefeb("genderStatuslist")

                if (genderselectedposition.isNotEmpty())
                {
                    var postion=genderselectedposition.toInt()

                    if (postion==adapterpositon)
                    {
                        holder.binding.checkcircle.visibility=View.VISIBLE
                    }
                    else
                    {
                        holder.binding.checkcircle.visibility=View.INVISIBLE
                    }
                }

                holder.binding.skillName.text = data

                holder.itemView.setOnClickListener {


                    context.putPrefeb("genderStatuslist",position.toString())

                    notifyDataSetChanged()



                    itemClick.onItemClicked(position,data)

                }

            }catch (e:Exception)
            {
                println(e)
            }


        }

    }


//    end gender adapter

}