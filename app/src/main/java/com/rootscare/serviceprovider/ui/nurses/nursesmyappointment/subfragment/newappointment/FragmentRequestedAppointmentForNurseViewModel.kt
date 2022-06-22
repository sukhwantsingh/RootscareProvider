package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.newappointment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.request.pushNotificationRequest.PushNotificationRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentRequestedAppointmentForNurseViewModel : BaseViewModel<FragmentRequestedAppointmentForNurseNavigator>() {
    fun apiNurseAppointmentRequestList(getDoctorUpcommingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
        val disposable = apiServiceWithGsonFactory.apiNurseAppointmentRequestList(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetDoctorRequestAppointmentResponse(response)
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

    fun apiUpdateNurseAppointmentRequest(
        updateAppointmentRequestBody: UpdateAppointmentRequest,
        position: Int
    ) {
        val disposable = apiServiceWithGsonFactory.apiUpdateNurseAppointmentRequest(updateAppointmentRequestBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetDoctorRequestAppointmentUpdateResponse(response, position)

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
}