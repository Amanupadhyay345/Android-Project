package com.rspl.rojgaarrakshak.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.R
import com.rspl.rojgaarrakshak.databinding.CustomProgressLayoutBinding
import com.rspl.rojgaarrakshak.landing.LandingPage
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.UnknownHostException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


      var SubSkillid:ArrayList<String> = arrayListOf()


val coroutineExceptionHandler = CoroutineExceptionHandler{cctx, throwable ->
    throwable.printStackTrace()
    if(throwable is UnknownHostException){
        Log.e("Exception handler","No internet exception")

        GlobalScope.launch{
            showNoInternetException()
        }

    }
}


suspend fun showNoInternetException(){
    withContext(Dispatchers.Main){

    }
}

private var httpClient: OkHttpClient? = null


fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window,
        window.decorView.findViewById(android.R.id.content)).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.hideSystemUIForSplash() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window,
        window.decorView.findViewById(android.R.id.content)).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.saveImageToSDCard(fileUri: Uri) {
    var tempFile = createImageFileOnDisk()
//        var inputFile = File(fileUri)
    val outputStream = contentResolver.openOutputStream(tempFile!!.toUri())
    val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
    if (outputStream != null) {
        inputStream!!.copyTo(outputStream, 1024)
        printToast("Image copied successfully!!")
    } else {
        Log.e("Output stream", "output stream is null.")
        printToast("Image copy failed!!!")
    }

}


@Throws(IOException::class)
fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri?): Bitmap? {
    val MAX_HEIGHT = 800
    val MAX_WIDTH = 800

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    var imageStream = context.contentResolver.openInputStream(selectedImage!!)
//    BitmapFactory.decodeStream(imageStream, null, options)
//    imageStream!!.close()

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
//    imageStream = context.contentResolver.openInputStream(selectedImage)
    var img = BitmapFactory.decodeStream(imageStream, null, options)
    img=resize(img!!,MAX_WIDTH,MAX_HEIGHT)
    imageStream!!.close()
    img = img?.let { rotateImageIfRequired(context, it, selectedImage) }
    return img
}

private fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap? {
    var image = image
    return if (maxHeight > 0 && maxWidth > 0) {
        val width = image.width
        val height = image.height
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
        image
    } else {
        image
    }
}

fun getPath(context: Context, uri: Uri): String? {
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val projection = arrayOf("_data")
        val cursor: Cursor?
        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            assert(cursor != null)
            val column_index = cursor!!.getColumnIndexOrThrow("_data")
            if (cursor.moveToFirst()) {
                return cursor.getString(column_index)
            }
            cursor.close()
        } catch (e: java.lang.Exception) {
            // Eat it
            e.printStackTrace()
        }
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}


private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int, reqHeight: Int
): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {

        val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
        val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())


        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio


        val totalPixels = (width * height).toFloat()

        // Anything more than 2x the requested pixels we'll sample down further
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
    }
    return inSampleSize
}

@Throws(IOException::class)
private fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap? {
    val input = context.contentResolver.openInputStream(selectedImage)
    val ei: ExifInterface
    ei =
        if (Build.VERSION.SDK_INT > 23) ExifInterface(input!!) else ExifInterface(selectedImage.path!!)
    val orientation =
        ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
        else -> img
    }
}

private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

fun compressBitmapAndSave(bmp: Bitmap, destFilePath: String) {
    try {
        FileOutputStream(destFilePath).use { out ->
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, out) // bmp is your Bitmap instance
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


fun Context.printToast(message: String) {
    // Toast with text
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.createImageFileOnDisk(): File? {
    var tempFile: File? = null
    try {
        val dirCheck = File(
            filesDir.canonicalPath + "/" + "amar"
        )
        if (!dirCheck.exists()) {
            dirCheck.mkdirs()
        }
        val filename = Date().time.toString() + ""
        tempFile = File(
            filesDir.canonicalPath + "/" + "amar" + "/"
                    + filename + ".jpg"
        )
    } catch (ioex: IOException) {
//            Log.e(TAG, "Couldn't create output file", ioex);
        ioex.printStackTrace()
    }
    return tempFile
}


fun String.toCapitalise(){
    this.toUpperCase();
}


val horizontalItemDecoration = object : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if(parent.getChildAdapterPosition(view)==0)
            outRect.left=60
        else
            outRect.left=30

        if(parent.getChildAdapterPosition(view)==parent.adapter!!.itemCount-1)
            outRect.right=60
    }
}

fun View.showView(visible : Boolean){
    visibility = if(visible) View.VISIBLE else View.GONE
}

enum class OnBoardingStep{
    LOGIN,NAME,AGE,LOCATION,LEAGUE,TEAM,PLAYER,DASHBOARD
}

val verticalItemDecoration = object : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top=30

        if(parent.getChildAdapterPosition(view)==parent.adapter!!.itemCount-1)
            outRect.bottom=30
    }
}

fun <T> Context.handleResponse(response: Response<T>? = null, responseList : MutableLiveData<NetworkResult<T>>?=null) {
    try {
        if (response!!.code() == 200) {
            val responseBody=response.body()!!
            responseList!!.postValue(NetworkResult.Success(responseBody))
            Log.e("response","$responseBody")
        } else if (response.errorBody() != null) {

            val errorobj = JSONObject(response.errorBody()!!.charStream().readText())
            responseList!!.postValue(NetworkResult.Error(errorobj.getString("message")))
            Log.e("response","$errorobj")
        } else {
            responseList!!.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }catch (ex: Exception){
        responseList!!.postValue(NetworkResult.Error(ex.message))
        ex.printStackTrace()
    }
}

fun Context.getProgressDialog() : Dialog {
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

    return dialog
}

fun Context.putPrefeb(key : String,value : String){
    val preferences = getSharedPreferences("amar_${packageName}", Context.MODE_PRIVATE)
    val editor=preferences.edit()
    editor.putString(key,value)
    editor.apply()
}

fun Context.getPrefeb(key : String) : String {
    val preferences = getSharedPreferences("amar_${packageName}", Context.MODE_PRIVATE)
    val prefebValue= preferences.getString(key,"")!!
    return prefebValue
}

fun Context.SaveSubSkillid(subskillid:String)
{
    SubSkillid.add(subskillid)


    val listcityid = SubSkillid.toString()
    val rcityid = listcityid.replace("[", "")
    val recityid = rcityid.replace("]", "")

    val gson=Gson()
    var json=   gson.toJson(SubSkillid)

    this.putPrefeb("UserLocationSubSkillidLIst",json)

    this.putPrefeb("UserLocationSubSkillid",recityid)
}

fun Context.RemoveSubSkillid(subskillid: String)
{
    SubSkillid.remove(subskillid)


    val gson=Gson()
    var json=   gson.toJson(SubSkillid)

    this.putPrefeb("UserLocationSubSkillidLIst",json)

    val listcityid = SubSkillid.toString()
    val rcityid = listcityid.replace("[", "")
    val recityid = rcityid.replace("]", "")

    this.putPrefeb("UserLocationSubSkillid",recityid)
}

fun Context.showCancelDialog(title: String, message: String) {
    val builder = AlertDialog.Builder(this)
    //set title for alert dialog
    builder.setTitle(title)
    //set message for alert dialog
    builder.setMessage(message)
    builder.setIcon(null)
    lateinit var alertDialog: AlertDialog

    builder.setNegativeButton("Close"){ dialogInterface, which ->
        alertDialog.dismiss()
    }
    // Create the AlertDialog
    alertDialog = builder.create()
    // Set other dialog properties
    alertDialog.setCancelable(true)
    alertDialog.show()
}

@SuppressLint("MissingInflatedId")
fun Context.showalertdilog(alertmessage:String)
{
    val builder = AlertDialog.Builder(this)
    val inflater=LayoutInflater.from(this)
    val dialogView = inflater.inflate(R.layout.single_button_custom_dilog, null)

    builder.setView(dialogView)

    val alertDialog = builder.create()

    var message:TextView=dialogView.findViewById(R.id.responsemessage)

    message.text = alertmessage

    val okbutton: Button =dialogView.findViewById(R.id.Ok)

    okbutton.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    alertDialog.setCancelable(false)

    alertDialog.show()
}

fun getDate(dateInput: String, inputFormat: String): Date? {
    val inputFormat: DateFormat = SimpleDateFormat(inputFormat)
    var date: Date? = null
    try {
        date = inputFormat.parse(dateInput)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date
}



