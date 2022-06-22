package com.rootscare.serviceprovider.ui.doctor.doctormyschedule

import com.rootscare.data.model.response.fetchscheduleresponse.FetchScheduleResponse

interface FragmentDoctorMyscheduleNavigator {

    fun onSuccessOnlineSlots(response: FetchScheduleResponse)
    fun onSuccessHomeVisitList(response: FetchScheduleResponse)
    fun onThrowable(throwable: Throwable)

    fun errorGetPatientFamilyListResponse(throwable: Throwable?)
    fun successSendSchedule(getsendScheduleres: com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse)
    fun successFetchSchedule(getsendScheduleres: FetchScheduleResponse)
}