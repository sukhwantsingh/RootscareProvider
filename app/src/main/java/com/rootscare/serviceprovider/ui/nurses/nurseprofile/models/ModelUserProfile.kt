package com.rootscare.serviceprovider.ui.nurses.nurseprofile.models


import androidx.annotation.Keep

@Keep
data class ModelUserProfile(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val booking_count: Int?,
        val avg_rating: String?,
        val description: String?,
        val dob: String?,
        val email: String?,
        val experience: String?,
        val first_name: String?,
        val work_area: String?,
        val hospital_id: String?,
        val hospital_name: String?,
        val gender: String?,
        val id_image: String?,
        val id_number: String?,
        val image: String?,
        val last_name: String?,
        val phone_number: String?,
        val country_code: String?,
        val currency_symbol: String?,
        val place_key: String?,
        val qualification: String?,
        val qualification_certificate: String?,
        val qualification_data: ArrayList<QualificationData?>?,
        val department_list: ArrayList<DepartmentList?>?,
        val scfhs_image: String?,
        val scfhs_number: String?,
        val speciality: String?,
        val user_id: String?,
        val hosp_moh_lic_no: String?,
        val lab_test_count: String?,
        val address: String?,
        val doctors_count: String?,
        val hosp_reg_no: String?,
        val moh_lic_image: String?,
        val hosp_reg_image: String?,
        val display_user_type: String?,
        val user_type: String?
    ) {
        @Keep
        data class QualificationData(
            val id: String?,
            val qualification: String?,
            val qualification_certificate: String?
        )

        @Keep
        data class DepartmentList(
            val department_id: String?,
            val title: String?
        )
    }
}