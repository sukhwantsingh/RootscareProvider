package com.rootscare.serviceprovider.ui.caregiver.caregiverupdatereviewandrating

import com.rootscare.data.model.response.doctor.review.ReviewResponse

interface FragmentCaregiverUpdateReviewAndRatingNavigator {
    fun onSuccessReview(response: ReviewResponse)

    fun onThrowable(throwable: Throwable)

}