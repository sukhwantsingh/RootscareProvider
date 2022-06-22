package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.newappointment

import com.google.gson.JsonObject
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentRequestedAppointmentForNurseNavigator {
    fun successGetDoctorRequestAppointmentResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
    fun successPushNotification(response: JsonObject)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}