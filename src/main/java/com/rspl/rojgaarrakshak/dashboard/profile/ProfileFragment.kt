package com.rspl.rojgaarrakshak.dashboard.profile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.bottom_sheet.ChooseFileTypeBottomSheet
import com.rspl.rojgaarrakshak.bottom_sheet.ChoosePhotoTypeBottomSheet
import com.rspl.rojgaarrakshak.core.getDate
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.Signlistener
import com.rspl.rojgaarrakshak.core.interfaces.genderclickListnerinterface
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb

import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentProfileBinding
import com.rspl.rojgaarrakshak.databinding.FragmentTicketBinding
import com.rspl.rojgaarrakshak.databinding.GenderItemLayoutBinding
import com.rspl.rojgaarrakshak.databinding.ItemSkillsBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import com.rspl.rojgaarrakshak.landing.signup.SignUpFragment
import com.rspl.rojgaarrakshak.models.skill_response.SkillsData
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    lateinit var userlogin:String

     var updatephoto=false
    private lateinit var binding: FragmentProfileBinding
    lateinit var username:String
    val Signupmessage:ArrayList<String> = arrayListOf()
    lateinit var dateofbirth:String
    lateinit var Useremail:String
    lateinit var Userlocation:String
    lateinit var UserMobileNumber:String
    lateinit var accesstoken:String
    lateinit var Skillid:String
    lateinit var gender:String
    lateinit var Maritialstatus:String
    lateinit var Mothertongue:String
    lateinit var UserUploadDoc:String
    lateinit var Skillname:String
    var ganderlist:ArrayList<String> = arrayListOf()
    val maritalstatus:ArrayList<String> = arrayListOf()
    private var progressDialog : Dialog? = null
    lateinit var UserDataid:String
    lateinit var EmployeeId:String
    lateinit var UserUniqueId:String
    var Userid:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
    }

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]


        val preferences =
            context?.getSharedPreferences("amar_${context?.packageName}", Context.MODE_PRIVATE)
        val editor = preferences?.edit()
        editor!!.remove("MartialList")
        editor!!.remove("genderStatuslist")
        editor.apply()

        binding.dobText.isClickable=false
        binding.dobText.isFocusable=false

        binding.skillContainer2.isClickable=false
        binding.skillContainer2.isFocusable=false

        binding.skillContainer1.isClickable=false
        binding.skillContainer1.isFocusable=false

        username=""
        gender=""
        Maritialstatus=""
        UserUploadDoc=""
        Mothertongue=""
        UserUniqueId=""
        EmployeeId=""

        dateofbirth=""
        Useremail=""
        Userlocation=""
        UserMobileNumber=""
        accesstoken=""
        UserDataid=""
        userlogin=""
        Skillid=""
        Skillname=""


        ganderlist.add("Male")
        ganderlist.add("Female")

        maritalstatus.add("married")
        maritalstatus.add("unmarried")

        try {
            userlogin = requireActivity().getPrefeb("UserToken")

            if (userlogin.isEmpty())
            {
               binding.inputBox.visibility=View.GONE
                binding.imagepickar.visibility=View.GONE
               binding.empId.visibility=View.GONE
                binding.header.visibility=View.GONE

                val builder = AlertDialog.Builder(requireContext())
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                builder.setView(dialogView)

                val alertDialog = builder.create()

                var okbutton:Button =dialogView.findViewById(R.id.Login)

                okbutton.setOnClickListener {
                    alertDialog.dismiss()
                    var intent=Intent(requireActivity(),LandingPage::class.java)

                    intent.putExtra("NavigateToSignInPage","NavigateToSignInPage")

                    startActivity(intent)
                    requireActivity().finish()

                }
                var cancelbutton:Button=dialogView.findViewById(R.id.cancel)

                cancelbutton.setOnClickListener {
                    alertDialog.dismiss()
                    val intent = Intent(requireContext(), DashboardActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }



                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                alertDialog.setCancelable(false)

                alertDialog.show()

            }

        }catch (e:Exception)
        {
//            requireActivity().printToast("null")
        }

        try {
            username = requireActivity().getPrefeb("Username")



          EmployeeId= requireActivity().getPrefeb("Employeeid")

            UserDataid = requireActivity().getPrefeb("UserDataid")

            Skillid = requireActivity().getPrefeb("UserSkillid")
            Skillname = requireActivity().getPrefeb("UserSkillname")

            gender = requireActivity().getPrefeb("Usergender")


            Maritialstatus = requireActivity().getPrefeb("MaritialStatus")


            Mothertongue = requireActivity().getPrefeb("MotherTongue")



            dateofbirth = requireActivity().getPrefeb("DateofBirth")

            UserUniqueId=requireActivity().getPrefeb("UserUniqueId")

            Useremail = requireActivity().getPrefeb("Useremail")

            Skillid = requireActivity().getPrefeb("UserSkillid")

            UserUploadDoc=requireActivity().getPrefeb("UserUploaddoc")

            viewModel.GetUserSkillid = Skillid

            UserMobileNumber = requireActivity().getPrefeb("UserMobilenumber")

            viewModel.UserProfilePicture = requireActivity().getPrefeb("UserImageUrl")

            Userlocation = requireActivity().getPrefeb("Userlocation")



            binding.username.setText(username)

            if (Useremail.isNotEmpty())
            {
                binding.Useremail.setText(Useremail)
            }
            else
            {
                binding.Useremail.setText("Email")
            }

            if (dateofbirth.isNotEmpty())
            {
                binding.dobText.setText(dateofbirth)
            }
            else
            {
                binding.dobText.setText("Date Of Birth")
            }

            if (Userlocation.isNotEmpty())
            {
                binding.Userlocation.setText(Userlocation)
            }
            else
            {
                binding.Userlocation.setText("Location")
            }






            binding.uploadedFileText.text=UserUploadDoc

            binding.skills1.text = gender
            binding.skills2.text = Maritialstatus
            binding.MotherTongue.setText(Mothertongue)


            binding.skills.text = Skillname

            binding.UserMobileNumber.setText(UserMobileNumber)

            if (viewModel.UserProfilePicture.isNotEmpty())
            {
                Glide.with(requireContext()).load(viewModel.UserProfilePicture).into(binding.imageview)
            }
            else
            {
                binding.imageview.setImageResource(R.drawable.account)
            }





        }catch (e:NullPointerException)
        {

            println(e)
        }

        try {
            if (EmployeeId.isNotEmpty())
            {
                binding.empId.visibility=View.VISIBLE
                binding.empId.text= "Employee ID : $EmployeeId"

            }
            else
            {
                binding.empId.visibility=View.VISIBLE
                binding.empId.text= "User ID : TEMP/$UserUniqueId"
            }

        }catch (e:Exception)
        {
            println(e)
        }


        binding.UpdateSkill.setOnClickListener {
            (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
        }




//        binding.dobBtn.setOnClickListener{
//            var datePicker = DatePickerDialog(requireContext())
//            datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
//                var pickedDate = "$dayOfMonth-${String.format(Locale.ENGLISH,"%02d",(month+1))}-$year"
//                getDate(pickedDate ,"dd-MM-yyyy")
////                viewModel.dobDate = pickedDate//getDate(pickedDate ,"dd/MM/yyyy")!!
//
//                Log.e("viewModel.dobDate",viewModel.dobDate.toString())
//                binding.dobText.text= pickedDate
//            }
//
//            datePicker.show()
//        }

        binding.floatingActionButton.setOnClickListener {
//            filePickerLauncher.launch(arrayOf("application/pdf","image/*"))
            var docBottomSheet = ChoosePhotoTypeBottomSheet(object : ChoosePhotoTypeBottomSheet.FilePickerListener{
                override fun onFilePicked(uri: Uri?, filePath : String) {
                    uri?.let { fileUri->

                        viewModel.selectedFileUriforimage = fileUri
                        viewModel.selectedFilePathforimage = filePath


                        requireActivity().contentResolver.query(fileUri,null,null,null,null)?.use {
                                cursor ->

                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

//                            val fileSize = cursor.getDouble(fileSizeIndex)

                            cursor.moveToFirst()

                            Glide.with(requireContext())
                                .load(viewModel.selectedFileUriforimage)
                                .into(binding.imageview)
//                            binding.uploadedFileText.text = cursor.getString(nameIndex)
                        }

                        Log.e("Picked file"," file name : $filePath" )


                    }
                }
            })

            docBottomSheet.show(childFragmentManager,"doc bottom sheet")
        }

//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                val fragmentManager = requireActivity().supportFragmentManager
//                for (i in 0 until fragmentManager.backStackEntryCount) {
//                    fragmentManager.popBackStack()
//                }
//
//                var Intent=Intent(requireActivity(),DashboardActivity::class.java)
//                startActivity(Intent)
//            }
//        }
//
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.imagepickar.setOnClickListener {
//            filePickerLauncher.launch(arrayOf("application/pdf","image/*"))
            var docBottomSheet = ChoosePhotoTypeBottomSheet(object : ChoosePhotoTypeBottomSheet.FilePickerListener{
                override fun onFilePicked(uri: Uri?, filePath : String) {
                    uri?.let { fileUri->

                        viewModel.selectedFileUriforimage = fileUri
                        viewModel.selectedFilePathforimage = filePath


                        requireActivity().contentResolver.query(fileUri,null,null,null,null)?.use {
                                cursor ->

                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

//                            val fileSize = cursor.getDouble(fileSizeIndex)

                            cursor.moveToFirst()

                            Glide.with(requireContext())
                                .load(viewModel.selectedFileUriforimage)
                                .into(binding.imageview)
//                            binding.uploadedFileText.text = cursor.getString(nameIndex)
                        }

                        Log.e("Picked file"," file name : $filePath" )


                    }
                }
            })

            docBottomSheet.show(childFragmentManager,"doc bottom sheet")
        }

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

                        requireActivity().showalertdilog(message)
                        Log.e("responcemessage",message)
                    }catch (e:Exception)
                    {
                        println(e)
                    }


                    viewModel.getSkillsResponse.postValue(null)

                }

                else -> {

                }
            }
        }

        binding.skillContainer.setOnClickListener {
            binding.skillList.visibility=if(binding.skillList.isVisible) View.GONE else View.VISIBLE
        }

//        binding.skillContainer1.setOnClickListener {
//            binding.skillList1.visibility=if(binding.skillList1.isVisible) View.GONE else View.VISIBLE
//        }
//        binding.skillContainer2.setOnClickListener {
//            binding.skillList2.visibility=if(binding.skillList2.isVisible) View.GONE else View.VISIBLE
//        }


        binding.skillList.apply {
            adapter = ItemAdapter(viewModel.skillList, object : Signlistener {
                override fun onItemClicked(pos: Int) {
//                    binding.skillList.visibility = View.GONE
                    binding.skills.text = ""
                    viewModel.skillList.forEach {
                        if (it!!.ischecked) {
                            binding.skills.text =
                                binding.skills.text.toString() + it!!.skillTitle + ", "
                        }

                    }

                }

            })
            layoutManager = LinearLayoutManager(requireContext())
        }


        binding.skillList1.apply {
            adapter = GenderAdapter(requireContext(),ganderlist, object : genderclickListnerinterface {
                override fun onItemClicked(pos: Int, data: String) {
                    binding.skillList1.visibility = View.GONE
                    binding.skills1.text = ""

                    binding.skills1.text = data
                }


            })
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.skillList2.apply {
            adapter =
                listAdapter(requireContext(),maritalstatus, object : genderclickListnerinterface {
                    override fun onItemClicked(pos: Int, data: String) {
                        binding.skillList2.visibility = View.GONE
                        binding.skills2.text = ""

                        binding.skills2.text = data
                    }


                })
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.fileUpload.setOnClickListener {
//            filePickerLauncher.launch(arrayOf("application/pdf","image/*"))
            var docBottomSheet = ChooseFileTypeBottomSheet(object : ChooseFileTypeBottomSheet.FilePickerListener{
                override fun onFilePicked(uri: Uri?, filePath : String) {
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

        binding.UpdateProfile.setOnClickListener {

            var documenttext=binding.uploadedFileText.text.toString()
            var lungage=binding.MotherTongue.text.toString()
            var skill=binding.skills.text.toString()

            if (lungage.equals(Mothertongue) && documenttext.equals(UserUploadDoc) && skill.equals(Skillname)
                && viewModel.selectedFilePathforimage.isEmpty())
            {
                requireActivity().printToast("Please Update Your Profile  ")
            }
            else
            {
                if (UserDataid.isNotEmpty())

                {
                    Userid=UserDataid.toInt()
                }

                if (viewModel.selectedFilePath.isNotEmpty())

                {

                    if (viewModel.fileSize<=2048)
                    {

                        try {
                            var url=viewModel.selectedFileUriforimage.toString()
                            var path=viewModel.selectedFilePathforimage

                            if (url.isNotEmpty() && path.isNotEmpty())
                            {
                                if (UserDataid.isNotEmpty())
                                {
                                    val userid=UserDataid.toInt()

                                    updatephoto=true

                                    viewModel.UpdateProfile(requireContext(),userid,viewModel.selectedFilePathforimage,viewModel.selectedFileUriforimage!!)


                                }
                            }
                            else
                            {
//                    requireActivity().printToast("null")
                            }

                        }catch (e:Exception)
                        {
                            println(e)
                        }
                        viewModel.uploadImage(requireActivity(),Userid,viewModel.selectedFilePath!!,viewModel.selectedFileUri!!)

                        var document=binding.uploadedFileText.text.toString()

                        val Skill=binding.skills.text.toString()

                        viewModel.dobDate=binding.dobText.text.toString()

                        var Usename=binding.username.text.toString()

                        var location=binding.Userlocation.text.toString()

                        var gender=binding.skills1.text.toString()

                        var maritialstatus=binding.skills2.text.toString()

                        var MotherTongue=binding.MotherTongue.text.toString()

                        try {

                            accesstoken=requireActivity().getPrefeb("UserToken")

                            if (accesstoken.isNotEmpty() && Usename.isNotEmpty() && location.isNotEmpty()  && Skill.isNotEmpty())

                            {
                                val ApplyToken = "Bearer $accesstoken"

                                viewModel.UpdateUser(ApplyToken,Usename,location, gender,maritialstatus,MotherTongue)

                            }
                            else
                            {
                                requireActivity().printToast("Somethings Went Wrong")
                            }

                        }catch (e:NullPointerException)
                        {

                            println(e)
//                requireActivity().printToast("null")
                        }

                    }
                    else
                    {
                        requireActivity().showalertdilog("Please Upload Your Resume Less Than 2 MB.")
                    }


                }
                else
                {

                    try {
                        var url=viewModel.selectedFileUriforimage.toString()
                        var path=viewModel.selectedFilePathforimage

                        if (url.isNotEmpty() && path.isNotEmpty())
                        {
                            if (UserDataid.isNotEmpty())
                            {
                                val userid=UserDataid.toInt()

                                updatephoto=true

                                viewModel.UpdateProfile(requireContext(),userid,viewModel.selectedFilePathforimage,viewModel.selectedFileUriforimage!!)


                            }
                        }
                        else
                        {
//                    requireActivity().printToast("null")
                        }

                    }catch (e:Exception)
                    {
                        println(e)
                    }

                    var document=binding.uploadedFileText.text.toString()

                    val Skill=binding.skills.text.toString()

                    viewModel.dobDate=binding.dobText.text.toString()

                    var Usename=binding.username.text.toString()

                    var location=binding.Userlocation.text.toString()

                    var gender=binding.skills1.text.toString()

                    var maritialstatus=binding.skills2.text.toString()

                    var MotherTongue=binding.MotherTongue.text.toString()

                    try {

                        accesstoken=requireActivity().getPrefeb("UserToken")

                        if (accesstoken.isNotEmpty() && Usename.isNotEmpty() && location.isNotEmpty()  && Skill.isNotEmpty())

                        {
                            val ApplyToken = "Bearer $accesstoken"

                            viewModel.UpdateUser(ApplyToken,Usename,location, gender,maritialstatus,MotherTongue)

                        }
                        else
                        {
                            requireActivity().printToast("Somethings Went Wrong")
                        }

                    }catch (e:NullPointerException)
                    {

                        println(e)
//                requireActivity().printToast("null")
                    }

                }







            }



        }

        progressDialog = requireActivity().getProgressDialog()

        viewModel.updateUserResponse.observe(viewLifecycleOwner) {


            when (it) {
                is NetworkResult.Loading ->{
                    progressDialog?.let {
                        it.show()
                    }

                }
                is NetworkResult.Success -> {

                    if (!updatephoto)
                    {
                        progressDialog?.dismiss()


                        if ( it.data!!.status==true)

                        {
                            requireActivity().showalertdilog("Update Successfully")
                        }
                    }


                    viewModel.updateUserResponse.postValue(null)

                }
                is NetworkResult.Error ->{

                    requireActivity().showalertdilog(it.message.toString())

                    viewModel.updateUserResponse.postValue(null)

                }

                else -> {

                }
            }
        }

        progressDialog = requireActivity().getProgressDialog()

        viewModel.uploadResponse.observe(viewLifecycleOwner) {

            progressDialog?.let {
                it.dismiss()
            }

            when (it) {
                is NetworkResult.Loading ->{
                    progressDialog?.let {
                        it.show()
                    }


                }
                is NetworkResult.Success -> {

//                  requireActivity().printToast("Success")

                    viewModel.uploadResponse.postValue(null)

                }
                is NetworkResult.Error ->{

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

        viewModel.updateprofileResponse.observe(viewLifecycleOwner) {


            when (it) {
                is NetworkResult.Loading ->{


                }
                is NetworkResult.Success -> {

                    progressDialog?.let {
                        it.dismiss()
                    }

                    if ( it.data!!.status==true)

                    {
                        requireActivity().showalertdilog("Update Successfully")
                    }


//                    requireActivity().printToast("Success")

                    viewModel.updateprofileResponse.postValue(null)

                }
                is NetworkResult.Error ->{

                    requireActivity().showalertdilog(it.message.toString())

                    viewModel.uploadResponse.postValue(null)

                }

                else -> {

                }
            }
        }



    }


    override fun onResume() {
        super.onResume()

        try {
            userlogin = requireActivity().getPrefeb("UserToken")

            if (userlogin.isEmpty())
            {
                binding.inputBox.visibility=View.GONE
                binding.imagepickar.visibility=View.GONE
                binding.empId.visibility=View.GONE
                binding.header.visibility=View.GONE

                val builder = AlertDialog.Builder(requireContext())
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                builder.setView(dialogView)

                val alertDialog = builder.create()

                var okbutton: Button =dialogView.findViewById(R.id.Login)

                okbutton.setOnClickListener {
                    alertDialog.dismiss()
                    var intent=Intent(requireActivity(),LandingPage::class.java)

                    intent.putExtra("NavigateToSignInPage","NavigateToSignInPage")

                    startActivity(intent)
                    requireActivity().finish()

                }
                var cancelbutton: Button =dialogView.findViewById(R.id.cancel)

                cancelbutton.setOnClickListener {
                    alertDialog.dismiss()
                    val intent = Intent(requireContext(), DashboardActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }



                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                alertDialog.setCancelable(false)

                alertDialog.show()

            }

        }catch (e:Exception)
        {
//            requireActivity().printToast("null")
        }


    }

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

    class listAdapter(val context: Context, val list: ArrayList<String>, val itemClick: genderclickListnerinterface) :
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

    class GenderAdapter(val context: Context, val list: ArrayList<String>, val itemClick: genderclickListnerinterface) :
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


}