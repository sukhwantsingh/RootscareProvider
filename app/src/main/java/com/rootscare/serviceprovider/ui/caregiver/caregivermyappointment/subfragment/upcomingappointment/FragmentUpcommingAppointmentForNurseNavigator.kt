package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.upcomingappointment

import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse

interface FragmentUpcommingAppointmentForNurseNavigator {
    fun successDoctorUpcomingAppointmentResponse(doctorUpcomingAppointmentResponse: DoctorUpcomingAppointmentResponse?)
    fun errorDoctorUpcomingAppointmentResponse(throwable: Throwable?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
}