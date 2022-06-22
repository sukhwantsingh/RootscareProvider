package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentCregiverUpdateProfileViewModel : BaseViewModel<FragmentCaregiverProfileUpdateNavigator>() {
    fun apicaregiverprofile(commonUserIdRequest: CommonUserIdRequest) {
        val disposable = apiServiceWithGsonFactory.apicaregivereprofile(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    val tempModel = Gson().fromJson(appSharedPref?.loginmodeldata, LoginResponse::class.java)
                    tempModel?.result?.image = response.result?.image
                    val loginresposeJson = Gson().toJson(tempModel)
                    appSharedPref?.loginmodeldata = loginresposeJson

                    navigator.successGetDoctorProfileResponse(response)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetDoctorProfileResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

}