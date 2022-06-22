package com.rootscare.data.model.response.loginresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName


@JsonIgnoreProperties(ignoreUnknown = true)
data class BankDetails(
    @field:JsonProperty("id")
    @field:SerializedName("id")
    val id: String? = null,
    @field:JsonProperty("user_id")
    @field:SerializedName("user_id")
    val userId: String? = null,
    @field:JsonProperty("bank_name")
    @field:SerializedName("bank_name")
    val bankName: String? = null,
    @field:JsonProperty("your_bank_name")
    @field:SerializedName("your_bank_name")
    val yourBankName: String? = null,
    @field:JsonProperty("account_no")
    @field:SerializedName("account_no")
    val accountNo: String? = null,
    @field:JsonProperty("iban_no")
    @field:SerializedName("iban_no")
    val ibanNo: String? = null,
    @field:JsonProperty("swift_no")
    @field:SerializedName("swift_no")
    val swiftNo: String? = null,
    @field:JsonProperty("message")
    @field:SerializedName("message")
    val message: String? = null,
    @field:JsonProperty("created_date")
    @field:SerializedName("created_date")
    val createdDate: String? = null,
    @field:JsonProperty("updated_date")
    @field:SerializedName("updated_date")
    val updatedDate: String? = null,
    @field:JsonProperty("created_by")
    @field:SerializedName("created_by")
    val createdBy: String? = null,
    @field:JsonProperty("updated_by")
    @field:SerializedName("updated_by")
    val updatedBy: String? = null,
    @field:JsonProperty("status")
    @field:SerializedName("status")
    val status: String? = null


)