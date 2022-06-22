package com.rootscare.serviceprovider.ui.supportmore

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.ui.supportmore.models.ModelIssueTypes

interface CommonActivityNavigator{
    fun onSuccessSubmitSupport(response: CommonResponse)

    // implement on need support screen
    fun onFetchHelpTopics(response: ModelIssueTypes){}

    fun errorInApi(throwable: Throwable?)
}