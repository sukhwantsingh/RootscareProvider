package com.rootscare.serviceprovider.ui.physiotherapy.reviewRating

import com.rootscare.data.model.response.doctor.review.ReviewResponse

interface FragmentPhysiotherapistReviewAndRatingNavigator {
    fun onSuccessReview(response: ReviewResponse)

    fun onThrowable(throwable: Throwable)
}