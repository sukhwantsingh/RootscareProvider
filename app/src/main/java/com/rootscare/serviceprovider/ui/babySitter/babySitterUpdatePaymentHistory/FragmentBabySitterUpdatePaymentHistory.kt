package com.rootscare.serviceprovider.ui.babySitter.babySitterUpdatePaymentHistory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.response.doctor.payment.PaymentResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesPaymentHistoryBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterUpdatePaymentHistory.adapter.AdapterBabySitterUpdatePaymentHistoryRecyclerview
import com.rootscare.serviceprovider.ui.base.BaseFragment

class FragmentBabySitterUpdatePaymentHistory :
    BaseFragment<FragmentNursesPaymentHistoryBinding, FragmentBabySitterUpdatePaymentHistoryViewModel>(),
    FragmentBabySitterUpdatePaymentHistoryNavigator {

    private var contactListAdapter: AdapterBabySitterUpdatePaymentHistoryRecyclerview? = null

    private var fragmentNursesPaymentHistoryBinding: FragmentNursesPaymentHistoryBinding? = null
    private var fragmentNursesPaymentHistoryViewModel: FragmentBabySitterUpdatePaymentHistoryViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_payment_history
    override val viewModel: FragmentBabySitterUpdatePaymentHistoryViewModel
        get() {
            fragmentNursesPaymentHistoryViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterUpdatePaymentHistoryViewModel::class.java
            )
            return fragmentNursesPaymentHistoryViewModel as FragmentBabySitterUpdatePaymentHistoryViewModel
        }

    companion object {
        fun newInstance(): FragmentBabySitterUpdatePaymentHistory {
            val args = Bundle()
            val fragment = FragmentBabySitterUpdatePaymentHistory()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesPaymentHistoryViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesPaymentHistoryBinding = viewDataBinding
        setUpPaymentHistorylistingRecyclerview()
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcomingAppointmentRequest.userId = fragmentNursesPaymentHistoryViewModel?.appSharedPref?.loginUserId
            fragmentNursesPaymentHistoryViewModel?.getPaymentHistoryFromApi(getDoctorUpcomingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpPaymentHistorylistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesPaymentHistoryBinding!!.recyclerViewNursesPaymenthistory != null)
        val recyclerView = fragmentNursesPaymentHistoryBinding!!.recyclerViewNursesPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterBabySitterUpdatePaymentHistoryRecyclerview(requireContext())
        recyclerView.adapter = contactListAdapter
    }

    override fun onSuccessPaymentList(response: PaymentResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentNursesPaymentHistoryBinding?.recyclerViewNursesPaymenthistory?.visibility = View.VISIBLE
                fragmentNursesPaymentHistoryBinding?.tvNoDate?.visibility = View.GONE
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            } else {
                fragmentNursesPaymentHistoryBinding?.recyclerViewNursesPaymenthistory?.visibility = View.INVISIBLE
                fragmentNursesPaymentHistoryBinding?.tvNoDate?.visibility = View.VISIBLE
            }
        } else {
            fragmentNursesPaymentHistoryBinding?.recyclerViewNursesPaymenthistory?.visibility = View.INVISIBLE
            fragmentNursesPaymentHistoryBinding?.tvNoDate?.visibility = View.VISIBLE
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}