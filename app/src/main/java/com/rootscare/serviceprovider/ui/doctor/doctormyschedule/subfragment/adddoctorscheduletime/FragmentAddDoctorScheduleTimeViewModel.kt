package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.myscheduleaddtimeslot.AddTimeSlotRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentAddDoctorScheduleTimeViewModel: BaseViewModel<FragmentAddDoctorScheduleTimeNavigator>() {

    fun saveTimeSlot(request: AddTimeSlotRequest) {
        val disposable = apiServiceWithGsonFactory.apiHitSaveTimeSlotForDoctor(request)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessTimeSlotSave(response)

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

    fun saveTimeSlotHospital(request: AddTimeSlotRequest) {
        val disposable = apiServiceWithGsonFactory.apiHitSaveTimeSlotForHospitalDoctor(request)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessTimeSlotSave(response)

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

    fun saveHomeTimeSlot(request: AddTimeSlotRequest) {
        val disposable = apiServiceWithGsonFactory.apiHitSaveHomeVisitSlotForDoctor(request)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessTimeSlotSave(response)

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