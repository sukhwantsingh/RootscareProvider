package com.rootscare.serviceprovider.ui.scheduless

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rootscare.data.model.request.fetchScheduleData.FetchScheduleRequest
import com.rootscare.data.model.request.sendschedule.SendScheduleRequest
import com.rootscare.data.model.request.sendschedule.Slot
import com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentScheduleCombineBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.dialog.CommonDialog

import com.google.android.libraries.places.widget.Autocomplete
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.utilitycommon.*

private const val ARG_SCHEDULE_TYPE = "ARG_SCHEDULE_TYPE"

class ScheduleFragmentCombine : BaseFragment<FragmentScheduleCombineBinding, ScheduleActivityViewModel>(),
    ScheduleActivityNavigator {

    private var scheduleType: String? = null
    private var activityScheduleBinding: FragmentScheduleCombineBinding? = null

    private var scheduleActivityViewModel: ScheduleActivityViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_schedule_combine
    override val viewModel: ScheduleActivityViewModel
        get() {
            scheduleActivityViewModel = ViewModelProviders.of(this).get(ScheduleActivityViewModel::class.java)
            return scheduleActivityViewModel as ScheduleActivityViewModel
        }


    var type: String = ""
    var s1: String = "1"
    var s2: String = "1"
    var s3: String = "1"
    var s4: String = "1"
    var s5: String = "1"
    var s6: String = "1"
    var s7: String = "1"
    var accept_booking: String = "0"

    var started: TextView? = null
    var ended: TextView? = null

    var serviceLat = ""
    var serviceLong = ""

    companion object {
        @JvmStatic
        fun newInstance(scheduleType: String = "") =
            ScheduleFragmentCombine().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCHEDULE_TYPE, scheduleType)
                }
            }
    }
    private var hospId : String? = null

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleActivityViewModel?.navigator = this
        arguments?.let {
            scheduleType = it.getString(ARG_SCHEDULE_TYPE) ?: ""
        }
         hospId = scheduleActivityViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result?.hospital_id.orEmpty()
    }

    private fun isHospitalDoctor() = hospId.isNullOrBlank().not()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityScheduleBinding = viewDataBinding
        activityScheduleBinding?.curRef = this

        initViews()
    }

    fun initViews() {

        fetchScheduleData()

        activityScheduleBinding?.btnSave?.setOnClickListener { apiHitSendSchedule() }

        activityScheduleBinding?.sMon?.isChecked = true
        activityScheduleBinding?.sTue?.isChecked = true
        activityScheduleBinding?.sWed?.isChecked = true
        activityScheduleBinding?.sThur?.isChecked = true
        activityScheduleBinding?.sfri?.isChecked = true
        activityScheduleBinding?.sSat?.isChecked = true
        activityScheduleBinding?.sSun?.isChecked = true


        when {
            scheduleType.equals(ScheduleTypes.TASK_BASED.nm, true) -> {
                is30MinSlotEnable(true)
            }
            scheduleType.equals(ScheduleTypes.HOURLY_BASED.nm, true) -> {
              is30MinSlotEnable(false)
            }
            scheduleType.equals(ScheduleTypes.ONLINE_HOME_BASED.nm, true) -> {
                slotEnableForDoctorOnlineHomeVisit()
            }
            scheduleType.equals(ScheduleTypes.ONLINE_BASED.nm, true) -> {
                slotEnableForDoctorOnlineVisit()
            }
            scheduleType.equals(ScheduleTypes.LAB_BASED.nm, true) -> {
                slotEnableForLabt()
            }
        }

        activityScheduleBinding?.tvMonStart?.setOnClickListener {
            type = "1"

            started = activityScheduleBinding?.tvMonStart
            ended = activityScheduleBinding?.tvMonEnd

            show(started)
        }
        activityScheduleBinding?.tvMonEnd?.setOnClickListener {
            type = "2"
            started = activityScheduleBinding?.tvMonStart
            ended = activityScheduleBinding?.tvMonEnd
            show(ended)
        }
        activityScheduleBinding?.tvTueStart?.setOnClickListener {
            type = "3"
            started = activityScheduleBinding?.tvTueStart
            ended = activityScheduleBinding?.tvTueEnd

            show(started)
        }
        activityScheduleBinding?.tvTueEnd?.setOnClickListener {
            type = "4"
            started = activityScheduleBinding?.tvTueStart
            ended = activityScheduleBinding?.tvTueEnd
            show(ended)
        }
        activityScheduleBinding?.tvWedStart?.setOnClickListener {
            type = "5"
            started = activityScheduleBinding?.tvWedStart
            ended = activityScheduleBinding?.tvWedEnd

            show(started)
        }
        activityScheduleBinding?.tvWedEnd?.setOnClickListener {
            type = "6"
            started = activityScheduleBinding?.tvWedStart
            ended = activityScheduleBinding?.tvWedEnd

            show(ended)
        }
        activityScheduleBinding?.tvThurStart?.setOnClickListener {
            type = "7"
            started = activityScheduleBinding?.tvThurStart
            ended = activityScheduleBinding?.tvThurEnd

            show(started)
        }
        activityScheduleBinding?.tvThurEnd?.setOnClickListener {
            type = "8"
            started = activityScheduleBinding?.tvThurStart
            ended = activityScheduleBinding?.tvThurEnd

            show(ended)
        }
        activityScheduleBinding?.tvFriStart?.setOnClickListener {
            type = "9"
            started = activityScheduleBinding?.tvFriStart
            ended = activityScheduleBinding?.tvFriEnd

            show(started)
        }
        activityScheduleBinding?.tvFriEnd?.setOnClickListener {
            type = "10"
            started = activityScheduleBinding?.tvFriStart
            ended = activityScheduleBinding?.tvFriEnd
            show(ended)
        }
        activityScheduleBinding?.tvStartSat?.setOnClickListener {
            type = "11"
            started = activityScheduleBinding?.tvStartSat
            ended = activityScheduleBinding?.tvEndSat
            show(started)
        }
        activityScheduleBinding?.tvEndSat?.setOnClickListener {
            type = "12"
            started = activityScheduleBinding?.tvStartSat
            ended = activityScheduleBinding?.tvEndSat

            show(ended)
        }
        activityScheduleBinding?.tvStartSun?.setOnClickListener {
            type = "13"
            started = activityScheduleBinding?.tvStartSun
            ended = activityScheduleBinding?.tvEndSun
            show(started)
        }
        activityScheduleBinding?.tvEndSun?.setOnClickListener {
            type = "14"
            started = activityScheduleBinding?.tvStartSun
            ended = activityScheduleBinding?.tvEndSun

            show(ended)
        }

        activityScheduleBinding?.rbBooking?.setOnCheckedChangeListener { group, _ ->

            val selectedId = activityScheduleBinding?.rbBooking?.checkedRadioButtonId
            val radio: RadioButton = group.findViewById(selectedId!!)

            if (radio.text.toString().equals("Accept Booking",ignoreCase = true)) {
                accept_booking = "0"
                updateUiForProvider()

            } else if (radio.text.toString().equals("Hide Booking",ignoreCase = true)) {
                accept_booking = "1"
                updateUiForProvider(false)
            }

        }

        activityScheduleBinding?.sMon?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                s1 = "1"

                activityScheduleBinding?.llOne?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                activityScheduleBinding?.tvMon?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                activityScheduleBinding?.tvMonStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
                activityScheduleBinding?.tvMonEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
            } else {

                activityScheduleBinding?.llOne?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_very_light_new))
                activityScheduleBinding?.tvMon?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvMonStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvMonEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

                s1 = "0"
            }


        }
        activityScheduleBinding?.sTue?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                s2 = "1"

                activityScheduleBinding?.llTwo?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                activityScheduleBinding?.tvTue?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                activityScheduleBinding?.tvTueStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
                activityScheduleBinding?.tvTueEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
            } else {
                s2 = "0"

                activityScheduleBinding?.llTwo?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_very_light_new))
                activityScheduleBinding?.tvTue?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvTueStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvTueEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        }
        activityScheduleBinding?.sWed?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                s3 = "1"
                activityScheduleBinding?.llWed?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                activityScheduleBinding?.tvWed?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                activityScheduleBinding?.tvWedStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
                activityScheduleBinding?.tvWedEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))

            } else {
                s3 = "0"

                activityScheduleBinding?.llWed?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_very_light_new))
                activityScheduleBinding?.tvWed?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvWedStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvWedEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        }
        activityScheduleBinding?.sThur?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                s4 = "1"
                activityScheduleBinding?.llThu?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                activityScheduleBinding?.tvThu?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                activityScheduleBinding?.tvThurStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
                activityScheduleBinding?.tvThurEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))

            } else {
                s4 = "0"

                activityScheduleBinding?.llThu?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_very_light_new))
                activityScheduleBinding?.tvThu?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvThurStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvThurEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        }
        activityScheduleBinding?.sfri?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s5 = "1"
                activityScheduleBinding?.llFri?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                activityScheduleBinding?.tvFri?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                activityScheduleBinding?.tvFriStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
                activityScheduleBinding?.tvFriEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))

            } else {
                s5 = "0"

                activityScheduleBinding?.llFri?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_very_light_new))
                activityScheduleBinding?.tvFri?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvFriStart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvFriEnd?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        }
        activityScheduleBinding?.sSat?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s6 = "1"
                activityScheduleBinding?.llSat?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                activityScheduleBinding?.tvSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                activityScheduleBinding?.tvStartSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
                activityScheduleBinding?.tvEndSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))

            } else {
                s6 = "0"
                activityScheduleBinding?.llSat?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_very_light_new))
                activityScheduleBinding?.tvSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvStartSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvEndSat?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        }
        activityScheduleBinding?.sSun?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                s7 = "1"
                activityScheduleBinding?.llSun?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
                activityScheduleBinding?.tvSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                activityScheduleBinding?.tvStartSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))
                activityScheduleBinding?.tvEndSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.txt_color))

            } else {
                s7 = "0"
                activityScheduleBinding?.llSun?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_very_light_new))
                activityScheduleBinding?.tvSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvStartSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                activityScheduleBinding?.tvEndSun?.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))

            }
        }

    }

    private fun slotEnableForDoctorOnlineHomeVisit(){
        activityScheduleBinding?.run {
            rbHours.visibility = View.VISIBLE; rbTask.visibility = View.VISIBLE
            rbTask.isChecked = true; rbHours.isChecked = true
            rbTask.text = getString(R.string.min_15_slots); rbHours.text = getString(R.string.min_45_slots)
            tvhAddRadius.text = getString(R.string.head_txt_add_radius_for_doctor)
            tvshAddRadius.text = getString(R.string.kilometer_desc_for_doctor)
            tvhBokingPrefha.text = getString(R.string.online_cons)
            tvhBokingPrefha2.text = getString(R.string.home_visit_cons)

            rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            rbHours.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

     }
    }

    private fun slotEnableForDoctorOnlineVisit(){
        activityScheduleBinding?.run {
            rbHours.visibility = View.GONE; rbTask.visibility = View.VISIBLE
            rbTask.isChecked = true
            rbTask.text = getString(R.string.min_15_slots)
            tvhBokingPrefha.text = getString(R.string.online_cons)

            tvhAddRadius.text = getString(R.string.head_txt_add_radius_for_doctor)
            tvshAddRadius.text = getString(R.string.kilometer_desc_for_doctor)

            rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
    }

    private fun slotEnableForLabt(){
        activityScheduleBinding?.run {
            rbHours.visibility = View.GONE; rbTask.visibility = View.VISIBLE
            rbTask.isChecked = true
            rbTask.text = getString(R.string.min_45_slots)

            rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
    }

  private fun is30MinSlotEnable(mFlag:Boolean){
        activityScheduleBinding?.run {
        if(mFlag){
              rbHours.visibility = View.GONE
              rbTask.isChecked = true
              rbTask.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        } else {
              rbTask.visibility = View.GONE
              rbHours.isChecked = true
              rbHours.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
     }
    }

    fun show(view: TextView?) {
        PopupMenu(requireContext(), view).apply {
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
        Log.e("type", type)
        if (type == "1" || type == "2") {
            if (compareDates(
                    activityScheduleBinding?.tvMonStart?.text.toString(),
                    activityScheduleBinding?.tvMonEnd?.text.toString()
                )
            ) {
                showToast(getString(R.string.end_time_should_be_greater))

                activityScheduleBinding?.tvMonStart?.text = "08:30 AM"
                activityScheduleBinding?.tvMonEnd?.text = "08:30 PM"

            }

        } else if (type == "4" || type == "3") {
            if (compareDates(
                    activityScheduleBinding?.tvTueStart?.text.toString(),
                    activityScheduleBinding?.tvTueEnd?.text.toString()
                )
            ) {
                showToast(getString(R.string.end_time_should_be_greater))

                activityScheduleBinding?.tvTueStart?.text = "08:30 AM"
                activityScheduleBinding?.tvTueEnd?.text = "08:30 PM"

            }

        } else if (type=="6" || type == "5") {
            if (compareDates(
                    activityScheduleBinding?.tvWedStart?.text.toString(),
                    activityScheduleBinding?.tvWedEnd?.text.toString()
                )
            ) {
                showToast(getString(R.string.end_time_should_be_greater))

                activityScheduleBinding?.tvWedStart?.text =  "08:30 AM"
                activityScheduleBinding?.tvWedEnd?.text = "08:30 PM"

            }

        } else if (type=="8" || type == "7") {
            if (compareDates(
                    activityScheduleBinding?.tvThurStart?.text.toString(),
                    activityScheduleBinding?.tvThurEnd?.text.toString()
                )
            ) {
                showToast(getString(R.string.end_time_should_be_greater))
                activityScheduleBinding?.tvThurStart?.text = "08:30 AM"
                activityScheduleBinding?.tvThurEnd?.text = "08:30 PM"

            }

        } else if (type== "10"|| type == "9") {
            if (compareDates(
                    activityScheduleBinding?.tvFriStart?.text.toString(),
                    activityScheduleBinding?.tvFriEnd?.text.toString()
                )
            ) {
                showToast(getString(R.string.end_time_should_be_greater))
                activityScheduleBinding?.tvFriStart?.text = "08:30 AM"
                activityScheduleBinding?.tvFriEnd?.text = "08:30 PM"

            }

        } else if (type=="12" || type == "11") {
            if (compareDates(
                    activityScheduleBinding?.tvStartSat?.text.toString(),
                    activityScheduleBinding?.tvEndSat?.text.toString()
                )
            ) {
                showToast(getString(R.string.end_time_should_be_greater))
                activityScheduleBinding?.tvStartSat?.text =  "08:30 AM"
                activityScheduleBinding?.tvEndSat?.text = "08:30 PM"

            }

        } else if (type=="14" || type == "13") {
            if (compareDates(
                    activityScheduleBinding?.tvStartSun?.text.toString(),
                    activityScheduleBinding?.tvEndSun?.text.toString())) {

                showToast(getString(R.string.end_time_should_be_greater))
                activityScheduleBinding?.tvStartSun?.text = "08:30 AM"
                activityScheduleBinding?.tvEndSun?.text = "08:30 PM"

            }

        }

    }

    override fun errorGetPatientFamilyListResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        showToast(throwable?.message?:getString(R.string.something_went_wrong))
    }

    override fun successSendSchedule(getsendScheduleres: GetDoctorProfileResponse) {
        baseActivity?.hideLoading()

        if (getsendScheduleres.code.equals(SUCCESS_CODE,ignoreCase = true)) {
            showToast(getsendScheduleres.message?:"")
        } else {
              CommonDialog.showDialogForSingleButton(requireActivity(), object : DialogClickCallback {
            }, null, getsendScheduleres.message?: getString(R.string.something_went_wrong))
        }

    }

    override fun successFetchSchedule(mResponse: ModelScheduleTiming) {
        baseActivity?.hideLoading()
        if (mResponse.code.equals(SUCCESS_CODE, ignoreCase = true)) {
            mResponse.let { getsendScheduleres ->

               if (getsendScheduleres.result?.accept_booking?.equals("0") == true) {
                   activityScheduleBinding?.rbAccept?.isChecked = true
                   activityScheduleBinding?.rbHide?.isChecked = false
                   updateUiForProvider()

               } else {
                   activityScheduleBinding?.rbAccept?.isChecked = false
                   activityScheduleBinding?.rbHide?.isChecked = true
                   updateUiForProvider(false)
               }

               if (getsendScheduleres.result?.slots.isNullOrEmpty().not()) {
                   activityScheduleBinding?.run {
                       // MON
                       getsendScheduleres.result?.slots?.let {
                           tvMonStart.text = it.getOrNull(0)?.slot_start_time
                           tvMonEnd.text = it.getOrNull(0)?.slot_end_time
                           sMon.isChecked = it.getOrNull(0)?.slot_day_enable.equals("1")
                           s1 = it.getOrNull(0)?.slot_day_enable ?: "0"

                           // Tue
                           tvTueStart.text = it.getOrNull(1)?.slot_start_time
                           tvTueEnd.text = it.getOrNull(1)?.slot_end_time
                           sTue.isChecked = it.getOrNull(1)?.slot_day_enable.equals("1")
                           s2 = it.getOrNull(1)?.slot_day_enable ?: "0"

                           //Wed
                           tvWedStart.text = it.getOrNull(2)?.slot_start_time
                           tvWedEnd.text = it.getOrNull(2)?.slot_end_time
                           sWed.isChecked = it.getOrNull(2)?.slot_day_enable.equals("1")
                           s3 = it.getOrNull(2)?.slot_day_enable ?: "0"

                           //Thr
                           tvThurStart.text = it.getOrNull(3)?.slot_start_time
                           tvThurEnd.text = it.getOrNull(3)?.slot_end_time
                           sThur.isChecked = it.getOrNull(3)?.slot_day_enable.equals("1")
                           s4 = it.getOrNull(3)?.slot_day_enable ?: "0"

                           //Fri
                           tvFriStart.text = it.getOrNull(4)?.slot_start_time
                           tvFriEnd.text = it.getOrNull(4)?.slot_end_time
                           sfri.isChecked = it.getOrNull(4)?.slot_day_enable.equals("1")
                           s5 = it.getOrNull(4)?.slot_day_enable ?: "0"

                           //Sat
                           tvStartSat.text = it.getOrNull(5)?.slot_start_time
                           tvEndSat.text = it.getOrNull(5)?.slot_end_time
                           sSat.isChecked = it.getOrNull(5)?.slot_day_enable.equals("1")
                           s6 = it.getOrNull(5)?.slot_day_enable ?: "0"

                           //sun
                           tvStartSun.text = it.getOrNull(6)?.slot_start_time
                           tvEndSun.text = it.getOrNull(6)?.slot_end_time
                           sSun.isChecked = it.getOrNull(6)?.slot_day_enable.equals("1")
                           s7 = it.getOrNull(6)?.slot_day_enable ?: "0"

                       }


                   }
               }

               // set the radius and address
               setupRadiusAndAddress(getsendScheduleres.result)
           }
        }
    }

    private fun updateUiForProvider(forHospDoc: Boolean = true) {
        if(forHospDoc){
            activityScheduleBinding?.rbAccept?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            activityScheduleBinding?.rbHide?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))

          if(isHospitalDoctor()) {
            activityScheduleBinding?.llHideAccept?.visibility = View.VISIBLE
            activityScheduleBinding?.llAddRadiusService?.visibility = View.GONE
            } else {
            activityScheduleBinding?.llHideAccept?.visibility = View.VISIBLE
            activityScheduleBinding?.llAddRadiusService?.visibility = View.VISIBLE
            }

        } else {
            activityScheduleBinding?.llHideAccept?.visibility = View.GONE
            activityScheduleBinding?.llAddRadiusService?.visibility = View.GONE

            activityScheduleBinding?.rbHide?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            activityScheduleBinding?.rbAccept?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))

        }

    }

    private fun setupRadiusAndAddress(result: ModelScheduleTiming.Result?) {
        result?.let { rl ->
        rl.service_address?.let { sa -> activityScheduleBinding?.edtAddress?.setText(sa) }
        rl.service_radius?.let { sr -> markRadius(sr) } ?: markRadius()
        rl.service_lat?.let { slat -> serviceLat = slat }
        rl.service_long?.let { slng -> serviceLong = slng }
        }
    }

    private fun fetchScheduleData() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val req = FetchScheduleRequest()
            req.user_id = scheduleActivityViewModel?.appSharedPref?.loginUserId

          //  when {
          //      viewModel.appSharedPref?.loginUserType?.equals(LoginTypes.HOSPITAL.type, true) == true -> {
          //          req.service_type = LoginTypes.PATHOLOGY.type
          //      }
          //      else -> {
                    req.service_type = scheduleActivityViewModel?.appSharedPref?.loginUserType
         //       }
        //    }

            // in case for nurse fetch will  use different api
            when {
                viewModel.appSharedPref?.loginUserType?.equals(LoginTypes.NURSE.type, true) == true -> {
                    if (scheduleType.equals(ScheduleTypes.TASK_BASED.nm, true)) {
                        scheduleActivityViewModel?.fetchScheduleForNurseTaskBased(req)
                    } else {
                        scheduleActivityViewModel?.fetchScheduleForNurseHourlyBased(req)
                    }
                }
                viewModel.appSharedPref?.loginUserType?.equals(LoginTypes.DOCTOR.type, true) == true -> { scheduleActivityViewModel?.fetchScheduleDataForDoctor(req) }

                else -> { scheduleActivityViewModel?.fetchScheduleData(req) }
            }
        }
    }

    private fun apiHitSendSchedule() {
        if (isNetworkConnected) {
            // validation over the data to save schedule
            val enableDays = arrayOf(s1, s2, s3, s4, s5, s6, s7).find { it == "1" } ?: "0"
            if(accept_booking.equals("0",true)) {

                if(isHospitalDoctor()){
                    when {
                        enableDays.equals("0", true) -> {
                            showToast(getString(R.string.enable_your_availability))
                            return
                        }
                    }
                } else {
                    when {
                        enableDays.equals("0", true) -> {
                            showToast(getString(R.string.enable_your_availability))
                            return
                        }
                        activityScheduleBinding?.edtAddress?.text.toString().isBlank() -> {
                            showToast(getString(R.string.choose_availability_location))
                            return
                        }
                        serviceRadius.isNullOrBlank() -> {
                            showToast(getString(R.string.select_eligibility_radius))
                            return
                        }
                    }
                }
            }


            val scheduleList: ArrayList<Slot> = ArrayList()
            activityScheduleBinding?.run {
                scheduleList.apply {
                    add(Slot(WEEKDAYS.MONDAY.get(), tvMonStart.text.toString(),  tvMonEnd.text.toString(), s1 ) )
                    add(Slot(WEEKDAYS.TUESDAY.get(), tvTueStart.text.toString(),tvTueEnd.text.toString(), s2  ) )
                    add(Slot(WEEKDAYS.WEDNESDAY.get(), tvWedStart.text.toString(), tvWedEnd.text.toString(), s3))
                    add(Slot(WEEKDAYS.THURSDAY.get(), tvThurStart.text.toString(), tvThurEnd.text.toString(), s4 ))
                    add(Slot(WEEKDAYS.FRIDAY.get(), tvFriStart.text.toString(), tvFriEnd.text.toString(),  s5 ) )
                    add(Slot(WEEKDAYS.SATURDAY.get(), tvStartSat.text.toString(), tvEndSat.text.toString(), s6) )
                    add(Slot(WEEKDAYS.SUNDAY.get(), tvStartSun.text.toString(), tvEndSun.text.toString(),  s7 ) )
                }
            }

            val sendRequest = SendScheduleRequest().apply {
                userId = scheduleActivityViewModel?.appSharedPref?.loginUserId
                acceptBooking = accept_booking
                slots = scheduleList

                service_radius = serviceRadius
                service_lat = serviceLat
                service_long = serviceLong
                service_address = activityScheduleBinding?.edtAddress?.text.toString().trim()
                serviceType = scheduleActivityViewModel?.appSharedPref?.loginUserType
            }

            baseActivity?.showLoading()
            when {
                viewModel.appSharedPref?.loginUserType?.equals(LoginTypes.NURSE.type, true) == true -> {
                    sendRequest.slot_type = if (scheduleType.equals(ScheduleTypes.TASK_BASED.nm, true)) PriceTypes.TASK_BASED.getMode() else PriceTypes.HOURLY_BASED.getMode()
                    scheduleActivityViewModel?.sendProviderSchedule(sendRequest)
                }
                viewModel.appSharedPref?.loginUserType?.equals(LoginTypes.LAB.type, true) == true -> {
                    sendRequest.slot_type = PriceTypes.TASK_BASED.getMode()
                    scheduleActivityViewModel?.sendProviderSchedule(sendRequest)
                }
                viewModel.appSharedPref?.loginUserType?.equals(LoginTypes.DOCTOR.type, true) == true -> {
                    sendRequest.slot_type = if (scheduleType.equals(ScheduleTypes.ONLINE_HOME_BASED.nm, true) ||
                        scheduleType.equals(ScheduleTypes.ONLINE_BASED.nm, true)) PriceTypes.ONLINE.getMode() else PriceTypes.HOME_VISIT.getMode()
                    scheduleActivityViewModel?.sendProviderScheduleForDoctor(sendRequest)
                }
                else -> { scheduleActivityViewModel?.sendProviderSchedule(sendRequest) }
            }



        } else { showToast(getString(R.string.check_network_connection)) }
    }

    private fun compareDates(starttime: String, endTime: String): Boolean {
        var isGreater = false
        try {
            val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

            val date1: Date? = formatter.parse(starttime)
            val date2: Date? = formatter.parse(endTime)
            isGreater = date2?.before(date1) == true || date2 == date1
        } catch (e1: ParseException) {
            e1.printStackTrace()
        }
        return isGreater
    }

    private var serviceRadius : String? = ""
    fun selectRadiusRadio(view: View) {
        activityScheduleBinding?.run {
            clearRadiusRadios()
            val mVie = when(view.id) {
                R.id.rb15km  -> rb15km
                R.id.rb30km  -> rb30km
                R.id.rb50km  -> rb50km
                R.id.rb75km  -> rb75km
                R.id.rb100km -> rb100km
                R.id.rb125km -> rb125km
                R.id.rb150km -> rb150km
                R.id.rb200km -> rb200km
                else -> rb15km
            }

            mVie.isChecked = true
            mVie.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            serviceRadius = mVie.text?.toString()?.replace("km","") ?: ""

        }


    }

    private fun markRadius(radiusValue: String? = "15") {
     activityScheduleBinding?.run {
         clearRadiusRadios()
         val mVie = when(radiusValue) {
             "15" -> rb15km
             "30"-> rb30km
             "50"-> rb50km
             "75"-> rb75km
             "100" -> rb100km
             "125"-> rb125km
             "150"-> rb150km
             "200"-> rb200km
             else -> rb15km
         }
         mVie.isChecked = true
         mVie.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
         serviceRadius = radiusValue ?: "15"

         Log.wtf("radius", "selectRadiusRadio: ${mVie.text}")
         Log.wtf("radius", "selectRadiusRadio_: $serviceRadius")
        }
    }

   private fun clearRadiusRadios(){
        activityScheduleBinding?.run {
            rb15km.isChecked = false
            rb15km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))
            rb30km.isChecked = false
            rb30km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))
            rb50km.isChecked = false
            rb50km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))
            rb75km.isChecked = false
            rb75km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))

            rb100km.isChecked = false
            rb100km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))
            rb125km.isChecked = false
            rb125km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))
            rb150km.isChecked = false
            rb150km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))
            rb200km.isChecked = false
            rb200km.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTxtGrey1))
        }
    }

    val AUTOCOMPLETE_REQUEST_CODE = 9098
    fun onCurrentLocation() { (activity as? ScheduleActivity)?.startGettingCurrenttLocation() }

    fun onManageLocation() {
            navigate<ActivityLocationFetch>()

    }

    fun onGoogleSearchCalled() {
        // Set the fields to specify which types of place data to return.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
        //.setCountry("NG")
        .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == -1) {
                try {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(it)
                        activityScheduleBinding?.edtAddress?.setText(place.address)
                        val destinationLatLng = place.latLng
                        val latitude = destinationLatLng?.latitude.toString()
                        val longitude = destinationLatLng?.longitude.toString()
                        serviceLat = latitude
                        serviceLong = longitude
                        Log.e("PLACE","latitude $latitude")
                        Log.e("PLACE","longitude $longitude")
                    }
                } catch (e:Exception){
                    println(e)
                }



            }
        }

    }

    fun updateAddress(address:String, lat:Double,lng:Double) {
        serviceLat = lat.toString()
        serviceLong = lng.toString()

     activityScheduleBinding?.edtAddress?.apply {
         setText(address)
         isEnabled = true

     }

    }

}

/*
      var autocompleteFragment: AutocompleteSupportFragment? =null

        autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteFragment?.setActivityMode(AutocompleteActivityMode.FULLSCREEN)
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {

            override fun onPlaceSelected(place: Place) {
              Log.e("PLACE", "Place: " + place.name + ", " + place.id)
                Log.e("PLACE","place $place")
                activityScheduleBinding?.edtAddress?.setText(place.address)
                val destinationLatLng = place.latLng
                val latitude = destinationLatLng?.latitude.toString()
                val longitude = destinationLatLng?.longitude.toString()
                serviceLat = latitude
                serviceLong = longitude
                Log.e("PLACE","latitude $latitude")
                Log.e("PLACE","longitude $longitude")
            }

            override fun onError(status: Status) {
                Log.e("ERROR", "An error occurred: $status")
            }
        })

*/