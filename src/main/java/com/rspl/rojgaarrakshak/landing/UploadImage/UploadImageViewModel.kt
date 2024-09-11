package com.rspl.rojgaarrakshak.landing.UploadImage

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.UploadPhotoResponse.UploadPhotoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel  @Inject constructor(
    private val repository: Repository
):ViewModel() {

    var selectedFileUri : Uri? = null
    var selectedFilePath : String = ""

    var uploadPictureResponce = MutableLiveData<NetworkResult<UploadPhotoResponse>>()

    fun uploadProfilepicture(
        ctx : Context,
        userId: Int,
        filePath: String,
        fileUri: Uri
    ) {

        val file = File(filePath)
        val fileBody: RequestBody =
            RequestBody.create(
                ctx.getContentResolver().getType(fileUri).toString()
                    .toMediaTypeOrNull(), file
            )

        val body = MultipartBody.Part.createFormData("profile_picture", file.name, fileBody)

        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(uploadPictureResponce)) {
            uploadPictureResponce.postValue(NetworkResult.Loading())
            val response = repository.uploadProfilepicture(userId,body)
            handleResponse(response,uploadPictureResponce)
        }
    }
}