package com.rootscare.serviceprovider.ui.manageDocLab.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.rootscare.serviceprovider.databinding.LayoutDoctorUnderHospitalBinding
import com.rootscare.serviceprovider.ui.manageDocLab.ActivityCreateEditHospitalDocs
import com.rootscare.serviceprovider.ui.manageDocLab.ActivityCreateEditHospitalLab
import com.rootscare.serviceprovider.ui.manageDocLab.ManageDocLabNavigator
import com.rootscare.serviceprovider.ui.manageDocLab.ManageDocLabViewModel
import com.rootscare.serviceprovider.ui.manageDocLab.adapter.AdapterDocListing
import com.rootscare.serviceprovider.ui.manageDocLab.adapter.OnHospitalDocsCallback
import com.rootscare.serviceprovider.ui.manageDocLab.model.ModelHospitalDocs
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.pricelistss.ModelPriceListing
import com.rootscare.serviceprovider.utilitycommon.HospitalUnder
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.navigate

private const val ARG_TYPE = "ARG_TYPE"

class FragmentManageHospitalDocsLab : BaseFragment<LayoutDoctorUnderHospitalBinding, ManageDocLabViewModel>(), ManageDocLabNavigator {

    private var manageType: String? = null
    private var binding: LayoutDoctorUnderHospitalBinding? = null

    private var mViewModel: ManageDocLabViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.layout_doctor_under_hospital

    override val viewModel: ManageDocLabViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(ManageDocLabViewModel::class.java)
            return mViewModel as ManageDocLabViewModel
        }


    private var mDocListingAdapter: AdapterDocListing? = null

    companion object {
        @JvmStatic
        fun newInstance(priceType_: String = "") =
            FragmentManageHospitalDocsLab().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, priceType_)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDocListingAdapter = activity?.let { AdapterDocListing(it) }
        mViewModel?.navigator = this
        arguments?.let {
            manageType = it.getString(ARG_TYPE) ?: ""
        }
    }

    override fun onResume() {
        super.onResume()

        if(FragmentNursesEditProfile.IS_PROFILE_UPDATE_){
            FragmentNursesEditProfile.IS_PROFILE_UPDATE_ = false
            fetchDocsList()
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        initViews()
    }

    private fun initViews() {
        binding?.rvDocs?.adapter = mDocListingAdapter
        when {
            manageType.equals(HospitalUnder.DOCTOR.getType(), true) -> {
                    binding?.run {
                        tvUsername.text = HospitalUnder.DOCTOR.get()
                        tvDesc.text = getString(R.string.hosp_can_do_for_doc)

                        binding?.tvCreateNew?.setOnClickListener {
                            ActivityCreateEditHospitalDocs.docId = ""
                            ActivityCreateEditHospitalDocs.needToCreateDoc = true
                            navigate<ActivityCreateEditHospitalDocs>()
                        }
                        mDocListingAdapter?.mCallback = object :OnHospitalDocsCallback {
                            override fun onDocsLabEdit(docId: String?) {
                              ActivityCreateEditHospitalDocs.docId = docId.orEmpty()
                              ActivityCreateEditHospitalDocs.needToCreateDoc = false
                              navigate<ActivityCreateEditHospitalDocs>()                             
                            }
                        }
                    }
                
                   fetchDocsList()
                }
            manageType.equals(HospitalUnder.LAB.getType(), true) -> {
                    binding?.run {
                        tvUsername.text = HospitalUnder.LAB.get()
                        tvDesc.text = getString(R.string.hosp_can_do_for_lab)

                        binding?.tvCreateNew?.setOnClickListener {
                         ActivityCreateEditHospitalLab.needToCreateLab = true
                          navigate<ActivityCreateEditHospitalLab>()
                        }
                        mDocListingAdapter?.mCallback = object :OnHospitalDocsCallback{
                            override fun onDocsLabEdit(docId: String?) {
                                ActivityCreateEditHospitalLab.needToCreateLab = false
                               navigate<ActivityCreateEditHospitalLab>()
                            }
                        }
                    }
               //     fetchDocsList(PriceTypes.HOURLY_BASED.getMode())
                }
            }
    }

    private fun fetchLabDetail(taskType: String) {
        if (isNetworkConnected) {

            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("task_type", taskType)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
          //   baseActivity?.showLoading()
          //   mViewModel?.getTasksListForDoctor(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }

    }

    private fun fetchDocsList() {
        if (isNetworkConnected) {
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("hospital_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("doctor_name", "")
                addProperty("page_count", "1")
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
             baseActivity?.showLoading()
           mViewModel?.getHospitalDocList(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }

    }


    override fun onSuccessDocResponse(taskList: ModelHospitalDocs?) {
        try {
            baseActivity?.hideLoading()
            if (taskList?.code.equals(SUCCESS_CODE)) {
                bindViews(taskList)
            } else noData(taskList?.message)
        } catch (e: Exception) {
            println(e)
        } finally {
            baseActivity?.hideLoading()
        }

    }

    private fun bindViews(taskList: ModelHospitalDocs?) {
        taskList?.result?.let {
          if (it.isNullOrEmpty().not()) {
                binding?.run {
                    tvNoDate.visibility = View.GONE
                    rvDocs.visibility = View.VISIBLE
                }
                mDocListingAdapter?.loadDataIntoList(it)
            } else noData(taskList.message)
        } ?: run { noData(taskList?.message) }

    }


    private fun noData(message: String?) {
        binding?.run {
            tvNoDate.visibility = View.VISIBLE
            rvDocs.visibility = View.GONE
            tvNoDate.text = message ?: getString(R.string.something_went_wrong)
        }
    }


    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
        noData(throwable.message)
    }

}