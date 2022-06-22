package com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement

import com.rootscare.data.model.response.hospital.ResponseLabPastAppointment

interface FragmentHospitalOrderManagementNavigator {
    fun responseListPastAppointment(response: ResponseLabPastAppointment)
    fun responseListUpcomingAppointment(response: ResponseLabPastAppointment)
    fun responseLisCancelledAppointment(response: ResponseLabPastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}