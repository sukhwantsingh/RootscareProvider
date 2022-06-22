package com.rootscare.serviceprovider.ui.manageDocLab.model


import androidx.annotation.Keep

@Keep
data class ModelHospitalDocs(
    val code: String?,
    val message: String?,
    val result: ArrayList<Result?>?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val avg_rating: String?,
        val booking_count: Int?,
        val email: String?,
        val experience: String?,
        val hospital_id: String?,
        val image: String?,
        val name: String?,
        val phone_number: String?,
        val speciality: String?,
        val user_id: String?,
        val user_type: String?
    )
}