package com.rootscare.serviceprovider.ui.babySitter.babySitterUpdateReviewAndRating

import com.rootscare.data.model.response.doctor.review.ReviewResponse

interface FragmentBabySitterUpdateReviewAndRatingNavigator {

    fun onSuccessReview(response: ReviewResponse)

    fun onThrowable(throwable: Throwable)

}