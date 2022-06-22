package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.hospital.getpathodetails
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentHospitalSampleCollectionDetailsViewModel: BaseViewModel<FragmentHospitalSampleCollectionDetailsnavigator>() {
    fun apidoctorprofile(
        commonUserIdRequest: getpathodetails

    ) {

            val disposable = apiServiceWithGsonFactory.apipathodetails(commonUserIdRequest)
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        // Store last login time
                        Log.d("check_response", ": " + Gson().toJson(response))
                        navigator.successGetDoctorProfileResponse(response)
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