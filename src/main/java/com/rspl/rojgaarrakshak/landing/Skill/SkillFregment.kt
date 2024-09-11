package com.rspl.rojgaarrakshak.landing.Skill

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.interfaces.SkillItemClickLisner
import com.rspl.rojgaarrakshak.core.printToast
import com.rspl.rojgaarrakshak.core.putPrefeb
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentSkillFregmentBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import com.rspl.rojgaarrakshak.models.GetSubSkillModel.PreferenceSubSkillsData
import com.rspl.rojgaarrakshak.models.SkillModel.PreferenceSkillsData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillFregment : Fragment() {

    companion object {
        fun newInstance() = SkillFregment()
    }
    lateinit var binding: FragmentSkillFregmentBinding
    lateinit var ViewModel: SkillViewModel
    var list: List<PreferenceSkillsData> = arrayListOf()
    private var adapter: ItemAdapter? = null
    var itemposition: Int = 0
    lateinit var accesstoken: String
    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_skill_fregment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSkillFregmentBinding.bind(view)

        ViewModel = ViewModelProvider(this)[SkillViewModel::class.java]

        accesstoken = ""


        val preferences =
            context?.getSharedPreferences("amar_${context?.packageName}", Context.MODE_PRIVATE)
        val editor = preferences?.edit()

        editor!!.remove("UserMainSkillid")
        editor!!.remove("UserLocationSubSkillid")
        editor.apply()

        progressDialog = requireActivity().getProgressDialog()

        ViewModel.getSKillData()
        binding.skillrecyclervie.setHasFixedSize(true)
        binding.skillrecyclervie.setLayoutManager(LinearLayoutManager(requireActivity()))

        try {

            accesstoken = requireActivity().getPrefeb("UserToken")

            if (accesstoken.isNotEmpty()) {
                val ApplyToken = "Bearer $accesstoken"

            }
        } catch (e: NullPointerException) {
            println(e)
        }


        ViewModel.getUserSkill.observe(viewLifecycleOwner) {

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

                    progressDialog?.dismiss()

                    var data = it.data

                    list = it.data!!.preferenceSkillsData

                    adapter = ItemAdapter(requireActivity(), list, object : SkillItemClickLisner {
                        override fun onClick(position: Int, id: String,skillname:String) {


                            requireActivity().putPrefeb("SelectedSkillName",skillname)

                            requireActivity().putPrefeb("UserMainSkillid", id)

                            itemposition = position

                            ViewModel.getUserSubSkill(id)
                        }
                    })
                    binding.skillrecyclervie.adapter = adapter

                }

                is NetworkResult.Error -> {

                    requireActivity().printToast(it.message.toString())

                    progressDialog?.dismiss()

                }
                else -> {

                }
            }
        }

        binding.backBtn.setOnClickListener {
            if (requireActivity() is LandingPage) {
                var intent = Intent(requireActivity(), DashboardActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                (requireActivity() as DashboardActivity).gotoPage(R.id.profileFragment, null)

            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (requireActivity() is LandingPage) {
                    var intent = Intent(requireActivity(), DashboardActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    (requireActivity() as DashboardActivity).gotoPage(R.id.profileFragment, null)

                }


            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        ViewModel.UserSubSkillResponse.observe(viewLifecycleOwner) {

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

                    progressDialog?.dismiss()
                      var data = it.data!!.preferenceSubSkillsData
                       val position = itemposition
                      adapter?.updateSubSkillsData(position, data)

                    if (data.isEmpty())
                    {

                          var skillname=requireActivity().getPrefeb("SelectedSkillName")
                          data.add(PreferenceSubSkillsData(0,skillname))

                         val position = itemposition

                         adapter?.updateSubSkillsData(position, data)
                    }

                }

                is NetworkResult.Error -> {

                    requireActivity().printToast(it.message.toString())

                    progressDialog?.dismiss()
                }

                else -> {

                }
            }
        }

        binding.NextPage.setOnClickListener {
            try {
                var UserSkillid = requireActivity().getPrefeb("UserMainSkillid")
                var UserSubSkillid = requireActivity().getPrefeb("UserLocationSubSkillid")



                if (UserSkillid.isNotEmpty() && UserSubSkillid.isNotEmpty()) {
                    if (requireActivity() is DashboardActivity) {
                        (requireActivity() as DashboardActivity).gotoPage(R.id.userLocation, null)

                    } else {
                        (requireActivity() as LandingPage).gotoPage(R.id.userLocation2, null, true)

                    }

                } else {
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

                        if (requireActivity() is DashboardActivity) {
                            (requireActivity() as DashboardActivity).gotoPage(
                                R.id.userLocation,
                                null
                            )
                        } else {
                            (requireActivity() as LandingPage).gotoPage(
                                R.id.userLocation2,
                                null,
                                true
                            )
                        }

                    }
                    var cancelbutton: Button = dialogView.findViewById(R.id.cancel)

                    cancelbutton.setOnClickListener {
                        alertDialog.dismiss()

                    }



                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    alertDialog.setCancelable(false)

                    alertDialog.show()

                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}


