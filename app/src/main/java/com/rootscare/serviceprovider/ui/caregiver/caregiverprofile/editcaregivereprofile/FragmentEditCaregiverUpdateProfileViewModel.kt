package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.editcaregivereprofile

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FragmentEditCaregiverUpdateProfileViewModel : BaseViewModel<FragmentEditCaregiverUpdateProfileNavigator>() {
    private val TAG = "FragmentEditDoctorProfi"


//    fun apiDepartmentList() {
//        val disposable = apiServiceWithGsonFactory.apidepartmentlist()
//            .subscribeOn(_scheduler_io)
//            .observeOn(_scheduler_ui)
//            .subscribe({ response ->
//                if (response != null) {
//                    Log.d("check_response", ": " + Gson().toJson(response))
//                    navigator.successDepartmentListResponse(response)
//                } else {
//                    Log.d("check_response", ": null response")
//                }
//            }, { throwable ->
//                run {
//                    navigator.errorInApi(throwable)
//                    Log.d("check_response_error", ": " + throwable.message)
//                }
//            })
//
//        compositeDisposable.add(disposable)
//    }

    fun apiDoctorProfile(
        commonUserIdRequest: CommonUserIdRequest,
        userType: String?
    ) {
        if (userType.equals("caregiver")) {
            val disposable = apiServiceWithGsonFactory.apicaregivereprofile(commonUserIdRequest)
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
        } else if (userType.equals("nurse")) {
            val disposable = apiServiceWithGsonFactory.apicaregivereprofile(commonUserIdRequest)
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


    fun apiHitForUpdateProfileWithProfileAndCertificationImage(
        user_id: RequestBody,
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        mobile_number: RequestBody,
        dob: RequestBody,
        gender: RequestBody,
        qualification: RequestBody? = null,
        passing_year: RequestBody? = null,
        institute: RequestBody? = null,
        description: RequestBody,
        experience: RequestBody,
        available_time: RequestBody,
        fees: RequestBody,
        department: RequestBody,
        startTime: RequestBody,
        endTime: RequestBody,
        image: MultipartBody.Part? = null,
        certificate: List<MultipartBody.Part>? = null
    ) {

        Log.d(TAG, "apiHitForUdateProfileWithProfileAndCertificationImage: false")
        val disposable = apiServiceWithGsonFactory.apiHitUpdateProfileWithImageAndCertificate(
            user_id,
            first_name,
            last_name,
            email,
            mobile_number,
            dob,
            gender,
            qualification,
            passing_year,
            institute,
            description,
            experience,
            available_time,
            fees,
            department,
            startTime,
            endTime,
            image,
            certificate
        )
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    val gson = Gson()
                    val loginresposeJson = gson.toJson(response)
                    appSharedPref?.loginmodeldata = loginresposeJson

                    navigator.onSuccessEditProfile(response)
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