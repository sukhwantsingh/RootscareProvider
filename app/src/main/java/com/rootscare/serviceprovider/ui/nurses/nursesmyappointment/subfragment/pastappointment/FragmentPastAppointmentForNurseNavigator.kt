package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.pastappointment

import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment

interface FragmentPastAppointmentForNurseNavigator {

    fun responseListPastAppointment(response: ResponsePastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}