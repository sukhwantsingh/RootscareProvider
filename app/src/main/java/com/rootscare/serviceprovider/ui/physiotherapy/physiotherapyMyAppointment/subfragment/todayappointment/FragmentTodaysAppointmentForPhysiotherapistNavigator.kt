package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.todayappointment

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse

interface FragmentTodaysAppointmentForPhysiotherapistNavigator {
    fun successGetDoctorTodaysAppointmentResponse(getDoctorTodaysAppointmentResponse: GetDoctorTodaysAppointmentResponse?)
    fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?)
    fun onSuccessMarkAsComplete(response:CommonResponse, position:Int)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
}