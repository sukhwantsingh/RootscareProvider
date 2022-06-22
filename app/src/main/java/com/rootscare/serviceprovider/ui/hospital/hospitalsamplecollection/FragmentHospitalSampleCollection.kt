package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.hospital.LabSearch
import com.rootscare.data.model.request.hospital.getdoctorhospital
import com.rootscare.data.model.response.hospital.HospitalLabUpload
import com.rootscare.data.model.response.hospital.ResultSetLab
import com.rootscare.interfaces.OnItemClikWithIdListenerMain
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalSamplecollectionBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.adapter.AdapterHospitalSampleCollectionRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment.FragmentHospitalSampleCollectionDetails
import java.util.*

class FragmentHospitalSampleCollection : BaseFragment<FragmentHospitalSamplecollectionBinding, FragmentHospitalSampleCollectionViewModel>(),
    FragmentHospitalSampleCollectionNavigator {
    private var fragmentHospitalSamplecollectionBinding: FragmentHospitalSamplecollectionBinding? = null
    private var fragmentHospitalSampleCollectionViewModel: FragmentHospitalSampleCollectionViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_samplecollection
    override val viewModel: FragmentHospitalSampleCollectionViewModel
        get() {
            fragmentHospitalSampleCollectionViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalSampleCollectionViewModel::class.java
            )
            return fragmentHospitalSampleCollectionViewModel as FragmentHospitalSampleCollectionViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalSampleCollection {
            val args = Bundle()
            val fragment = FragmentHospitalSampleCollection()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalSampleCollectionViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalSamplecollectionBinding = viewDataBinding
        // setUpViewSampleCollectionlistingRecyclerview()
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = getdoctorhospital()
            getDoctorUpcommingAppointmentRequest.hospital_id =
                fragmentHospitalSampleCollectionViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentHospitalSampleCollectionViewModel!!.apidoctorappointmentupcomingList(
                getDoctorUpcommingAppointmentRequest
            )

        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
        fragmentHospitalSamplecollectionBinding?.txtSearchHospitalOrClinic?.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable) { // you can call or do what you want with your EditText here
// yourEditText...
                if (s.toString().isEmpty()) {
                    val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
                    getDoctorUpcomingAppointmentRequest.hospital_id =
                        fragmentHospitalSampleCollectionViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
                    fragmentHospitalSampleCollectionViewModel!!.apidoctorappointmentupcomingList(
                        getDoctorUpcomingAppointmentRequest
                    )
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
//                if(s.toString().length>2){
                if (isNetworkConnected) {
                    baseActivity?.showLoading()
                    val nurseSearchByNameRequest = LabSearch()
                    nurseSearchByNameRequest.hospital_id =
                        fragmentHospitalSampleCollectionViewModel?.appSharedPref?.loginUserId
                    nurseSearchByNameRequest.searchContent =
                        fragmentHospitalSamplecollectionBinding?.txtSearchHospitalOrClinic?.text?.toString()
                    fragmentHospitalSampleCollectionViewModel?.searchApiDoctorAppointmentUpcomingList(nurseSearchByNameRequest)
                }
//                }
            }
        })

    }

    // Set up recycler view for service listing if available


    private fun setUpLablistingRecyclerview(requestedappointmentList: LinkedList<ResultSetLab?>?) {
        assert(fragmentHospitalSamplecollectionBinding!!.recyclerViewHospitalSamplecollectionList != null)
        val recyclerView =
            fragmentHospitalSamplecollectionBinding!!.recyclerViewHospitalSamplecollectionList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalSampleCollectionRecyclerview(requestedappointmentList, context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListenerMain {
            override fun onItemClick(id: String) {
                //  lastPosition = position
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentHospitalSampleCollectionDetails.newInstance(id)
                )
            }

        }

    }

    override fun responseLisCancelledAppointment(response: HospitalLabUpload) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentHospitalSamplecollectionBinding?.recyclerViewHospitalSamplecollectionList?.visibility =
                    View.VISIBLE
                fragmentHospitalSamplecollectionBinding?.tvNoDate?.visibility = View.GONE
                setUpLablistingRecyclerview(response.result)
            } else {
                fragmentHospitalSamplecollectionBinding?.recyclerViewHospitalSamplecollectionList?.visibility =
                    View.GONE
                fragmentHospitalSamplecollectionBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalSamplecollectionBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalSamplecollectionBinding?.recyclerViewHospitalSamplecollectionList?.visibility =
                View.GONE
            fragmentHospitalSamplecollectionBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalSamplecollectionBinding?.tvNoDate?.text = response.message
            Toast.makeText(
                activity,
                response.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?) {
    }


}