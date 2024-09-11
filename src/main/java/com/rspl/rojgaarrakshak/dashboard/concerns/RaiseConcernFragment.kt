package com.rspl.rojgaarrakshak.dashboard.concerns

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentRaiseConcernBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RaiseConcernFragment : Fragment() {

    companion object {
        fun newInstance() = RaiseConcernFragment()
    }

    private lateinit var viewModel: RaiseConcernViewModel
    lateinit var userlogin: String
    private var progressDialog: Dialog? = null
    private lateinit var binding: FragmentRaiseConcernBinding
    private lateinit var acesstoken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_raise_concern, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRaiseConcernBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RaiseConcernViewModel::class.java)

        acesstoken = ""
        userlogin = ""

        try {
            userlogin = requireActivity().getPrefeb("UserToken")

            if (userlogin.isEmpty()) {
                binding.concornmessage.visibility = View.GONE
                binding.SubbmitButton.visibility = View.GONE
                binding.concernBox.visibility = View.GONE

                val builder = AlertDialog.Builder(requireContext())
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                builder.setView(dialogView)

                val alertDialog = builder.create()

                var okbutton: Button = dialogView.findViewById(R.id.Login)

                okbutton.setOnClickListener {
                    alertDialog.dismiss()

                    val intent = Intent(requireActivity(), LandingPage::class.java)

                    intent.putExtra("NavigateToSignInPage", "NavigateToSignInPage")

                    startActivity(intent)
                    requireActivity().finish()

                }
                var cancelbutton: Button = dialogView.findViewById(R.id.cancel)

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

        } catch (e: Exception) {
//            requireActivity().printToast("null")
        }




        binding.backBtn.setOnClickListener {
            (requireActivity() as DashboardActivity).onBackBtnPressed()
        }

        binding.moreBtn.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.moreBtn)

            // Inflating popup menu from popup_menu.xml file

            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.concern_menu, popupMenu.getMenu())
            popupMenu.setOnMenuItemClickListener { menuItem ->

                (requireActivity() as DashboardActivity).gotoPage(R.id.concernHistoryFragment, null)
                true
            }
            // Showing the popup menu
            // Showing the popup menu
            popupMenu.show()
        }

        try {
            acesstoken = requireActivity().getPrefeb("UserToken")


        } catch (e: Exception) {
            println(e)
        }

        binding.SubbmitButton.setOnClickListener {
            val message = binding.concornmessage.text.toString()

            if (acesstoken.isNotEmpty() && message.isNotEmpty()) {
                val Accesstoken = "Bearer $acesstoken"
                viewModel.CreateConcern(Accesstoken, message)
            } else {
                requireActivity().showalertdilog("Please Provide valid Message")
            }

        }

        progressDialog = requireActivity().getProgressDialog()

        viewModel.CreteConcernResponce.observe(viewLifecycleOwner) {
            progressDialog?.dismiss()
            when (it) {
                is NetworkResult.Loading -> {
                    progressDialog?.show()

                }

                is NetworkResult.Success -> {

                    val builder = AlertDialog.Builder(requireContext())
                    val inflater = layoutInflater
                    val dialogView = inflater.inflate(R.layout.single_button_custom_dilog, null)

                    builder.setView(dialogView)

                    val alertDialog = builder.create()

                    val message: TextView = dialogView.findViewById(R.id.responsemessage)

                    message.text = it.data!!.data.toString()

                    var okbutton: Button = dialogView.findViewById(R.id.Ok)

                    okbutton.setOnClickListener {
                        alertDialog.dismiss()
                        (requireActivity() as DashboardActivity).gotoPage(
                            R.id.concernHistoryFragment,
                            null
                        )


                    }

                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    alertDialog.setCancelable(false)

                    alertDialog.show()

                    viewModel.CreteConcernResponce.postValue(null)


                }

                is NetworkResult.Error -> {
                    requireActivity().showalertdilog(it.message!!)
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

            if (userlogin.isEmpty()) {
                binding.concornmessage.visibility = View.GONE
                binding.SubbmitButton.visibility = View.GONE
                binding.concernBox.visibility = View.GONE

                val builder = AlertDialog.Builder(requireContext())
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                builder.setView(dialogView)

                val alertDialog = builder.create()

                var okbutton: Button = dialogView.findViewById(R.id.Login)

                okbutton.setOnClickListener {
                    alertDialog.dismiss()
                    var intent = Intent(requireActivity(), LandingPage::class.java)

                    intent.putExtra("NavigateToSignInPage", "NavigateToSignInPage")

                    startActivity(intent)
                    requireActivity().finish()

                }
                var cancelbutton: Button = dialogView.findViewById(R.id.cancel)

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

        } catch (e: Exception) {
//            requireActivity().printToast("null")
        }


    }

}