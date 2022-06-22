package com.rootscare.data.model.response.doctor.profileresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class ReviewRatingItem(
	@field:JsonProperty("review")
	@field:SerializedName("review")
	var review: String? = null,
	@field:JsonProperty("rating")
	@field:SerializedName("rating")
    var rating: String? = null,
	@field:JsonProperty("review_by")
	@field:SerializedName("review_by")
	var reviewBy: String? = null
)