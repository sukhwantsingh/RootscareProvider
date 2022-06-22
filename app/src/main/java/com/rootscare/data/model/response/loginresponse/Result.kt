package com.rootscare.data.model.response.loginresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(
    @field:JsonProperty("date")
    @field:SerializedName("date")
    val date: String? = null,
    @field:JsonProperty("qualification_data")
    @field:SerializedName("qualification_data")
    val qualificationData: ArrayList<QualificationDataItem?>? = null,
    @field:JsonProperty("fcm_token")
    @field:SerializedName("fcm_token")
    val fcm_token: String? = null,
    @field:JsonProperty("device_type")
    @field:SerializedName("device_type")
    val device_type: String? = null,

    @field:JsonProperty("fees")
    @field:SerializedName("fees")
    val fees: String? = null,
    @field:JsonProperty("daily_rate")
    @field:SerializedName("daily_rate")
    val dailyRate: String? = null,
    @field:JsonProperty("gender")
    @field:SerializedName("gender")
    val gender: String? = null,
    @field:JsonProperty("description")
    @field:SerializedName("description")
    val description: String? = null,
    @field:JsonProperty("experience")
    @field:SerializedName("experience")
    val experience: String? = null,
    @field:JsonProperty("passing_year")
    @field:SerializedName("passing_year")
    val passingYear: String? = null,
    @field:JsonProperty("available_time")
    @field:SerializedName("available_time")
    val availableTime: String? = null,
    @field:JsonProperty("user_type")
    @field:SerializedName("user_type")
    val userType: String? = null,
    @field:JsonProperty("u_details_id")
    @field:SerializedName("u_details_id")
    val uDetailsId: String? = null,
    @field:JsonProperty("first_name")
    @field:SerializedName("first_name")
    val firstName: String? = null,
    @field:JsonProperty("email")
    @field:SerializedName("email")
    val email: String? = null,
    @field:JsonProperty("image")
    @field:SerializedName("image")
    var image: String? = null,
    @field:JsonProperty("id_number")
    @field:SerializedName("id_number")
    val idNumber: String? = null,
    @field:JsonProperty("address")
    @field:SerializedName("address")
    val address: String? = null,
    @field:JsonProperty("last_name")
    @field:SerializedName("last_name")
    val lastName: String? = null,
    @field:JsonProperty("qualification")
    @field:SerializedName("qualification")
    val qualification: String? = null,
    @field:JsonProperty("user_id")
    @field:SerializedName("user_id")
    val userId: String? = null,
    @field:JsonProperty("dob")
    @field:SerializedName("dob")
    val dob: String? = null,
    @field:JsonProperty("phone_number")
    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,
    @field:JsonProperty("location")
    @field:SerializedName("location")
    val location: String? = null,
    @field:JsonProperty("institute")
    @field:SerializedName("institute")
    val institute: String? = null,
    @field:JsonProperty("updated_date")
    @field:SerializedName("updated_date")
    val updatedDate: String? = null,
    @field:JsonProperty("age")
    @field:SerializedName("age")
    val age: String? = null,
    @field:JsonProperty("qualification_certificate")
    @field:SerializedName("qualification_certificate")
    val qualificationCertificate: String? = null,
    @field:JsonProperty("status")
    @field:SerializedName("status")
    val status: String? = null,


    // for nurse (extra params)
    @field:SerializedName("hourly_rates")
    val hourlyRates: LinkedList<HourlyRatesItem?>? = null,
    @field:SerializedName("height")
    val height: String? = null,
    @field:SerializedName("weight")
    val weight: String? = null,
    @field:SerializedName("marital_status")
    val maritalStatus: String? = null,
    @field:SerializedName("nationality")
    val nationality: String? = null,
    @field:SerializedName("hospital_id")
    val hospitalId: String? = null,
    @field:JsonProperty("bank_details")
    @field:SerializedName("bank_details")
    var bankDetails: ArrayList<BankDetails>? = null,
    @field:SerializedName("task")
    val task: LinkedList<ResultItem?>? = null,
    @field:JsonProperty("start_time")
    @field:SerializedName("start_time")
    val startTime: String? = null,
    @field:JsonProperty("end_time")
    @field:SerializedName("end_time")
    val endTime: String? = null,
    @field:SerializedName("test")
    val test: LinkedList<ResultItem?>? = null,
)