package com.rootscare.serviceprovider.utilitycommon

import androidx.annotation.Keep

enum class LoginTypes(val type: String, val displayName: String) {
    DOCTOR("doctor", "Doctor"),
    HOSPITAL_DOCTOR("hospital_doctor", "Hospital Doctor"),
    NURSE("nurse", "Nurse"),
    CAREGIVER("caregiver", "Nurse Assistant"),
    BABYSITTER("babysitter", "Babysitter"),
    HOSPITAL("hospital", "Hospital"),
    LAB_TECHNICIAN("lab-technician", "Lab-Technician"),
    PHYSIOTHERAPY("physiotherapy", "Physiotherapy"),
    PATHOLOGY("pathology", "Pathology");

}

enum class AppointmentTypes(private val n: String) {
    ONGOING("Ongoing"),
    PENDING("Pending"),
    UPCOMING("Upcoming"),
    PAST("Past");
    fun get() = this.n
}

enum class ScheduleTypes(val nm: String) {
    TASK_BASED("Task Schedule"),
    HOURLY_BASED("Hourly Schedule"),
    ONLINE_HOME_BASED("Online and Home Consultation"),
    ONLINE_BASED("Online Consultation"),
    HOME_VISIT_BASED("Home Visit");
}

enum class PriceTypes(private val pt: String,private val pmode: String) {
    TASK_BASED("Task","task_base"),
    HOURLY_BASED("Hourly","hour_base"),
    ONLINE("Online Consultation","online"),
    HOME_VISIT("Home Visit Consultation","home_visit");

    fun get() = this.pt
    fun getMode() = this.pmode
}

enum class TransTypes(private val tt: String) {
    TRANSACTION("Transaction"),
    WITHDRAWAL("Withdrawal");

    fun get() = this.tt
}

enum class CurrencyTypes(private val curr: String,private val cc: String) {
    SAR("SAR","966"), AED("AED","971");

    fun get() = this.curr
    fun getCc() = this.cc
}

enum class HospitalUnder(private val hu: String, private val typ: String) {
    DOCTOR("Doctor","DOCTOR"),
    LAB("Lab","LAB");

    fun get() = this.hu
    fun getType() = this.typ
}

enum class SupportMoreUrls(val eng: String, val ar: String) {
    ABOUTUS("about-us/eng", "about-us/ar"),
    PRIVACY_POLICY("privacy-policy/eng", "privacy-policy/ar"),
    TERMS_CONIDTIONS("provider-terms-and-conditions/eng", "provider-terms-and-conditions/ar");

    fun getEngLink() = API_BASE_URL + this.eng
    fun getArabicLink() = API_BASE_URL + this.ar

}

@Keep
enum class LanguageModes(val lg: String, private val lgLocale: String) {
    ENG("ENG","en"),
    AR("AR","ar");

    fun get() = this.lg
    fun getLangLocale() = this.lgLocale
}
enum class GenderType(val gn: String) {
    MALE("Male"),
    FEMALE("Female");

    fun get() = this.gn
}

enum class TransactionStatus(private val ts: String, private val clr: String) {
    PENDING("Pending","#F9D800"),
    PAID("Paid","#4FB82A"),
    ACCEPTED("Accepted","#4FB82A"),
    COMPLETED("Completed","#4FB82A"),
    SUCCESS("Success","#4FB82A"),
    CANCELLED("Cancelled","#FF4E00"),
    REJECTED("Rejected","#FF4E00"),
    ELSE("Else","#515C6F");

    fun get() = this.ts
    fun getColor() = this.clr;
}



















