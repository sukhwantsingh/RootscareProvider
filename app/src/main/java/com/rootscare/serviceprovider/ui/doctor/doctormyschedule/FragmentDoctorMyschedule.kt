package com.rootscare.serviceprovider.ui.doctor.doctormyschedule

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.rootscare.data.model.request.fetchScheduleData.FetchScheduleRequest
import com.rootscare.data.model.request.sendschedule.SendScheduleRequest
import com.rootscare.data.model.request.sendschedule.Slot
import com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.fetchscheduleresponse.FetchScheduleResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorMyScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FragmentDoctorMyschedule : BaseFragment<FragmentDoctorMyScheduleBinding, FragmentDoctorMyscheduleViewModel>(),
    FragmentDoctorMyscheduleNavigator {

    private var fragmentDoctorMyScheduleBinding: FragmentDoctorMyScheduleBinding? = null
    private var fragmentDoctorMyscheduleViewModel: FragmentDoctorMyscheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_my_schedule
    override val viewModel: FragmentDoctorMyscheduleViewModel
        get() {
            fragmentDoctorMyscheduleViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorMyscheduleViewModel::class.java
            )
            return fragmentDoctorMyscheduleViewModel as FragmentDoctorMyscheduleViewModel
        }

    companion object {
        fun newInstance(): FragmentDoctorMyschedule {
            val args = Bundle()
            val fragment = FragmentDoctorMyschedule()
            fragment.arguments = args
            return fragment
        }
    }

    var type: String = ""
    var s1: String = "0"
    var s1t: String = "0"
    var s2: String = "0"
    var s2t: String = "0"
    var s3: String = "0"
    var s3t: String = "0"
    var s4: String = "0"
    var s4t: String = "0"
    var s5: String = "0"
    var s5t: String = "0"
    var s6: String = "0"
    var s6t: String = "0"
    var s7: String = "0"
    var s7t: String = "0"

    var accept_booking: String = "0"
    var accept_bookingOnline: String = "0"

    var started: TextView? = null
    var ended: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorMyscheduleViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorMyScheduleBinding = viewDataBinding
        fragmentDoctorMyScheduleBinding?.curRef = this


        setHospitalDocUi()
        onClicks()
        apiCalling()

    }

    fun apiCalling() {
        try {
            if (isNetworkConnected) {
                if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType?.equals("doctor", ignoreCase = true) == true) {
                    apiToGetTimeScheduleForDoctor("online")
                }

                if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType?.equals("hospital_doctor", ignoreCase = true) == true) {
                    fetchScheduleData()
                }
            } else {
                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            println("$e")
        }

    }

    private fun setHospitalDocUi() {
        // Toast.makeText(requireContext(),"show me "+ fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType, Toast.LENGTH_SHORT).show()
        if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType?.equals("hospital_doctor", ignoreCase = true)!!) {
            fragmentDoctorMyScheduleBinding?.run {
                tvHeading.text = "Hospital"

                llScheduleForOnlineAppointments.visibility = View.GONE
                btnSaveona.visibility = View.GONE
                tvhHva.visibility = View.GONE

                rbTask.isChecked = true
                rbHours.visibility = View.GONE
                rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
        } else {
            fragmentDoctorMyScheduleBinding?.run {
                tvHeading.text = "Online Appointment"

                llScheduleForOnlineAppointments.visibility = View.VISIBLE
                btnSaveona.visibility = View.VISIBLE


                rbTask.isChecked = true
                rbTaskt.isChecked = true
                rbHours.visibility = View.GONE
                rbHourst.visibility = View.GONE
                rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                rbTaskt.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                true // return type run

            }
        }
       /* if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType.equals("hospital_doctor")) {
            fragmentDoctorMyScheduleBinding?.run {
                rbTask.isChecked = true

//                        rbHours.isChecked = false
                rbHours.visibility = View.GONE

                //  rbHours.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
        } else if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType.equals("doctor")) {
            fragmentDoctorMyScheduleBinding?.run {
                rbTask.isChecked = true
                rbTaskt.isChecked = true

                rbHours.visibility = View.GONE
                rbHourst.visibility = View.GONE

                //rbHours.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                rbTaskt.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
        }
*/
    }

    private fun apiToGetTimeScheduleForDoctor(slotsFor: String) {
        // slots for: online/homeVisit
        baseActivity?.showLoading()
        val fetchScheduleRequest = FetchScheduleRequest()
        fetchScheduleRequest.user_id = viewModel.appSharedPref?.loginUserId
        fetchScheduleRequest.service_type = "normal_doctor"

        if (slotsFor.equals("online", true)) {
            viewModel.getTimeScheduleForOnline(fetchScheduleRequest)
        } else {
            viewModel.getHomeVisitSlot(fetchScheduleRequest)
        }


    }

    override fun onSuccessOnlineSlots(getsendScheduleres: FetchScheduleResponse) {
        baseActivity?.hideLoading()
        if (getsendScheduleres.code.equals("200")) {
            apiToGetTimeScheduleForDoctor("homeVisit")
            if (null != getsendScheduleres.result) {
                if (getsendScheduleres.result?.acceptBooking?.equals("0") == true) {
                    fragmentDoctorMyScheduleBinding?.run {
                        rbAcceptt.isChecked = true
                        rbHidet.isChecked = false

                        rbAcceptt.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                        rbHidet.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                        llHideAcceptt.visibility = View.VISIBLE
                        btnSaveona.visibility = View.VISIBLE
                    }

                } else {
                    fragmentDoctorMyScheduleBinding?.run {
                        rbAcceptt.isChecked = false
                        rbHidet.isChecked = true
                        llHideAcceptt.visibility = View.GONE
                        btnSaveona.visibility = View.VISIBLE

                        rbHidet.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                        rbAcceptt.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                    }

                }

                // set slots data to the views
                if (getsendScheduleres.result?.slots.isNullOrEmpty().not()) {
                    fragmentDoctorMyScheduleBinding?.run {
                        // MON
                        tvMonStartt.text = getsendScheduleres.result.slots.getOrNull(0)?.slotStartTime
                        tvMonEndt.text = getsendScheduleres.result.slots.getOrNull(0)?.slotEndTime
                        sMont.isChecked = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable.equals("1")
                        s1t = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        // Tue
                        tvTueStartt.text = getsendScheduleres.result.slots.getOrNull(1)?.slotStartTime
                        tvTueEndt.text = getsendScheduleres.result.slots.getOrNull(1)?.slotEndTime
                        sTuet.isChecked = getsendScheduleres.result?.slots?.getOrNull(1)?.slotDayEnable.equals("1")
                        s2t = getsendScheduleres.result?.slots?.getOrNull(1)?.slotDayEnable ?: "0"

                        //Wed
                        tvWedStartt.text = getsendScheduleres.result.slots.getOrNull(2)?.slotStartTime
                        tvWedEndt.text = getsendScheduleres.result.slots.getOrNull(2)?.slotEndTime
                        sWedt.isChecked = getsendScheduleres.result?.slots?.getOrNull(2)?.slotDayEnable.equals("1")
                        s3t = getsendScheduleres.result?.slots?.getOrNull(2)?.slotDayEnable ?: "0"

                        //Thr
                        tvThurStartt.text = getsendScheduleres.result.slots.getOrNull(3)?.slotStartTime
                        tvThurEndt.text = getsendScheduleres.result?.slots?.getOrNull(3)?.slotEndTime
                        sThurt.isChecked = getsendScheduleres.result?.slots?.getOrNull(3)?.slotDayEnable.equals("1")
                        s4t = getsendScheduleres.result?.slots?.getOrNull(3)?.slotDayEnable ?: "0"

                        //Fri
                        tvFriStartt.text = getsendScheduleres.result.slots.get(4).slotStartTime
                        tvFriEndt.text = getsendScheduleres.result.slots.get(4).slotEndTime
                        sfrit.isChecked = getsendScheduleres.result?.slots?.get(4)?.slotDayEnable.equals("1")
                        s5t = getsendScheduleres.result?.slots?.getOrNull(4)?.slotDayEnable ?: "0"

                        //Sat
                        tvStartSatt.text = getsendScheduleres.result.slots.get(5).slotStartTime
                        tvEndSatt.text = getsendScheduleres.result.slots.get(5).slotEndTime
                        sSatt.isChecked = getsendScheduleres.result?.slots?.get(5)?.slotDayEnable.equals("1")
                        s6t = getsendScheduleres.result?.slots?.getOrNull(5)?.slotDayEnable ?: "0"

                        //sun
                        tvStartSunt.text = getsendScheduleres.result.slots.get(6).slotStartTime
                        tvEndSunt.text = getsendScheduleres.result.slots.get(6).slotEndTime
                        sSunt.isChecked = getsendScheduleres.result?.slots?.get(6)?.slotDayEnable.equals("1")
                        s7t = getsendScheduleres.result?.slots?.getOrNull(6)?.slotDayEnable ?: "0"
                    }
                } else {
                    //       showToast("Online Schedule not added yet.")
                }


            } else {
                showToast(getsendScheduleres.message)
            }
        }


    }

    override fun onSuccessHomeVisitList(getsendScheduleres: FetchScheduleResponse) {
        baseActivity?.hideLoading()
        if (getsendScheduleres.code.equals("200")) {
            if (null != getsendScheduleres.result) {
                if (getsendScheduleres.result?.acceptBooking?.equals("0") == true) {
                    fragmentDoctorMyScheduleBinding?.run {
                        rbAccept.isChecked = true
                        rbHide.isChecked = false

                        rbAccept.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                        rbHide.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                        llHideAccept.visibility = View.VISIBLE
                        llAddRadiusService.visibility = View.VISIBLE
                    }
                } else {
                    fragmentDoctorMyScheduleBinding?.run {
                        rbAccept.isChecked = false
                        rbHide.isChecked = true
                        llHideAccept.visibility = View.GONE
                        llAddRadiusService.visibility = View.GONE

                        rbHide.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                        rbAccept.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                    }
                }

                // set slots data to the views
                if (getsendScheduleres.result?.slots.isNullOrEmpty().not()) {
                    fragmentDoctorMyScheduleBinding?.run {
                        // MON
                        tvMonStart.text = getsendScheduleres.result.slots.getOrNull(0)?.slotStartTime
                        tvMonEnd.text = getsendScheduleres.result.slots.getOrNull(0)?.slotEndTime
                        sMon.isChecked = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable.equals("1")
                        s1 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        // Tue
                        tvTueStart.text = getsendScheduleres.result.slots.getOrNull(1)?.slotStartTime
                        tvTueEnd.text = getsendScheduleres.result.slots.getOrNull(1)?.slotEndTime
                        sTue.isChecked = getsendScheduleres.result?.slots?.getOrNull(1)?.slotDayEnable.equals("1")
                        s2 = getsendScheduleres.result?.slots?.getOrNull(1)?.slotDayEnable ?: "0"

                        //Wed
                        tvWedStart.text = getsendScheduleres.result.slots.getOrNull(2)?.slotStartTime
                        tvWedEnd.text = getsendScheduleres.result.slots.getOrNull(2)?.slotEndTime
                        sWed.isChecked = getsendScheduleres.result?.slots?.getOrNull(2)?.slotDayEnable.equals("1")
                        s3 = getsendScheduleres.result?.slots?.getOrNull(2)?.slotDayEnable ?: "0"

                        //Thr
                        tvThurStart.text = getsendScheduleres.result.slots.getOrNull(3)?.slotStartTime
                        tvThurEnd.text = getsendScheduleres.result?.slots?.getOrNull(3)?.slotEndTime
                        sThur.isChecked = getsendScheduleres.result?.slots?.getOrNull(3)?.slotDayEnable.equals("1")
                        s4 = getsendScheduleres.result?.slots?.getOrNull(3)?.slotDayEnable ?: "0"

                        //Fri
                        tvFriStart.text = getsendScheduleres.result.slots.get(4).slotStartTime
                        tvFriEnd.text = getsendScheduleres.result.slots.get(4).slotEndTime
                        sfri.isChecked = getsendScheduleres.result?.slots?.get(4)?.slotDayEnable.equals("1")
                        s5 = getsendScheduleres.result?.slots?.getOrNull(4)?.slotDayEnable ?: "0"

                        //Sat
                        tvStartSat.text = getsendScheduleres.result.slots.get(5).slotStartTime
                        tvEndSat.text = getsendScheduleres.result.slots.get(5).slotEndTime
                        sSat.isChecked = getsendScheduleres.result?.slots?.get(5)?.slotDayEnable.equals("1")
                        s6 = getsendScheduleres.result?.slots?.getOrNull(5)?.slotDayEnable ?: "0"

                        //sun
                        tvStartSun.text = getsendScheduleres.result.slots.get(6).slotStartTime
                        tvEndSun.text = getsendScheduleres.result.slots.get(6).slotEndTime
                        sSun.isChecked = getsendScheduleres.result?.slots?.get(6)?.slotDayEnable.equals("1")
                        s7 = getsendScheduleres.result?.slots?.getOrNull(6)?.slotDayEnable ?: "0"
                    }
                } else {
                    //     showToast("Online Schedule not added yet.")
                }
            } else {
                showToast(getsendScheduleres.message)
            }
        }


    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }

    private fun onClicks() {
        fragmentDoctorMyScheduleBinding?.btnSave?.setOnClickListener {
            // it will work for hospital_doctor and doctor (home_visit)
            apiHitSendSchedule("home_visit")
        }
        fragmentDoctorMyScheduleBinding?.btnSaveona?.setOnClickListener {
            // it will work for hospital_doctor and doctor (home_visit)
            apiHitSendSchedule("online")
        }

        if (viewModel.appSharedPref?.loginUserType?.equals("doctor", ignoreCase = true) == true) {

            fragmentDoctorMyScheduleBinding?.tvMonStartt?.setOnClickListener {
                type = "1t"

                started = fragmentDoctorMyScheduleBinding?.tvMonStartt
                ended = fragmentDoctorMyScheduleBinding?.tvMonEndt
                show(started)
            }
            fragmentDoctorMyScheduleBinding?.tvMonEndt?.setOnClickListener {
                type = "2t"
                started = fragmentDoctorMyScheduleBinding?.tvMonEndt
                show(started)
            }
            fragmentDoctorMyScheduleBinding?.tvTueStartt?.setOnClickListener {
                type = "3t"
                started = fragmentDoctorMyScheduleBinding?.tvTueStartt
                show(started)
            }
            fragmentDoctorMyScheduleBinding?.tvTueEndt?.setOnClickListener {
                type = "4t"
                started = fragmentDoctorMyScheduleBinding?.tvTueEndt
                show(started)
            }
            fragmentDoctorMyScheduleBinding?.tvWedStartt?.setOnClickListener {
                type = "5t"
                started = fragmentDoctorMyScheduleBinding?.tvWedStartt
                show(started)
            }
            fragmentDoctorMyScheduleBinding?.tvWedEndt?.setOnClickListener {
                type = "6t"
                              started = fragmentDoctorMyScheduleBinding?.tvWedEndt
                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvThurStartt?.setOnClickListener {
                type = "7t"

                started = fragmentDoctorMyScheduleBinding?.tvThurStartt

                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvThurEndt?.setOnClickListener {
                type = "8t"

                started = fragmentDoctorMyScheduleBinding?.tvThurEndt

                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvFriStartt?.setOnClickListener {
                type = "9t"

                started = fragmentDoctorMyScheduleBinding?.tvFriStartt
                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvFriEndt?.setOnClickListener {
                type = "10t"

                started = fragmentDoctorMyScheduleBinding?.tvFriEndt
                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvStartSatt?.setOnClickListener {
                type = "11t"

                started = fragmentDoctorMyScheduleBinding?.tvStartSatt
                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvEndSatt?.setOnClickListener {
                type = "12t"

                started = fragmentDoctorMyScheduleBinding?.tvEndSatt
                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvStartSunt?.setOnClickListener {
                type = "13t"

                started = fragmentDoctorMyScheduleBinding?.tvStartSunt
                show( started)
            }
            fragmentDoctorMyScheduleBinding?.tvEndSunt?.setOnClickListener {
                type = "14t"

                started = fragmentDoctorMyScheduleBinding?.tvEndSunt
                show(started)
            }

            fragmentDoctorMyScheduleBinding?.rbBookingt?.setOnCheckedChangeListener { group, _ ->

                val selectedId = fragmentDoctorMyScheduleBinding?.rbBookingt?.checkedRadioButtonId
                val radio: RadioButton = group.findViewById(selectedId!!)
                Log.e("selectedtext-->", radio.text.toString())

                if (radio.text.toString().equals("Accept Booking", ignoreCase = true)) {
                    fragmentDoctorMyScheduleBinding?.rbAcceptt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.rbHidet?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                    accept_bookingOnline = "0"
                    fragmentDoctorMyScheduleBinding?.llHideAcceptt?.visibility = View.VISIBLE
                } else if (radio.text.toString().equals("Hide Booking")) {
                    accept_bookingOnline = "1"
                    fragmentDoctorMyScheduleBinding?.rbHidet?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.rbAcceptt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))

                    fragmentDoctorMyScheduleBinding?.llHideAcceptt?.visibility = View.GONE
                }

            }

            fragmentDoctorMyScheduleBinding?.sMont?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    s1t = "1"

                    fragmentDoctorMyScheduleBinding?.llOnet?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    fragmentDoctorMyScheduleBinding?.tvMont?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.tvMonStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    fragmentDoctorMyScheduleBinding?.tvMonEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                } else {

                    fragmentDoctorMyScheduleBinding?.llOnet?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_very_light_new
                        )
                    )
                    fragmentDoctorMyScheduleBinding?.tvMont?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvMonStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvMonEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                    s1t = "0"
                }
            }
            fragmentDoctorMyScheduleBinding?.sTuet?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    s2t = "1"
                    fragmentDoctorMyScheduleBinding?.llTwot?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    fragmentDoctorMyScheduleBinding?.tvTuet?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.tvTueStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    fragmentDoctorMyScheduleBinding?.tvTueEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))


                } else {
                    s2t = "0"
                    fragmentDoctorMyScheduleBinding?.llTwot?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_very_light_new
                        )
                    )
                    fragmentDoctorMyScheduleBinding?.tvTuet?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvTueStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvTueEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                }
            }
            fragmentDoctorMyScheduleBinding?.sWedt?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    s3t = "1"
                    fragmentDoctorMyScheduleBinding?.llWedt?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    fragmentDoctorMyScheduleBinding?.tvWedt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.tvWedStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    fragmentDoctorMyScheduleBinding?.tvWedEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                } else {
                    s3t = "0"
                    fragmentDoctorMyScheduleBinding?.llWedt?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_very_light_new
                        )
                    )
                    fragmentDoctorMyScheduleBinding?.tvWedt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvWedStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvWedEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                }
            })
            fragmentDoctorMyScheduleBinding?.sThurt?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    s4t = "1"
                    fragmentDoctorMyScheduleBinding?.llThut?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    fragmentDoctorMyScheduleBinding?.tvThut?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.tvThurStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    fragmentDoctorMyScheduleBinding?.tvThurEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                } else {
                    s4t = "0"

                    fragmentDoctorMyScheduleBinding?.llThut?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_very_light_new
                        )
                    )
                    fragmentDoctorMyScheduleBinding?.tvThut?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvThurStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvThurEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                }
            })
            fragmentDoctorMyScheduleBinding?.sfrit?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    s5t = "1"
                    fragmentDoctorMyScheduleBinding?.llFrit?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    fragmentDoctorMyScheduleBinding?.tvFrit?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.tvFriStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    fragmentDoctorMyScheduleBinding?.tvFriEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                } else {
                    s5t = "0"

                    fragmentDoctorMyScheduleBinding?.llFrit?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_very_light_new
                        )
                    )
                    fragmentDoctorMyScheduleBinding?.tvFrit?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvFriStartt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvFriEndt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                }
            })
            fragmentDoctorMyScheduleBinding?.sSatt?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    s6t = "1"
                    fragmentDoctorMyScheduleBinding?.llSatt?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    fragmentDoctorMyScheduleBinding?.tvSatt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.tvStartSatt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    fragmentDoctorMyScheduleBinding?.tvEndSatt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                } else {
                    s6t = "0"
                    fragmentDoctorMyScheduleBinding?.llSatt?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_very_light_new
                        )
                    )
                    fragmentDoctorMyScheduleBinding?.tvSatt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvStartSatt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvEndSatt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                }
            })
            fragmentDoctorMyScheduleBinding?.sSunt?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    s7t = "1"
                    fragmentDoctorMyScheduleBinding?.llSunt?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                    fragmentDoctorMyScheduleBinding?.tvSunt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.tvStartSunt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    fragmentDoctorMyScheduleBinding?.tvEndSunt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                } else {
                    s7t = "0"
                    fragmentDoctorMyScheduleBinding?.llSunt?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_very_light_new
                        )
                    )
                    fragmentDoctorMyScheduleBinding?.tvSunt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvStartSunt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    fragmentDoctorMyScheduleBinding?.tvEndSunt?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                }
            })

        }

        fragmentDoctorMyScheduleBinding?.tvMonStart?.setOnClickListener {
            type = "1"

            started = fragmentDoctorMyScheduleBinding?.tvMonStart
            ended = fragmentDoctorMyScheduleBinding?.tvMonEnd

            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvMonEnd?.setOnClickListener {
            type = "2"
              started = fragmentDoctorMyScheduleBinding?.tvMonEnd
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvTueStart?.setOnClickListener {
            type = "3"

            started = fragmentDoctorMyScheduleBinding?.tvTueStart
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvTueEnd?.setOnClickListener {
            type = "4"

            started = fragmentDoctorMyScheduleBinding?.tvTueEnd
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvWedStart?.setOnClickListener {
            type = "5"

            started = fragmentDoctorMyScheduleBinding?.tvWedStart
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvWedEnd?.setOnClickListener {
            type = "6"

            started = fragmentDoctorMyScheduleBinding?.tvWedEnd
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvThurStart?.setOnClickListener {
            type = "7"
            started = fragmentDoctorMyScheduleBinding?.tvThurStart
            show(started)
        }
        fragmentDoctorMyScheduleBinding?.tvThurEnd?.setOnClickListener {
            type = "8"
            started = fragmentDoctorMyScheduleBinding?.tvThurEnd
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvFriStart?.setOnClickListener {
            type = "9"

            started = fragmentDoctorMyScheduleBinding?.tvFriStart
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvFriEnd?.setOnClickListener {
            type = "10"
            started = fragmentDoctorMyScheduleBinding?.tvFriEnd
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvStartSat?.setOnClickListener {
            type = "11"

            started = fragmentDoctorMyScheduleBinding?.tvStartSat
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvEndSat?.setOnClickListener {
            type = "12"

            started = fragmentDoctorMyScheduleBinding?.tvEndSat
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvStartSun?.setOnClickListener {
            type = "13"
            started = fragmentDoctorMyScheduleBinding?.tvStartSun
            show( started)
        }
        fragmentDoctorMyScheduleBinding?.tvEndSun?.setOnClickListener {
            type = "14"

            started = fragmentDoctorMyScheduleBinding?.tvEndSun
            show( started)
        }

        fragmentDoctorMyScheduleBinding?.rbBooking?.setOnCheckedChangeListener { group, _ ->

            val selectedId = fragmentDoctorMyScheduleBinding?.rbBooking?.checkedRadioButtonId
            val radio: RadioButton = group.findViewById(selectedId!!)
            Log.e("selectedtext-->", radio.text.toString())

            if (radio.text.toString().equals("Accept Booking", ignoreCase = true)) {
                fragmentDoctorMyScheduleBinding?.rbAccept?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.rbHide?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                accept_booking = "0"
                fragmentDoctorMyScheduleBinding?.llHideAccept?.visibility = View.VISIBLE

                if(isDoctor()) fragmentDoctorMyScheduleBinding?.llAddRadiusService?.visibility = View.VISIBLE

            } else if (radio.text.toString().equals("Hide Booking")) {
                accept_booking = "1"
                fragmentDoctorMyScheduleBinding?.rbHide?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.rbAccept?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))

                fragmentDoctorMyScheduleBinding?.llHideAccept?.visibility = View.GONE
                if(isDoctor()) fragmentDoctorMyScheduleBinding?.llAddRadiusService?.visibility = View.GONE
            }

        }

        fragmentDoctorMyScheduleBinding?.sMon?.setOnCheckedChangeListener {  _, isChecked ->
            if (isChecked) {
                s1 = "1"

                fragmentDoctorMyScheduleBinding?.llOne?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                fragmentDoctorMyScheduleBinding?.tvMon?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.tvMonStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fragmentDoctorMyScheduleBinding?.tvMonEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            } else {

                fragmentDoctorMyScheduleBinding?.llOne?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_very_light_new
                    )
                )
                fragmentDoctorMyScheduleBinding?.tvMon?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvMonStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvMonEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                s1 = "0"
            }


        }
        fragmentDoctorMyScheduleBinding?.sTue?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s2 = "1"
                fragmentDoctorMyScheduleBinding?.llTwo?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                fragmentDoctorMyScheduleBinding?.tvTue?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.tvTueStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fragmentDoctorMyScheduleBinding?.tvTueEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))


            } else {
                s2 = "0"
                fragmentDoctorMyScheduleBinding?.llTwo?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_very_light_new
                    )
                )
                fragmentDoctorMyScheduleBinding?.tvTue?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvTueStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvTueEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        })
        fragmentDoctorMyScheduleBinding?.sWed?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s3 = "1"
                fragmentDoctorMyScheduleBinding?.llWed?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                fragmentDoctorMyScheduleBinding?.tvWed?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.tvWedStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fragmentDoctorMyScheduleBinding?.tvWedEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            } else {
                s3 = "0"
                fragmentDoctorMyScheduleBinding?.llWed?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_very_light_new
                    )
                )
                fragmentDoctorMyScheduleBinding?.tvWed?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvWedStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvWedEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        })
        fragmentDoctorMyScheduleBinding?.sThur?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s4 = "1"
                fragmentDoctorMyScheduleBinding?.llThu?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                fragmentDoctorMyScheduleBinding?.tvThu?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.tvThurStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fragmentDoctorMyScheduleBinding?.tvThurEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            } else {
                s4 = "0"

                fragmentDoctorMyScheduleBinding?.llThu?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_very_light_new
                    )
                )
                fragmentDoctorMyScheduleBinding?.tvThu?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvThurStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvThurEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        })
        fragmentDoctorMyScheduleBinding?.sfri?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s5 = "1"
                fragmentDoctorMyScheduleBinding?.llFri?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                fragmentDoctorMyScheduleBinding?.tvFri?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.tvFriStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fragmentDoctorMyScheduleBinding?.tvFriEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            } else {
                s5 = "0"

                fragmentDoctorMyScheduleBinding?.llFri?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_very_light_new
                    )
                )
                fragmentDoctorMyScheduleBinding?.tvFri?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvFriStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvFriEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        })
        fragmentDoctorMyScheduleBinding?.sSat?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s6 = "1"
                fragmentDoctorMyScheduleBinding?.llSat?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                fragmentDoctorMyScheduleBinding?.tvSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.tvStartSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fragmentDoctorMyScheduleBinding?.tvEndSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            } else {
                s6 = "0"
                fragmentDoctorMyScheduleBinding?.llSat?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_very_light_new
                    )
                )
                fragmentDoctorMyScheduleBinding?.tvSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvStartSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvEndSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        })
        fragmentDoctorMyScheduleBinding?.sSun?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s7 = "1"
                fragmentDoctorMyScheduleBinding?.llSun?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                fragmentDoctorMyScheduleBinding?.tvSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                fragmentDoctorMyScheduleBinding?.tvStartSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                fragmentDoctorMyScheduleBinding?.tvEndSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            } else {
                s7 = "0"
                fragmentDoctorMyScheduleBinding?.llSun?.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray_very_light_new
                    )
                )
                fragmentDoctorMyScheduleBinding?.tvSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvStartSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                fragmentDoctorMyScheduleBinding?.tvEndSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        })


    }
   private fun isDoctor(): Boolean = viewModel.appSharedPref?.loginUserType?.equals("doctor", ignoreCase = true)==true

    fun show(view: TextView?) {
        PopupMenu(ContextThemeWrapper(requireContext(), R.style.popupMenuStyle), view).apply {
            for (element in resources.getStringArray(R.array.time_array)) {
                menu.add(element)
            }
            setOnMenuItemClickListener { item ->
                view?.text = item.title?.toString() ?: ""
                onCheckDateWhileSelecting(item)
                true
            }
            show()
        }


    }

    private fun onCheckDateWhileSelecting(item: MenuItem) {
         //   Log.e("type", "--" + type)
            if (type == "2") {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvMonStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvMonEnd?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvMonEnd?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvMonEnd?.setText(item.title)
                }

            } else if (type.equals("4")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvTueStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvTueEnd?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvTueEnd?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvTueEnd?.setText(item.title)
                }

            } else if (type.equals("6")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvWedStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvWedEnd?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvWedEnd?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvWedEnd?.setText(item.title)
                }

            } else if (type.equals("8")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvThurStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvThurEnd?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvThurEnd?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvThurEnd?.setText(item.title)
                }

            } else if (type.equals("10")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvFriStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvFriEnd?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvFriEnd?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvFriEnd?.setText(item.title)
                }

            } else if (type.equals("12")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvStartSat?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSat?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvEndSat?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvEndSat?.setText(item.title)
                }

            } else if (type.equals("14")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvStartSun?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSun?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvEndSun?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvEndSun?.setText(item.title)
                }

            } else if (type == "2t") {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvMonStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvMonEndt?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvMonEndt?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvMonEndt?.setText(item.title)
                }

            } else if (type.equals("4t")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvTueStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvTueEndt?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvTueEndt?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvTueEndt?.setText(item.title)
                }

            } else if (type.equals("6t")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvWedStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvWedEndt?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvWedEndt?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvWedEndt?.setText(item.title)
                }

            } else if (type.equals("8t")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvThurStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvThurEndt?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvThurEndt?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvThurEndt?.setText(item.title)
                }

            } else if (type.equals("10t")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvFriStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvFriEndt?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvFriEndt?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvFriEndt?.setText(item.title)
                }

            } else if (type.equals("12t")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvStartSatt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSatt?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvEndSatt?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvEndSatt?.setText(item.title)
                }

            } else if (type.equals("14t")) {
                if (compareDates(
                        fragmentDoctorMyScheduleBinding?.tvStartSunt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSunt?.text.toString()
                    )
                ) {
                    showToast("End time should be greater than start time")
                    fragmentDoctorMyScheduleBinding?.tvEndSunt?.setText("08:00 PM")

                } else {
                    fragmentDoctorMyScheduleBinding?.tvEndSunt?.setText(item.title)
                }

            }

        }


    override fun errorGetPatientFamilyListResponse(throwable: Throwable?) {

    }

    override fun successSendSchedule(getsendScheduleres: GetDoctorProfileResponse) {
        baseActivity?.hideLoading()

        if (getsendScheduleres.code.equals("200")) {
            Toast.makeText(requireContext(), getsendScheduleres.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successFetchSchedule(getsendScheduleres: FetchScheduleResponse) {
        baseActivity?.hideLoading()
        if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType?.equals("hospital_doctor")!!) {
            fragmentDoctorMyScheduleBinding?.tvHeading?.setText("Hospital")
            fragmentDoctorMyScheduleBinding?.llScheduleLayout?.visibility = View.VISIBLE
        } else {
            fragmentDoctorMyScheduleBinding?.tvHeading?.setText("Clinic")
            fragmentDoctorMyScheduleBinding?.llScheduleLayout?.visibility = View.GONE
        }

        if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType.equals("hospital_doctor")) {
            fragmentDoctorMyScheduleBinding?.rbHours?.isChecked = false
            fragmentDoctorMyScheduleBinding?.rbTask?.isChecked = true


            fragmentDoctorMyScheduleBinding?.rbHours?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
            fragmentDoctorMyScheduleBinding?.rbTask?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else if (fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserType.equals("doctor")) {
            fragmentDoctorMyScheduleBinding?.run {
                rbTask.isChecked = true
                rbTaskt.isChecked = true

                rbHours.visibility = View.GONE
                rbHourst.visibility = View.GONE

                //rbHours.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                rbTaskt.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }
        }

        if (getsendScheduleres.code.equals("200")) {
            if (getsendScheduleres.result != null) {
                if (getsendScheduleres.result?.acceptBooking?.equals("0")!!) {
                    fragmentDoctorMyScheduleBinding?.rbAccept?.isChecked = true
                    fragmentDoctorMyScheduleBinding?.rbHide?.isChecked = false

                    fragmentDoctorMyScheduleBinding?.rbAccept?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.rbHide?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                    fragmentDoctorMyScheduleBinding?.llHideAccept?.visibility = View.VISIBLE


                } else {
                    fragmentDoctorMyScheduleBinding?.rbAccept?.isChecked = false
                    fragmentDoctorMyScheduleBinding?.rbHide?.isChecked = true
                    fragmentDoctorMyScheduleBinding?.llHideAccept?.visibility = View.GONE


                    fragmentDoctorMyScheduleBinding?.rbHide?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    fragmentDoctorMyScheduleBinding?.rbAccept?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
                }

                // set slots data to the views
                if (getsendScheduleres.result?.slots.isNullOrEmpty().not()) {
                    fragmentDoctorMyScheduleBinding?.run {
                        // MON
                        tvMonStart.text = getsendScheduleres.result.slots.getOrNull(0)?.slotStartTime
                        tvMonEnd.text = getsendScheduleres.result.slots.getOrNull(0)?.slotEndTime
                        sMon.isChecked = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable.equals("1")
                        s1 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        // Tue
                        tvTueStart.text = getsendScheduleres.result.slots.getOrNull(1)?.slotStartTime
                        tvTueEnd.text = getsendScheduleres.result.slots.getOrNull(1)?.slotEndTime
                        sTue.isChecked = getsendScheduleres.result?.slots?.getOrNull(1)?.slotDayEnable.equals("1")
                        s2 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        //Wed
                        tvWedStart.text = getsendScheduleres.result.slots.getOrNull(2)?.slotStartTime
                        tvWedEnd.text = getsendScheduleres.result.slots.getOrNull(2)?.slotEndTime
                        sWed.isChecked = getsendScheduleres.result?.slots?.getOrNull(2)?.slotDayEnable.equals("1")
                        s3 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        //Thr
                        tvThurStart.text = getsendScheduleres.result.slots.getOrNull(3)?.slotStartTime
                        tvThurEnd.text = getsendScheduleres.result?.slots?.getOrNull(3)?.slotEndTime
                        sThur.isChecked = getsendScheduleres.result?.slots?.getOrNull(3)?.slotDayEnable.equals("1")
                        s4 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        //Fri
                        tvFriStart.text = getsendScheduleres.result.slots.get(4).slotStartTime
                        tvFriEnd.text = getsendScheduleres.result.slots.get(4).slotEndTime
                        sfri.isChecked = getsendScheduleres.result?.slots?.get(4)?.slotDayEnable.equals("1")
                        s5 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        //Sat
                        tvStartSat.text = getsendScheduleres.result.slots.get(5).slotStartTime
                        tvEndSat.text = getsendScheduleres.result.slots.get(5).slotEndTime
                        sSat.isChecked = getsendScheduleres.result?.slots?.get(5)?.slotDayEnable.equals("1")
                        s6 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"

                        //sun
                        tvStartSun.text = getsendScheduleres.result.slots.get(6).slotStartTime
                        tvEndSun.text = getsendScheduleres.result.slots.get(6).slotEndTime
                        sSun.isChecked = getsendScheduleres.result?.slots?.get(6)?.slotDayEnable.equals("1")
                        s7 = getsendScheduleres.result?.slots?.getOrNull(0)?.slotDayEnable ?: "0"
                    }
                } else {
                    showToast("Online Schedule not added yet.")
                }
            } else {
                showToast(getsendScheduleres.message)
            }
        }
    }

    fun fetchScheduleData() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val fetchScheduleRequest = FetchScheduleRequest()
            fetchScheduleRequest.user_id = viewModel.appSharedPref?.loginUserId

            if (viewModel.appSharedPref?.loginUserType?.equals("hospital")!!) {
                fetchScheduleRequest.service_type = "pathology"
            } else {
                fetchScheduleRequest.service_type = viewModel.appSharedPref?.loginUserType
            }
            viewModel.fetchScheduleData(fetchScheduleRequest)
        }
    }

    fun apiHitSendSchedule(hitFor: String) {
        // hitFor: online/home_visit
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var scheduleList: ArrayList<Slot> = ArrayList()
            val sendScheduleRequest = SendScheduleRequest()
            sendScheduleRequest.userId = viewModel.appSharedPref?.loginUserId

            when {
                viewModel.appSharedPref?.loginUserType?.equals("hospital") == true -> {
                    sendScheduleRequest.acceptBooking = accept_booking
                    sendScheduleRequest.serviceType = "pathology"
                }
                viewModel.appSharedPref?.loginUserType?.equals("doctor") == true -> {
                    sendScheduleRequest.serviceType = "normal_doctor"
                    sendScheduleRequest.slot_type = if (hitFor.equals("online", ignoreCase = true)) {
                        sendScheduleRequest.acceptBooking = accept_bookingOnline
                        "online"
                    } else {
                        sendScheduleRequest.acceptBooking = accept_booking
                        "home_visit"
                    }
                }
                else -> {
                    sendScheduleRequest.acceptBooking = accept_booking
                    sendScheduleRequest.serviceType = viewModel.appSharedPref?.loginUserType
                }
            }

            if (hitFor.equals("online", true)) {
                scheduleList.add(
                    Slot(
                        "MON",
                        fragmentDoctorMyScheduleBinding?.tvMonStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvMonEndt?.text.toString(), s1t
                    )
                )
                scheduleList.add(
                    Slot(
                        "TUE", fragmentDoctorMyScheduleBinding?.tvTueStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvTueEndt?.text.toString(), s2t
                    )
                )
                scheduleList.add(
                    Slot(
                        "WED", fragmentDoctorMyScheduleBinding?.tvWedStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvWedEndt?.text.toString(), s3t
                    )
                )
                scheduleList.add(
                    Slot(
                        "THU", fragmentDoctorMyScheduleBinding?.tvThurStartt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvThurEndt?.text.toString(), s4t
                    )
                )
                scheduleList.add(
                    Slot(
                        "FRI", fragmentDoctorMyScheduleBinding?.tvFriStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvFriEndt?.text.toString(), s5t
                    )
                )
                scheduleList.add(
                    Slot(
                        "SAT", fragmentDoctorMyScheduleBinding?.tvStartSatt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSatt?.text.toString(), s6t
                    )
                )
                scheduleList.add(
                    Slot(
                        "SUN", fragmentDoctorMyScheduleBinding?.tvStartSunt?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSunt?.text.toString(), s7t
                    )
                )

                sendScheduleRequest.slots = scheduleList
                viewModel.sendProviderScheduleForDoctor(sendScheduleRequest)

            } else {
                scheduleList.add(
                    Slot(
                        "MON", fragmentDoctorMyScheduleBinding?.tvMonStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvMonEnd?.text.toString(), s1
                    )
                )
                scheduleList.add(
                    Slot(
                        "TUE", fragmentDoctorMyScheduleBinding?.tvTueStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvTueEnd?.text.toString(), s2
                    )
                )
                scheduleList.add(
                    Slot(
                        "WED", fragmentDoctorMyScheduleBinding?.tvWedStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvWedEnd?.text.toString(), s3
                    )
                )
                scheduleList.add(
                    Slot(
                        "THU", fragmentDoctorMyScheduleBinding?.tvThurStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvThurEnd?.text.toString(), s4
                    )
                )
                scheduleList.add(
                    Slot(
                        "FRI", fragmentDoctorMyScheduleBinding?.tvFriStart?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvFriEnd?.text.toString(), s5
                    )
                )
                scheduleList.add(
                    Slot(
                        "SAT", fragmentDoctorMyScheduleBinding?.tvStartSat?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSat?.text.toString(), s6
                    )
                )
                scheduleList.add(
                    Slot(
                        "SUN", fragmentDoctorMyScheduleBinding?.tvStartSun?.text.toString(),
                        fragmentDoctorMyScheduleBinding?.tvEndSun?.text.toString(), s7
                    )
                )

                sendScheduleRequest.slots = scheduleList
                if (viewModel.appSharedPref?.loginUserType?.equals("doctor") == true) {
                    viewModel.sendProviderScheduleForDoctor(sendScheduleRequest)
                } else {
                    viewModel.sendProviderSchedule(sendScheduleRequest)
                }

            }


        } else {
          showToast(getString(R.string.check_network_connection))
        }
    }


    fun compareDates(starttime: String, endTime: String): Boolean {

        var isGreater: Boolean = false
        try {
            val formatter = SimpleDateFormat("hh:mm a")

            val date1: Date = formatter.parse(starttime)

            val date2: Date = formatter.parse(endTime)
            isGreater = date2.before(date1)
        } catch (e1: ParseException) {
            e1.printStackTrace()
        }
        return isGreater
    }

    fun selectRadiusRadio(view: View) {
        fragmentDoctorMyScheduleBinding?.run {
            rb15km.isChecked = false
            rb15km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
            rb30km.isChecked = false
            rb30km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
            rb50km.isChecked = false
            rb50km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
            rb75km.isChecked = false
            rb75km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))

            rb100km.isChecked = false
            rb100km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
            rb125km.isChecked = false
            rb125km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
            rb150km.isChecked = false
            rb150km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))
            rb200km.isChecked = false
            rb200km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey))

        }

        val mVie = when (view.id) {
            R.id.rb15km -> fragmentDoctorMyScheduleBinding?.rb15km
            R.id.rb30km -> fragmentDoctorMyScheduleBinding?.rb30km
            R.id.rb50km -> fragmentDoctorMyScheduleBinding?.rb50km
            R.id.rb75km -> fragmentDoctorMyScheduleBinding?.rb75km
            R.id.rb100km -> fragmentDoctorMyScheduleBinding?.rb100km
            R.id.rb125km -> fragmentDoctorMyScheduleBinding?.rb125km
            R.id.rb150km -> fragmentDoctorMyScheduleBinding?.rb150km
            R.id.rb200km -> fragmentDoctorMyScheduleBinding?.rb200km
            else -> fragmentDoctorMyScheduleBinding?.rb15km
        }
        mVie?.isChecked = true
        mVie?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        Log.wtf("radius", "selectRadiusRadio: ${mVie?.text}")

    }

}