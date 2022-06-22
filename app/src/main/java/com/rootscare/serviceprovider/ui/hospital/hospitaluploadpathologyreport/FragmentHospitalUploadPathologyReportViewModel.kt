package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.hospital.getdoctorhospital
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentHospitalUploadPathologyReportViewModel : BaseViewModel<FragmentHospitalUploadPathologyReportNavigator>() {

    fun apiPathologyReportList(getDoctorUpcomingAppointmentRequest: getdoctorhospital) {
        val disposable = apiServiceWithGsonFactory.apiReportPathology(getDoctorUpcomingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successPathologyListResponse(response)
//                    navigator.responseListPastAppointment(sortListByDate(response))
                    /* Saving access token after sign up or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorPathologyListResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
//    fun searchApiDoctorAppointmentUpcomingList(getDoctorUpcomingAppointmentRequest: LabSearch) {
//        val disposable = apiServiceWithGsonFactory.searchApiLab(getDoctorUpcomingAppointmentRequest)
//            .subscribeOn(_scheduler_io)
//            .observeOn(_scheduler_ui)
//            .subscribe({ response ->
//                if (response != null) {
//                    // Store last login time
//                    Log.d("check_response", ": " + Gson().toJson(response))
//                    navigator.responseLisCancelledAppointment(response)
////                    navigator.responseListPastAppointment(sortListByDate(response))
//                    /* Saving access token after singup or login */
//
//                } else {
//                    Log.d("check_response", ": null response")
//                }
//            }, { throwable ->
//                run {
//                    navigator.errorGetDoctorRequestAppointmentResponse(throwable)
//                    Log.d("check_response_error", ": " + throwable.message)
//                }
//            })
//
//        compositeDisposable.add(disposable)
//    }
}