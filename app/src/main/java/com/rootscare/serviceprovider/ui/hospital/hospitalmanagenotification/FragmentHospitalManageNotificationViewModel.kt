package com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.commonuseridrequest.CommonNotificationIdRequest
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentHospitalManageNotificationViewModel : BaseViewModel<FragmentHospitalManageNotificationNavigator>() {

    fun apiGetAllUserNotifications(commonUserIdRequest: CommonUserIdRequest) {
        val disposable = apiServiceWithGsonFactory.apiUserNotification(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successNotificationListResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result != null) {
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorPatientPaymentHistoryResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
    fun apiUpdateRead(commonUserIdRequest: CommonNotificationIdRequest) {
        val disposable = apiServiceWithGsonFactory.apiUpdateNotification(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.successUpdateRead(response)
                }
            }, { throwable ->
                run {
                    navigator.errorPatientPaymentHistoryResponse(throwable)
                }
            })

        compositeDisposable.add(disposable)
    }
}