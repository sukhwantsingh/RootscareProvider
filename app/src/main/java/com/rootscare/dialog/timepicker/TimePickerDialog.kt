package com.rootscare.dialog.timepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.latikaseafood.utils.DateTimeUtils
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.DateTimePickerLayoutBinding
import com.rootscare.serviceprovider.ui.base.BaseDialog
import com.rootscare.utils.AppConstants
import java.text.SimpleDateFormat
import java.util.*


class TimePickerDialog(internal var activity: Activity, callbackAfterDateTimeSelect: CallbackAfterDateTimeSelect) :
    BaseDialog(), TimeNavigator, View.OnClickListener {


    interface CallbackAfterDateTimeSelect {
        fun selectDateTime(dateTime: String)
    }

    constructor(
        activity: Activity, callbackAfterDateTimeSelect: CallbackAfterDateTimeSelect, timeValue: String? = null,
        timeValueGivenPattern: String? = null, minTime: String? = null, maxTime: String? = null
    ) :
            this(activity, callbackAfterDateTimeSelect) {
        this.minTime = minTime
        this.maxTime = maxTime
        this.strinDateToShowInitiLLY = timeValue
        this.dateValueGivenPattern = timeValueGivenPattern
    }


    companion object {
        private val TAG = TimePickerDialog::class.java.simpleName
    }

    private var dateValueGivenPattern: String? = null
    private var strinDateToShowInitiLLY: String? = null
    private var minTime: String? = null
    private var maxTime: String? = null
    private var callbackAfterDateTimeSelect: CallbackAfterDateTimeSelect? = callbackAfterDateTimeSelect
    private var dateAndTime = ""
    private lateinit var layoutBinding: DateTimePickerLayoutBinding
    private var forgotPasswordViewModel: TimePickerViewModel? = null


    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        layoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.date_time_picker_layout, null, false
        )
        // setting viewmodel to layout bind
        forgotPasswordViewModel = ViewModelProviders.of(this).get(TimePickerViewModel::class.java)
        layoutBinding.setVariable(BR.viewModel, forgotPasswordViewModel)
        forgotPasswordViewModel?.navigator = this
        isCancelable = false

        with(layoutBinding) {

            datePicker.visibility = View.VISIBLE
            tvSubmit.setOnClickListener(this@TimePickerDialog)
            tvCancel.setOnClickListener(this@TimePickerDialog)

            datePicker.visibility = View.GONE
            timePicker.visibility = View.VISIBLE

            if (strinDateToShowInitiLLY != null && !TextUtils.isEmpty(strinDateToShowInitiLLY?.trim()) && dateValueGivenPattern != null) {
                if (dateValueGivenPattern.equals("hh:mm a")) {
                    val sdf = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT_FOR_TIME_PICKER_WILL_TAKE_FROM_OTHER_PAGE)
                    val date: Date = sdf.parse(strinDateToShowInitiLLY!!)!!
                    val c = Calendar.getInstance()
                    c.time = date
                    val hourFromGivenValue = c.get(Calendar.HOUR_OF_DAY)
                    val minuteFromGivenValue = c.get(Calendar.MINUTE)
                    timePicker.hour = hourFromGivenValue
                    timePicker.minute = minuteFromGivenValue

                    if (maxTime != null) {
//                        tvSubmit.isEnabled = false
//                        tvSubmit.foreground = ColorDrawable(ContextCompat.getColor(activity, R.color.transparent_white))
                        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                            Log.d(TAG, "--check_selected_hour-- $hourOfDay")
                            Log.d(TAG, "--check_selected_minute-- $minute")
                            Log.d(TAG, "--check_given_hour-- $hourFromGivenValue")
                            Log.d(TAG, "--check_given_minute-- $minuteFromGivenValue")
                            if (hourOfDay < hourFromGivenValue) {
                                tvSubmit.isEnabled = true
                                tvSubmit.foreground = ColorDrawable(ContextCompat.getColor(activity, R.color.transparent))
                            } else if ((hourOfDay == hourFromGivenValue) && (minute <= minuteFromGivenValue)) {
                                tvSubmit.isEnabled = true
                                tvSubmit.foreground = ColorDrawable(ContextCompat.getColor(activity, R.color.transparent))
                            } else {
                                tvSubmit.isEnabled = false
                                tvSubmit.foreground = ColorDrawable(ContextCompat.getColor(activity, R.color.transparent_white))
                            }
                        }
                    }
                }
            }

        }

        return layoutBinding.root
    }


    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onClick(v: View?) {
        with(layoutBinding) {
            if (v!!.id == tvSubmit.id) {
                dateAndTime = getTimeFromTimePicker()
                callbackAfterDateTimeSelect?.selectDateTime(dateAndTime.trim())
                dismiss()

            }
            if (v.id == tvCancel.id) {
                callbackAfterDateTimeSelect?.selectDateTime("")
                dismiss()
            }
        }
    }


    private fun getTimeFromTimePicker(): String {
        var hour: Int = layoutBinding.timePicker.hour
        val min: Int = layoutBinding.timePicker.minute
        val format: String
        when {
            hour == 0 -> {
                hour += 12
                format = "AM"
            }
            hour == 12 -> {
                format = "PM"
            }
            hour > 12 -> {
                hour -= 12
                format = "PM"
            }
            else -> {
                format = "AM"
            }
        }

        var finalValue = ""
        finalValue += if (hour in 0..9) {
            "0${hour}"
        } else {
            "$hour"
        }
        finalValue += if (min in 0..9) {
            ":0${min}"
        } else {
            ":${min}"
        }
        finalValue += " $format"
        return finalValue
    }
}