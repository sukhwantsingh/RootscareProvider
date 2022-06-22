package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.pastappointment

import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment

interface FragmentPastAppointmentForPhysiotherapistNavigator {

    fun responseListPastAppointment(response: ResponsePastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}