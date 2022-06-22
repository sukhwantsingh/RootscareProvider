package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.myscheduletimeslot.GetTimeSlotMyScheduleRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentdoctorManageScheduleViewModel : BaseViewModel<FragmentdoctorManageScheduleNavigator>() {


    fun getAllTimeSlotOfEveryday(getDoctorUpcommingAppointmentRequest: GetTimeSlotMyScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.getAllTimeSlotMyschedule(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessTimeSlotList(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun getAllTimeSlotOfDaySpecific(getDoctorUpcomingAppointmentRequest: GetTimeSlotMyScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.getDaySpecificTimeSlotMyschedule(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessTimeSlotList(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun getAllTimeSlotOfHome(getDoctorUpcomingAppointmentRequest: GetTimeSlotMyScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.getDaySpecificHomeSlot(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessTimeHomeSlotList(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiHitRemoveTimeSlot(position: Int, getDoctorUpcommingAppointmentRequest: GetTimeSlotMyScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.apiHitRemoveTimeSlotMySchedule(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterRemoveTimeSlot(position, response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiHitRemoveHomeSlot(position: Int, getDoctorUpcomingAppointmentRequest: GetTimeSlotMyScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.apiHitRemoveHomeSlot(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterRemoveHomeTimeSlot(position, response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }


}