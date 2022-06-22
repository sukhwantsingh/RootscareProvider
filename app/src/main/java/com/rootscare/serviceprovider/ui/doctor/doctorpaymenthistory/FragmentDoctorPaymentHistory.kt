package com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.response.doctor.payment.PaymentResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorPaymenthistoryBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.adapter.AdapterDoctorPaymentHistoryRecyclerview

class FragmentDoctorPaymentHistory : BaseFragment<FragmentDoctorPaymenthistoryBinding, FragmentDoctorPaymentHistoryViewModel>(),
    FragmentDoctorPaymentHistoryNavigator {

    private var contactListAdapter: AdapterDoctorPaymentHistoryRecyclerview? = null


    private var fragmentDoctorPaymenthistoryBinding: FragmentDoctorPaymenthistoryBinding? = null
    private var fragmentDoctorPaymentHistoryViewModel: FragmentDoctorPaymentHistoryViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_paymenthistory
    override val viewModel: FragmentDoctorPaymentHistoryViewModel
        get() {
            fragmentDoctorPaymentHistoryViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorPaymentHistoryViewModel::class.java!!
            )
            return fragmentDoctorPaymentHistoryViewModel as FragmentDoctorPaymentHistoryViewModel
        }

    companion object {
        fun newInstance(): FragmentDoctorPaymentHistory {
            val args = Bundle()
            val fragment = FragmentDoctorPaymentHistory()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorPaymentHistoryViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorPaymenthistoryBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })
        setUpPaymentHistorylistingRecyclerview()

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId = fragmentDoctorPaymentHistoryViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId = "11"    // for testing
            fragmentDoctorPaymentHistoryViewModel!!.getPaymentHistoryFromApi(
                getDoctorUpcommingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpPaymentHistorylistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorPaymenthistoryBinding!!.recyclerViewDoctorPaymenthistory != null)
        val recyclerView = fragmentDoctorPaymenthistoryBinding!!.recyclerViewDoctorPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterDoctorPaymentHistoryRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }

    override fun onSuccessPaymentList(response: PaymentResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentDoctorPaymenthistoryBinding?.recyclerViewDoctorPaymenthistory?.visibility = View.VISIBLE
                fragmentDoctorPaymenthistoryBinding?.tvNoDate?.visibility = View.GONE
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            }else{
                fragmentDoctorPaymenthistoryBinding?.recyclerViewDoctorPaymenthistory?.visibility = View.INVISIBLE
                fragmentDoctorPaymenthistoryBinding?.tvNoDate?.visibility = View.VISIBLE
            }
        }else{
            fragmentDoctorPaymenthistoryBinding?.recyclerViewDoctorPaymenthistory?.visibility = View.INVISIBLE
            fragmentDoctorPaymenthistoryBinding?.tvNoDate?.visibility = View.VISIBLE
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}