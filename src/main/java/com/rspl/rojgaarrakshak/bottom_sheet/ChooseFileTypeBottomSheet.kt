package com.rspl.rojgaarrakshak.bottom_sheet

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.core.compressBitmapAndSave
import com.rspl.rojgaarrakshak.core.getPrefeb
import com.rspl.rojgaarrakshak.core.handleSamplingAndRotationBitmap
import com.rspl.rojgaarrakshak.databinding.ChooseFileTypeBottomsheetBinding
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ChooseFileTypeBottomSheet(val filePickerListener: FilePickerListener) : BottomSheetDialogFragment(){

    companion object {
        private val TAG = "MainActivity"


        fun newInstance(filePickerListener: FilePickerListener) = ChooseFileTypeBottomSheet(filePickerListener)
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                takeImage()
            }
            else
            {

                val builder = AlertDialog.Builder(requireContext())
                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.custom_dilog, null)

                builder.setView(dialogView)

                val alertDialog = builder.create()

                val message: TextView = dialogView.findViewById(R.id.messagetext)

                message.text = "This Feature not Available Without Camara Permission please Enable Camara Permission"

                val okbutton: Button =dialogView.findViewById(R.id.Login)

                okbutton.text = " Settings "

                okbutton.setTextColor(Color.WHITE)



                okbutton.setOnClickListener {
                    alertDialog.dismiss()

                    val packageName = requireContext().packageName
                    val intent = Intent()

                    // Check if the Android version is M or higher
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                    } else {
                        // For versions lower than M, open the application settings
                        intent.action = Settings.ACTION_APPLICATION_SETTINGS
                    }

                    try {
                        requireContext().startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Unable to open app settings", Toast.LENGTH_SHORT).show()
                    }



                }

                val cancelbutton: Button =dialogView.findViewById(R.id.cancel)

                cancelbutton.text=" Cancel "

                cancelbutton.setTextColor(Color.WHITE)



                cancelbutton.setOnClickListener {
                    alertDialog.dismiss()

                }

                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



                alertDialog.show()

            }
        }



    var filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){
//        Log.e("file uri","${it!!.encodedPath}")

        try {
            it.let {

                val file = File(it!!.path)
                //            Log.e("file uri", "${camPicUri!!.encodedPath}")
                if(file.extension.contains("jp",true)||file.extension.contains("png",true)) {
                    var bitmapCaptured = handleSamplingAndRotationBitmap(requireActivity(), it)
                    val tmpFile =
                        File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir)
                            .apply {
                                createNewFile()
                                deleteOnExit()
                            }
                    compressBitmapAndSave(bitmapCaptured!!, tmpFile!!.absolutePath)
                    bitmapCaptured.recycle()
                    var savedUri = FileProvider.getUriForFile(
                        requireActivity(),
                        "${requireActivity().packageName}.provider",
                        tmpFile
                    )

//                var deleteFile = File(camPicUri!!.path!!)
//                var deleted = deleteFile.delete()
//                Log.e("gallery pic ", " file uri : ${camPicUri!!.path!!} is deleted: $deleted")
                    filePickerListener.onFilePicked(savedUri,tmpFile!!.absolutePath)
                }else{
                    requireActivity().contentResolver.query(it,null,null,null,null)?.use {
                            cursor ->

                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        var fileNameArray = cursor.getString(nameIndex).split(".")
                        cursor.close()

                        val tmpFile =
                            File.createTempFile("resume_", ".${fileNameArray[fileNameArray.size-1]}", requireActivity().cacheDir)
                                .apply {
                                    createNewFile()
                                    deleteOnExit()
                                }

                        var inputStream = requireActivity().contentResolver.openInputStream(it!!)
                        var outputStream = FileOutputStream(tmpFile)
                        try {
                            var read = 0
                            val maxBufferSize = 1 * 1024 * 1024
                            val bytesAvailable = inputStream!!.available()

                            //int bufferSize = 1024;
                            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                            val buffers = ByteArray(bufferSize)
                            while (inputStream.read(buffers).also { read = it } != -1) {
                                outputStream.write(buffers, 0, read)
                            }
                            Log.e("File Size", "Size " + file.length())
                            inputStream.close()
                            outputStream.close()
                            Log.e("File Path", "Path " + file.path)
                            Log.e("File Size", "Size " + file.length())
                        } catch (e: Exception) {
                            Log.e("Exception", e.message!!)
                        }
                        filePickerListener.onFilePicked(it!!,tmpFile!!.absolutePath)
                    }

                }

                dismiss()
            }
        }catch (e:Exception)
        {
            println(e)
        }


    }

    var camPicUri : Uri? = null

    var cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){

        if(it) {
//            Log.e("file uri", "${camPicUri!!.encodedPath}")
            var bitmapCaptured = handleSamplingAndRotationBitmap(requireActivity(),camPicUri!!)
            val tmpFile = File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
            compressBitmapAndSave(bitmapCaptured!!, tmpFile!!.absolutePath)
            bitmapCaptured.recycle()
            var savedUri= FileProvider.getUriForFile(
                requireActivity(),
                "${requireActivity().packageName}.provider",
                tmpFile
            )

//            var deleteFile= File(camPicUri!!.path!!)
//            var deleted = deleteFile.delete()
//            Log.e("Cam pic "," file uri : ${camPicUri!!.path!!} is deleted: $deleted")
            filePickerListener.onFilePicked(savedUri,tmpFile!!.absolutePath)
            dismiss()
        }

    }

    lateinit var binding : ChooseFileTypeBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_file_type_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChooseFileTypeBottomsheetBinding.bind(view)

        binding.fromGallery.setOnClickListener {
            filePickerLauncher.launch(arrayOf("application/pdf","image/*"))
        }

        binding.fromCamera.setOnClickListener {

            requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        }


    }

    interface FilePickerListener{
        fun onFilePicked(uri : Uri?, filePath : String)
    }

    private fun takeImage() {
        lifecycleScope.launch {
            getTmpFileUri().let { uri ->
                camPicUri=uri
                cameraLauncher.launch(uri)
            }
        }
    }


    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireActivity(),
            "${requireActivity().packageName}.provider",
            tmpFile
        )
    }

}