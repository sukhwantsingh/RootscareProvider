package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.newappointment

import com.google.gson.JsonObject
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentRequestedAppointmentForPhysiotherapistNavigator {
    fun successGetDoctorRequestAppointmentResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
    fun successPushNotification(response: JsonObject)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}