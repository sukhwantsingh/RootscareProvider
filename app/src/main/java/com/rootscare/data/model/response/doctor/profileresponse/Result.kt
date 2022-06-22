package com.rootscare.data.model.response.doctor.profileresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(
	@field:JsonProperty("date")
	@field:SerializedName("date")
	val date: String? = null,
	@field:JsonProperty("qualification_data")
	@field:SerializedName("qualification_data")
	val qualificationData: ArrayList<QualificationDataItem>? = null,
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
	@field:JsonProperty("review_rating")
	@field:SerializedName("review_rating")
	val reviewRating: ArrayList<ReviewRatingItem?>? = null,
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
	@field:JsonProperty("department")
	@field:SerializedName("department")

	val department: ArrayList<DepartmentItem?>? = null,
	@field:JsonProperty("first_name")
	@field:SerializedName("first_name")
	val firstName: String? = null,
	@field:JsonProperty("email")
	@field:SerializedName("email")
	val email: String? = null,
	@field:JsonProperty("image")
	@field:SerializedName("image")
	val image: String? = null,
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
	@field:JsonProperty("avg_rating")
	@field:SerializedName("avg_rating")
	val avgRating: String? = null,
	@field:JsonProperty("start_time")
	@field:SerializedName("start_time")
	val startTime: String? = null,
	@field:JsonProperty("end_time")
	@field:SerializedName("end_time")
	val endTime: String? = null
)