package com.rootscare.serviceprovider.ui.manageDocLab

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.ui.manageDocLab.model.ModelHospitalDocs

interface ManageDocLabNavigator {

    fun onSuccessAfterSavePrice(response: CommonResponse){}
    fun onSuccessDocResponse(taskList: ModelHospitalDocs?){}
    fun onThrowable(throwable: Throwable)


}