package com.rootscare.serviceprovider.ui.doctor.profile.bankDetails

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.RequestBody

class FragmentBankDetailsViewModel : BaseViewModel<FragmentDoctorBankDetailsNavigator>() {
    private val TAG = "FragmentBankDetails"


    fun updateBankDetails(
        userId: RequestBody,
        id: RequestBody,
        bankName: RequestBody,
        yourName: RequestBody,
        accountNo: RequestBody,
        iranNo: RequestBody,
        swiftNo: RequestBody,
        message: RequestBody
    ) {
        val disposable =
            apiServiceWithGsonFactory.apiHitUpdateBankDetails(
                userId, id, bankName, yourName, accountNo, iranNo, swiftNo,
                message
            )
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        Log.d("check_response", ": " + Gson().toJson(response))

                        navigator.onSuccessUpdateBankDetails(response)
                        val loginResponse = Gson().fromJson(appSharedPref?.loginmodeldata!!, LoginResponse::class.java)

                        loginResponse.result?.bankDetails?.add(0, response.result!!)
//                        response.result?.let { loginResponse.result?.bankDetails?.set(0, it) }
                        val loginResponseJson = Gson().toJson(loginResponse)

                        appSharedPref?.loginmodeldata = loginResponseJson

                    } else {
                        Log.d("check_response", ": null response")
                    }
                }, { throwable ->
                    run {
                        navigator.errorInApi(throwable)
                        Log.d("check_response_error", ": " + throwable.message)
                    }
                })

        compositeDisposable.add(disposable)
    }
}