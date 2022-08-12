package com.rootscare.serviceprovider.utilitycommon

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.rootscare.customviews.MyEditTextView
import com.rootscare.serviceprovider.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("strikeThrough")
fun strikeThrough(textView: TextView, strikeThrough: Boolean) {
    if (strikeThrough) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

@BindingAdapter("visibilityWithNullEmptyCheck")
fun View.visibilityWithNullEmptyCheck(vl: Any?) {
    val mVisi = when (vl) {
        is String -> { if (vl.isNotBlank()) View.VISIBLE else View.GONE }
        is Boolean ->{ if (vl) View.VISIBLE else View.GONE }
        else -> View.GONE
    }
    this.visibility = mVisi
}
@BindingAdapter("invisibleWithNullEmptyCheck")
fun View.invisibleWithNullEmptyCheck(vl: Any?) {
    val mVisi = when (vl) {
        is String -> { if (vl.isNotBlank()) View.VISIBLE else View.INVISIBLE }
        is Boolean ->{ if (vl) View.VISIBLE else View.INVISIBLE }
        else -> View.INVISIBLE
    }
    this.visibility = mVisi
}

@BindingAdapter("changeColorWithCheck")
fun TextView.changeColorWithCheck(checkStatus: Boolean? = false) {
    checkStatus?.let {
        if (it) this.setTextColor(Color.parseColor("#0888D1"))
        else this.setTextColor(Color.parseColor("#8F98A7"))
    }

}

@BindingAdapter("setHtmlContent")
fun TextView.setHtmlContent(txt: String?) {
    this@setHtmlContent.text = txt?.let { it.parseAsHtml() }
}
@BindingAdapter("setAmount")
fun TextView.setAmount(amt: String?) {
    this@setAmount.text = amt?.let { "$amt SAR" } ?: ""
}

@BindingAdapter("setAmount")
fun TextView.setAmount(amt: Int?) {
    this@setAmount.text = amt?.let { "$amt SAR" } ?: ""
}

@BindingAdapter("setCircularRemoteImage")
fun CircleImageView.setCircularRemoteImage(url_: String?) {
    try {
        val options: RequestOptions = RequestOptions() .centerCrop().placeholder(R.drawable.profile_no_image).priority(Priority.HIGH)
        this.let {
            Glide.with(this.context).load("${BaseMediaUrls.USERIMAGE.url}${url_?.trim()}")
                .apply(options).into(it)
        }
    }catch (e:Exception){
        println(e)
    }

}
@BindingAdapter("setRemotePrescImage")
fun ImageView.setRemotePrescImage(url_: String?) {
    val options: RequestOptions = RequestOptions().placeholder(R.drawable.ic_presc).priority(Priority.HIGH)
    this.let {
        Glide
            .with(this.context)
            .load("${BaseMediaUrls.USERIMAGE.url}${url_?.trim()}")
            .apply(options)
            .into(it)
    }
}

@BindingAdapter("changeColorWithCheck")
fun MyEditTextView.changeColorWithCheck(checkStatus: Boolean? = false) {
    checkStatus?.let {
        if (it) this.setTextColor(Color.parseColor("#041A27"))
        else this.setTextColor(Color.parseColor("#8F98A7"))
    }

}

@BindingAdapter("backgroundStatusColor")
fun TextView.backgroundStatusColor(mStatus: String? = "") {
    when {
        mStatus.equals(TransactionStatus.PAID.get(), ignoreCase = true) ||
                mStatus.equals(TransactionStatus.COMPLETED.get(), ignoreCase = true) ||
                mStatus.equals(TransactionStatus.ACCEPTED.get(), ignoreCase = true) ||
                mStatus.equals(TransactionStatus.SUCCESS.get(), ignoreCase = true) -> {
            this.setBackgroundColor(Color.parseColor(TransactionStatus.PAID.getColor()))
        }
        mStatus.equals(TransactionStatus.PENDING.get(), ignoreCase = true) -> {
            this.setBackgroundColor(Color.parseColor(TransactionStatus.PENDING.getColor()))
        }
        mStatus.equals(TransactionStatus.CANCELLED.get(), ignoreCase = true) ||
                mStatus.equals(TransactionStatus.REJECTED.get(), ignoreCase = true) -> {
            this.setBackgroundColor(Color.parseColor(TransactionStatus.CANCELLED.getColor()))
        }
        else -> this.setBackgroundColor(Color.parseColor(TransactionStatus.ELSE.getColor()))
    }

}

@BindingAdapter("setDisplayUserType")
fun TextView.setDisplayUserType(mType: String? = "") {
    this.text = when (mType?.trim()) {
        LoginTypes.HOSPITAL.type -> LoginTypes.HOSPITAL.displayName
        LoginTypes.HOSPITAL_DOCTOR.type -> LoginTypes.HOSPITAL_DOCTOR.displayName
        LoginTypes.DOCTOR.type -> LoginTypes.DOCTOR.displayName
        LoginTypes.NURSE.type -> LoginTypes.NURSE.displayName
        LoginTypes.CAREGIVER.type -> LoginTypes.CAREGIVER.displayName
        LoginTypes.BABYSITTER.type -> LoginTypes.BABYSITTER.displayName
        LoginTypes.PHYSIOTHERAPY.type -> LoginTypes.PHYSIOTHERAPY.displayName
        LoginTypes.LAB.type -> LoginTypes.LAB.displayName
        LoginTypes.PATHOLOGY.type -> LoginTypes.PATHOLOGY.displayName
        else -> "Unknown"
    }

}







