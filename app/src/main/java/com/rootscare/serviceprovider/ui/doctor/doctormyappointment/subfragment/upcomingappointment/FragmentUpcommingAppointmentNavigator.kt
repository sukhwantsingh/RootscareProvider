package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment

import com.google.gson.JsonObject
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse

interface FragmentUpcommingAppointmentNavigator {
    fun successDoctorUpcomingAppointmentResponse(doctorUpcomingAppointmentResponse: DoctorUpcomingAppointmentResponse?)
    fun errorDoctorUpcomingAppointmentResponse(throwable: Throwable?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )

    fun successPushNotification(response: JsonObject)
}