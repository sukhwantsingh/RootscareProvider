package com.rootscare.serviceprovider.ui.transactionss

import android.util.Log
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.RequestBody

class PaymentTransactionsViewModel : BaseViewModel<PaymentTransactionsNavigator>() {

    fun getPaymentHistoryFromApi(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getPaymentTransactionsDetails(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessPaymentList(response)
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

    fun getWithdrawalDetail(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.getWithdrawalDetails(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessWithdrawal(response)
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

    fun deleteBankDetails(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.delBankDetails(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccressDeleteBankDetails(response)
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

    fun updateBankDetails(
        userId: RequestBody,
        id: RequestBody,
        bankName: RequestBody,
        yourName: RequestBody,
        accountNo: RequestBody,
        iranNo: RequestBody,
        swiftNo: RequestBody,
        message: RequestBody
    ) {
        val disposable =
            apiServiceWithGsonFactory.apiHitUpdateBankDetails(userId, id, bankName, yourName, accountNo, iranNo, swiftNo, message)
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        Log.d("check_response", ": " + Gson().toJson(response))
                        navigator.onSuccessUpdateBankDetails(response)
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