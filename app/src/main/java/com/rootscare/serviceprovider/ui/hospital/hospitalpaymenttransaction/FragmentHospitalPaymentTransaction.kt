package com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.hospital.requestdoctor
import com.rootscare.data.model.response.hospital.PaymentResponseHospital
import com.rootscare.data.model.response.hospital.ResultItemPayment
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalPaymentTransactionBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction.adapter.AdapterHospitalPaymentTransactionRecyclerView

class FragmentHospitalPaymentTransaction: BaseFragment<FragmentHospitalPaymentTransactionBinding, FragmentHospitalPaymentTransactionViewModel>(),
    FragmentHospitalPaymentTransactionNavigator {
    private var fragmentHospitalPaymentTransactionBinding: FragmentHospitalPaymentTransactionBinding? = null
    private var fragmentHospitalPaymentTransactionViewModel: FragmentHospitalPaymentTransactionViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_payment_transaction
    override val viewModel: FragmentHospitalPaymentTransactionViewModel
        get() {
            fragmentHospitalPaymentTransactionViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalPaymentTransactionViewModel::class.java!!)
            return fragmentHospitalPaymentTransactionViewModel as FragmentHospitalPaymentTransactionViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalPaymentTransaction {
            val args = Bundle()
            val fragment = FragmentHospitalPaymentTransaction()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalPaymentTransactionViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalPaymentTransactionBinding = viewDataBinding
        var commonUserIdRequest= requestdoctor()
        commonUserIdRequest.hospital_id=fragmentHospitalPaymentTransactionViewModel?.appSharedPref?.loginUserId
        fragmentHospitalPaymentTransactionViewModel!!.apidoctorlist(commonUserIdRequest)

    }
    private fun setUpViewReviewAndRatinglistingRecyclerview(requestedappointmentList: ArrayList<ResultItemPayment?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalPaymentTransactionBinding!!.recyclerViewHospitalPaymenthistory != null)
        val recyclerView = fragmentHospitalPaymentTransactionBinding!!.recyclerViewHospitalPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
     //   val contactListAdapter = AdapterHospitalManageDoctorRecyclerview(requestedappointmentList, context!!)
        val contactListAdapter = AdapterHospitalPaymentTransactionRecyclerView(requestedappointmentList, context!!)
        recyclerView.adapter = contactListAdapter
/*
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListenerHos {
            override fun onItemClick(id: String) {
                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorProfileHospital.newInstance(id))

            }

            override fun onItemClickSeconf(id: String) {
                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentEditDoctorProfile.newInstance("doctor",id))
            }

            override fun onItemClickThird(id: String) {
                var commonUserIdRequest= hospitaldelte()
                commonUserIdRequest.hospital_id=fragmenthospitalManageDoctorViewModel?.appSharedPref?.loginUserId
                commonUserIdRequest.id=id
                fragmenthospitalManageDoctorViewModel!!.apidoctordelete(commonUserIdRequest)
            }


        }
*/



    }
    override fun successDoctorDepartmentListingResponse(doctorDepartmentListingResponse: PaymentResponseHospital?) {
        baseActivity?.hideLoading()
        if (doctorDepartmentListingResponse?.code.equals("200")) {
            if (doctorDepartmentListingResponse?.result != null && doctorDepartmentListingResponse.result?.size!! > 0) {
                fragmentHospitalPaymentTransactionBinding?.recyclerViewHospitalPaymenthistory?.visibility =
                    View.VISIBLE
                fragmentHospitalPaymentTransactionBinding?.tvNoDate?.visibility = View.GONE
                setUpViewReviewAndRatinglistingRecyclerview(doctorDepartmentListingResponse.result)
            } else {
                fragmentHospitalPaymentTransactionBinding?.recyclerViewHospitalPaymenthistory?.visibility =
                    View.GONE
                fragmentHospitalPaymentTransactionBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalPaymentTransactionBinding?.tvNoDate?.setText("Doctor Appointment Not Found.")
            }

        } else {
            fragmentHospitalPaymentTransactionBinding?.recyclerViewHospitalPaymenthistory?.visibility =
                View.GONE
            fragmentHospitalPaymentTransactionBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalPaymentTransactionBinding?.tvNoDate?.setText(
                doctorDepartmentListingResponse?.message
            )
            Toast.makeText(
                activity,
                doctorDepartmentListingResponse?.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun errorDoctorDepartmentListingResponse(throwable: Throwable?) {
    }

    // Set up recycler view for service listing if available

}