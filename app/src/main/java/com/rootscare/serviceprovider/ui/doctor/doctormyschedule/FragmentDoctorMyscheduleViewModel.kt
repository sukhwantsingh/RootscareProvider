package com.rootscare.serviceprovider.ui.doctor.doctormyschedule

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.myscheduleaddhospital.AddHospitalRequest
import com.rootscare.data.model.request.fetchScheduleData.FetchScheduleRequest
import com.rootscare.data.model.request.sendschedule.SendScheduleRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentDoctorMyscheduleViewModel: BaseViewModel<FragmentDoctorMyscheduleNavigator>() {

    fun sendProviderSchedule(sendScheduleRequest: SendScheduleRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.sendProviderSchedule(sendScheduleRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successSendSchedule(response)
                    /* Saving access token after singup or login */
                    if (response.result != null) {
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetPatientFamilyListResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun sendProviderScheduleForDoctor(sendScheduleRequest: SendScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.sendProviderSchedule(sendScheduleRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successSendSchedule(response)
               } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetPatientFamilyListResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun fetchScheduleData(sendScheduleRequest: FetchScheduleRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.fetchProviderSchedule(sendScheduleRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                //    navigator.successFetchSchedule(response)
                    /* Saving access token after singup or login */
                    if (response.result != null) {
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetPatientFamilyListResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun getTimeScheduleForOnline(mModel: FetchScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.apiDoctorOnlineScheduleSlots(mModel)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessOnlineSlots(response)
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

    fun getHomeVisitSlot(mModel: FetchScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.apiDoctorHomeVisitScheduleSlots(mModel)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessHomeVisitList(response)
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


    fun addHospital(request: AddHospitalRequest) {
        val disposable = apiServiceWithGsonFactory.addHospitalMyschedule(request)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                //    navigator.onnSuccessAddHospital(response)
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