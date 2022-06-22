package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments

import com.google.gson.JsonObject
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.videoPushResponse.VideoPushResponse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelAppointmentDetails

interface AppointmentNavigator {
    // Pending (Section)
    fun successPushNotification(response: JsonObject) {}

    fun onMarkAcceptReject(response: ModelAppointmentsListing?, position: Int) {}
    fun onMarkComplete(response: ModelAppointmentDetails?) {}
    fun onAppointmentDetail(response: ModelAppointmentDetails?) {}
    fun onSuccessResponse(response: ModelAppointmentsListing?) {}
    fun onPrescriptionUploaded(response: CommonResponse?) {}
    fun successVideoPushResponse(videoPushResponse: VideoPushResponse?){}
    fun errorInApi(throwable: Throwable?)

}