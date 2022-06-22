package com.rootscare.serviceprovider.ui.login.subfragment.forgotpassword

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.forgotpassword.forgotpasswordchangerequest.ForgotPasswordChangeRequest
import com.rootscare.data.model.request.forgotpassword.forgotpasswordemailrequest.ForgotPasswordSendEmailRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentForgotPasswordViewModel : BaseViewModel<FragmentForgotPasswordNavigator>() {

    fun apiforgotpasswordemail(forgotPasswordSendEmailRequest: ForgotPasswordSendEmailRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiforgotpasswordemail(forgotPasswordSendEmailRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successForgotPasswordSendMailResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorForgotPasswordSendMailResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiforgotchangepassword(forgotPasswordChangeRequest: ForgotPasswordChangeRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiforgotchangepassword(forgotPasswordChangeRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successForgotPasswordChangePasswordResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorForgotPasswordSendMailResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}