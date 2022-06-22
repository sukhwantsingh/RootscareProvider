package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.upcomingappointment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.filterappointmentrequest.FilterAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentUpcommingAppointmentForNurseViewModel : BaseViewModel<FragmentUpcommingAppointmentForNurseNavigator>()  {

    fun apiNurseUpComingAppointmentList(getDoctorUpcomingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
        val disposable = apiServiceWithGsonFactory.apiCaregiverUpComingAppointmentList(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorUpcomingAppointmentResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorDoctorUpcomingAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiFilterNurseUpComingAppointmentList(filterAppointmentRequest: FilterAppointmentRequest) {
        val disposable = apiServiceWithGsonFactory.apiFilterCaregiverUpComingAppointmentList(filterAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorUpcomingAppointmentResponse(response)
                    /* Saving access token after singup or login */
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorDoctorUpcomingAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }





    fun apiUpdateNurseAppointmentRequest(
        updateAppointmentRequestBody: UpdateAppointmentRequest,
        position: Int
    ) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
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
                    navigator.errorDoctorUpcomingAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}