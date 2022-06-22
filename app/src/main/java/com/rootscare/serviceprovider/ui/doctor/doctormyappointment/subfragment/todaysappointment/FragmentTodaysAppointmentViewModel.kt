package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.latikaseafood.utils.DateTimeUtils
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.request.pushNotificationRequest.PushNotificationRequest
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*

class FragmentTodaysAppointmentViewModel : BaseViewModel<FragmentTodaysAppointmentNavigator>() {
    fun apiDoctorTodayAppointmentList(getDoctorUpcomingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apidoctortodayappointmentlist(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    sortListByDate(response)
                    navigator.successGetDoctorTodaysAppointmentResponse(response)

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
                    navigator.errorGetDoctorTodaysAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiHitForMarkAsComplete(getDoctorUpcommingAppointmentRequest: CommonUserIdRequest, position: Int) {
        val disposable = apiServiceWithGsonFactory.apiHitForMarkAsComplete(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessMarkAsComplete(response, position)
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

    fun apiupdatedoctorappointmentrequest(
        updateAppointmentRequestBody: UpdateAppointmentRequest,
        position: Int
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
        val disposable: Disposable = apiServiceWithGsonFactory.uploadPrescription(
            patient_id, doctor_id, appointment_id, prescription_number, prescription
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
        compositeDisposable.add(disposable)
    }


    private fun sortListByDate(response: GetDoctorTodaysAppointmentResponse): GetDoctorTodaysAppointmentResponse {
        if (response.result != null && response.result.size > 0) {
            val tempData = response.result
            for (item in tempData) {
                if (item?.appointmentDate != null && !TextUtils.isEmpty(item.appointmentDate.trim()) && item.fromTime != null && !TextUtils.isEmpty(
                        item.fromTime.trim()
                    )
                ) {
                    val appointmentDateTimeAsString = "${item.appointmentDate} ${item.fromTime}"
                    val appointmentDateTimeAsDate = DateTimeUtils.getDateByGivenString(appointmentDateTimeAsString, "yyyy-MM-dd hh:mm a")
                    val currentDate = Date()
                    item.isCompletedButtonVisible =
                        !(currentDate.compareTo(appointmentDateTimeAsDate) < 0 || currentDate.compareTo(appointmentDateTimeAsDate) == 0)
                }
            }
        }
        return response
    }
}