package com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating

import com.rootscare.data.model.response.doctor.review.ReviewResponse

interface FragmentReviewAndRatingNavigator {

    fun onSuccessReview(response: ReviewResponse)

    fun onThrowable(throwable: Throwable)

}