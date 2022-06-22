package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments


import androidx.annotation.Keep

@Keep
data class ModelAppointmentsListing(
    val code: String?,
    val message: String?,
    val result: ArrayList<Result?>?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val acceptance_status: String?,
        val app_date: String?,
        val app_time: String?,
        val appointment_status: String?,
        val appointment_type: String?,
        val booked_by: String?,
        val booking_date: String?,
        val booking_type: String?,
        val family_member_id: String?,
        val id: String?,
        val order_id: String?,
        val hospital_name: String?,
        val hospital_id: String?,
        val patient_id: String?,
        val patient_name: String?,
        val paymentStatus: String?,
        val paymentType: String?,
        val price: String?,
        val provider_name: String?,
        val service_type: String?,
        val speciality: String?,
        val symptom_recording: String?,
        val symptom_text: String?,
        val task_time: String?,
        val task_type: String?,
        val upload_prescription: String?
    )
}