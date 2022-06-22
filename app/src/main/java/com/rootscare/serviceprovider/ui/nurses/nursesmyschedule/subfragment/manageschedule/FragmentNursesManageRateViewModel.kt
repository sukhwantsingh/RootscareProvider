package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule

import android.util.Log
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.RequestBody

class FragmentNursesManageRateViewModel : BaseViewModel<FragmentNursesManageRateNavigator>() {

    fun getTaskRate(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getTaskRateApi(requestUserRegister)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterGetTaskPrice(response)
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

    fun savePrice(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.savePriceForSlot1(requestUserRegister)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterSavePrice(response)
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

    fun saveHourlyPrice(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.saveHourlyPrice(requestUserRegister)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterSavePrice(response)
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

    fun saveTaskSlot(userId: RequestBody, taskId: RequestBody, price: RequestBody, userType: String) {
        if (userType == "physiotherapy") {
            val disposable = apiServiceWithGsonFactory.savePhysiotherapyTaskPrice(userId, taskId, price)
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        Log.d("check_response", ": " + Gson().toJson(response))
                        navigator.onSuccessAfterSavePrice(response)
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
        } else {
            val disposable = apiServiceWithGsonFactory.saveTaskPrice(userId, taskId, price)
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        Log.d("check_response", ": " + Gson().toJson(response))
                        navigator.onSuccessAfterSavePrice(response)
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

    fun getTestRate() {
        val disposable = apiServiceWithGsonFactory.getTestRateApi()
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterGetTaskPrice(response)
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

    fun saveTestPrice(requestBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.saveTestPriceApi(requestBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessAfterSavePrice(response)
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