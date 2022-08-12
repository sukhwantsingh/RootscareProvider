package com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.myfilepickesexample.FileUtil
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewEditProfileForProvidersBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileNavigator
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileViewModel
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterQualificationMore
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterSelectedDepartments
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelQualificationMore
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.supportmore.models.ModelIssueTypes
import com.rootscare.serviceprovider.utilitycommon.*
import com.rootscare.utils.ManagePermissions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelHospDeparts
import android.app.Dialog
import android.view.ViewGroup
import android.view.Window
import android.widget.Button

import android.widget.NumberPicker
import com.rootscare.model.ModelServiceFor
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.OnDepartListingCallback
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile.Companion.IS_PROFILE_UPDATE_


class FragmentNursesEditProfile : BaseFragment<LayoutNewEditProfileForProvidersBinding, FragmentNursesProfileViewModel>(),
    FragmentNursesProfileNavigator {

    private val PICKFILE_RESULT_CODE = 4
    private var selectedGender = ""

    private var monthOfDob: String = ""
    private var dayOfDob: String = ""

    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var imageFile: File? = null
    private lateinit var adapQualiMore: AdapterQualificationMore


    private var binding: LayoutNewEditProfileForProvidersBinding? = null
    private var mViewModel: FragmentNursesProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.layout_new_edit_profile_for_providers
    override val viewModel: FragmentNursesProfileViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(
                FragmentNursesProfileViewModel::class.java
            )
            return mViewModel as FragmentNursesProfileViewModel
        }

    private var hashListDepart = HashMap<String, String>()

    companion object {
        var IS_PROFILE_UPDATE_ = false

        fun newInstance(): FragmentNursesEditProfile {
            val args = Bundle()
            val fragment = FragmentNursesEditProfile()
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var noAttchQualiTv: TextView
    private val mAdapSelectedDeparts: AdapterSelectedDepartments by lazy { AdapterSelectedDepartments() }
    private val workFromList : ArrayList<String?> by lazy { ArrayList() }
    private val workFromListMap : HashMap<String?, String?> by lazy { HashMap() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.navigator = this
        adapQualiMore = AdapterQualificationMore(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        managePermissions = ManagePermissions(
            this.requireActivity(),
            listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), PermissionsRequestCode
        )

        initViews()
        callingAPi()

    }

    private fun callingAPi() {
        fetchProfileData()
       // fetchServiceForApi()
    }

    private fun fetchServiceForApi() {
        if (isNetworkConnected) {
            mViewModel?.apiServiceFor()
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    private fun initViews() {
        binding?.run {
            rvMoreQuali.adapter = adapQualiMore
            adapQualiMore.callback = object : OnItemClickWithReportIdListener {
                override fun uploadCerti(dispTv: TextView) {
                    noAttchQualiTv = dispTv

                    if (checkAndRequestPermissionsTest()) {
                        uplaodCertificate(PICKFILE_RESULT_CODE)
                    }
                }
            }

            rvDeparts.setHasFixedSize(false)
            rvDeparts.adapter = mAdapSelectedDeparts
            mAdapSelectedDeparts.mCallback = object :OnDepartListingCallback{
                override fun onDelDepart() {
                  toggleDepartmentList()
                }
            }

            imgProfile.setOnClickListener {
            //    openCamera()
                 uploadForType = 3
                if (checkAndRequestPermissionsTest()) {
                  //  pickupImage(PICKFILE_RESULT_CODE)
               captureImage()
                }


            }
            profileImageCamera.setOnClickListener { imgProfile.performClick() }
            txtRegDob.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(requireActivity(), { _, yr, monthOfYear, dayOfMonth ->

                    monthOfDob = if ((monthOfYear + 1) < 10) { "0" + (monthOfYear + 1)} else { (monthOfYear + 1).toString() }
                    dayOfDob = if (dayOfMonth < 10) { "0$dayOfMonth" } else { dayOfMonth.toString() }
                    txtRegDob.setText("$yr-$monthOfDob-$dayOfDob")
                }, year, month, day)

                var yearForReopen: Int? = null
                var monthForReopen: Int? = null
                var dayForReopen: Int? = null
                if (!TextUtils.isDigitsOnly(txtRegDob.text.toString().trim())) {
                    val strings = txtRegDob.text.toString().split("-")
                    yearForReopen = strings[0].toInt()
                    monthForReopen = strings[1].toInt() - 1
                    dayForReopen = strings[2].toInt()
                }
                if (yearForReopen != null && monthForReopen != null && dayForReopen != null) {
                    dpd.updateDate(yearForReopen, monthForReopen, dayForReopen)
                }

                val date = Date()
                date.year = Date().year - 5
//                dpd.datePicker.maxDate = date.time // for 5 years
//                dpd.datePicker.maxDate = date.time
                dpd.show()
                dpd.datePicker.maxDate = System.currentTimeMillis()

            }

            btnSubmit.setOnClickListener { submitDetailsForEditProfile() }
// 2nd phase  tvhAddMoreQuali.setOnClickListener { addMoreQualif() }
            edtEstablish.setOnClickListener { showNumberDialog() }

            edtSpeciality.setOnClickListener {
                CommonDialog.showDialogForDropDownList(requireActivity(), getString(com.rootscare.serviceprovider.R.string.speciality), listSpeciality, object :
                    DropDownDialogCallBack {
                    override fun onConfirm(text: String) {
                        edtSpeciality.setText(text)
                    }
                })
            }
            tvAddMorddeDepartment.setOnClickListener {
                val fnDpt = ArrayList<String?>()
                fnDpt.addAll(listDepartments)
                mAdapSelectedDeparts.updatedArrayList?.forEach { ldp ->   fnDpt.remove(ldp?.title.orEmpty()) }

            if(fnDpt.isEmpty()) return@setOnClickListener
              CommonDialog.showDialogForDropDownList(requireActivity(), getString(com.rootscare.serviceprovider.R.string.departments), fnDpt, object :   DropDownDialogCallBack {
                    override fun onConfirm(text: String) {
                        val isPresent = mAdapSelectedDeparts.updatedArrayList?.find { it?.title.equals(text) }
                        if(null == isPresent) {
                        mAdapSelectedDeparts.loadDataIntoList(ModelHospDeparts.Result(hashListDepart.get(text), text))
                        startSmoothScroll(0, binding?.rvDeparts)
                        }
                        toggleDepartmentList()
                    }
                })
            }

//            tvWorkFrom.setOnClickListener {
//                CommonDialog.showDialogForDropDownList(requireActivity(), getString(R.string.select),
//                    workFromList, object : DropDownDialogCallBack {
//                        override fun onConfirm(text: String) {
//                            binding?.run {
//                                if(tvWorkFrom.text.toString().equals(text,true).not()){
//                                    tvWorkFrom.text = text
//
//                                    val mCode = workFromListMap[text].orEmpty()
//                                        edtCc.setText(mCode)
//                                }
//                            }
//                        }
//                })
//            }

            radioYesOrNo.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == com.rootscare.serviceprovider.R.id.radio_btn_reg_male) {
                    selectedGender = GenderType.MALE.get()
                }
                if (checkedId == com.rootscare.serviceprovider.R.id.radio_btn_reg_female) {
                    selectedGender = GenderType.FEMALE.get()
                }
            }
            tvhIdnUpload.setOnClickListener {
                uploadForType = 0
                noAttchQualiTv = tvIdnAttachment
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
            tvhDocUploadCertificate.setOnClickListener {
                uploadForType = 1
                noAttchQualiTv = tvDocAttachementCertificate
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
            tvhSchfsRegUploadDoc.setOnClickListener {
                uploadForType = 2
                noAttchQualiTv = tvSchfsRegAttachementDoc
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
        }
    }

    private fun toggleDepartmentList(){
        binding?.rvDeparts?.visibility = if(mAdapSelectedDeparts.updatedArrayList.isNullOrEmpty()){
         View.GONE } else View.VISIBLE
    }
    private fun fetchProfileData() {
//      /*  if (isNetworkConnected) {
//            baseActivity?.showLoading()
//            val jsonObject = JsonObject().apply {
//                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
//                addProperty("id", mViewModel?.appSharedPref?.loginUserId)
//            }
//            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
//            mViewModel?.apiProfile(body)
//        } else {
//            showToast(getString(R.string.network_unavailable))
//        }*/
        bindNewLayout(mViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result)
    }

    override fun onSuccessUserProfile(response: ModelUserProfile?) {
        baseActivity?.hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            response?.result?.let {
                bindNewLayout(it)
            } ?: run { showToast(response?.message ?: "") }
        } else {
            showToast(response?.message ?: "")
        }
    }

    override fun onSuccessServiceFor(specialityList: ModelServiceFor?) {
        baseActivity?.hideLoading()
        if (specialityList?.code.equals(SUCCESS_CODE)) {
            CoroutineScope(Dispatchers.IO).launch {
                specialityList?.result?.let {
                    if (it.isNotEmpty()) {
                        workFromList.clear(); workFromListMap.clear()
                        it.forEach { lst ->
                            workFromList.add(lst?.name)
                            workFromListMap[lst?.name] = lst?.country_code
                        }
                    }
                }
            }
        } else {
            showToast(specialityList?.message ?: "")
        }
    }


    override fun onSuccessEditProfile(response: ModelUserProfile?) {
        baseActivity?.hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            IS_PROFILE_UPDATE_ = true

            CommonDialog.showDialogForSingleButton(
                requireActivity(), object : DialogClickCallback {
                    override fun onConfirm() { activity?.onBackPressed() } }, getString(com.rootscare.serviceprovider.R.string.profile), response?.message ?: "")
        } else {
            CommonDialog.showDialogForSingleButton(
                requireActivity(), object : DialogClickCallback { }, getString(com.rootscare.serviceprovider.R.string.profile), response?.message ?: "")
        }
    }

    private fun addMoreQualif() {
        try {
            binding?.run {
                adapQualiMore.updateData(
                    arrayListOf(
                        ModelQualificationMore("", getString(com.rootscare.serviceprovider.R.string.no_attachment), "")
                    )
                )
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private var currency = ""
    private fun bindNewLayout(response: ModelUserProfile.Result?) {
        binding?.run {
            response?.let {
                currency = it.currency_symbol.orEmpty().trim()
                when (mViewModel?.appSharedPref?.loginUserType) {
                    // important details
                    LoginTypes.NURSE.type, LoginTypes.DOCTOR.type,
                    LoginTypes.PHYSIOTHERAPY.type,
                    LoginTypes.HOSPITAL_DOCTOR.type -> {
                        fetchSpecialityApi()
                        tilSpeciality.visibility = View.VISIBLE
                        tilScfhs.visibility = View.VISIBLE
                        relScfhsDoc.visibility = View.VISIBLE
                        tilExp.visibility = View.VISIBLE

                        txtRegDob.setText(it.dob)
                        if (it.gender.isNullOrBlank().not()) {
                            selectedGender = if (it.gender?.trim().equals(GenderType.MALE.get(), ignoreCase = true)) {
                                radioYesOrNo.check(R.id.radio_btn_reg_male)
                                GenderType.MALE.get()
                            } else {
                                radioYesOrNo.check(R.id.radio_btn_reg_female)
                                GenderType.FEMALE.get()
                            }
                        }

                        edtSpeciality.setText(it.speciality ?: "")

                        edtDocIdentityNumber.setText(it.id_number ?: "")
                        tvIdnAttachment.text = it.id_image

                        edtDocHighestQualification.setText(it.qualification_data?.getOrNull(0)?.qualification ?: "")
                        tvDocAttachementCertificate.text = it.qualification_data?.getOrNull(0)?.qualification_certificate ?: ""

                        edtDocScfhs.setText(it.scfhs_number)
                        tvSchfsRegAttachementDoc.text = it.scfhs_image
                        edtDocYearsExperience.setText(if (it.experience.isNullOrBlank().not()) "${it.experience}" else "0")

                        setQualifications(it.qualification_data)

                    }
                    LoginTypes.CAREGIVER.type, LoginTypes.BABYSITTER.type -> {
                        tilExp.visibility = View.VISIBLE

                        txtRegDob.setText(it.dob)
                        if (it.gender.isNullOrBlank().not()) {
                            selectedGender = if (it.gender?.trim().equals(GenderType.MALE.get(), ignoreCase = true)) {
                                radioYesOrNo.check(R.id.radio_btn_reg_male)
                                GenderType.MALE.get()
                            } else {
                                radioYesOrNo.check(R.id.radio_btn_reg_female)
                                GenderType.FEMALE.get()
                            }
                        }

                        edtDocIdentityNumber.setText(it.id_number ?: "")
                        tvIdnAttachment.text = it.id_image

                        edtDocHighestQualification.setText(it.qualification_data?.getOrNull(0)?.qualification ?: "")
                        tvDocAttachementCertificate.text = it.qualification_data?.getOrNull(0)?.qualification_certificate ?: ""
                        edtDocYearsExperience.setText(if (it.experience.isNullOrBlank().not()) "${it.experience}" else "0")

                        setQualifications(it.qualification_data)
                    }
                    LoginTypes.HOSPITAL.type -> {
                        fetchDepartmentsApi()
                        relDepartment.visibility = View.VISIBLE
                        tilEstablish.visibility = View.VISIBLE
                        tilAddr.visibility = View.VISIBLE

                        llGender.visibility = View.GONE
                        tilDob.visibility = View.GONE
                        tilExp.visibility = View.GONE

                     // 2nd phase tvhAddMoreQuali.visibility = View.GONE
                        tilScfhs.visibility = View.GONE
                        relScfhsDoc.visibility = View.GONE

                        tilFirstname.hint = getString(R.string.hospital_name)

                        tilIdnNumber.hint = getString(R.string.moh_licence_number)
                        edtDocIdentityNumber.setText(it.hosp_moh_lic_no)  // licence number

                        tvIdnAttachment.text = it.moh_lic_image

                        tilHql.hint = getString(R.string.registration_number)
                        edtDocHighestQualification.setText(it.hosp_reg_no) // reg number

                        tvhDocUploadCertificate.text = getString(R.string.upload_document)
                        tvDocAttachementCertificate.text = it.hosp_reg_image

                        setDepartments(it)
                    }
                    LoginTypes.LAB.type -> {
                        tilEstablish.visibility = View.VISIBLE
                        tilAddr.visibility = View.VISIBLE

                        llGender.visibility = View.GONE
                        tilDob.visibility = View.GONE
                        tilExp.visibility = View.GONE

                     // 2nd phase tvhAddMoreQuali.visibility = View.GONE
                        tilScfhs.visibility = View.GONE
                        relScfhsDoc.visibility = View.GONE

                        tilFirstname.hint = getString(R.string.lab_name)

                        tilIdnNumber.hint = getString(R.string.moh_licence_number)
                        edtDocIdentityNumber.setText(it.hosp_moh_lic_no)  // licence number

                        tvIdnAttachment.text = it.moh_lic_image

                        tilHql.hint = getString(R.string.registration_number)
                        edtDocHighestQualification.setText(it.hosp_reg_no) // reg number

                        tvhDocUploadCertificate.text = getString(R.string.upload_document)
                        tvDocAttachementCertificate.text = it.hosp_reg_image
                    }
                    else -> Unit
                }

                cnsHspdoc.visibility = if(it.hospital_id.isNullOrBlank()){ View.GONE } else { tvhAcname1.text = it.hospital_name; View.VISIBLE }


                if (it.image.isNullOrBlank().not()) imgProfile.setCircularRemoteImage(it.image)

                tvWorkFrom.text = it.work_area?.trim().orEmpty()
                edtCc.setText(it.country_code.orEmpty())

                tvUsername.text = (it.first_name + " " + it.last_name.orEmpty()).trim()
                tvhEmailId.text = it.email
             // tvhAcname.text = it.user_type?.replaceFirstChar { ut -> if (ut.isLowerCase()) ut.titlecase(Locale.getDefault()) else ut.toString() }
                tvhAcname.text = it.display_user_type
                edtRegFirstname.setText((it.first_name + " " + it.last_name.orEmpty()).trim())
                edtRegEmailaddress.setText(it.email)
                edtRegPhonenumber.setText(it.phone_number)
                edtEstablish.setText(it.experience)
                edtHospAddr.setText(it.address)

                edtAbout.setText(it.description)

                true
            }
        }
    }


    private fun setDepartments(it: ModelUserProfile.Result?) {
        binding?.run {
           if (it?.department_list.isNullOrEmpty().not()) {
               rvDeparts.visibility = View.VISIBLE
               val departData = ArrayList<ModelHospDeparts.Result?>()
               it?.department_list?.forEach {
                   departData.add(0,ModelHospDeparts.Result(it?.department_id.orEmpty(), it?.title.orEmpty()))
               }
               mAdapSelectedDeparts.loadAllDataIntoList(departData)
             } else {
                 rvDeparts.visibility = View.GONE
             }

        }
    }



    private fun setQualifications(qualiData: ArrayList<ModelUserProfile.Result.QualificationData?>?) {
        try {
            val dList = ArrayList<ModelQualificationMore>()
            qualiData?.forEachIndexed { index, qData ->
                if (index != 0) {
                    dList.add(ModelQualificationMore(qData?.qualification, qData?.qualification_certificate, ""))
                }
            }
            adapQualiMore.updateData(dList)
        } catch (e: Exception) {
            println(e.message)
        }

    }

    private fun fetchSpecialityApi() {
        if (isNetworkConnected) {
            val jsonObject = JsonObject().apply { addProperty("service_type", mViewModel?.appSharedPref?.loginUserType) }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.apiSpecialityList(body)
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    private fun fetchDepartmentsApi() {
        if (isNetworkConnected) {
          //  val jsonObject = JsonObject().apply { addProperty("service_type", mViewModel?.appSharedPref?.loginUserType) }
          //  val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.apiDepartmentListing()
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
           showToast(getString(R.string.something_went_wrong))
        }
    }

    var listSpeciality = ArrayList<String?>()
    override fun onSuccessSpeciality(specialityList: ModelIssueTypes?) {
        baseActivity?.hideLoading()
        if (specialityList?.code.equals(SUCCESS_CODE)) {
            CoroutineScope(Dispatchers.IO).launch {
                specialityList?.result?.let {
                    if (it.isNotEmpty()) {
                        listSpeciality.clear()
                        it.forEach { lst -> listSpeciality.add(lst?.name) }
                    }
                }
            }
        } else {
            showToast(specialityList?.message ?: "")
        }
    }

    var listDepartments = ArrayList<String?>()
    override fun successDepartmentListResponse(response: ModelHospDeparts?) {
        baseActivity?.hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            CoroutineScope(Dispatchers.IO).launch {
                response?.result?.let {
                    if (it.isNotEmpty()) {
                        listDepartments.clear(); hashListDepart.clear()
                        it.forEach { lst ->
                            listDepartments.add(lst?.title)
                            hashListDepart[lst?.title.orEmpty()] = lst?.id.orEmpty()
                        }
                    }
                }
            }
        } else { showToast(response?.message ?: "") }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == -1) {
                    fileUri = data?.data
                    filePath = fileUri?.path
                    try {
                        val file = fileUri?.let { FileUtil.from(this.requireActivity(), it) }
                        Log.wtf("file", "File...: - " + file?.path + " file -" + file + " : " + file?.exists())
                       file?.let { displayCertificateName(file) }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
             else if (requestCode == REQUEST_CAMERA) {
                try {
                    onCaptureImageResult(data!!)
                } catch (e: Exception) {
                    println("Exception===>$e")
                }

            } else if (requestCode == SELECT_FILE) {
                if (data != null) {
                    onSelectFromGalleryResult(data)
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (data != null) {
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    imageFile = File(result.uri.path)
                    binding?.imgProfile?.setImageURI(resultUri)

                }
                hideKeyboard()
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val result = CropImage.getActivityResult(data)
                val error = result.error
               showToast( error.message ?: getString(R.string.something_went_wrong))
            }
        }
    }

    private val qualiDocsList = ArrayList<File>()
    private var imgIdentity: File? = null
    private var imgQualification: File? = null
    private var imgScfhs: File? = null
    private var uploadForType = -1 // ie. are 0-id_num,1-qualification,2-SCFHS Registration ID , 3- image

    private fun displayCertificateName(fFile: File) {
        when (uploadForType) {
            0 -> {
                imgIdentity = fFile
                noAttchQualiTv.text = fFile.name
            }
            1 -> {
                imgQualification = fFile
                noAttchQualiTv.text = fFile.name
            }
            2 -> {
                imgScfhs = fFile
                noAttchQualiTv.text = fFile.name
            }
            3 -> {
                imageFile = fFile
                binding?.imgProfile?.setCircularLocalImage(fFile.absolutePath ?: "")
            }
            else -> {
                if (qualiDocsList.contains(fFile).not()) {
                    qualiDocsList.add(fFile)
                }
            }
        }

    }


    private fun submitDetailsForEditProfile() {
        binding?.run {
            if (null != mViewModel?.appSharedPref?.loginUserId && checkValidationForRegStepOne()) {

                val uId = mViewModel?.appSharedPref?.loginUserId?.asReqBody()
                val uType = mViewModel?.appSharedPref?.loginUserType?.asReqBody()
                val flName = edtRegFirstname.text?.trim().toString().asReqBody()
                var mobNum = edtRegPhonenumber.text?.trim().toString().trim()
                val cc_ = edtCc.text?.trim().toString().trim()
                   mobNum = cc_ + mobNum

                val email = edtRegEmailaddress.text?.trim().toString().asReqBody()
                val dob = txtRegDob.text?.trim().toString().asReqBody()
                val gen = selectedGender.asReqBody()
                val about = edtAbout.text?.trim().toString().asReqBody()
                val workArea = binding?.tvWorkFrom?.text?.toString() ?: ""

                var idNum = ""
                var spec = ""
                var hQualification = ""
                var expYears = ""
                var scfhsRegistrationId = ""
                var hspMohLicNum = ""
                var hospRegId = ""
                var departmentsSelected = ""
                var address_ = ""

                when (mViewModel?.appSharedPref?.loginUserType) {
                    LoginTypes.NURSE.type, LoginTypes.DOCTOR.type, LoginTypes.PHYSIOTHERAPY.type -> {
                        binding?.run {
                            idNum = edtDocIdentityNumber.text.toString()
                            spec = edtSpeciality.text.toString()
                            hQualification = edtDocHighestQualification.text.toString()
                            expYears = edtDocYearsExperience.text.toString()
                            scfhsRegistrationId = edtDocScfhs.text.toString()
                        }
                    }
                    LoginTypes.BABYSITTER.type, LoginTypes.CAREGIVER.type -> {
                        binding?.run {
                            idNum = edtDocIdentityNumber.text.toString()
                            hQualification = edtDocHighestQualification.text.toString()
                            expYears = edtDocYearsExperience.text.toString()
                        }

                    }
                    LoginTypes.HOSPITAL.type  -> {
                        binding?.run {
                            hspMohLicNum = edtDocIdentityNumber.text.toString()
                            hospRegId = edtDocHighestQualification.text.toString()
                            expYears = edtEstablish.text.toString()
                            address_ = edtHospAddr.text.toString()
                        }
                        departmentsSelected =  mAdapSelectedDeparts.updatedArrayList?.joinToString { it?.id.toString() } ?: ""
                    }
                    LoginTypes.LAB.type -> {
                        binding?.run {
                            hspMohLicNum = edtDocIdentityNumber.text.toString()
                            hospRegId = edtDocHighestQualification.text.toString()
                            expYears = edtEstablish.text.toString()
                            address_ = edtHospAddr.text.toString()
                        }
                    }

                    else -> Unit
                }

                var uImage: MultipartBody.Part? = null
                var idImg: MultipartBody.Part? = null
                var qlImg: MultipartBody.Part? = null
                var scfhsImg: MultipartBody.Part? = null

                imageFile?.let {
                    val nmg = imageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    uImage = MultipartBody.Part.createFormData("image", imageFile?.name, nmg)
                }
                imgIdentity?.let {
                    val idnmg = imgIdentity!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    idImg = MultipartBody.Part.createFormData("id_image", imgIdentity?.name, idnmg)
                }
                imgQualification?.let {
                    val qmg = imgQualification!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    qlImg = MultipartBody.Part.createFormData("certificate", imgQualification?.name, qmg)
                }
                imgScfhs?.let {
                    val mscfhs = imgScfhs!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    scfhsImg = MultipartBody.Part.createFormData("scfhs_image", imgScfhs?.name, mscfhs)
                }

/*
// 2nd phase
                var certificateMultipartBody: ArrayList<MultipartBody.Part>? = null
                if ((certificateListAdapter?.qualificationDataList != null && certificateListAdapter?.qualificationDataList?.size!! > 0)) {
                    val tempList: ArrayList<QualificationDataItem> = ArrayList()
                    for (i in 0 until certificateListAdapter?.qualificationDataList?.size!!) {
                        if (!certificateListAdapter?.qualificationDataList!![i].isOldData) {
                            tempList.add(certificateListAdapter?.qualificationDataList!![i])
                        }
                    }
                    if (tempList.size > 0) {
                        certificateMultipartBody = ArrayList()
                        for (item in tempList) {
                            if (item.certificateFileTemporay != null && !item.isOldData) {
                                val certificate = item.certificateFileTemporay!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                certificateMultipartBody.add(
                                    MultipartBody.Part.createFormData(
                                        "certificate[]",
                                        item.certificateFileTemporay?.name,
                                        certificate
                                    )
                                )
                            }
                        }
                    }
                }
*/

                baseActivity?.showLoading()
                baseActivity?.hideKeyboard()
                mViewModel?.apiSubmitEditProfile(
                    uId, uType, flName, email, mobNum.asReqBody(), dob, gen,
                    hQualification.asReqBody(), idNum.asReqBody(),
                    spec.asReqBody(), expYears.asReqBody(),
                    scfhsRegistrationId.asReqBody(), about, uImage, idImg, qlImg, scfhsImg,
                    hspMohLicNum.asReqBody(), hospRegId.asReqBody(), address_.asReqBody(),
                    departmentsSelected.asReqBody() , workArea.asReqBody()
                )

            }

        }
    }

    private fun checkValidationForRegStepOne(): Boolean {
        return when {
            binding?.edtRegFirstname?.text.isNullOrBlank() -> {
                binding?.edtRegFirstname?.error =
                    if (mViewModel?.appSharedPref?.loginUserType != LoginTypes.HOSPITAL.type) getString(R.string.enter_your_name) else getString(R.string.enter_hospital_name)
                binding?.edtRegFirstname?.requestFocus()
                false
            }
            binding?.tvWorkFrom?.text.isNullOrBlank() -> {
                showToast(getString(R.string.provide_service_area))
                false
            }
            !isValidPassword(binding?.edtRegPhonenumber?.text?.toString()!!) -> {
                binding?.edtRegPhonenumber?.error =  getString(R.string.enter_mobile_number)
                binding?.edtRegPhonenumber?.requestFocus()
                false
            }

            (binding?.edtRegPhonenumber?.toString()?.length ?: 0) < 9 -> {
                binding?.edtRegPhonenumber?.error = getString(R.string.use_mobile_number_note)
                binding?.edtRegPhonenumber?.requestFocus()
                false
            }

            binding?.edtRegEmailaddress?.text.isNullOrBlank() -> {
                binding?.edtRegEmailaddress?.error =  getString(R.string.provide_email)
                binding?.edtRegEmailaddress?.requestFocus()
                false
            }
            !binding?.edtRegEmailaddress?.text?.toString()!!.matches(emailPattern.toRegex()) -> {
                binding?.edtRegEmailaddress?.error = getString(R.string.enter_valid_email)
                binding?.edtRegEmailaddress?.requestFocus()
                false
            }

            mViewModel?.appSharedPref?.loginUserType?.equals(LoginTypes.NURSE.type) == true ||
            mViewModel?.appSharedPref?.loginUserType?.equals(LoginTypes.DOCTOR.type) == true ||
            mViewModel?.appSharedPref?.loginUserType?.equals(LoginTypes.PHYSIOTHERAPY.type) == true -> {
                val mLt = binding
                when {
                    mViewModel?.appSharedPref?.loginUserType != LoginTypes.HOSPITAL.type && binding?.txtRegDob?.text.isNullOrBlank() -> {
                        showToast("Please select your dob!")
                        false
                    }
                    mViewModel?.appSharedPref?.loginUserType != LoginTypes.HOSPITAL.type && selectedGender.isBlank() -> {
                        showToast("Please select gender!")
                        false
                    }

                    mLt?.edtDocIdentityNumber?.text.isNullOrBlank() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.provide_id_number)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }

                    currency.equals(CurrencyTypes.SAR.get(), ignoreCase = true) && isStartWithIdExact().not() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.id_must_start_from_1_or_2)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }
                    currency.equals(CurrencyTypes.AED.get(), ignoreCase = true) && isStartWithIdExact7().not() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.id_must_start_from_7)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }

                    ((mLt?.edtDocIdentityNumber?.text?.toString()?.length ?: 0) < 10) || ((mLt?.edtDocIdentityNumber?.text?.toString()?.length ?: 0) > 15) -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.id_number_must_be_10)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }


                    // check for upload certificate
//                    null == imgIdentity-> {
//                        showToast("Please provide ID Proof!")
//                        false
//                    }
                    mLt?.edtSpeciality?.text.isNullOrBlank() -> {
                        showToast("Please provide Speciality!")
                        false
                    }
                    mLt?.edtDocHighestQualification?.text.isNullOrBlank() -> {
                        mLt?.edtDocHighestQualification?.error = "Please provide Qualification!"
                        mLt?.edtDocHighestQualification?.requestFocus()
                        false
                    }
                    // check for upload certificate
//                    null == imgQualification -> {
//                        showToast("Please provide Qualification Proof!")
//                        false
//                    }
                    mLt?.edtDocYearsExperience?.text.isNullOrBlank() -> {
                        mLt?.edtDocYearsExperience?.error = "Please provide Experience!"
                        mLt?.edtDocYearsExperience?.requestFocus()
                        false
                    }
                    mLt?.edtDocScfhs?.text.isNullOrBlank() -> {
                        mLt?.edtDocScfhs?.error = "Please provide SCFHS Registration ID!"
                        mLt?.edtDocScfhs?.requestFocus()
                        false
                    }
                    ((mLt?.edtDocScfhs?.text?.toString()?.length ?: 0) < 8) ||
                    ((mLt?.edtDocScfhs?.text?.toString()?.length ?: 0) > 11) -> {
                     mLt?.edtDocScfhs?.error = getString(R.string.not_more_than_11_characters)
                     mLt?.edtDocScfhs?.requestFocus()
                     false
                    }

                    // check for upload certificate
//                    null == imgScfhs -> {
//                        showToast("Please provide SCFHS Registration Proof!")
//                        false
//                    }
                    else -> true
                }
            }

            mViewModel?.appSharedPref?.loginUserType?.equals(LoginTypes.BABYSITTER.type) == true ||
            mViewModel?.appSharedPref?.loginUserType?.equals(LoginTypes.CAREGIVER.type) == true -> {
                val mLt = binding
                when {
                    mViewModel?.appSharedPref?.loginUserType != LoginTypes.HOSPITAL.type && binding?.txtRegDob?.text.isNullOrBlank() -> {
                        showToast("Please select your dob!")
                        false
                    }
                    mViewModel?.appSharedPref?.loginUserType != LoginTypes.HOSPITAL.type && selectedGender.isBlank() -> {
                        showToast("Please select gender!")
                        false
                    }

                    mLt?.edtDocIdentityNumber?.text.isNullOrBlank() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.provide_id_number)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }

                    currency.equals(CurrencyTypes.SAR.get(), ignoreCase = true) && isStartWithIdExact().not() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.id_must_start_from_1_or_2)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }
                    currency.equals(CurrencyTypes.AED.get(), ignoreCase = true) && isStartWithIdExact7().not() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.id_must_start_from_7)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }

                   ((mLt?.edtDocIdentityNumber?.text?.toString()?.length ?: 0) < 10) || ((mLt?.edtDocIdentityNumber?.text?.toString()?.length ?: 0) > 15) -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.id_number_must_be_10)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }


                    // check for upload certificate
//                    null == imgIdentity -> {
//                        showToast("Please provide ID Proof!")
//                        false
//                    }
                    mLt?.edtDocHighestQualification?.text.isNullOrBlank() -> {
                        mLt?.edtDocHighestQualification?.error = "Please provide Qualification!"
                        mLt?.edtDocHighestQualification?.requestFocus()
                        false
                    }
                    // check for upload certificate
//                    null == imgQualification -> {
//                        showToast("Please provide Qualification Proof!")
//                        false
//                    }
                    mLt?.edtDocYearsExperience?.text.isNullOrBlank() -> {
                        mLt?.edtDocYearsExperience?.error = "Please provide Experience!"
                        mLt?.edtDocYearsExperience?.requestFocus()
                        false
                    }
                    else -> true
                }
            }

            mViewModel?.appSharedPref?.loginUserType?.equals(LoginTypes.HOSPITAL.type) == true -> {
                val mLt = binding
                when {
                    binding?.edtEstablish?.text.isNullOrBlank() -> {
                        showToast(getString(R.string.provide_year_of_establishment))
                        false
                    }
                    binding?.edtHospAddr?.text.isNullOrBlank() -> {
                        binding?.edtHospAddr?.error = getString(R.string.provide_address)
                        binding?.edtHospAddr?.requestFocus()
                        false
                    }

                    mLt?.edtDocIdentityNumber?.text.isNullOrBlank() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.provide_moh_licence_no)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }
                    // check for upload certificate
//                    null == imgIdentity -> {
//                        showToast("Please provide MOH Licence Proof!")
//                        false
//                    }
                    mLt?.edtDocHighestQualification?.text.isNullOrBlank() -> {
                        mLt?.edtDocHighestQualification?.error = getString(R.string.provide_registration_number)
                        mLt?.edtDocHighestQualification?.requestFocus()
                        false
                    }

                    // Check for upload certificate
//                    null == imgQualification -> {
//                        showToast("Please provide Registration Proof!")
//                        false
//                    }
                    else -> true
                }
            }
            mViewModel?.appSharedPref?.loginUserType?.equals(LoginTypes.LAB.type) == true -> {
                val mLt = binding
                when {
                    binding?.edtEstablish?.text.isNullOrBlank() -> {
                        showToast(getString(R.string.provide_year_of_establishment))
                        false
                    }
                    binding?.edtHospAddr?.text.isNullOrBlank() -> {
                        binding?.edtHospAddr?.error = getString(R.string.provide_address)
                        binding?.edtHospAddr?.requestFocus()
                        false
                    }

                    mLt?.edtDocIdentityNumber?.text.isNullOrBlank() -> {
                        mLt?.edtDocIdentityNumber?.error =  getString(R.string.provide_moh_licence_no)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }
                    // check for upload certificate
//                    null == imgIdentity -> {
//                        showToast("Please provide MOH Licence Proof!")
//                        false
//                    }
                    mLt?.edtDocHighestQualification?.text.isNullOrBlank() -> {
                        mLt?.edtDocHighestQualification?.error =  getString(R.string.provide_registration_number)
                        mLt?.edtDocHighestQualification?.requestFocus()
                        false
                    }

                    // Check for upload certificate
//                    null == imgQualification -> {
//                        showToast("Please provide Registration Proof!")
//                        false
//                    }
                    else -> true
                }
            }
            else -> true
        }
    }

    private fun isStartWithIdExact() =  binding?.edtDocIdentityNumber?.text.toString().startsWith("1") || binding?.edtDocIdentityNumber?.text.toString().startsWith("2")

    private fun isStartWithIdExact7() =  binding?.edtDocIdentityNumber?.text.toString().startsWith("7")

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions.processPermissionsResult(grantResults)
                if (isPermissionsGranted) {
                    if(uploadForType==3){
                        // Do the task now
                        goToImageIntent()
                    } else {
                        // Do the task now
                        uplaodCertificate(PICKFILE_RESULT_CODE)
                    }
                } else {
                    showToast("Permissions denied.")
                }
                return
            }
        }
    }

    var REQUEST_ID_MULTIPLE_PERMISSIONS = 123
    var requested = false
    private fun checkAndRequestPermissionsTest(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            val permissionCamera = ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.CAMERA)
            val permissionWriteExternalStorage =
                ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

            val listPermissionsNeeded: MutableList<String> = java.util.ArrayList()

            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }

            if (permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (listPermissionsNeeded.isNotEmpty()) {
                requested = true
                requestPermissions(listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
                false
            } else {
                true
            }
        } else {
            requested = false
            true
        }
    }

    private fun captureImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
                options[item] == "Choose from Gallery" -> {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT //
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }


    private fun openPictureEditActivity() {
        if (!TextUtils.isEmpty(imageFile?.path) && File(imageFile?.path).exists()) {
            CropImage.activity(Uri.fromFile(File(imageFile?.path)))
                .setCropShape(CropImageView.CropShape.OVAL)
                .setInitialCropWindowPaddingRatio(0F)
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this.requireActivity())
        }
    }

    private val PICK_IMAGE_REQUEST = 1
    var thumbnail: Bitmap? = null
    var bytes: ByteArrayOutputStream? = null
    private val REQUEST_CAMERA = 0
    private var SELECT_FILE: Int = 1

    private fun onCaptureImageResult(data: Intent) {
        thumbnail = data.extras!!["data"] as Bitmap?
        bytes = ByteArrayOutputStream()
        thumbnail?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        imageFile = File(activity?.externalCacheDir!!.absolutePath,System.currentTimeMillis().toString() + ".jpg"
        )
        val fo: FileOutputStream
        try {
            imageFile?.createNewFile()
            fo = FileOutputStream(imageFile)
            fo.write(bytes?.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        openPictureEditActivity()
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data.data)
                bytes = ByteArrayOutputStream()
                thumbnail?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        imageFile = File(activity?.externalCacheDir!!.absolutePath,System.currentTimeMillis().toString() + ".jpg")
        val fo: FileOutputStream
        try {
            imageFile?.createNewFile()
            fo = FileOutputStream(imageFile)
            fo.write(bytes?.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        openPictureEditActivity()
        /* im_upload.setImageBitmap(thumbnail);
        im_editbutton.setVisibility(View.GONE);
        im_holder.setVisibility(View.GONE);*/
    }
    private fun goToImageIntent() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

   private fun showNumberDialog() {
        val d = Dialog(requireActivity())
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.setCancelable(false)
        d.setContentView(R.layout.layout_number_picker)
        val b1: Button = d.findViewById(R.id.button1) as Button
        val b2: Button = d.findViewById(R.id.button2) as Button
        val np = d.findViewById(R.id.numberPicker1) as NumberPicker
        np.maxValue = 2022
        np.minValue = 1970
        np.wrapSelectorWheel = false
        np.setOnValueChangedListener { _, _, _ -> }
        b2.setOnClickListener {
            binding?.edtEstablish?.setText(np.value.toString())
            d.dismiss()
        }
        b1.setOnClickListener { d.dismiss() }
        d.show()
        val wind = d.window
        wind?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}