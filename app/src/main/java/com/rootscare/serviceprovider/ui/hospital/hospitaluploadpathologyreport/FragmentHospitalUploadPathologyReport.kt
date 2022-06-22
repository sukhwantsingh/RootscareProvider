package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.hospital.getdoctorhospital
import com.rootscare.data.model.response.hospital.ReportData
import com.rootscare.data.model.response.hospital.ReportResultItem
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalUploadPathologyReportBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.adapter.AdapterUploadPathologyReportUserRecyclerView
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment.FragmentPathReportDocumentUpload
import java.util.*

class FragmentHospitalUploadPathologyReport :
    BaseFragment<FragmentHospitalUploadPathologyReportBinding, FragmentHospitalUploadPathologyReportViewModel>(),
    FragmentHospitalUploadPathologyReportNavigator {
    private var fragmentHospitalUploadPathologyReportBinding: FragmentHospitalUploadPathologyReportBinding? = null
    private var fragmentHospitalUploadPathologyReportViewModel: FragmentHospitalUploadPathologyReportViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_upload_pathology_report
    override val viewModel: FragmentHospitalUploadPathologyReportViewModel
        get() {
            fragmentHospitalUploadPathologyReportViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalUploadPathologyReportViewModel::class.java
            )
            return fragmentHospitalUploadPathologyReportViewModel as FragmentHospitalUploadPathologyReportViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalUploadPathologyReport {
            val args = Bundle()
            val fragment = FragmentHospitalUploadPathologyReport()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalUploadPathologyReportViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalUploadPathologyReportBinding = viewDataBinding
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
            getDoctorUpcomingAppointmentRequest.hospital_id = fragmentHospitalUploadPathologyReportViewModel?.appSharedPref?.loginUserId
            fragmentHospitalUploadPathologyReportViewModel!!.apiPathologyReportList(getDoctorUpcomingAppointmentRequest)

        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    // Set up recycler view for report listing if available
    private fun setUpViewReportListingRecyclerView(requestedReportResultItemList: LinkedList<ReportData?>?) {
        val recyclerView = fragmentHospitalUploadPathologyReportBinding!!.recyclerViewHospitalUploadpathologytestreportList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterUploadPathologyReportUserRecyclerView(requestedReportResultItemList!!, context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
            override fun onItemClick(id: Int) {
                println("id $id")
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentPathReportDocumentUpload.newInstance(
                        requestedReportResultItemList[id]!!.patientId!!,
                        requestedReportResultItemList[id]!!.appointmentId!!
                    )
                )
            }

        }

    }

    override fun successPathologyListResponse(response: ReportResultItem) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentHospitalUploadPathologyReportBinding?.recyclerViewHospitalUploadpathologytestreportList?.visibility =
                    View.VISIBLE
                fragmentHospitalUploadPathologyReportBinding?.tvNoDate?.visibility = View.GONE
                setUpViewReportListingRecyclerView(response.result)
            } else {
                fragmentHospitalUploadPathologyReportBinding?.recyclerViewHospitalUploadpathologytestreportList?.visibility =
                    View.GONE
                fragmentHospitalUploadPathologyReportBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalUploadPathologyReportBinding?.tvNoDate?.text = "Pathology Appointment Not Found."
            }

        } else {
            fragmentHospitalUploadPathologyReportBinding?.recyclerViewHospitalUploadpathologytestreportList?.visibility =
                View.GONE
            fragmentHospitalUploadPathologyReportBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalUploadPathologyReportBinding?.tvNoDate?.text = response.message
            Toast.makeText(
                activity,
                response.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun errorPathologyListResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
    }

}