package com.rootscare.serviceprovider.ui.pricelistss

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackageDetails
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackages
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPriceListing

interface ManagePriceNavigator {

    fun onSuccessAfterSavePrice(response: CommonResponse){}
    fun onSuccessAfterGetTaskPrice(taskList: ModelPriceListing){}
    fun onSuccessAfterGetTaskPrice(taskList: ModelPackages){}
    fun onSuccessPackageDetails(response: ModelPackageDetails?){}
    fun onThrowable(throwable: Throwable)


}