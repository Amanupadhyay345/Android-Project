package app.peerpicks.core.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import app.peerpicks.core.interfaces.ItemClickListener
import com.google.android.material.snackbar.Snackbar
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.databinding.CustomProgressLayoutBinding
import org.json.JSONObject
import retrofit2.Response


internal fun Activity.showSnackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
        anchorView = view.rootView
        show()
    }
}

internal fun Context.getProgressDialog() : Dialog {
    val dialog = Dialog(this)
    val binding = CustomProgressLayoutBinding.inflate(LayoutInflater.from(this))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    dialog.window?.setLayout(
        ActionBar.LayoutParams.MATCH_PARENT,
        ActionBar.LayoutParams.MATCH_PARENT
    )
    dialog.setCancelable(false)
    dialog.setContentView(binding.root)
    try {
        dialog.show()
    }catch (e : Exception){
        e.printStackTrace()
    }

    return dialog
}

fun Context.getNavOptions() : NavOptions = NavOptions.Builder()
    .setEnterAnim(android.R.anim.fade_in)
    .setExitAnim(android.R.anim.fade_out)
    .setPopEnterAnim(android.R.anim.fade_in)
    .setPopExitAnim(android.R.anim.fade_out)
    .build()


fun <T> handleResponse(response: Response<T>? = null, responseList: MutableLiveData<NetworkResult<T>>?=null) {
    try {
        if (response!!.code() == 200 || response!!.code() == 201) {
            val responseBody=response.body()!!
            responseList!!.postValue(NetworkResult.Success(responseBody))
            Log.e("response","$responseBody")
        } else if (response.errorBody() != null) {
            val errorobj = JSONObject(response.errorBody()!!.charStream().readText())
            responseList!!.postValue(NetworkResult.Error(errorobj.getString("message")))

            val message=errorobj.getString("message")
            Log.e("errorresponse","$errorobj")
            Log.e("errorrmessage","$message")
        } else {
            responseList!!.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }catch (ex: Exception){
        responseList!!.postValue(NetworkResult.Error(ex.message))
        ex.printStackTrace()
    }
}

internal fun Context.showAdharVerificationDialog(message: String, clickListener : ItemClickListener) : Dialog{
    val dialog = Dialog(this)
    val binding = CustomProgressLayoutBinding.inflate(LayoutInflater.from(this))
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    dialog.window?.setLayout(
        ActionBar.LayoutParams.MATCH_PARENT,
        ActionBar.LayoutParams.MATCH_PARENT
    )
    dialog.setCancelable(false)
    dialog.setContentView(binding.root)
    try {
        dialog.show()
    }catch (e : Exception){
        e.printStackTrace()
    }

    return dialog
}

internal fun Fragment.showSnackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
        anchorView = view.rootView
        show()
    }
}

internal fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

internal inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    block(intent)
    startActivity(intent)
}

