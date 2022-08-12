package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models


import androidx.annotation.Keep

@Keep
data class ModelAppointmentDetails(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val OTP: String?,
        val acceptance_status: String?,
        val app_date: String?,
        val app_time: String?,
        val appointment_status: String?,
        val appointment_date: String?,
        val from_time: String?,
        val to_time: String?,
        val avg_rating: String?,
        val booking_date: String?,
        val booking_type: String?,
        val distance_fee: String?,
        val family_member_id: String?,
        val id: String?,
        val order_id: String?,
        val patient_address: String?,
        val patient_contact: String?,
        val patient_id: String?,
        val patient_lat: String?,
        val patient_long: String?,
        val patient_name: String?,
        val paymentStatus: String?,
        val paymentType: String?,
        val price: String?,
        val sub_total_price: String?,
        val provider_experience: String?,
        val provider_image: String?,
        val provider_name: String?,
        val hospital_name: String?,
        val hospital_id: String?,
        val service_type: String?,
        val speciality: String?,
        val symptom_recording: String?,
        val symptom_text: String?,
        val slot_booking_id: String?,
        val patient_prescription: String?,
        val provider_prescription: String?,
        val provider_upd: String?,
        val task_details: List<TaskDetail?>?,
        val report: ArrayList<LabTestReports?>?,
        val task_id: String?,
        val task_time: String?,
        val task_type: String?,
        val user_id: String?,
        val vat: String?,
        val vat_percent: String?
    ) {
        @Keep
        data class TaskDetail(
            val id: String?,
            val name: String?,
            val price: String?
        )
        @Keep
        data class LabTestReports(
            val id: String?,
            val report: String?,
            val upload_date: String?
        )
    }
}