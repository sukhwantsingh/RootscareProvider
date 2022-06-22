package com.rootscare.serviceprovider.ui.nurses.nurseprofile

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.registrationresponse.RegistrationResponse
import com.rootscare.model.ModelServiceFor
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelHospDeparts
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.supportmore.models.ModelIssueTypes

interface FragmentNursesProfileNavigator {
    fun successDepartmentListResponse(response: ModelHospDeparts?){}
    fun onSuccessUserProfile(response: ModelUserProfile?){}
    fun onSuccessEditProfile(response: ModelUserProfile?){}
    fun onSuccessSpeciality(specialityList: ModelIssueTypes?){}
    fun onSuccessServiceFor(specialityList: ModelServiceFor?){}
    fun successRegistrationResponse(response: RegistrationResponse?){}
    fun onSuccessCommon(response: CommonResponse?){}
    fun errorInApi(throwable: Throwable?)
}