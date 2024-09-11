package com.rspl.rojgaarrakshak.landing.UserLocation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aqube.ram.extension.makeVisible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.StateAdapterLisner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentUserLocationBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import com.rspl.rojgaarrakshak.landing.Skill.CityAdapter
import com.rspl.rojgaarrakshak.landing.Skill.LocationCityAdapter
import com.rspl.rojgaarrakshak.landing.Skill.StateAdapter
import com.rspl.rojgaarrakshak.models.CityModelList
import com.rspl.rojgaarrakshak.models.SkillCityModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class UserLocation : Fragment(), CityAdapter.BtnClickListener {
    companion object {
        fun newInstance() = UserLocation()
    }

    lateinit var binding: FragmentUserLocationBinding
    lateinit var accesstoken: String
    lateinit var StateAdapter: StateAdapter
    var useraction=false
    var stateresponse=false
    var cityresponse=false
    lateinit var popupWindow:PopupWindow
    lateinit var CitypopupWindow:PopupWindow
    private var citymodellist: ArrayList<SkillCityModel>? = null

    val citylist = arrayListOf<CityModelList>()
    val idlist = arrayListOf<String>()

    val FinalCityid = arrayListOf<Int>()
    lateinit var Cityidlist: String
    lateinit var UserSkillId:String
    lateinit var UserSubSkillid:String

    lateinit var cityid: String
    lateinit var UserCityId:String
    lateinit var filterAdapter: CityAdapter

    lateinit var CityAdapter: LocationCityAdapter


    var Stateid: Int = 0
    lateinit var newRemovecityid: String

    var selectedCityResponse= arrayListOf<Int>()

    var SelectedCityid: Int = 0
    lateinit var newCityidlist: String
    var Userselectedstate = arrayListOf<Int>()

    var UserSelectedCityId = arrayListOf<Int>()
    lateinit var selectcityid:String
    lateinit var viewmodel: UserLocationViewModel
     lateinit var  StateRecyclearView:RecyclerView
    val selected_states = arrayListOf<Int>()
    val user_selected_state = arrayListOf<String>()
    val StateNamelist = arrayListOf<String>()
    private var progressDialog: Dialog? = null
    lateinit var StateName: String

    data class CityReqModel(val selected_states: List<Int>)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserLocationBinding.bind(view)

        viewmodel = ViewModelProvider(this)[UserLocationViewModel::class.java]

        progressDialog = requireActivity().getProgressDialog()

        if (requireActivity() is LandingPage)
        {
            (requireActivity() as LandingPage).enableBackBtn(false)
        }

        citymodellist = arrayListOf()
        accesstoken=""
        UserCityId=""
        UserSkillId=""
        selectcityid=""
        UserSubSkillid=""
        StateName = ""
        cityid = ""
        Cityidlist = ""
        newCityidlist = ""
        newRemovecityid = ""

        Log.e("citymodellist", citymodellist.toString())

        try {

            val json1 = requireContext().getPrefeb("UserSelectedStateid")

            val gson1= Gson()

            val listType1 = object : TypeToken<ArrayList<Int>>() {}.type
            val list1 = gson1.fromJson<ArrayList<Int>>(json1, listType1)

            Userselectedstate = list1 ?: ArrayList()

            if (Userselectedstate.isNotEmpty())

            {
                viewmodel.getSaveCity(CityReqModel(Userselectedstate))
            }

            val json2 = requireContext().getPrefeb("UserSelectedCityResponseid")

            val gson= Gson()

            val listType = object : TypeToken<ArrayList<Int>>() {}.type
            val list = gson.fromJson<ArrayList<Int>>(json2, listType)

            selectedCityResponse = list ?: ArrayList()


        }catch (e:Exception)
        {
            println(e)
        }

        viewmodel.allSaveCityResponse.observe(viewLifecycleOwner)
        {
            progressDialog?.let {
                it.dismiss()
            }

            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.let {
                        it.show()
                    }

                }

                is NetworkResult.Success -> {
                    val data = it.data!!

                    Log.e("datatocitylist", data.toString())
                    viewmodel.saveCitydata.clear()

                    viewmodel.saveCitydata.addAll(data.citiesData!!)

                    Log.e("CityList", viewmodel.saveCitydata.toString())

                        val filteredCityList = viewmodel.saveCitydata.filter { selectedCityResponse.contains(it.id) }

                        filteredCityList.forEach {

                            SaveFilterData(it.name!!,it.id!!)

                        }



                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)
                }
            }
        }





        viewmodel.getAllState()

        binding.autoCompleteState.inputType = EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        binding.autoCompleteCity.inputType = EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        viewmodel.allStateResponse.observe(viewLifecycleOwner) {
            progressDialog?.dismiss()

            when (it) {

                is NetworkResult.Loading -> {

                    progressDialog?.show()

                }

                is NetworkResult.Success -> {

                    val data = it.data!!
                    viewmodel.stateList.addAll(data.states_data!!)
                    Log.e("Statename", viewmodel.stateList.toString())

                    for (i in 0 until viewmodel.stateList.size) {
                        StateName = viewmodel.stateList[i].name!!

                        StateNamelist.add(StateName)
                    }
                    StateAdapter =
                        StateAdapter(requireActivity(), StateNamelist, object : StateAdapterLisner {
                            override fun onclick(pos: Int, Name: String) {

                                for (i in 0 until viewmodel.stateList.size) {
                                    var name = viewmodel.stateList.get(i).name

                                    if (Name == name) {
                                        Stateid = viewmodel.stateList.get(i).id!!

                                    }
                                }

                                selected_states.clear()

                                selected_states.add(Stateid)

                                user_selected_state.add(Stateid.toString())

                                val newstateid=user_selected_state.toString()

                                val newreplaceid=newstateid.replace("[","")

                                val replaceid=newreplaceid.replace("]","")

                                selectcityid=replaceid

                                viewmodel.getAllCity(CityReqModel(selected_states))

                                binding.autoCompleteState.setText(Name)


//                                binding.statelistlayout.visibility=View.GONE

                                popupWindow.dismiss()
                            }

                        })

                    val view = LayoutInflater.from(requireContext()).inflate(R.layout.popup_layout, null)
                    val recyclerView = view.findViewById<RecyclerView>(R.id.popupRecyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = StateAdapter

                    val dpValue = 250
                    val density = resources.displayMetrics.density
                    val pixels = (dpValue * density).toInt()
                    popupWindow = PopupWindow(view, pixels, ViewGroup.LayoutParams.WRAP_CONTENT, true)

                    stateresponse=true

                    popupWindow.showAsDropDown(binding.stateEdittext)
                    popupWindow.dismiss()

//                     binding.stateListRecyclear.adapter=StateAdapter
//                     binding.stateListRecyclear.layoutManager=LinearLayoutManager(requireContext())

                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)

                }
            }

        }

        try {
            val jsonArray: String = requireActivity().getPrefeb("Skill_data")

            if (jsonArray.isNotEmpty()) {
                var cityArray = JSONArray(jsonArray)

                idlist.clear()
                for (i in 0 until cityArray.length()) {
                    var jsonObj = JSONObject(cityArray[i].toString())

                    cityid = jsonObj.getString("skill_id")

                    idlist.add(cityid)

                }
                Cityidlist = idlist.toString()

                Log.e("Cityitemidname", Cityidlist)

                newCityidlist = Cityidlist.replace("[", "")
                newRemovecityid = newCityidlist.replace("]", "")

                Log.e("newRemovecityid", newRemovecityid)

            }
        } catch (e: NullPointerException) {
            println(e)
        }

        try {
            val jsonArray: String = requireActivity().getPrefeb("Skill_data")

            if (jsonArray.isNotEmpty()) {
                var cityArray = JSONArray(jsonArray)


                citymodellist!!.clear()

                for (i in 0 until cityArray.length()) {
                    var jsonObj = JSONObject(cityArray[i].toString())
                    var model =
                        SkillCityModel(jsonObj.getString("skill_name"), jsonObj.getInt("skill_id"))
                    citymodellist!!.add(model)


                }
            }



            filterAdapter = CityAdapter(citymodellist!!, this)

//             mainmodel.add(MainModels(citymodellist!!))
            filterAdapter.notifyDataSetChanged()




            var list=  filterAdapter.getCityList()

            FinalCityid.clear()
            for (i in list.indices)
            {
                var cityid=list[i].cityid
                FinalCityid.add(cityid)
            }
            val listcityid = FinalCityid.toString()
            val rcityid = listcityid.replace("[", "")
            val recityid = rcityid.replace("]", "")



            requireActivity().putPrefeb("CityLocationUserid",recityid)



            binding.SkillItemRecyclear.adapter = filterAdapter

            Log.e("citymodellistjson", citymodellist.toString())
        } catch (e: NullPointerException) {
            println(e)
        }

        viewmodel.allCityResponse.observe(viewLifecycleOwner)
        {
            progressDialog?.let {
                it.dismiss()
            }

            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.let {
                        it.show()
                    }

                }

                is NetworkResult.Success -> {
                    val data = it.data!!

                    Log.e("datatocitylist", data.toString())
                    viewmodel.citylist.clear()

                    viewmodel.citylist.addAll(data.citiesData!!)
//                    viewModel.citylist.addAll(data.citiesData!!)

                    Log.e("CityList", viewmodel.citylist.toString())

                    citylist.clear()
//                    citymodellist!!.clear()
                    viewmodel.citylist.forEach {
                        citylist.add(CityModelList(it.name!!,it.id!!))

//                        if (selectedCityResponse.contains(it.id))
//                        {
//                            requireActivity().printToast(it.name!!)
//                            requireActivity().printToast(it.id.toString())
//
//                            var model =
//                                SkillCityModel(it.name!!,it.id!!)
//                            citymodellist!!.add(model)
//                        }
//
//                        requireActivity().printToast(citymodellist.toString())
//                        filterAdapter = CityAdapter(citymodellist!!, this)
//
//                        filterAdapter.notifyDataSetChanged()
//                        binding.SkillItemRecyclear.adapter=filterAdapter
                    }

                    CityAdapter = LocationCityAdapter(
                        requireContext(),
                        citylist,
                        object : StateAdapterLisner {
                            override fun onclick(pos: Int, Name: String) {

                                useraction=true

                                binding.autoCompleteState.text.clear()
                                binding.autoCompleteCity.text.clear()
                                popupWindow.dismiss()
                                CitypopupWindow.dismiss()
                                for (i in 0 until viewmodel.citylist.size) {
                                    var name = viewmodel.citylist.get(i).name

                                    if (Name == name) {
                                        SelectedCityid = viewmodel.citylist.get(i).id!!

                                        SaveFilterData(Name, SelectedCityid)

                                    }
                                }


//                                binding.cityListRecyclear.visibility=View.GONE
//                                binding.statelistlayout.visibility=View.GONE



                            }
                        })

//                    binding.cityListRecyclear.adapter=CityAdapter
//                    binding.cityListRecyclear.layoutManager=LinearLayoutManager(requireContext())

                    val view = LayoutInflater.from(requireContext()).inflate(R.layout.city_popup_layout, null)
                    val recyclerView = view.findViewById<RecyclerView>(R.id.citypopuplayout)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = CityAdapter

                    val dpValue = 250
                    val density = resources.displayMetrics.density
                    val pixels = (dpValue * density).toInt()
                    CitypopupWindow = PopupWindow(view, pixels, ViewGroup.LayoutParams.WRAP_CONTENT, true)

                    cityresponse=true

                    CitypopupWindow.showAsDropDown(binding.selectCityLInier)
                    CitypopupWindow.dismiss()



                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)
                }
            }
        }


        binding.statedropdown.setOnClickListener {

            binding.autoCompleteState.text.clear()

            toggleListViewVisibility()
        }

        binding.backBtn.setOnClickListener {

            try {

                if (requireActivity() is LandingPage)
                {

                    if (cityresponse)
                    {

                        CitypopupWindow.dismiss()
                        (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                    }
                    else if(stateresponse){
                        popupWindow.dismiss()
                        (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                    }
                    else if (stateresponse && cityresponse)
                    {
                        CitypopupWindow.dismiss()
                        popupWindow.dismiss()
                        (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                    }
                    else
                    {
                        (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                    }

                }
                else
                {

                    if (cityresponse)
                    {

                        CitypopupWindow.dismiss()
                        (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                    }
                    else if(stateresponse)
                    {
                        popupWindow.dismiss()
                        (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                    }
                    else if(stateresponse && cityresponse)
                    {
                        CitypopupWindow.dismiss()
                        popupWindow.dismiss()
                        (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                    }
                    else
                    {
                        (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                    }

                }

            }catch (e:Exception)
            {
                println(e)
            }

        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                try {


                    if (requireActivity() is LandingPage)
                    {

                        if (cityresponse)
                        {

                            CitypopupWindow.dismiss()
                            (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                        }
                        else if (stateresponse)
                        {
                            popupWindow.dismiss()
                            (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                        }
                        else if(stateresponse && cityresponse)
                        {
                            popupWindow.dismiss()
                            CitypopupWindow.dismiss()
                            (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                        }
                        else
                        {
                            (requireActivity() as LandingPage).gotoPage(R.id.skillFregment,null,false)
                        }


                    }
                    else
                    {

                        if (cityresponse)
                        {

                            CitypopupWindow.dismiss()
                            (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                        }
                        else if(stateresponse)
                        {
                            popupWindow.dismiss()
                            (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                        }
                        else if(stateresponse && cityresponse)
                        {
                            popupWindow.dismiss()
                            CitypopupWindow.dismiss()
                            (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                        }
                        else
                        {
                            (requireActivity() as DashboardActivity).gotoPage(R.id.skillFregment2,null)
                        }



                    }

                }catch (e:Exception)
                {
                    println(e)
                }




            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.Citydropdown.setOnClickListener {

            if (binding.autoCompleteState.text.isEmpty())
            {
                requireActivity().printToast("Please Select State First.")
            }
            else
            {
                toggleListViewVisibilityCity()
            }

        }



           binding.autoCompleteCity.addTextChangedListener(object : TextWatcher {
               override fun beforeTextChanged(
                   charSequence: CharSequence?,
                   start: Int,
                   count: Int,
                   after: Int
               ) {

               }

               override fun onTextChanged(
                   charSequence: CharSequence?,
                   start: Int,
                   before: Int,
                   count: Int
               ) {
                   try {

                           if (charSequence.toString().isEmpty())
                           {
                               CitypopupWindow.dismiss()
                           }
                           else
                           {
                               CitypopupWindow.isFocusable=false
                               CitypopupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
                               CitypopupWindow.showAsDropDown(binding.selectCityLInier)
                               CityAdapter.filter(charSequence.toString())
                           }



                   }catch (e:Exception)
                   {
                       println(e)
                   }

               }

               override fun afterTextChanged(editable: Editable?) {





               }
           })





        binding.autoCompleteState.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                try {

                        popupWindow.isFocusable=false
                        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
                        popupWindow.showAsDropDown(binding.stateEdittext)
                        StateAdapter.filter(charSequence.toString())



                }catch (e:Exception)
                {
                    println(e)
                }



//                    binding.statelistlayout.visibility=View.GONE
//                    statePopupVisible(false)




//                binding.statelistlayout.visibility=View.VISIBLE
//                statePopupVisible(true)



            }

            override fun afterTextChanged(editable: Editable?) {

                try {



                }catch (e:Exception)
                {
                    println(e)
                }





            }
        })

        binding.ViewJob.setOnClickListener {


            if (!useraction)
            {
               requireActivity().printToast("Please enter Location")
            }
            else
            {
                try {

                    UserCityId = requireActivity().getPrefeb("CityLocationUserid")
                    UserSkillId = requireActivity().getPrefeb("UserMainSkillid")
                    UserSubSkillid = requireActivity().getPrefeb("UserLocationSubSkillid")

                    val cityIds = UserCityId.split(",")

                    if (cityIds.size < 3) {
                        requireActivity().showalertdilog("Please Select Atleast 3 Location")
                    } else {

                        if (UserSkillId.isNotEmpty() && UserSubSkillid.isNotEmpty() && UserCityId.isNotEmpty()) {

                            try {
                                accesstoken = requireActivity().getPrefeb("UserToken")
                                if (accesstoken.isNotEmpty()) {
                                    val ApplyToken = "Bearer $accesstoken"
                                    viewmodel.saveSkillPrefrences(
                                        ApplyToken,
                                        UserSkillId,
                                        UserSubSkillid,
                                        UserCityId,
                                        selectcityid
                                    )

                                }
                            } catch (e: NullPointerException) {
                                println(e)
                            }
                        }
                        else {


                            if (UserCityId.isEmpty() && UserSubSkillid.isEmpty() && UserSkillId.isEmpty())
                            {

                                if (requireActivity() is LandingPage)
                                {
                                    val builder = AlertDialog.Builder(requireContext())
                                    val inflater = layoutInflater
                                    val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                                    builder.setView(dialogView)

                                    val alertDialog = builder.create()

                                    var okbutton: Button = dialogView.findViewById(R.id.Login)

                                    var messagetext: TextView = dialogView.findViewById(R.id.messagetext)

                                    messagetext.text = "Are You sure Want to Continue Without Skill"

                                    okbutton.text = "OK"

                                    okbutton.setOnClickListener {
                                        alertDialog.dismiss()

                                        (requireActivity() as LandingPage).gotoPage(R.id.uploadImage,null,false)

                                    }
                                    var cancelbutton: Button = dialogView.findViewById(R.id.cancel)

                                    cancelbutton.setOnClickListener {
                                        alertDialog.dismiss()

                                    }



                                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                    alertDialog.setCancelable(false)

                                    alertDialog.show()
                                }
                                else
                                {
                                    requireActivity().showalertdilog("No Changes Applied")
                                }

                            }
                            else
                            {
                                try {

                                    accesstoken = requireActivity().getPrefeb("UserToken")

                                    if (accesstoken.isNotEmpty()) {
                                        val ApplyToken = "Bearer $accesstoken"
                                        viewmodel.saveSkillPrefrences(
                                            ApplyToken,
                                            UserSkillId,
                                            UserSubSkillid,
                                            UserCityId,
                                            selectcityid
                                        )

                                    }
                                } catch (e: NullPointerException) {
                                    println(e)
                                }
                            }

                        }

                    }




                } catch (e: Exception) {
                    println(e)
                }
            }

        }


        viewmodel.saveSkillResponse.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is NetworkResult.Loading ->{

                    progressDialog?.show()

                }
                is NetworkResult.Success ->{

                    progressDialog?.dismiss()

                    val data=it.data!!

                    if (data.status==true)
                    {

                        if (requireActivity() is LandingPage)
                        {
                            (requireActivity() as LandingPage).gotoPage(R.id.uploadImage,null,false)
                        }
                        else
                        {
                            requireActivity().showalertdilog("Preferences Saved Successfully")
                        }

                    }

                }
                is NetworkResult.Error ->{

                    requireActivity().showalertdilog(it.message.toString())

                    progressDialog?.dismiss()

                }
            }
        }

    }

    private fun toggleListViewVisibility() {

        try {

            if (popupWindow.isShowing) {

                popupWindow.dismiss()
            } else {

                popupWindow.showAsDropDown(binding.stateEdittext)
            }

        }catch (e:Exception)
        {
            println(e)
        }


    }


    private fun toggleListViewVisibilityCity() {
        if (CitypopupWindow.isShowing) {
         CitypopupWindow.dismiss()

        } else {

           CitypopupWindow.showAsDropDown(binding.selectCityLInier)
        }
    }

    private fun statePopupVisible(status:Boolean)
    {
        try {

            val view = LayoutInflater.from(requireContext()).inflate(R.layout.popup_layout, null)

            view.makeVisible(status)

        }catch (e:Exception)
        {
            println(e)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun SaveFilterData(selectCityName: String, selectedCityid: Int) {

        val filterArray = requireActivity().getPrefeb("Skill_data")

        if (filterArray.contains(selectCityName)) {
//            requireContext().showalertdilog("$selectCityName City is already selected")
        } else {


            var newObj = JSONObject()

            newObj.put("skill_name", selectCityName)
            newObj.put("skill_id", selectedCityid)
            if (filterArray.isEmpty()) {
                var cityArray = JSONArray()


                cityArray.put(newObj)

                requireActivity().putPrefeb("Skill_data", cityArray.toString())
            } else {
                var cityArray = JSONArray(filterArray)
                cityArray.put(newObj)


                requireActivity().putPrefeb("Skill_data", cityArray.toString())
            }

        }


        try {
            val jsonArray: String = requireActivity().getPrefeb("Skill_data")

            if (jsonArray.isNotEmpty()) {
                var cityArray = JSONArray(jsonArray)


                citymodellist!!.clear()

                for (i in 0 until cityArray.length()) {
                    var jsonObj = JSONObject(cityArray[i].toString())
                    var model =
                        SkillCityModel(jsonObj.getString("skill_name"), jsonObj.getInt("skill_id"))
                    citymodellist!!.add(model)


                }
            }



            filterAdapter = CityAdapter(citymodellist!!, this)

//             mainmodel.add(MainModels(citymodellist!!))
            filterAdapter.notifyDataSetChanged()




          var list=  filterAdapter.getCityList()

             FinalCityid.clear()
            for (i in list.indices)
            {
                var cityid=list[i].cityid
                FinalCityid.add(cityid)
            }
            val listcityid = FinalCityid.toString()
            val rcityid = listcityid.replace("[", "")
            val recityid = rcityid.replace("]", "")



            requireActivity().putPrefeb("CityLocationUserid",recityid)



            binding.SkillItemRecyclear.adapter = filterAdapter

            Log.e("citymodellistjson", citymodellist.toString())
        } catch (e: NullPointerException) {
            println(e)
        }
        if (citymodellist == null) {
            citymodellist = ArrayList()
        }
        showRecentSearchesPanel()


        try {
            val jsonArray: String = requireActivity().getPrefeb("Skill_data")

            if (jsonArray.isNotEmpty()) {
                var cityArray = JSONArray(jsonArray)

                idlist.clear()
                for (i in 0 until cityArray.length()) {
                    var jsonObj = JSONObject(cityArray[i].toString())

                    cityid = jsonObj.getString("skill_id")

                    idlist.add(cityid)

                }
                Cityidlist = idlist.toString()

                Log.e("Cityitemidname", Cityidlist)

                newCityidlist = Cityidlist.replace("[", "")
                newRemovecityid = newCityidlist.replace("]", "")

                Log.e("newRemovecityid", newRemovecityid)

            }
        } catch (e: NullPointerException) {
            println(e)
        }
    }

    fun showRecentSearchesPanel() {
        try {
            if (binding != null) {
                Log.e(
                    "showRecentSearchesPanel",
                    "citymodellist!!.isNotEmpty() => ${citymodellist!!.isNotEmpty()}"
                );

                binding.SkillItemRecyclear.isVisible = citymodellist!!.isNotEmpty()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBtnClick(position: Int) {

        val preferences =
            context?.getSharedPreferences("amar_${context?.packageName}", Context.MODE_PRIVATE)
        val editor = preferences?.edit()

        val jsonArray: String = requireActivity().getPrefeb("Skill_data")
        var updateResult = ""
        if (jsonArray.isNotEmpty()) {
            var cityArray = JSONArray(jsonArray)
            cityArray.remove(position)
            updateResult = cityArray.toString()
        }
        requireActivity().putPrefeb("Skill_data", updateResult)

        citymodellist?.removeAt(position)
        filterAdapter.notifyDataSetChanged()

        var list=  filterAdapter.getCityList()

        FinalCityid.clear()
        for (i in list.indices)
        {
            var cityid=list[i].cityid
            FinalCityid.add(cityid)
        }

        val listcityid = FinalCityid.toString()
        val rcityid = listcityid.replace("[", "")
        val recityid = rcityid.replace("]", "")

        requireActivity().putPrefeb("CityLocationUserid",recityid)

        idlist.removeAt(position)

//        val listcityid = idlist.toString()
//        val rcityid = listcityid.replace("[", "")
//        val recityid = rcityid.replace("]", "")

//        showRecentSearchesPanel()
    }


}