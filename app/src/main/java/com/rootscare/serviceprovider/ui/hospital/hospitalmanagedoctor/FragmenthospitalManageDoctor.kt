package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.hospital.CommonHospitalId
import com.rootscare.data.model.request.hospital.hospitaldelte
import com.rootscare.data.model.request.hospital.requestdoctor
import com.rootscare.data.model.response.hospital.AllDoctorHosListingRes
import com.rootscare.data.model.response.hospital.ResultItemDoctor
import com.rootscare.interfaces.OnItemClickWithIdListenerHos
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageDoctorBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.adddoctoreinhospital.FragmentAddDoctorProfile
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.adapter.AdapterHospitalManageDoctorRecyclerview
import com.rootscare.serviceprovider.ui.hospital.profiledoctorhospital.FragmentDoctorProfileHospital


class FragmenthospitalManageDoctor : BaseFragment<FragmentHospitalManageDoctorBinding, FragmenthospitalManageDoctorViewModel>(),
    FragmenthospitalManageDoctorNavigator {
    private var fragmentHospitalManageDoctorBinding: FragmentHospitalManageDoctorBinding? = null
    private var fragmenthospitalManageDoctorViewModel: FragmenthospitalManageDoctorViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_doctor
    override val viewModel: FragmenthospitalManageDoctorViewModel
        get() {
            fragmenthospitalManageDoctorViewModel = ViewModelProviders.of(this).get(
                FragmenthospitalManageDoctorViewModel::class.java
            )
            return fragmenthospitalManageDoctorViewModel as FragmenthospitalManageDoctorViewModel
        }

    companion object {
        fun newInstance(): FragmenthospitalManageDoctor {
            val args = Bundle()
            val fragment = FragmenthospitalManageDoctor()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmenthospitalManageDoctorViewModel!!.navigator = this

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageDoctorBinding = viewDataBinding
        baseActivity?.showLoading()
        val commonUserIdRequest = requestdoctor()
        commonUserIdRequest.hospital_id = fragmenthospitalManageDoctorViewModel?.appSharedPref?.loginUserId
        fragmenthospitalManageDoctorViewModel!!.apidoctorlist(commonUserIdRequest)

        fragmentHospitalManageDoctorBinding?.txtAddHospitalSpeciality?.setOnClickListener {
            val commonRequest = CommonHospitalId()
            commonRequest.hospital_id = fragmenthospitalManageDoctorViewModel?.appSharedPref?.loginUserId
            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                FragmentAddDoctorProfile.newInstance("doctor", commonRequest.hospital_id!!)
            )

        }


    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview(requestedAppointmentList: ArrayList<ResultItemDoctor?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageDoctorBinding!!.recyclerViewHospitalManageDoctors != null)
        val recyclerView = fragmentHospitalManageDoctorBinding!!.recyclerViewHospitalManageDoctors
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalManageDoctorRecyclerview(requestedAppointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithIdListenerHos {
            override fun onItemClick(id: String) {
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentDoctorProfileHospital.newInstance(id)
                )
            }

            override fun onItemClickSecond(id: String) {
                println("id $id")
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentEditDoctorProfile.newInstance("hospital_doctor", id)
                )
            }

            override fun onItemClickThird(id: String) {
                val commonUserIdRequest = hospitaldelte()
                commonUserIdRequest.hospital_id = fragmenthospitalManageDoctorViewModel?.appSharedPref?.loginUserId
                commonUserIdRequest.id = id
                fragmenthospitalManageDoctorViewModel!!.apidoctordelete(commonUserIdRequest)
            }

        }

    }


    override fun successDoctorDepartmentListingResponse(doctorDepartmentListingResponse: AllDoctorHosListingRes?) {
        baseActivity?.hideLoading()
        if (doctorDepartmentListingResponse?.code.equals("200")) {
            if (doctorDepartmentListingResponse?.result != null && doctorDepartmentListingResponse.result.size > 0) {
                fragmentHospitalManageDoctorBinding?.recyclerViewHospitalManageDoctors?.visibility =
                    View.VISIBLE
                fragmentHospitalManageDoctorBinding?.tvNoDate?.visibility = View.GONE
                setUpViewReviewAndRatinglistingRecyclerview(doctorDepartmentListingResponse.result)
            } else {
                fragmentHospitalManageDoctorBinding?.recyclerViewHospitalManageDoctors?.visibility =
                    View.GONE
                fragmentHospitalManageDoctorBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageDoctorBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalManageDoctorBinding?.recyclerViewHospitalManageDoctors?.visibility =
                View.GONE
            fragmentHospitalManageDoctorBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageDoctorBinding?.tvNoDate?.text = doctorDepartmentListingResponse?.message
            Toast.makeText(
                activity,
                doctorDepartmentListingResponse?.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun errorDoctorDepartmentListingResponse(throwable: Throwable?) {

    }

}