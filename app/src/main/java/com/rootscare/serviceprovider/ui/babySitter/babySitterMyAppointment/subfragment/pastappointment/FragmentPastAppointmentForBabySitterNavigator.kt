package com.rootscare.serviceprovider.ui.babySitter.babySitterMyAppointment.subfragment.pastappointment

import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment

interface FragmentPastAppointmentForBabySitterNavigator {

    fun responseListPastAppointment(response: ResponsePastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}