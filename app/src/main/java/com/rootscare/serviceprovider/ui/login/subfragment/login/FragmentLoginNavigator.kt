package com.rootscare.serviceprovider.ui.login.subfragment.login

import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile

interface FragmentLoginNavigator {
    fun successLoginResponse(loginResponse: ModelUserProfile?)
    fun errorLoginResponse(throwable: Throwable?)
}