package com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments

import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment

interface FragmentHospitalManageAppointmentsNavigator {
    fun responseListPastAppointmentDoctor(response: ResponsePastAppointment)
    fun responseListPastAppointmentHospital(response: ResponsePastAppointment)
    fun responseListUpcomingAppointmentDoctor(response: ResponsePastAppointment)
    fun responseListUpcomingAppointmentHospital(response: ResponsePastAppointment)
    fun responseLisCancelledAppointmentDoctor(response: ResponsePastAppointment)
    fun responseLisCancelledAppointmentHospital(response: ResponsePastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}