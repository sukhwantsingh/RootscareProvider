package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.rootscare.data.model.request.videoPushRequest.VideoPushRequest
import com.rootscare.data.model.response.videoPushResponse.VideoPushResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragNewAppointmentListingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter.AdapterAppointmentListingCommon
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter.OnAppointmentListingCallback
import com.rootscare.serviceprovider.utilitycommon.AppointmentTypes
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import com.rootscare.serviceprovider.utilitycommon.navigate
import com.rootscare.twilio.VideoCallActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.collections.ArrayList

private const val ARG_APPOINT_TYPE = "ARG_APPOINT_TYPE"

class FragNewAppointmentListing : BaseFragment<FragNewAppointmentListingBinding, ViewModelMyAppointments>(), AppointmentNavigator {

    private var binding: FragNewAppointmentListingBinding? = null
    private var mViewModel: ViewModelMyAppointments? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.frag_new_appointment_listing
    override val viewModel: ViewModelMyAppointments
        get() {
            mViewModel = ViewModelProviders.of(this).get(ViewModelMyAppointments::class.java)
            return mViewModel as ViewModelMyAppointments
        }

    var lastPosition: Int? = null
    private var detailModel : ModelAppointmentsListing.Result? = null

    companion object {
        val showSearch = MutableLiveData(false)

        var isAcceptedOrRejcted: Boolean? = null
        var icCompleted: Boolean = false

        @JvmStatic
        fun newInstance(appointType: String = "") =
            FragNewAppointmentListing().apply {
                arguments = Bundle().apply {
                    putString(ARG_APPOINT_TYPE, appointType)
                }
            }
    }

    private var appointmentType: String? = null
    private var mAppointAdapter: AdapterAppointmentListingCommon? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.navigator = this
        arguments?.let {
            appointmentType = it.getString(ARG_APPOINT_TYPE) ?: ""
        }
        mAppointAdapter = activity?.let { AdapterAppointmentListingCommon(it, appointmentType) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        initViews()
        observers()
        fetchAppointments()
    }


    private fun observers() {
        showSearch.observe(viewLifecycleOwner)  {
            if(it) {
               binding?.inclSearch?.root?.visibility = View.VISIBLE
            } else {
               binding?.inclSearch?.edtSearch?.setText("")
                binding?.inclSearch?.root?.visibility = View.GONE
            }
        }
    }

    private fun initViews() {
       binding?.inclSearch?.edtSearch?.addTextChangedListener { filterTaskList(it.toString()) }
       binding?.inclSearch?.imgCross?.setOnClickListener { hideKeyboard(); showSearch.value = false }

        binding?.swipeContainer?.setOnRefreshListener { fetchAppointments(showLoading = false)  }
        binding?.swipeContainer?.setColorSchemeResources(R.color.colorPrimary, R.color.green_paid,R.color.colorPrimary, R.color.green_paid)

        binding?.rvAppointment?.adapter = mAppointAdapter
        mAppointAdapter?.mCallback = object : OnAppointmentListingCallback {
            override fun onItemClick(pos: Int, id: String?) {
                lastPosition = pos
                AppointmentDetailScreen.appointmentId = id
                navigate<AppointmentDetailScreen>()
            }

            override fun onAcceptBtnClick(id: String, pos: Int, action: String) {
                CommonDialog.showDialog(requireActivity(), object : DialogClickCallback {
                    override fun onConfirm() { apiRejAccept(id, pos, action) }
                }, getString(R.string.confirmation), getString(R.string.do_realy_want_to_accept_appointment))
            }

            override fun onRejectBtnBtnClick(id: String, pos: Int, action: String) {
                CommonDialog.showDialog(requireActivity(), object : DialogClickCallback {
                    override fun onConfirm() { apiRejAccept(id, pos, action) }
                }, getString(R.string.confirmation), getString(R.string.do_realy_want_to_reject_appointment))
            }

            override fun onVideoCall(dataModel: ModelAppointmentsListing.Result?) {
                detailModel = dataModel
                hitVideoCall()
            }

        }
    }

    private fun filterTaskList(text: String) {
        val temp: ArrayList<ModelAppointmentsListing.Result?> = ArrayList()
        mAppointAdapter?.getUpdatedList()?.forEach { d ->
            d?.order_id?.let {
                if (it.lowercase().contains(text.lowercase())) {
                    temp.add(d)
                }
            }
        }

        binding?.tvNoDate?.visibility = if(temp.isNotEmpty()) View.GONE else View.VISIBLE

        // update recyclerview
        mAppointAdapter?.updateAfterFilterList(temp)
    }








    private fun hitVideoCall(){
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val videoPushRequest = VideoPushRequest()
            videoPushRequest.fromUserId = mViewModel?.appSharedPref?.loginUserId.orEmpty() // doctor_id
            videoPushRequest.toUserId = detailModel?.patient_id.orEmpty()  // patient_id
            roomName = "rootvideo_room_" + videoPushRequest.toUserId + "_" + videoPushRequest.fromUserId
            videoPushRequest.orderId = detailModel?.order_id // response.result.orderId
            videoPushRequest.roomName = "rootvideo_room_" + videoPushRequest.toUserId + "_" + videoPushRequest.fromUserId
            videoPushRequest.type = "doctor_to_patient_video_call"
            videoPushRequest.fromUserName = detailModel?.provider_name.orEmpty().trim() // response.result.doctorName
            videoPushRequest.toUserName = detailModel?.patient_name.orEmpty().trim()  // response.result.patientName
            mViewModel?.apiSendVideoPushNotification(videoPushRequest)

        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    private fun apiRejAccept(id: String, pos: Int, action: String) {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("id", id)
                addProperty("acceptance_status", action)
            }

            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.apiMarkAs(body, pos)

        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    fun updateData() {
        when (appointmentType) {
            AppointmentTypes.ONGOING.get() -> {
                lastPosition?.let {
                    if(isAcceptedOrRejcted == false && icCompleted) {
                        removePendingStatus(lastPosition)
                        resetStatic()
                    }
                }
            }

            AppointmentTypes.PENDING.get() -> {
                lastPosition?.let {
                    if (isAcceptedOrRejcted == true && !icCompleted) {
                        removePendingStatus(lastPosition)
                        resetStatic()
                    }else if (icCompleted) {
                        mAppointAdapter?.updatedArrayList?.clear()
                        fetchAppointments()
                        resetStatic()
                    }

                }
            }

            AppointmentTypes.UPCOMING.get() -> {
                lastPosition?.let {
                    if (icCompleted) {
                        mAppointAdapter?.updatedArrayList?.clear()
                        fetchAppointments()
                        resetStatic()
                    }
                }
            }
            AppointmentTypes.PAST.get() -> {
                lastPosition?.let {
                    if (isAcceptedOrRejcted == true && !icCompleted) {
                     //   removePendingStatus(lastPosition)
                        fetchAppointments()
                        resetStatic()
                    } else if (icCompleted) {
                        mAppointAdapter?.updatedArrayList?.clear()
                        fetchAppointments()
                        resetStatic()
                    }
                }
            }

            else -> Unit
        }
    }

    fun resetStatic(){
        lastPosition = null
        isAcceptedOrRejcted = null
        icCompleted = false
    }

    private fun removePendingStatus(postn: Int?) {
        when (appointmentType) {
            AppointmentTypes.ONGOING.get() -> mAppointAdapter?.let {
                it.updateMarkAcceptReject(postn)
                // noMoreData()
            }
            AppointmentTypes.PENDING.get() -> mAppointAdapter?.let {
                it.updateMarkAcceptReject(postn)
                // noMoreData()
            }
            AppointmentTypes.UPCOMING.get() -> mAppointAdapter?.let {
                it.updateMarkAcceptReject(postn)
                // noMoreData()
            }
            AppointmentTypes.PAST.get() -> mAppointAdapter?.let {
                it.updateMarkAcceptReject(postn)
                // noMoreData()
            }
            else -> Unit
        }

    }

    fun noMoreData() {
         if(mAppointAdapter?.updatedArrayList.isNullOrEmpty()){
           noData(getString(R.string.no_more_data_available))
          }
    }

    fun fetchAppointments(showLoading: Boolean = true) {
        if (isNetworkConnected) {
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            when (appointmentType) {
                AppointmentTypes.ONGOING.get() -> { if(showLoading) baseActivity?.showLoading()
                    mViewModel?.apiOngoing(body)
                }
                AppointmentTypes.PENDING.get() -> { if(showLoading) baseActivity?.showLoading()
                    mViewModel?.apiPending(body)
                }
                AppointmentTypes.UPCOMING.get() -> { if(showLoading) baseActivity?.showLoading()
                    mViewModel?.apiUpcoming(body)
                }
                AppointmentTypes.PAST.get() -> { if(showLoading) baseActivity?.showLoading()
                    mViewModel?.apiPast(body)
                }
                else -> Unit
            }

        } else {
            noData(getString(R.string.check_network_connection))
        }
    }

    override fun onSuccessResponse(response: ModelAppointmentsListing?) {
        try {
            baseActivity?.hideLoading()
            binding?.swipeContainer?.isRefreshing = false
            if (response?.code.equals(SUCCESS_CODE)) {
                response?.result?.let {
                    if (it.isNullOrEmpty().not()) {
                        binding?.run {
                            tvNoDate.visibility = View.GONE
                            rvAppointment.visibility = View.VISIBLE
                        }
                        mAppointAdapter?.loadDataIntoList(it)
                      //  startSmoothScroll(lastPosition, binding?.rvAppointment)
                    } else noData(response.message)
                } ?: run { noData(response?.message) }
            } else noData(response?.message)
        } catch (e: Exception) {
            baseActivity?.hideLoading()
            println(e)
        }
    }

    override fun onMarkAcceptReject(response: ModelAppointmentsListing?, position: Int) {
        baseActivity?.hideLoading()
        try {
            if (response?.code.equals(SUCCESS_CODE)) {
                removePendingStatus(position)
            } else showToast(response?.message ?: getString(R.string.something_went_wrong))

        } catch (e: Exception) {
            baseActivity?.hideLoading()
            println(e)
        }
    }

    private fun noData(message: String?) {
        binding?.run {
            tvNoDate.visibility = View.VISIBLE
            rvAppointment.visibility = View.GONE
            tvNoDate.text = message ?: getString(R.string.something_went_wrong)
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        binding?.swipeContainer?.isRefreshing = false
        baseActivity?.hideLoading()
        noData(throwable?.message)
    }

    private var roomName: String? = null
    override fun successVideoPushResponse(videoPushResponse: VideoPushResponse?) {
       baseActivity?.hideLoading()
        if (videoPushResponse?.status == true && videoPushResponse.code.equals(SUCCESS_CODE)) {
            val bundle = Bundle().apply {
                putString("roomName", roomName)
                putString("fromUserId", mViewModel?.appSharedPref?.loginUserId) // doctor_id
                putString("fromUserName", detailModel?.provider_name.orEmpty().trim())
                putString("toUserId", detailModel?.patient_id.orEmpty().trim())
                putString("toUserName",  detailModel?.patient_name.orEmpty().trim())
                putString("orderId",  detailModel?.order_id.orEmpty().trim())
                putBoolean("isDoctorCalling", true)
            }

            val intent = Intent(activity, VideoCallActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
//            activity?.finish()
        } else { showToast(videoPushResponse?.message ?: getString(R.string.something_went_wrong)) }
    }

}