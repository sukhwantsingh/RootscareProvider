package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.todayappointment

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
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorTodaysAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.todayappointment.adapter.AdapterNurseTodaysAppointmentRecyclerview
import java.util.*

class FragmentTodaysAppointmentForNurse : BaseFragment<FragmentDoctorTodaysAppointmentBinding, FragmentTodaysAppointmentForNurseViewModel>(),
    FragmentTodaysAppointmentForNurseNavigator {
    private var lastPosition: Int? = null
    private var uploadedImageItemPosition: Int? = null
    private var contactListAdapter: AdapterNurseTodaysAppointmentRecyclerview? = null

    //    private var fileNameForTempUse: String = ""
    private var imageSelectionModel: AddlabTestImageSelectionModel? = null

    private var fragmentDoctorTodaysAppointmentBinding: FragmentDoctorTodaysAppointmentBinding? = null
    private var fragmentTodaysAppointmentViewModel: FragmentTodaysAppointmentForNurseViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_todays_appointment
    override val viewModel: FragmentTodaysAppointmentForNurseViewModel
        get() {
            fragmentTodaysAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentTodaysAppointmentForNurseViewModel::class.java
            )
            return fragmentTodaysAppointmentViewModel as FragmentTodaysAppointmentForNurseViewModel
        }

    companion object {
        private val TAG = FragmentTodaysAppointmentForNurse::class.java.simpleName
        fun newInstance(): FragmentTodaysAppointmentForNurse {
            val args = Bundle()
            val fragment = FragmentTodaysAppointmentForNurse()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTodaysAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorTodaysAppointmentBinding = viewDataBinding

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val reqData = GetDoctorUpcommingAppointmentRequest()
            reqData.userId = fragmentTodaysAppointmentViewModel?.appSharedPref?.loginUserId
            fragmentTodaysAppointmentViewModel!!.apiNurseTodayAppointmentList(reqData)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview(todaysAppointList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        val recyclerView = fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterNurseTodaysAppointmentRecyclerview(todaysAppointList, requireContext())
        recyclerView?.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView2 = object : OnClickOfDoctorAppointment2 {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as NursrsHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter?.todaysAppointList!![id]!!.id!!,
                        "nurse", "today"
                    )
                )
            }

            override fun onAcceptBtnClick(id: String, text: String) {
                apiHitForCompleted(contactListAdapter?.todaysAppointList!![id.toInt()]!!.id!!, id.toInt())
            }

            override fun onUploadBtnClick(id: String, text: String) {
//                showPictureDialog()
                uploadedImageItemPosition = id.toInt()
            }

            override fun onRejectBtnBtnClick(id: String, text: String) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            val updateAppointmentRequest = UpdateAppointmentRequest()
                            updateAppointmentRequest.id = contactListAdapter?.todaysAppointList!![id.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus = text
                            fragmentTodaysAppointmentViewModel!!.apiUpdateNurseAppointmentRequest(updateAppointmentRequest, id.toInt())
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

    override fun successGetDoctorTodaysAppointmentResponse(getDoctorTodaysAppointmentResponse: GetDoctorTodaysAppointmentResponse?) {

        baseActivity?.hideLoading()
        if (getDoctorTodaysAppointmentResponse?.code.equals("200")) {
            if (getDoctorTodaysAppointmentResponse?.result != null && getDoctorTodaysAppointmentResponse.result.size > 0) {
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(getDoctorTodaysAppointmentResponse.result)
            } else {
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.text = "Doctor Today's Appointment List Not Found."
            }

        } else {
            fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.text = getDoctorTodaysAppointmentResponse?.message
            Toast.makeText(activity, getDoctorTodaysAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessMarkAsComplete(response: CommonResponse, position: Int) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            contactListAdapter?.todaysAppointList!![position]?.appointmentStatus = "Completed"
            contactListAdapter?.notifyItemChanged(position)
            /*contactListAdapter?.todaysAppointList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.todaysAppointList?.size!!)
            if (contactListAdapter?.todaysAppointList?.size == 0){
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            }*/
        }
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    ) {

        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse?.code.equals("200")) {
            contactListAdapter?.todaysAppointList!![position]?.acceptanceStatus = "reject"
            contactListAdapter?.notifyItemChanged(position)
            contactListAdapter?.todaysAppointList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.todaysAppointList?.size!!)
            if (contactListAdapter?.todaysAppointList?.size == 0) {
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            }
        }
    }

    private fun apiHitForCompleted(id: String, position: Int) {
        var request = CommonUserIdRequest()
        request.id = id
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentTodaysAppointmentViewModel!!.apiHitForMarkAsCompleteForNurse(
                request, position
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }


}