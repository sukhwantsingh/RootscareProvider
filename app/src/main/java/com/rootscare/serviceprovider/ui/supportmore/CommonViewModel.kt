package com.rootscare.serviceprovider.ui.supportmore

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.commonuseridrequest.NeedSupportRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class CommonViewModel : BaseViewModel<CommonActivityNavigator>() {

    fun submitSupportApi(reqBody: NeedSupportRequest) {
        val disposable = apiServiceWithGsonFactory.apiToSubmitNeedSupport(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.wtf("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessSubmitSupport(response)
                } else {
                    Log.wtf("check_response", ": null response")
                } }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.wtf("check_response_error", ": " + throwable.message)
                }
            })
        compositeDisposable.add(disposable)

    }

    fun fetchHelpTopics() {
        val disposable = apiServiceWithGsonFactory.getHelpTopics()
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onFetchHelpTopics(response)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
    /*
    *
    fun apiHitForUdateProfileWithProfileAndCertificationImage(
        description: RequestBody,
       fileToUpload: MultipartBody.Part? = null,
        certificate: List<MultipartBody.Part>? = null
    ) {

    }

    * */
}