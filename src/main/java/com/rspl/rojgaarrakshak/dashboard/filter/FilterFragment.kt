package com.rspl.rojgaarrakshak.dashboard.filter


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.dashboard.search_job.Adapter.FilterAdapter
import com.rspl.rojgaarrakshak.databinding.FragmentFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject


@AndroidEntryPoint
class FilterFragment : Fragment() {

    companion object {
        fun newInstance() = FilterFragment()
    }

    private lateinit var viewModel: FilterViewModel
    private lateinit var binding: FragmentFilterBinding
    lateinit var name: String
    lateinit var Cityname: String
    var Stateid: Int = 0
    private var progressDialog: Dialog? = null
    lateinit var stateAdapter: ArrayAdapter<String>
    lateinit var CityAdapter: ArrayAdapter<String>
    val list = arrayListOf<String>()
    val citylist = arrayListOf<String>()
    val selected_states = arrayListOf<Int>()
    var cityitemid: Int = 0
    var SelectedCityid: Int = 0
    lateinit var filteradapter: FilterAdapter
    var CityModellist = ArrayList<String>()


    lateinit var SelectCityName: String

    data class CityReqModel(val selected_states: List<Int>)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[FilterViewModel::class.java]
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterBinding.bind(view)

        name = ""
        Cityname = ""
        SelectCityName = ""
        CityModellist = arrayListOf<String>()

        progressDialog = requireActivity().getProgressDialog()

        viewModel.statesResponse.observe(viewLifecycleOwner) {


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
                    viewModel.stateList.addAll(data.states_data!!)
                    Log.e("Statename", viewModel.stateList.toString())

                    for (i in 0 until viewModel.stateList.size) {
                        name = viewModel.stateList[i].name!!

                        list.add(name)
                    }

                    Log.e("listname", list.toString())


                    stateAdapter.notifyDataSetChanged()
                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)

                }

                else -> {

                }
            }
        }


        stateAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item, list
            )

        binding.statesspinner.setAdapter(stateAdapter)
        binding.statesspinner.threshold = 1

        binding.statesspinner.setOnItemClickListener { parent, view, position, id ->

            val selectedItem = parent.getItemAtPosition(position) as String

            for (i in 0 until viewModel.stateList.size) {
                var name = viewModel.stateList.get(i).name

                if (selectedItem == name) {
                    Stateid = viewModel.stateList.get(i).id!!
//                    requireActivity().printToast(id.toString())
                }
            }

            selected_states.clear()
            selected_states.add(Stateid)


            viewModel.getcity(CityReqModel(selected_states))


//            requireActivity().printToast(selectedItem)
        }

        CityAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, citylist)
        binding.Citysspinner.setAdapter(CityAdapter)
        binding.Citysspinner.threshold = 1

        binding.Citysspinner.setOnItemClickListener { parent, view, position, id ->
            SelectCityName = parent.getItemAtPosition(position) as String

            for (i in 0 until viewModel.citylist.size) {
                var name = viewModel.citylist.get(i).name

                if (SelectCityName == name) {
                    SelectedCityid = viewModel.citylist.get(i).id!!
                }
            }
        }

        viewModel.cityResponce.observe(viewLifecycleOwner) {

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
                    viewModel.citylist.clear()
                    Log.e("CityResponce", viewModel.cityResponce.toString())
                    viewModel.citylist.addAll(data.citiesData!!)
//                    viewModel.citylist.addAll(data.citiesData!!)

                    Log.e("CityList", viewModel.citylist.toString())

                    citylist.clear()
                    viewModel.citylist.forEach {
                        citylist.add(it.name.toString())
                    }

                    citylist.add(0, "Select Your City")
                    binding.Citysspinner.setAdapter(CityAdapter)

                }

                is NetworkResult.Error -> {

                    requireActivity().showalertdilog(it.message!!)
                }
            }
        }

        binding.backBtn.setOnClickListener {
            (requireActivity() as DashboardActivity).onBackBtnPressed()
        }

        binding.filterbutton.setOnClickListener {


            val statespiner = binding.statesspinner.text.toString()
            val cityspiner = binding.Citysspinner.text.toString()


            if (list.contains(statespiner) && citylist.contains(cityspiner)) {
                SaveFilterData(SelectCityName, SelectedCityid)
            } else {
                requireActivity().printToast("Please Provide Valid Input")
            }

        }


    }

    private fun SaveFilterData(selectCityName: String, cityitemid: Int) {

        val filterArray = requireActivity().getPrefeb("filter_data")

        if (filterArray.contains(selectCityName)) {
            requireContext().showalertdilog("$selectCityName City is already selected")
        } else {

            (requireActivity() as DashboardActivity).gotoPage(R.id.searchJobFragment, null)

            var newObj = JSONObject()


//        var cityname:String="",val cityid:Int=0
            newObj.put("city_name", selectCityName)
            newObj.put("city_id", cityitemid)
            if (filterArray.isEmpty()) {
                var cityArray = JSONArray()


                cityArray.put(newObj)
                requireActivity().putPrefeb("filter_data", cityArray.toString())
            } else {
                var cityArray = JSONArray(filterArray)
                cityArray.put(newObj)
                requireActivity().putPrefeb("filter_data", cityArray.toString())
            }

        }
    }
}