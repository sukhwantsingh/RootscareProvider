package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentPastAppointmentViewModel : BaseViewModel<FragmentPastAppointmentNavigator>() {
    fun apidoctorappointmentPastList(getDoctorUpcommingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
        val disposable = apiServiceWithGsonFactory.apidoctorappointmentPastList(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.responseListPastAppointment(response)
//                    navigator.responseListPastAppointment(sortListByDate(response))
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

//    private fun sortListByDate(response: ResponsePastAppointment): ResponsePastAppointment {
//        if (response.result != null && response.result?.size!! > 0) {
//            val tempData = response.result
//            val tempList = LinkedList<ResultItem>()
//            for (item in tempData!!) {
//                item.date = DateTimeUtils.getDateByGivenString("${item.appointmentDate} ${item.fromDate}", "yyyy-MM-dd hh:mm a")
//                tempList.add(item)
//            }
//            tempList.sortWith(Comparator { o1, o2 ->
//                (if (o1.date == null || o2.date == null) 0 else o2.date?.compareTo(o1.date))!!
//            })
//            response.result = tempList
//        }
//        return response
//    }
}