package com.rootscare.serviceprovider.ui.nurses.nurseprofile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewProfileForProvidersBinding
import com.rootscare.serviceprovider.ui.babySitter.home.BabySitterHomeActivity
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctorImportantDocumentrecyclerview
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterHospitalDepartments
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.ui.pricelistss.PriceListScreen
import com.rootscare.serviceprovider.ui.scheduless.ScheduleActivity
import com.rootscare.serviceprovider.utilitycommon.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class FragmentNursesProfile : BaseFragment<LayoutNewProfileForProvidersBinding, FragmentNursesProfileViewModel>(),
    FragmentNursesProfileNavigator {
    private var fragmentNursesProfileBinding: LayoutNewProfileForProvidersBinding? = null
    private var fragmentNursesProfileViewModel: FragmentNursesProfileViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.layout_new_profile_for_providers
    override val viewModel: FragmentNursesProfileViewModel
        get() {
            fragmentNursesProfileViewModel = ViewModelProviders.of(this).get(
                FragmentNursesProfileViewModel::class.java
            )
            return fragmentNursesProfileViewModel as FragmentNursesProfileViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesProfile {
            val args = Bundle()
            val fragment = FragmentNursesProfile()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesProfileViewModel?.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesProfileBinding = viewDataBinding

        initViews()
        apiCalling()
    }


    private fun initViews() {
        fragmentNursesProfileBinding?.run {
            tvEditprofile.setOnClickListener {
                when (activity) {
                    is NursrsHomeActivity? -> {
                        (activity as? NursrsHomeActivity)?.checkFragmentInBackStackAndOpen(
                            FragmentNursesEditProfile.newInstance())
                    }
                    is BabySitterHomeActivity? -> {
                        (activity as? BabySitterHomeActivity)?.checkFragmentInBackStackAndOpen(
                            FragmentNursesEditProfile.newInstance())
                    }
                    is CaregiverHomeActivity? -> {
                        (activity as? CaregiverHomeActivity)?.checkFragmentInBackStackAndOpen(
                            FragmentNursesEditProfile.newInstance())
                    }
                    is PhysiotherapyHomeActivity? -> {
                        (activity as? PhysiotherapyHomeActivity)?.checkFragmentInBackStackAndOpen(
                            FragmentNursesEditProfile.newInstance())
                    }
                    is HospitalHomeActivity? -> {
                        (activity as? HospitalHomeActivity)?.checkFragmentInBackStackAndOpen(
                            FragmentNursesEditProfile.newInstance())
                    }
                }
            }
            tvSchdelavaility.setOnClickListener { navigate<ScheduleActivity>() }
            tvPricelistEdit.setOnClickListener { navigate<PriceListScreen>() }
        }
    }

    private fun apiCalling() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", fragmentNursesProfileViewModel?.appSharedPref?.loginUserType)
                addProperty("id", fragmentNursesProfileViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            fragmentNursesProfileViewModel?.apiProfile(body)
        } else {
            showToast(getString(R.string.network_unavailable))
        }

   //     bindNewLayout(fragmentNursesProfileViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result)
    }

    override fun onSuccessUserProfile(response: ModelUserProfile?) {
        baseActivity?.hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            if (response?.result != null) {
               bindNewLayout(response.result)
            } else {
                showToast(response?.message ?: "")
            }
        } else {
            showToast(response?.message ?: "")
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
      showToast(getString(R.string.something_went_wrong))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindNewLayout(response: ModelUserProfile.Result?) {
        fragmentNursesProfileBinding?.run {
            response?.let {
                when (fragmentNursesProfileViewModel?.appSharedPref?.loginUserType) {
                    // important details
                    LoginTypes.NURSE.type, LoginTypes.DOCTOR.type, LoginTypes.PHYSIOTHERAPY.type -> {
                        grpNurPhyDoc.visibility = View.VISIBLE
                        grpBabyCareHosp.visibility = View.VISIBLE

                        tvSpeciality.text = it.speciality
                        tvIdentityNum.text = it.id_number
                        tvQualification.text = it.qualification
                        tvSchfs.text = it.scfhs_number

                        bindProviderData(it)

                    }
                    LoginTypes.BABYSITTER.type, LoginTypes.CAREGIVER.type -> {
                        grpBabyCareHosp.visibility = View.VISIBLE
                        tvIdentityNum.text = it.id_number
                        tvQualification.text = it.qualification
                        bindProviderData(it)
                    }

                    LoginTypes.HOSPITAL.type -> {
                        tvhIdentityNum.text = getString(R.string.moh_licence)
                        tvhQualification.text = getString(R.string.registration_number)
                        tvhLabTest.text = getString(R.string.lab_test)

                        tvIdentityNum.text = it.hosp_moh_lic_no   // moh licence number
                        tvQualification.text = it.hosp_reg_no  // registration number

                        grpBabyCareHosp.visibility = View.VISIBLE
                        tvhLabTest.visibility = View.VISIBLE
                        tvLabTest.visibility = View.VISIBLE

                        bindHospitalData(it)
                    }
                      LoginTypes.LAB.type -> {
                        tvhIdentityNum.text = getString(R.string.moh_licence)
                        tvhQualification.text = getString(R.string.registration_number)
                        tvhLabTest.text = getString(R.string.lab_test)

                        tvIdentityNum.text = it.hosp_moh_lic_no   // moh licence number
                        tvQualification.text = it.hosp_reg_no  // registration number

                        grpBabyCareHosp.visibility = View.VISIBLE

                        bindLabData(it)
                    }
                    else -> Unit
                }


                true
            }
        }
    }

    private fun bindProviderData(result: ModelUserProfile.Result?) {
        fragmentNursesProfileBinding?.run {
            result?.let {
                crdBtmSchPricelist.visibility = View.VISIBLE

                imgProfile.setCircularRemoteImage(it.image)
                tvUsername.text = it.first_name + " " + it.last_name.orEmpty()
                tvhSpecialityHeader.text = if(it.hospital_id.isNullOrBlank()) it.speciality else "${it.hospital_name}"
                tvemail.text = it.email
                tvphn.text = it.phone_number

                tvBankName.text = if (it.experience.isNullOrBlank().not()) "${it.experience} YR" else "-"  // exp
                tvAcName.text = it.booking_count?.toString() ?: "0"   // Booking count
                tvAcNum.text = it.avg_rating?.toFloat().toString()  //avg  rating
                tvAddress.text = it.description // desc about

              // show documents which was uploaded
                setDocumentsNL(rvDocuments, it)
        }
    }
    }

    private fun bindLabData(result: ModelUserProfile.Result?) {
        fragmentNursesProfileBinding?.run {
            result?.let {
            crdDepartments.visibility = View.GONE

                 tvhAcname.text = getString(R.string.lab_test)
                 tvhBank.text = getString(R.string.established)

                imgProfile.setCircularRemoteImage(it.image)
                tvUsername.text = it.first_name
                tvhSpecialityHeader.text = it.address

                tvemail.text = it.email
                tvphn.text = it.phone_number

                tvBankName.text = if (it.experience.isNullOrBlank().not()) "${it.experience}" else "N/A"  // establishment

                tvAcNum.text = it.avg_rating?.toFloat()?.toString() ?: 0.0.toString() // avg  rating
                tvAcName.text = it.lab_test_count   // lab test count
                tvAddress.text = it.description // desc about

                setDocumentsNLForHospital(rvDocuments, it)

        }
    }
  }

    private fun bindHospitalData(result: ModelUserProfile.Result?) {
        fragmentNursesProfileBinding?.run {
            result?.let {
            crdDepartments.visibility = View.VISIBLE

            tvhBank.text = getString(R.string.established)
            tvhAcname.text = getString(R.string.doctors)

                imgProfile.setCircularRemoteImage(it.image)
                tvUsername.text = it.first_name
                tvhSpecialityHeader.text = it.address

                tvemail.text = it.email
                tvphn.text = it.phone_number

                tvBankName.text = if (it.experience.isNullOrBlank().not()) "${it.experience}" else "N/A"  // establishment
                tvAcName.text = it.doctors_count   // doctors count
                tvAcNum.text = it.avg_rating?.toFloat()?.toString() ?: 0.0.toString() // avg  rating
                tvLabTest.text = it.lab_test_count // it.booking_count?.toString() ?: "0"   // lab test count
                tvAddress.text = it.description // desc about

                setDocumentsNLForHospital(rvDocuments, it)
                setDepartments(it)

        }
    }
  }

    private fun setDepartments(it: ModelUserProfile.Result) {
        fragmentNursesProfileBinding?.run {
            if (it.department_list.isNullOrEmpty().not()) {
                rvDepartments.visibility = View.VISIBLE
                tvNoDepartFound.visibility = View.GONE
                rvDepartments.setHasFixedSize(false)

                val departData = ArrayList<String>()
               it.department_list?.forEach { departData.add(it?.title.orEmpty()) }

                val mAdp = AdapterHospitalDepartments(departData, requireActivity())
                rvDepartments.adapter = mAdp
            } else {
                rvDepartments.visibility = View.GONE
                tvNoDepartFound.visibility = View.VISIBLE
                tvNoDepartFound.text = getString(R.string.no_depart_found)
            }

        }
    }


    // Set up recycler view for service listing if available
    private fun setDocumentsNLForHospital(rvDocuments: RecyclerView, it: ModelUserProfile.Result) {
        fragmentNursesProfileBinding?.run {
            if (it.moh_lic_image.isNullOrBlank().not()) {

                rvDocuments.visibility = View.VISIBLE
                tvDocumentFound.visibility = View.GONE
                rvDocuments.setHasFixedSize(false)

                val qualiData = ArrayList<com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem>()

                if(it.moh_lic_image.isNullOrBlank().not()){
                    qualiData.add(
                        com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem(
                        "", it.hosp_moh_lic_no, "", it.moh_lic_image))
                }

                if(it.hosp_reg_image.isNullOrBlank().not()){
                    qualiData.add(
                        com.rootscare.data.model.response.doctor
                        .profileresponse.QualificationDataItem("", it.hosp_reg_no, "", it.hosp_reg_image))
                }

                val contactListAdapter = AdapterDoctorImportantDocumentrecyclerview(qualiData, requireActivity())
                rvDocuments.adapter = contactListAdapter
                contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
                    override fun onItemClick(id: Int) {
                        val imageUrl =  BaseMediaUrls.USERIMAGE.url + contactListAdapter.qualificationDataList?.get(id)?.qualificationCertificate
                        requireActivity() viewFileEnlarge imageUrl
                    }
                }
            } else {
                rvDocuments.visibility = View.GONE
                tvDocumentFound.visibility = View.VISIBLE
                tvDocumentFound.text =getString(R.string.no_important_doc_found)
            }

        }

    }

    // Set up recycler view for service listing if available
    private fun setDocumentsNL(rvDocuments: RecyclerView, it: ModelUserProfile.Result) {
        fragmentNursesProfileBinding?.run {
            if (null != it.qualification_data && it.qualification_data.isNotEmpty()) {
                rvDocuments.visibility = View.VISIBLE
                tvDocumentFound.visibility = View.GONE
                rvDocuments.setHasFixedSize(false)

                val qualiData = ArrayList<com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem>()

                if(it.id_image.isNullOrBlank().not()){
                    qualiData.add(com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem("", it.id_number, "", it.id_image))
                }

                if(it.scfhs_image.isNullOrBlank().not()){
                    qualiData.add(
                        com.rootscare.data.model.response.doctor
                        .profileresponse.QualificationDataItem("", it.scfhs_number, "", it.scfhs_image))
                }
               it.qualification_data.forEach {
                    qualiData.add(com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem("", it?.qualification, "", it?.qualification_certificate))
                }

                val contactListAdapter = AdapterDoctorImportantDocumentrecyclerview(qualiData, requireActivity())
                rvDocuments.adapter = contactListAdapter
                contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
                    override fun onItemClick(id: Int) {
                        val imageUrl =  BaseMediaUrls.USERIMAGE.url + contactListAdapter.qualificationDataList?.get(id)?.qualificationCertificate
                        requireActivity() viewFileEnlarge imageUrl
                    }
                }
            } else {
                rvDocuments.visibility = View.GONE
                tvDocumentFound.visibility = View.VISIBLE
                tvDocumentFound.text =getString(R.string.no_important_doc_found)
            }

        }

    }


}