package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment

import android.util.Log
import com.rootscare.data.model.request.hospital.CommonHospitalId
import com.rootscare.data.model.request.hospital.DeleteDepartment
import com.rootscare.data.model.request.hospital.EditDepart
import com.rootscare.data.model.request.hospital.EditNew
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentHospitalUpdateProfileViewModel : BaseViewModel<FragmentHospitalProfileUpdateNavigator>() {
    fun apicaregiverprofile(commonUserIdRequest: CommonHospitalId) {
        val disposable = apiServiceWithGsonFactory.apihospitaldepartment(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                  /*  Log.d("check_response", ": " + Gson().toJson(response))
                    val tempModel = Gson().fromJson(appSharedPref?.loginmodeldata, LoginResponse::class.java)
                    tempModel?.result?.image = response.result?.image
                    val loginresposeJson = Gson().toJson(tempModel)
                    appSharedPref?.loginmodeldata=loginresposeJson*/

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
    fun apideletedepartprofile(commonUserIdRequest: DeleteDepartment) {
        val disposable = apiServiceWithGsonFactory.apideletedepartment(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                  /*  Log.d("check_response", ": " + Gson().toJson(response))
                    val tempModel = Gson().fromJson(appSharedPref?.loginmodeldata, LoginResponse::class.java)
                    tempModel?.result?.image = response.result?.image
                    val loginresposeJson = Gson().toJson(tempModel)
                    appSharedPref?.loginmodeldata=loginresposeJson*/

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
    fun apieditepartprofile(commonUserIdRequest: EditDepart) {
        val disposable = apiServiceWithGsonFactory.apieditdepartment(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                  /*  Log.d("check_response", ": " + Gson().toJson(response))
                    val tempModel = Gson().fromJson(appSharedPref?.loginmodeldata, LoginResponse::class.java)
                    tempModel?.result?.image = response.result?.image
                    val loginresposeJson = Gson().toJson(tempModel)
                    appSharedPref?.loginmodeldata=loginresposeJson*/

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
    fun apieditnewepartprofile(commonUserIdRequest: EditNew) {
        val disposable = apiServiceWithGsonFactory.apieditnewdepartment(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                  /*  Log.d("check_response", ": " + Gson().toJson(response))
                    val tempModel = Gson().fromJson(appSharedPref?.loginmodeldata, LoginResponse::class.java)
                    tempModel?.result?.image = response.result?.image
                    val loginresposeJson = Gson().toJson(tempModel)
                    appSharedPref?.loginmodeldata=loginresposeJson*/

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