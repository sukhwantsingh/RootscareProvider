package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment

import com.google.gson.JsonObject
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentRequestedAppointmentNavigator {
    fun successGetDoctorRequestAppointmentResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
    fun successPushNotification(response: JsonObject)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}