package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.rootscare.data.model.request.hospital.CommonHospitalId
import com.rootscare.data.model.request.hospital.DeleteDepartment
import com.rootscare.data.model.request.hospital.EditDepart
import com.rootscare.data.model.request.hospital.EditNew
import com.rootscare.data.model.response.hospital.HospitalHDepartmentListResponse
import com.rootscare.data.model.response.hospital.HospitalResultItem
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClickHospitalListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageDepartmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.adapter.AdapterHospitalManageDepartmentRecyclerview
import java.util.*

class FragmentHospitalManageDepartment : BaseFragment<FragmentHospitalManageDepartmentBinding, FragmentHospitalUpdateProfileViewModel>(),
    FragmentHospitalProfileUpdateNavigator {
    private var fragmentHospitalManageDepartmentBinding: FragmentHospitalManageDepartmentBinding? = null
    private var fragmentHospitalManageDepartmentViewModel: FragmentHospitalUpdateProfileViewModel? = null

    //    private var autocompleteFragment: AutocompleteSupportFragment? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_department
    override val viewModel: FragmentHospitalUpdateProfileViewModel
        get() {
            fragmentHospitalManageDepartmentViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalUpdateProfileViewModel::class.java
            )
            return fragmentHospitalManageDepartmentViewModel as FragmentHospitalUpdateProfileViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalManageDepartment {
            val args = Bundle()
            val fragment = FragmentHospitalManageDepartment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalManageDepartmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageDepartmentBinding = viewDataBinding

//        autocomplete_fragment = activity?.supportFragmentManager?.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?




        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
//                Log.e("PLACE", "Place: " + place.name + ", " + place.id)
                println("place $place")
                val destinationLatLng = place.latLng
                val latitude = destinationLatLng?.latitude.toString()
                val longitude = destinationLatLng?.longitude.toString()
                println("latitude $latitude")
                println("longitude $longitude")
            }

            override fun onError(status: Status) {
                Log.e("ERROR", "An error occurred: $status")
            }
        })

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val commonUserIdRequest = CommonHospitalId()
            commonUserIdRequest.hospital_id = fragmentHospitalManageDepartmentViewModel?.appSharedPref?.loginUserId
            fragmentHospitalManageDepartmentViewModel!!.apicaregiverprofile(commonUserIdRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        // setUpViewReviewAndRatingListingRecyclerview()

        fragmentHospitalManageDepartmentBinding?.txtAddHpospitalSpeciality?.setOnClickListener {
            val commonUserIdRequest = CommonHospitalId()
            commonUserIdRequest.hospital_id = fragmentHospitalManageDepartmentViewModel?.appSharedPref?.loginUserId

            val hospitalId: String? = commonUserIdRequest.hospital_id
            hospitalId?.let { addDialog(it) }
        }


    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatingListingRecyclerview(requestedAppointmentList: ArrayList<HospitalResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
//        assert(fragmentHospitalManageDepartmentBinding!!.recyclerViewHospitalManageDepartmentSpecialities != null)
        val recyclerView = fragmentHospitalManageDepartmentBinding!!.recyclerViewHospitalManageDepartmentSpecialities
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalManageDepartmentRecyclerview(requestedAppointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickHospitalListener {
            override fun onItemClick(position: Int, toString: String) {
                val commonUserIdRequest = EditNew()
                commonUserIdRequest.hospital_id = fragmentHospitalManageDepartmentViewModel?.appSharedPref?.loginUserId
                commonUserIdRequest.department_id = position

                val hospitalId: String? = commonUserIdRequest.hospital_id
                hospitalId?.let { addNewDialog(it, position, toString) }
            }

            override fun onSecondItemClick(position: Int) {

                CommonDialog.showDialogForDelete(context!!, object :
                    DialogClickCallback {
                    override fun onConfirm() {
                        baseActivity?.showLoading()

                        val commonUserIdRequest = DeleteDepartment()
                        commonUserIdRequest.hospital_id = fragmentHospitalManageDepartmentViewModel?.appSharedPref?.loginUserId
                        commonUserIdRequest.department_id = position
                        fragmentHospitalManageDepartmentViewModel!!.apideletedepartprofile(commonUserIdRequest)

                    }

                    override fun onDismiss() {

                    }
                })
            }

        }

    }

    private fun showDialog(hospital_id: String, title: String) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_hospital_add_sepciality)
        val body = dialog?.findViewById(R.id.edit_text_title) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.btn_positive) as TextView
        //val noBtn = dialog.findViewById(R.id.noBtn) as TextView
        yesBtn.setOnClickListener {
            val commonUserIdRequest = EditDepart()
            commonUserIdRequest.hospital_id = hospital_id
            //  commonUserIdRequest.department_id = id
            commonUserIdRequest.title = title
            fragmentHospitalManageDepartmentViewModel!!.apieditepartprofile(commonUserIdRequest)

            dialog.dismiss()
        }
        //  noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun addNewDialog(hospital_id: String, id: Int, title: String) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setContentView(R.layout.dialog_hospital_add_sepciality)
        val body = dialog?.findViewById(R.id.edit_text_title) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.btn_positive) as TextView
        //val noBtn = dialog.findViewById(R.id.noBtn) as TextView
        yesBtn.setOnClickListener {
            baseActivity?.showLoading()
            val commonUserIdRequest = EditNew()
            commonUserIdRequest.hospital_id = hospital_id
            commonUserIdRequest.department_id = id
            commonUserIdRequest.title = body.text.toString()
            fragmentHospitalManageDepartmentViewModel!!.apieditnewepartprofile(commonUserIdRequest)

            dialog.dismiss()
        }
        //  noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun addDialog(hospital_id: String) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setContentView(R.layout.dialog_hospital_add_sepciality)
        val body = dialog?.findViewById(R.id.edit_text_title) as TextView

        val yesBtn = dialog.findViewById(R.id.btn_positive) as TextView
        //val noBtn = dialog.findViewById(R.id.noBtn) as TextView
        yesBtn.setOnClickListener {
            baseActivity?.showLoading()
            val commonUserIdRequest = EditDepart()
            commonUserIdRequest.hospital_id = hospital_id
            //  commonUserIdRequest.department_id=id
            commonUserIdRequest.title = body.text.toString()
            fragmentHospitalManageDepartmentViewModel!!.apieditepartprofile(commonUserIdRequest)

            dialog.dismiss()
        }
        //  noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun successGetDoctorProfileResponse(getDoctorProfileResponse: HospitalHDepartmentListResponse?) {
        baseActivity?.hideLoading()
        if (getDoctorProfileResponse?.code.equals("200")) {
            if (getDoctorProfileResponse?.result != null && getDoctorProfileResponse.result.size > 0) {
                fragmentHospitalManageDepartmentBinding?.recyclerViewHospitalManageDepartmentSpecialities?.visibility =
                    View.VISIBLE
                fragmentHospitalManageDepartmentBinding?.tvNoDate?.visibility = View.GONE
                setUpViewReviewAndRatingListingRecyclerview(getDoctorProfileResponse.result)
            } else {
                fragmentHospitalManageDepartmentBinding?.recyclerViewHospitalManageDepartmentSpecialities?.visibility =
                    View.GONE
                fragmentHospitalManageDepartmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageDepartmentBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalManageDepartmentBinding?.recyclerViewHospitalManageDepartmentSpecialities?.visibility =
                View.GONE
            fragmentHospitalManageDepartmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageDepartmentBinding?.tvNoDate?.text = getDoctorProfileResponse?.message
            Toast.makeText(
                activity,
                getDoctorProfileResponse?.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun errorGetDoctorProfileResponse(throwable: Throwable?) {

    }
}