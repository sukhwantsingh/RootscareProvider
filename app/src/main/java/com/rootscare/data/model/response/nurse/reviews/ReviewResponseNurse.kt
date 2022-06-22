package com.rootscare.data.model.response.nurse.reviews

import com.google.gson.annotations.SerializedName
import java.util.*

data class ReviewResponseNurse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class QualificationDataItem(

	@field:SerializedName("passing_year")
	val passingYear: String? = null,

	@field:SerializedName("qualification")
	val qualification: String? = null,

	@field:SerializedName("institute")
	val institute: String? = null,

	@field:SerializedName("qualification_certificate")
	val qualificationCertificate: String? = null
)

data class Result(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("qualification_data")
	val qualificationData: LinkedList<QualificationDataItem?>? = null,

	@field:SerializedName("fees")
	val fees: String? = null,

	@field:SerializedName("daily_rate")
	val dailyRate: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("experience")
	val experience: String? = null,

	@field:SerializedName("review_rating")
	val reviewRating: LinkedList<ReviewRatingItem>? = null,

	@field:SerializedName("passing_year")
	val passingYear: String? = null,

	@field:SerializedName("available_time")
	val availableTime: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("avg_rating")
	val avgRating: String? = null,

	@field:SerializedName("u_details_id")
	val uDetailsId: String? = null,

	@field:SerializedName("department")
	val department: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("height")
	val height: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("id_number")
	val idNumber: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("qualification")
	val qualification: String? = null,

	@field:SerializedName("marital_status")
	val maritalStatus: String? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("institute")
	val institute: String? = null,

	@field:SerializedName("updated_date")
	val updatedDate: String? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("qualification_certificate")
	val qualificationCertificate: String? = null
)

data class ReviewRatingItem(

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("review_by")
	val reviewBy: String? = null
)
