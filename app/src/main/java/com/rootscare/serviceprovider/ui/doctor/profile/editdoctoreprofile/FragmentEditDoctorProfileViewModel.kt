package com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.request.hospital.CommonHospitalId
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FragmentEditDoctorProfileViewModel : BaseViewModel<FragmentEditDoctorProfileNavigator>() {
    private val TAG = "FragmentEditDoctorProfile"

    fun apiDepartmentList(commonUserIdRequest: CommonHospitalId) {
        val disposable = apiServiceWithGsonFactory.apihospitaldepartment(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDepartmentListResponse(response)
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

    fun apiDoctorProfile(commonUserIdRequest: CommonUserIdRequest,userType: String?) {
        when {
            (userType.equals("doctor") || userType.equals("hospital_doctor")) -> {
                val disposable = apiServiceWithGsonFactory.apidoctorprofile(commonUserIdRequest)
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
            userType.equals("nurse") -> {
                val disposable = apiServiceWithGsonFactory.apinurseprofile(commonUserIdRequest)
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
            userType.equals("physiotherapy") -> {
                val disposable = apiServiceWithGsonFactory.apiPhysiotherapyProfile(commonUserIdRequest)
                    .subscribeOn(_scheduler_io)
                    .observeOn(_scheduler_ui)
                    .subscribe({ response ->
                        if (response != null) {
                            // Store last login time
                            Log.d("check_response", ": " + Gson().toJson(response))
                            val tempModel = Gson().fromJson(appSharedPref?.loginmodeldata, LoginResponse::class.java)
                            tempModel?.result?.image = response.result?.image
                            val loginResponseJson = Gson().toJson(tempModel)
                            appSharedPref?.loginmodeldata = loginResponseJson

                            navigator.successGetPhysiotherapyProfileResponse(response)

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
    }


    fun apiHitForUdateProfileWithProfileAndCertificationImage(
        user_id: RequestBody,   first_name: RequestBody,   last_name: RequestBody,
        email: RequestBody, mobile_number: RequestBody,    dob: RequestBody,
        gender: RequestBody, qualification: RequestBody? = null,   passing_year: RequestBody? = null,
        institute: RequestBody? = null,  description: RequestBody,  experience: RequestBody,
        available_time: RequestBody,  fees: RequestBody,  department: RequestBody,
        startTime: RequestBody, endTime: RequestBody,  image: MultipartBody.Part? = null,
        certificate: List<MultipartBody.Part>? = null
    ) {

        Log.d("TAG", "apiHitForUpdateProfileWithProfileAndCertificationImage: false")
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
            certificate,

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

    fun apiUserProfile(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiUserprofile(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
//                    // Store last login time
//                    Log.d("check_response", ": " + Gson().toJson(response))
//                    val tempModel = Gson().fromJson(appSharedPref?.loginmodeldata, LoginResponse::class.java)
//                    tempModel?.result?.image = response.result?.image
//                    val loginResponseJson = Gson().toJson(tempModel)
//                    appSharedPref?.loginmodeldata = loginResponseJson

                    navigator.onSuccessUserProfile(response)

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