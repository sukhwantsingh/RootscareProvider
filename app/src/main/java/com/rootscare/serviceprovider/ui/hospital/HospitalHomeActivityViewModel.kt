package com.rootscare.serviceprovider.ui.hospital

import android.util.Log
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivityNavigator
import okhttp3.RequestBody

class HospitalHomeActivityViewModel : BaseViewModel<HospitalHomeActivityNavigator>() {

    fun apiLogout(appointmentRequest: RequestBody) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiLogoutUser(appointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successLogoutResponse(response)
                    /* Saving access token after signup or login */
                    if (response.result != null) {
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorLogoutResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiUnreadNotifications(reqBody: RequestBody?) {
        val disposable = apiServiceWithGsonFactory.apiNotificationUnreadCounts(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.onSuccessUnreadNotification(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorLogoutResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })
        compositeDisposable.add(disposable)
    }
}