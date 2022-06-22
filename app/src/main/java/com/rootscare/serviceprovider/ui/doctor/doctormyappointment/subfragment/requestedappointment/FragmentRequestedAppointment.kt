package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.dialog.CommonDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.request.pushNotificationRequest.PushNotificationRequest
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.ResultItem
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorRequestedAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment.adapter.AdapterRequestedAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import java.util.ArrayList

class FragmentRequestedAppointment : BaseFragment<FragmentDoctorRequestedAppointmentBinding,
        FragmentRequestedAppointmentViewModel>(), FragmentRequestedAppointmentNavigator {
    private var lastPosition: Int? = null
    private var contactListAdapter: AdapterRequestedAppointmentListRecyclerview? = null
    private var booleanIsAcceptedClick = true
    private var doctorPosition: Int? = null
    private var doctorAppointmentMessage: String? = null
    var loginresponse: LoginResponse? = null
    private var fragmentDoctorRequestedAppointmentBinding: FragmentDoctorRequestedAppointmentBinding? = null
    private var fragmentRequestedAppointmentViewModel: FragmentRequestedAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_requested_appointment
    override val viewModel: FragmentRequestedAppointmentViewModel
        get() {
            fragmentRequestedAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentRequestedAppointmentViewModel::class.java
            )
            return fragmentRequestedAppointmentViewModel as FragmentRequestedAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentRequestedAppointment {
            val args = Bundle()
            val fragment = FragmentRequestedAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRequestedAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorRequestedAppointmentBinding = viewDataBinding
        val gson = Gson()
        val loginModelDataString: String = fragmentRequestedAppointmentViewModel?.appSharedPref?.loginmodeldata!!
        loginresponse = gson.fromJson(loginModelDataString, LoginResponse::class.java)
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcomingAppointmentRequest.userId = fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcomingAppointmentRequest.userId="18"
            fragmentRequestedAppointmentViewModel!!.apiDoctorAppointmentRequestList(getDoctorUpcomingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentListingRecyclerView(requestedAppointmentList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        val recyclerView = fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorRequestedAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterRequestedAppointmentListRecyclerview(requestedAppointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView = object : OnClickOfDoctorAppointment {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter?.requestedappointmentList!![id]!!.id!!,
                        "doctor",
                        "pending"
                    )
                )
            }

            override fun onAcceptBtnClick(id: String, text: String) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
//                homeViewModel?.appSharedPref?.deleteUserId()
                        booleanIsAcceptedClick = true
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            val updateAppointmentRequest = UpdateAppointmentRequest()
//            getDoctorUpcommingAppointmentRequest.userId=fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
                            updateAppointmentRequest.id = contactListAdapter?.requestedappointmentList!![id.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus = text
                            fragmentRequestedAppointmentViewModel!!.apiUpdateDoctorAppointmentRequest(
                                updateAppointmentRequest, id.toInt()
                            )
                        } else {
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to accept this appointment?")


            }

            override fun onRejectBtnBtnClick(id: String, text: String) {

                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
//                        homeViewModel?.appSharedPref?.deleteUserId()
                        booleanIsAcceptedClick = false
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            val updateAppointmentRequest = UpdateAppointmentRequest()
//                            getDoctorUpcomingAppointmentRequest.userId = fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
                            updateAppointmentRequest.id = contactListAdapter?.requestedappointmentList!![id.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus = text
                            fragmentRequestedAppointmentViewModel!!.apiUpdateDoctorAppointmentRequest(
                                updateAppointmentRequest, id.toInt()
                            )
                        } else {
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to reject this appointment?")
            }

        }

        if (lastPosition != null) {
            Handler().postDelayed({
                activity?.runOnUiThread {
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 400)
        }
    }

    override fun successGetDoctorRequestAppointmentResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?) {
        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse?.code.equals("200")) {
            if (getDoctorRequestAppointmentResponse?.result != null && getDoctorRequestAppointmentResponse.result.size > 0) {
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility = View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentListingRecyclerView(getDoctorRequestAppointmentResponse.result)
            } else {
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility = View.GONE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = "Doctor Appointment Request Not Found."
            }

        } else {
            fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility = View.GONE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = getDoctorRequestAppointmentResponse?.message
            Toast.makeText(activity, getDoctorRequestAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?, position: Int
    ) {
        baseActivity?.hideLoading()
        val userFirstName = if (loginresponse?.result?.firstName != null && !loginresponse?.result?.firstName.equals("")) {
            loginresponse?.result?.firstName!!
        } else {
            ""
        }

        val userLastName = if (loginresponse?.result?.lastName != null && !loginresponse?.result?.lastName.equals("")) {
            loginresponse?.result?.lastName!!
        } else {
            ""
        }
        if (getDoctorRequestAppointmentResponse?.code.equals("200")) {
            doctorPosition = position
            doctorAppointmentMessage = getDoctorRequestAppointmentResponse?.message
            val pushNotificationRequest = PushNotificationRequest()
            pushNotificationRequest.name = "$userFirstName $userLastName"
            pushNotificationRequest.userId = contactListAdapter?.requestedappointmentList!![position]!!.patientId
            pushNotificationRequest.userType = "patient"
            pushNotificationRequest.notificationFor = if (booleanIsAcceptedClick) "Appointment Accepted" else "Appointment Rejected"
//            val doctorName = fragmentRequestedAppointmentViewModel?.appSharedPref?.studentName
            if (booleanIsAcceptedClick)
                pushNotificationRequest.message = "$userFirstName $userLastName accepted this appointment"
            else
                pushNotificationRequest.message = "$userFirstName $userLastName rejected this appointment"
            fragmentRequestedAppointmentViewModel?.apiSendPush(pushNotificationRequest)
        } else {
            Toast.makeText(activity, getDoctorRequestAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successPushNotification(response: JsonObject) {
        baseActivity?.hideLoading()
        val jsonObject: JsonObject = response
        val status = jsonObject["status"].asBoolean
        val code = jsonObject["code"].asString
        val message = jsonObject["message"].asString
        if (code == "200") {
            if (booleanIsAcceptedClick) {
                contactListAdapter?.requestedappointmentList!![doctorPosition!!]?.acceptanceStatus = "accept"
            } else {
                contactListAdapter?.requestedappointmentList!![doctorPosition!!]?.acceptanceStatus = "reject"
            }
            contactListAdapter?.notifyItemChanged(doctorPosition!!)
            Toast.makeText(activity, doctorAppointmentMessage, Toast.LENGTH_SHORT).show()
//            if (isNetworkConnected) {
//                baseActivity?.showLoading()
//                val getDoctorUpcomingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
//                getDoctorUpcomingAppointmentRequest.userId = fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
//                fragmentRequestedAppointmentViewModel!!.apiDoctorAppointmentRequestList(getDoctorUpcomingAppointmentRequest)
//            } else {
//                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
//            }
            contactListAdapter?.requestedappointmentList?.removeAt(doctorPosition!!)
            contactListAdapter?.notifyItemRemoved(doctorPosition!!)
            contactListAdapter?.notifyItemRangeChanged(doctorPosition!!, contactListAdapter?.requestedappointmentList?.size!!)
            if (contactListAdapter?.requestedappointmentList?.size == 0) {
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility = View.GONE
            }
        }
    }

    override fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}