package com.rootscare.serviceprovider.ui.scheduless

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.fetchScheduleData.FetchScheduleRequest
import com.rootscare.data.model.request.sendschedule.SendScheduleRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class ScheduleActivityViewModel  : BaseViewModel<ScheduleActivityNavigator>() {

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

  fun sendProviderScheduleForDoctor(req: SendScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.sendProviderScheduleForDoctor(req)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successSendSchedule(response)
                    /* Saving access token after singup or login */

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

    fun fetchScheduleDataForDoctor(req: FetchScheduleRequest) {

        val disposable = apiServiceWithGsonFactory.fetchProviderScheduleForDoctor(req)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successFetchSchedule(response)

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

        val disposable = apiServiceWithGsonFactory.fetchProviderSchedule(sendScheduleRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successFetchSchedule(response)

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



    fun fetchScheduleForNurseTaskBased(sendScheduleRequest: FetchScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.fetchProviderScheduleForNurseTaskBase(sendScheduleRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successFetchSchedule(response)

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
    fun fetchScheduleForNurseHourlyBased(sendScheduleRequest: FetchScheduleRequest) {
        val disposable = apiServiceWithGsonFactory.fetchProviderScheduleForNurseHourBase(sendScheduleRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successFetchSchedule(response)

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
}