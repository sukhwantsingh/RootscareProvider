package com.rootscare.serviceprovider.ui.caregiver.caregivermyschedule.subfragment.manageschedule

import android.util.Log
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.RequestBody

class FragmentCaregiverUpdateManageRateViewModel: BaseViewModel<FragmentCaregiverUpdateManageRateNavigator>() {

    fun savePrice(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.savePriceForSlot1(requestUserRegister)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterSavePrice(response)
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

    fun saveHourlyPrice(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.saveHourlyPrice(requestUserRegister)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterSavePrice(response)
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