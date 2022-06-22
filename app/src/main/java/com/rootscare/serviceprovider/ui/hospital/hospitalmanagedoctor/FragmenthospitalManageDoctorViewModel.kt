package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.hospital.hospitaldelte
import com.rootscare.data.model.request.hospital.requestdoctor
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmenthospitalManageDoctorViewModel : BaseViewModel<FragmenthospitalManageDoctorNavigator>() {



    fun apidoctorlist(homeSearchRequestRequestBody: requestdoctor) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apidoctorlistviahospitalModule(homeSearchRequestRequestBody)
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
    fun apidoctordelete(homeSearchRequestRequestBody: hospitaldelte) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apideletedoctor(homeSearchRequestRequestBody)
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


    //Doctor Search Api Call
}