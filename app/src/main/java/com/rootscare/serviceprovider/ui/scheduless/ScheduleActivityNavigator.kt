package com.rootscare.serviceprovider.ui.scheduless

interface ScheduleActivityNavigator {
    fun errorGetPatientFamilyListResponse(throwable: Throwable?)
    fun successSendSchedule(getsendScheduleres: com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse)
    fun successFetchSchedule(getsendScheduleres: ModelScheduleTiming)
}