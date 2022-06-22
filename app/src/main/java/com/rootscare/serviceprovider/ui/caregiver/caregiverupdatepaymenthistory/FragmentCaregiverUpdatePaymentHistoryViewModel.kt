package com.rootscare.serviceprovider.ui.caregiver.caregiverupdatepaymenthistory

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentCaregiverUpdatePaymentHistoryViewModel : BaseViewModel<FragmentCaregiverUpdatePaymentHistoryNavigator>() {

    fun getPaymentHistoryFromApi(getDoctorUpcomingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
        val disposable = apiServiceWithGsonFactory.getPaymentHistoryForCaregiver(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessPaymentList(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}