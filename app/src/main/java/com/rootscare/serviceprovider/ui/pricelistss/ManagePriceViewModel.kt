package com.rootscare.serviceprovider.ui.pricelistss

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.RequestBody

class ManagePriceViewModel : BaseViewModel<ManagePriceNavigator>() {

    fun getTasksList(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getProvidersTasksApi(requestUserRegister)
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

    fun getLabTestsList(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getLabTestsApi(requestUserRegister)
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

    fun getLabPackagesList(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getLabPackagesApi(requestUserRegister)
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

    fun getLabPackagesDetails(requestUserRegister: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getLabPackagesDetails(requestUserRegister)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessPackageDetails(response)
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

    fun getTasksListForDoctor(req: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getProvidersTasksApiForDoc(req)
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

   fun saveTaskSlot(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.insertUpdatePriceApi(reqBody)
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

   fun disableLabPackage(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.disableLabPackage(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.onSuccessAfterSavePrice(response)
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                }
            })
        compositeDisposable.add(disposable)
    }

   fun saveLabTestsPrice(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.insertUpdateLabTestsPriceApi(reqBody)
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

    fun saveTaskSlotForDoc(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.insertUpdatePriceApiForDoc(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.onSuccessAfterSavePrice(response)
                } else {
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun savePackagePrice(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.insertUpdatePackageApi(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.onSuccessAfterSavePrice(response)
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                }
            })

        compositeDisposable.add(disposable)
    }

}