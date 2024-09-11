package com.rspl.rojgaarrakshak.landing.UploadImage

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.bottom_sheet.ChooseFileTypeBottomSheet
import com.rspl.rojgaarrakshak.bottom_sheet.ChoosePhotoTypeBottomSheet
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.getProgressDialog
import com.rspl.rojgaarrakshak.core.printToast

import com.rspl.rojgaarrakshak.core.showalertdilog
import com.rspl.rojgaarrakshak.dashboard.DashboardActivity
import com.rspl.rojgaarrakshak.databinding.FragmentUploadImageBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import com.rspl.rojgaarrakshak.landing.signup.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadImage : Fragment() {

    companion object {
        fun newInstance() = UploadImage()
    }

    private lateinit var binding: FragmentUploadImageBinding
    private lateinit var viewModel: UploadImageViewModel
    private lateinit var Userid:String
    var progressDialog : Dialog?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_image, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUploadImageBinding.bind(view)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[UploadImageViewModel::class.java]

        progressDialog = requireActivity().getProgressDialog()
        Userid=""


        binding.backBtn.setOnClickListener {

            (requireActivity() as LandingPage).gotoPage(R.id.userLocation2,null,true)

//            val intent = Intent(requireActivity(), DashboardActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                (requireActivity() as LandingPage).gotoPage(R.id.userLocation2,null,true)

//                val intent = Intent(requireActivity(), DashboardActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.floatingActionButton.setOnClickListener {
//            filePickerLauncher.launch(arrayOf("application/pdf","image/*"))
            var docBottomSheet = ChoosePhotoTypeBottomSheet(object : ChoosePhotoTypeBottomSheet.FilePickerListener{
                override fun onFilePicked(uri: Uri?, filePath : String) {
                    uri?.let { fileUri->

                        viewModel.selectedFileUri = fileUri
                        viewModel.selectedFilePath = filePath

//                        requireActivity().printToast(viewModel.selectedFilePath)
//                        requireActivity().printToast(viewModel.selectedFileUri.toString())


                        requireActivity().contentResolver.query(fileUri,null,null,null,null)?.use {
                                cursor ->

                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

//                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
//
//                            val fileSize = cursor.getDouble(fileSizeIndex)

                            cursor.moveToFirst()
                            Glide.with(requireContext())
                                .load(viewModel.selectedFileUri)
                                .into(binding.userimage)
                        }

                        Log.e("Picked file"," file name : $filePath" )


                    }
                }
            })

            docBottomSheet.show(childFragmentManager,"doc bottom sheet")
        }

        binding.image.setOnClickListener {
//            filePickerLauncher.launch(arrayOf("application/pdf","image/*"))
            var docBottomSheet = ChoosePhotoTypeBottomSheet(object : ChoosePhotoTypeBottomSheet.FilePickerListener{
                override fun onFilePicked(uri: Uri?, filePath : String) {
                    uri?.let { fileUri->

                        viewModel.selectedFileUri = fileUri
                        viewModel.selectedFilePath = filePath

//                        requireActivity().printToast(viewModel.selectedFilePath)
//                        requireActivity().printToast(viewModel.selectedFileUri.toString())


                        requireActivity().contentResolver.query(fileUri,null,null,null,null)?.use {
                                cursor ->

                            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

//                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
//
//                            val fileSize = cursor.getDouble(fileSizeIndex)

                            cursor.moveToFirst()
                           Glide.with(requireContext())
                           .load(viewModel.selectedFileUri)
                            .into(binding.userimage)
                        }

                        Log.e("Picked file"," file name : $filePath" )


                    }
                }
            })

            docBottomSheet.show(childFragmentManager,"doc bottom sheet")
        }

        binding.uploadImage.setOnClickListener {
            try {
                Userid=requireContext().getPrefeb("SignupUserid")

                if (Userid.isNotEmpty())
                {
                    var id=Userid.toInt()
                    viewModel.uploadProfilepicture(requireActivity()
                        ,id!!,viewModel.selectedFilePath!!,viewModel.selectedFileUri!!)
                }
                else
                {
                    Log.e("Null","Null")
                }

            }catch (e:Exception)
            {
                println(e)
            }

            viewModel.uploadPictureResponce.observe(viewLifecycleOwner){
                progressDialog?.let {
                    it.dismiss()
                }
                when(it){
                    is NetworkResult.Loading->{
                        progressDialog?.let {
                            it.show()
                        }

                    }

                    is NetworkResult.Success->{


                        (requireActivity() as LandingPage).gotoPage(R.id.payAndCompleteFragment,null,true)


//                        requireActivity().printToast("Success")
                        viewModel.uploadPictureResponce.postValue(null)


                    }

                    is NetworkResult.Error->{

                        requireActivity().showalertdilog(it.message!!)

                    }

                    else -> {

                    }
                }
            }



        }
    }

}










