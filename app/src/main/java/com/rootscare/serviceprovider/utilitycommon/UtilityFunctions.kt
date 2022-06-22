package com.rootscare.serviceprovider.utilitycommon

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.rootscare.serviceprovider.BuildConfig
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.ui.showimagelarger.TransaprentPopUpActivityForImageShow
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.*
import com.ApplicationClass
import java.text.ParseException
import java.text.SimpleDateFormat


const val IS_PRODUCTION = false
val API_BASE_URL: String by lazy { if(IS_PRODUCTION) "https://rootscare.net/application/" else "https://teq-dev-var19.co.in/rootscare/" }

const val SUCCESS_CODE= "200"

enum class BaseMediaUrls(val url: String){
    USERIMAGE("${API_BASE_URL}uploads/images/"),
    AUDIO("${API_BASE_URL}uploads/images/")
}

fun getAppLocale() = if (ApplicationClass.instance?.appSharedPref?.languagePref.equals(LanguageModes.ENG.get(), ignoreCase = true)) {
    LanguageModes.ENG.getLangLocale() } else LanguageModes.AR.getLangLocale()

class IntentParams(key: String = "", value: String = "") {
    var key: String? = key
    var value: String? = value

}
val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
fun isValidPassword(s: String): Boolean {
 //   val PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9!@#$]{8,24}")

//        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches()
    return !TextUtils.isEmpty(s)
}

infix fun Activity.viewFileEnlarge(imgUrl:String) {
    startActivity(TransaprentPopUpActivityForImageShow.newIntent(this, imgUrl))
}

infix fun Fragment.viewFileEnlarge(imgUrl:String) {
    startActivity(TransaprentPopUpActivityForImageShow.newIntent(requireActivity(), imgUrl))
}

  fun Activity.doAnimation(anim:Int) = AnimationUtils.loadAnimation(this, anim)

 fun Fragment.pickupImage(reqCode:Int) {
    var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, "Choose a image")
    startActivityForResult(chooseFile, reqCode)
}

 fun Activity.uplaodCertificate(reqCode:Int) {
    var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
    chooseFile.type = "*/*"
    chooseFile = Intent.createChooser(chooseFile, "Choose a file")
    startActivityForResult(chooseFile, reqCode)
}

 fun Fragment.uplaodCertificate(reqCode:Int) {
    var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
    chooseFile.type = "*/*"
    chooseFile = Intent.createChooser(chooseFile, "Choose a file")
    startActivityForResult(chooseFile, reqCode)
}
fun Activity.openDialer(num:String){
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$num")
    startActivity(intent)
}
fun Fragment.openDialer(num:String){
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$num")
    startActivity(intent)
}
fun Activity.openGoogleMap(lat:String, lng:String){
    val gmmIntentUri = Uri.parse("https://maps.google.com/maps?daddr=$lat,$lng")
 //   val gmmIntentUri = Uri.parse("geo:$lat,$lng")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(Intent.createChooser(mapIntent, "Open via"))
}

fun Activity.openGoogleMapWithDirectNavigationStart(lat:String,lng:String){
    val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(Intent.createChooser(mapIntent, "Open via"))
//    mapIntent.resolveActivity(packageManager)?.let {
//        startActivity(mapIntent)
//    }
}

inline fun <reified T : Activity> Activity.navigate(params: List<IntentParams> = emptyList()) {
    val intent = Intent(this, T::class.java)
    for (parameter in params) {
        intent.putExtra(parameter.key, parameter.value)
    }
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.navigate(params: List<IntentParams> = emptyList()) {
    val intent = Intent(activity, T::class.java)
    for (parameter in params) {
        intent.putExtra(parameter.key, parameter.value)
    }
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)

}

inline fun <reified T : Activity> Fragment.navigateWithResultSet(resultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(requireActivity(), T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    resultLauncher.launch(intent)
}

inline fun <reified T : Activity> Activity.navigateWithResultSet(resultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    resultLauncher.launch(intent)
}

 inline fun <reified T: Any> String.getModelFromPref() : T? {
      return Gson().fromJson(this, T::class.java)
}


fun ImageView.setRemoteProfileImage(url_: String) {
    val options: RequestOptions = RequestOptions().centerCrop().placeholder(R.drawable.profile_no_image).priority(Priority.HIGH)

    this.let {
        Glide
            .with(this.context)
            .load("${BaseMediaUrls.USERIMAGE.url}${url_.trim()}")
            .apply(options)
            .into(it)
    }
}
fun CircleImageView.setCircularLocalImage(url_: String) {
    val options: RequestOptions = RequestOptions().centerCrop().placeholder(R.drawable.profile_no_image).priority(Priority.HIGH)

    this.let {
        Glide
            .with(this.context)
            .load(url_.trim())
            .apply(options)
            .into(it)
    }
}

fun CircleImageView.setHamburgerImage(url_: String) {
    val options: RequestOptions = RequestOptions().error(R.drawable.ic_circle_img_hamburger).placeholder(R.drawable.ic_circle_img_hamburger).priority(
        Priority.HIGH)

    this.let {
        Glide
            .with(this.context)
            .load("${BaseMediaUrls.USERIMAGE.url}${url_.trim()}")
            .apply(options)
            .into(it)
    }


}
fun Activity.materialAlert(title: String? = "", msg: String? = "", positiveText: String = "Ok", cancelable: Boolean = false) {
//    try {
//        val builder: MaterialDialog.Builder? = MaterialDialog.Builder(this)
//            .cancelable(cancelable)
//            .content(msg.orEmpty())
//            .positiveText(positiveText)
//            .onPositive { d, _ -> d.dismiss() }
//        if (title?.isNotBlank() == true) builder?.title(title)
//        builder?.build()?.show()
//    } catch (e: java.lang.Exception) {
//        Crashlytics.logException(e)
//    }
}

fun Fragment.materialAlert(title: String? = "", msg: String = "", positiveText: String = "Ok", cancelable: Boolean = false) {
//    try {
//        val builder: MaterialDialog.Builder? = MaterialDialog.Builder(requireContext())
//            .cancelable(cancelable)
//            .content(msg)
//            .positiveText(positiveText)
//            .onPositive { d, _ -> d.dismiss() }
//        if (title?.isNotBlank() == true) builder?.title(title)
//        builder?.build()?.show()
//    } catch (e: java.lang.Exception) {
//        Crashlytics.logException(e)
//    }
}

fun String.asReqBody() = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), this)


 fun setClickableString(wholeValue: String, textView: TextView?, clickableValue: Array<String>, clickableSpans: Array<ClickableSpan?>) {
    val spannableString = SpannableString(wholeValue)
    for (i in clickableValue.indices) {
        val clickableSpan = clickableSpans[i]
        val link = clickableValue[i]
        val startIndexOfLink = wholeValue.indexOf(link)
        spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    textView?.highlightColor = Color.TRANSPARENT // prevent TextView change background when highlight
    textView?.movementMethod = LinkMovementMethod.getInstance()
    textView?.setText(spannableString, TextView.BufferType.SPANNABLE)
}


 fun Activity.startSmoothScroll(pos:Int?, recycle:RecyclerView?){
    if (pos != null) {
        Handler(Looper.getMainLooper()).postDelayed({
            runOnUiThread {
                val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(this) {
                    override fun getVerticalSnapPreference(): Int { return SNAP_TO_ANY }}
                smoothScroller.targetPosition = pos
                recycle?.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }, 400)
    }
}

 fun Fragment.startSmoothScroll(pos:Int?, recycle:RecyclerView?){
    if (pos != null) {
        Handler(Looper.getMainLooper()).postDelayed({
            requireActivity().runOnUiThread {
                val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(requireContext()) {
                    override fun getVerticalSnapPreference(): Int { return SNAP_TO_ANY }}
                smoothScroller.targetPosition = pos
                recycle?.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }, 400)
    }
}


fun getAppVersionText() = "RC Version ${BuildConfig.VERSION_NAME }(${BuildConfig.VERSION_CODE})"

fun needToShowVideoCall(appDate: String?, appTime: String?, btnVideoCall: TextView) {

    val sdf = SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.ENGLISH)
    val sdf1 = SimpleDateFormat("EEE dd MMM yyyy hh:mm a", Locale.ENGLISH)
    val appDateTimeMilli = if(appDate?.contains(",")==true){
        sdf.parse("$appDate $appTime")?.time ?: Date().time
    } else {
        sdf1.parse("$appDate $appTime")?.time ?: Date().time
    }

    val appDateTime = Calendar.getInstance()
    appDateTime.timeInMillis = appDateTimeMilli
    val now = Calendar.getInstance()
    val diff = appDateTime.timeInMillis - now.timeInMillis

    var diffInMin = (diff / (1000 * 60)) % 60
    val diffInHour = ((diff / (1000 * 60 * 60)) % 24)*60
    diffInMin += diffInHour
    val diffInDays = ((diff / (1000 * 60 * 60 * 24)) % 365) * 24 * 60
    diffInMin += diffInDays

    try {
        val yesNo = when {
            now.get(Calendar.YEAR) == appDateTime.get(Calendar.YEAR) &&
                    now.get(Calendar.MONTH) == appDateTime.get(Calendar.MONTH) &&
                    now.get(Calendar.DATE) == appDateTime.get(Calendar.DATE) &&
                    //    now.get(Calendar.HOUR) >= appDateTime.get(Calendar.HOUR) &&
                    diffInMin <= 10  -> {

                btnVideoCall.visibility = View.VISIBLE
                true
            }
            else ->{
                btnVideoCall.visibility = View.GONE
                false
            }
        }
        Log.wtf("OkHttp", "$diffInMin needToShowVideoCall: - $yesNo")

    } catch (e: ParseException) {
        btnVideoCall.visibility = View.GONE
        e.printStackTrace()
    }
}


fun needToShowUploadPrescription(appDate: String?, appTime: String?, btnView: TextView) {
    // "app_date":"Tue, 19 Apr 2022","app_time":"07:30 PM"
    val sdf = SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.ENGLISH)
    val sdf1 = SimpleDateFormat("EEE dd MMM yyyy hh:mm a", Locale.ENGLISH)

    val appDateTimeMilli = if(appDate?.contains(",")==true){
        sdf.parse("$appDate $appTime")?.time ?: Date().time
    } else {
        sdf1.parse("$appDate $appTime")?.time ?: Date().time
    }

    val appDateTime = Calendar.getInstance()
    appDateTime.timeInMillis = appDateTimeMilli

    val now = Calendar.getInstance()
    val diff = now.timeInMillis - appDateTime.timeInMillis

    var diffInMin = (diff / (1000 * 60)) % 60
    val diffInHour = ((diff / (1000 * 60 * 60)) % 24)*60
        diffInMin += diffInHour
    val diffInDays = ((diff / (1000 * 60 * 60 * 24)) % 365) * 24 * 60
        diffInMin += diffInDays

    try {
        val yesNo = when {
                  now.get(Calendar.YEAR) == appDateTime.get(Calendar.YEAR) &&
                  now.get(Calendar.MONTH) == appDateTime.get(Calendar.MONTH) &&
                  now.get(Calendar.DATE) >= appDateTime.get(Calendar.DATE) &&
                //  now.get(Calendar.HOUR) >= appDateTime.get(Calendar.HOUR) &&
                  diffInMin >= 0  -> {
                  btnView.visibility = View.VISIBLE
                true
            }
            else -> {
                btnView.visibility = View.GONE
                false
            }
        }
        Log.wtf("OkHttp", "$diffInMin- ${now.get(Calendar.HOUR)} - ${appDateTime.get(Calendar.HOUR)}Prescrip: - $yesNo")

    } catch (e: ParseException) {
        btnView.visibility = View.GONE
        e.printStackTrace()
    }
}

fun Activity.openWebLink(webLink: String?) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(webLink)
    val title = "Choose one"
    val chooser = Intent.createChooser(intent, title)
    startActivity(chooser)
}