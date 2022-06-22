package com.rootscare.serviceprovider.ui.babySitter.babySitterMySchedule.subfragment.manageschedule

import com.rootscare.data.model.response.loginresponse.LoginResponse

interface FragmentBabySitterUpdateManageRateNavigator {
    fun onSuccessAfterSavePrice(loginResponse: LoginResponse)

    fun onThrowable(throwable: Throwable)


}