package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.videoPushResponse.VideoPushResponse

interface FragmentDoctorAppointmentDetailsNavigator {


    fun onSuccessDetails(response: AppointmentDetailsResponse)

    fun onThrowable(throwable: Throwable)

    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?
    )
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)

    fun onSuccessMarkAsComplete(response: CommonResponse)

    fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?)

    fun onSuccessUploadPrescription(response:CommonResponse)

    fun successVideoPushResponse(videoPushResponse: VideoPushResponse)

    fun errorVideoPushResponse(throwable: Throwable?)
}