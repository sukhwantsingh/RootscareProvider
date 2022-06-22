package com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.commonuseridrequest.NeedSupportRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentNursesReviewAndRatingViewModel : BaseViewModel<FragmentNursesReviewAndRatingNavigator>() {

    fun getReviewFromApi(reqBody: NeedSupportRequest) {
        val disposable = apiServiceWithGsonFactory.apiAllProviders(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessReview(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

}