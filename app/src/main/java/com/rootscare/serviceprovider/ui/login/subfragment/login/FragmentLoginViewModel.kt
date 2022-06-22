package com.rootscare.serviceprovider.ui.login.subfragment.login

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.loginrequest.LoginRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel


class FragmentLoginViewModel : BaseViewModel<FragmentLoginNavigator>() {

    fun apiserviceproviderlogin(loginRequest: LoginRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiServiceProviderLogin(loginRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    /* Saving access token after singup or login */
                    if (response.result != null) {
                        appSharedPref?.deleteLoginModelData()
                        appSharedPref?.loginmodeldata = Gson().toJson(response)
                        appSharedPref?.loginUserType = response.result.user_type
                        appSharedPref?.loginUserId = response.result.user_id
                        println("appSharedPref?.loginUserType ${appSharedPref?.loginUserType}")
                        println("loginRequest.userType ${loginRequest.userType}")

                    }
                    navigator.successLoginResponse(response)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorLoginResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}