package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.request.pushNotificationRequest.PushNotificationRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentRequestedAppointmentViewModel : BaseViewModel<FragmentRequestedAppointmentNavigator>() {
    fun apiDoctorAppointmentRequestList(getDoctorUpcomingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apidoctorappointmentrequestlist(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetDoctorRequestAppointmentResponse(response)
                    /* Saving access token after signup or login */
                    if (response.result != null) {

//                        appSharedPref?.loginmodeldata = loginresposeJson
//                        appSharedPref?.loginUserType=response?.result?.userType


                        //To get the data
//                        val json: String = mPrefs.getString("MyObject", "")
//                        val json: String =appSharedPref?.loginmodeldata!!
//                        val obj: Result = gson.fromJson(json, Result::class.java)
//                        appSharedPref?.userName=response?.result?.firstName+" "+response?.result?.lastName
//                        appSharedPref?.userEmail=response?.result?.email
//                        appSharedPref?.userImage=response?.result?.image


                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetDoctorRequestAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiSendPush(pushNotificationRequest: PushNotificationRequest) {
        val disposable = apiServiceWithGsonFactory.sendPushNotification(pushNotificationRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successPushNotification(response)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetDoctorRequestAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiUpdateDoctorAppointmentRequest(
        updateAppointmentRequestBody: UpdateAppointmentRequest, position: Int
    ) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiUpdateDoctorAppointmentRequest(updateAppointmentRequestBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetDoctorRequestAppointmentUpdateResponse(response, position)
                    /* Saving access token after singup or login */
                    if (response.result != null) {

//                        appSharedPref?.loginmodeldata=loginresposeJson
//                        appSharedPref?.loginUserType=response?.result?.userType


                        //To get the data
//                        val json: String = mPrefs.getString("MyObject", "")
//                        val json: String =appSharedPref?.loginmodeldata!!
//                        val obj: Result = gson.fromJson(json, Result::class.java)
//                        appSharedPref?.userName=response?.result?.firstName+" "+response?.result?.lastName
//                        appSharedPref?.userEmail=response?.result?.email
//                        appSharedPref?.userImage=response?.result?.image


                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetDoctorRequestAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}