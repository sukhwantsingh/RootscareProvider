package com.rootscare.serviceprovider.ui.pricelistss

import com.rootscare.data.model.response.CommonResponse

interface ManagePriceNavigator {

    fun onSuccessAfterSavePrice(response: CommonResponse)
    fun onSuccessAfterGetTaskPrice(taskList: ModelPriceListing)
    fun onThrowable(throwable: Throwable)


}