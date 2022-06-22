package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.request.videoPushRequest.VideoPushRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FragmentDoctorAppointmentDetailsViewModel : BaseViewModel<FragmentDoctorAppointmentDetailsNavigator>() {


    fun getDetails(getDoctorUpcomingAppointmentRequest: CommonUserIdRequest) {
//        val disposable = apiServiceWithGsonFactory.getAppointmentDetails(getDoctorUpcomingAppointmentRequest)
//            .subscribeOn(_scheduler_io)
//            .observeOn(_scheduler_ui)
//            .subscribe({ response ->
//                if (response != null) {
//                    // Store last login time
//                    Log.d("check_response", ": " + Gson().toJson(response))
//                    navigator.onSuccessDetails(response)
//                    /* Saving access token after singup or login */
//
//                } else {
//                    Log.d("check_response", ": null response")
//                }
//            }, { throwable ->
//                run {
//                    navigator.onThrowable(throwable)
//                    Log.d("check_response_error", ": " + throwable.message)
//                }
//            })
//
//        compositeDisposable.add(disposable)
    }


    fun apiupdatedoctorappointmentrequest(
        updateAppointmentRequestBody: UpdateAppointmentRequest
    ) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiUpdateDoctorAppointmentRequest(updateAppointmentRequestBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetDoctorRequestAppointmentUpdateResponse(response)
                    /* Saving access token after singup or login */

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

    fun apiHitForMarkAsComplete(getDoctorUpcommingAppointmentRequest: CommonUserIdRequest) {
        val disposable = apiServiceWithGsonFactory.apiHitForMarkAsComplete(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessMarkAsComplete(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetDoctorTodaysAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun uploadPrescription(
        patient_id: RequestBody,
        doctor_id: RequestBody,
        appointment_id: RequestBody,
        prescription_number: RequestBody,
        prescription: MultipartBody.Part? = null
    ) {
        val disposable: Disposable? = apiServiceWithGsonFactory.uploadPrescription(
            patient_id,doctor_id,appointment_id, prescription_number, prescription
        )
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessUploadPrescription(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetDoctorTodaysAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })
        compositeDisposable.add(disposable!!)
    }

    fun apiSendVideoPushNotification(videoPushRequest: VideoPushRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.sendVideoPushNotification(videoPushRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successVideoPushResponse(response)
                    /* Saving access token after sign up or login */
//                    if (response.result != null) {
//                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorVideoPushResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

}