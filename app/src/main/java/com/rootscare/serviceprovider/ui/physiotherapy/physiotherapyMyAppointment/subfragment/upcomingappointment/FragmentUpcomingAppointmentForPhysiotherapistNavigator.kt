package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.upcomingappointment

import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse

interface FragmentUpcomingAppointmentForPhysiotherapistNavigator {
    fun successDoctorUpcomingAppointmentResponse(doctorUpcomingAppointmentResponse: DoctorUpcomingAppointmentResponse?)
    fun errorDoctorUpcomingAppointmentResponse(throwable: Throwable?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
}