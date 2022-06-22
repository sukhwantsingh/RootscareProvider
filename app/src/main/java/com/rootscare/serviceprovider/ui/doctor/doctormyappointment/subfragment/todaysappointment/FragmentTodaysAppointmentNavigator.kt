package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment

import com.google.gson.JsonObject
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse

interface FragmentTodaysAppointmentNavigator {
    fun successGetDoctorTodaysAppointmentResponse(getDoctorTodaysAppointmentResponse: GetDoctorTodaysAppointmentResponse?)
    fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?)
    fun onSuccessMarkAsComplete(response: CommonResponse, position: Int)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
    fun successPushNotification(response: JsonObject)
    fun onSuccessUploadPrescription(response: CommonResponse)
}