package com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating

import com.rootscare.data.model.response.doctor.review.ReviewResponse

interface FragmentNursesReviewAndRatingNavigator {

    fun onSuccessReview(response: ReviewResponse)

    fun onThrowable(throwable: Throwable)

}