package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment

import com.rootscare.data.model.response.doctor.myschedule.homeVisitList.HomeVisitSchedule
import com.rootscare.data.model.response.doctor.myschedule.timeslotlist.MyScheduleTimeSlotResponse

interface FragmentdoctorManageScheduleNavigator {

    fun onSuccessTimeSlotList(response: MyScheduleTimeSlotResponse)

    fun onSuccessTimeHomeSlotList(response: HomeVisitSchedule)

    fun onSuccessAfterRemoveTimeSlot(position: Int, response: MyScheduleTimeSlotResponse)

    fun onSuccessAfterRemoveHomeTimeSlot(position: Int, response: MyScheduleTimeSlotResponse)

    fun onThrowable(throwable: Throwable)


}