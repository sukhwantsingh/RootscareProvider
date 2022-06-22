package com.rootscare.data.model.response.hospital

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import java.util.*

data class HospitalLabUpload(
    @field:SerializedName("result")
    val result: LinkedList<ResultSetLab?>? = null,
    @field:JsonProperty("code")
    @field:SerializedName("code")
    val code: String? = null,
    @field:JsonProperty("message")
    @field:SerializedName("message")
    val message: String? = null,
    @field:JsonProperty("status")
    @field:SerializedName("status")
    val status: Boolean? = null
)

data class ResultSetLab(

    @field:SerializedName("first_name")
    val first_name: String? = null,

    @field:SerializedName("last_name")
    val last_name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phone_number")
    val phone_number: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("age")
    var age: String? = null,

    @field:SerializedName("order_id")
    var order_id: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("pathology_test_id")
    val pathology_test_id: String? = null,

    @field:SerializedName("family_member_id")
    val family_member_id: String? = null,

    @field:SerializedName("patient_id")
    val patient_id: String? = null,

    @field:SerializedName("appointment_date")
    val appointment_date: String? = null,

    @field:SerializedName("lab")
    val lab: LinkedList<LabDetails>? = null


)

data class Lab(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("price")
    val price: String? = null
)