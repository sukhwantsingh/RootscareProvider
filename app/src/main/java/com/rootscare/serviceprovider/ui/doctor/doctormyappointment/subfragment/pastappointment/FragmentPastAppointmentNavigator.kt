package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment

import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment

interface FragmentPastAppointmentNavigator {

    fun responseListPastAppointment(response: ResponsePastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}