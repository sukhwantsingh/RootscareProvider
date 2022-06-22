package com.rootscare.serviceprovider.ui.splash

import com.rootscare.serviceprovider.ui.base.BaseViewModel

class SplashActivityViewModel : BaseViewModel<SplashActivityNavigator>() {

    fun apiVersionCheck() {
        val disposable = apiServiceWithGsonFactory.apiVersionCheck()
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.onSuccessVersion(response)
                }
            }, { throwable ->
                run { navigator.errorInApi(throwable) }
            })

        compositeDisposable.add(disposable)
    }
}