package com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.hospital.requestdoctor
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentHospitalPaymentTransactionViewModel  : BaseViewModel<FragmentHospitalPaymentTransactionNavigator>(){
    fun apidoctorlist(homeSearchRequestRequestBody: requestdoctor) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.PaymenetModule(homeSearchRequestRequestBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorDepartmentListingResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorDoctorDepartmentListingResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

}