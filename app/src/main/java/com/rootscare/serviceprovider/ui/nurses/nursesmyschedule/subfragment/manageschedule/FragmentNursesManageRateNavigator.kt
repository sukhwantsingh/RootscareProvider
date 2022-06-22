package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule

import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.data.model.response.loginresponse.TaskListResponse

interface FragmentNursesManageRateNavigator {
    fun onSuccessAfterSavePrice(loginResponse: LoginResponse)

    fun onSuccessAfterGetTaskPrice(loginResponse: TaskListResponse)

    fun onThrowable(throwable: Throwable)


}