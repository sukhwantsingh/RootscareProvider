package com.rootscare.serviceprovider.ui.manageDocLab

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
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.registrationresponse.RegistrationResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.model.ModelServiceFor
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewCreateEditDocUnderHospitalBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.manageDocLab.fragments.FragmentManageHospitalDocsLab
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileNavigator
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileViewModel
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterQualificationMore
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelQualificationMore
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile.Companion.IS_PROFILE_UPDATE_
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
class ActivityCreateEditHospitalDocs : BaseActivity<LayoutNewCreateEditDocUnderHospitalBinding, FragmentNursesProfileViewModel>(),
    FragmentNursesProfileNavigator {

    private val PICKFILE_RESULT_CODE = 4
    private var selectedGender = ""

    private var monthOfDob: String = ""
    private var dayOfDob: String = ""

    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var imageFile: File? = null
    private lateinit var adapQualiMore: AdapterQualificationMore


    private var binding: LayoutNewCreateEditDocUnderHospitalBinding? = null
    private var mViewModel: FragmentNursesProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.layout_new_create_edit_doc_under_hospital
    override val viewModel: FragmentNursesProfileViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(
                FragmentNursesProfileViewModel::class.java
            )
            return mViewModel as FragmentNursesProfileViewModel
        }

    companion object {
        var needToCreateDoc = true
        var  docId = ""
    }

    private val workFromList : ArrayList<String?> by lazy { ArrayList() }
    private val workFromListMap : HashMap<String?, String?> by lazy { HashMap() }
    private var currency = ""
    private var logModel : ModelUserProfile? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        mViewModel?.navigator = this
        binding?.topToolbar?.run {
            tvHeader.text = getString(R.string.manage_doctor_amp_lab)
            btnBack.setOnClickListener { finish() }
        }
        logModel = mViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref()
        currency = logModel?.result?.currency_symbol.orEmpty()

        adapQualiMore = AdapterQualificationMore(this)

        managePermissions = ManagePermissions(this,listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), PermissionsRequestCode)

        initViews()
        callingAPi()
    }


    private fun callingAPi() {
        if(needToCreateDoc) {
            binding?.tvhEd?.text = getString(R.string.create_doctor)
            binding?.rlAccountDown?.visibility =  View.GONE
            binding?.rlTopImgMail?.visibility =  View.GONE
            binding?.cnsCba?.visibility =  View.GONE
            binding?.edtAbout?.visibility =  View.GONE

            binding?.tilPwd?.visibility = View.VISIBLE
            binding?.tilCnfpwd?.visibility = View.VISIBLE
            binding?.tvnD1?.visibility = View.VISIBLE
            binding?.tvnD2?.visibility = View.VISIBLE

            fetchSpecialityApi()

        } else {

            binding?.tvhEd?.text = getString(R.string.edit_doctor)
            binding?.rlAccountDown?.visibility = View.VISIBLE
            binding?.rlTopImgMail?.visibility =  View.VISIBLE
            binding?.cnsCba?.visibility =  View.VISIBLE
            binding?.edtAbout?.visibility =  View.VISIBLE

            binding?.tilPwd?.visibility = View.GONE
            binding?.tilCnfpwd?.visibility = View.GONE
            binding?.tvnD1?.visibility = View.GONE
            binding?.tvnD2?.visibility = View.GONE

           fetchProfileData()
        }

        logModel?.let {
            binding?.run {
            tvWorkFrom.text = it.result?.work_area.orEmpty()
            edtCc.setText(it.result?.country_code.orEmpty())
        }
        }

     //   if(needToCreateDoc) fetchServiceForApi()

    }

    private lateinit var noAttchQualiTv: TextView

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

                val dpd = DatePickerDialog(this@ActivityCreateEditHospitalDocs, { _, yr, monthOfYear, dayOfMonth ->

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
//2nd phase  tvhAddMoreQuali.setOnClickListener { addMoreQualif() }
            edtSpeciality.setOnClickListener {
                CommonDialog.showDialogForDropDownList(this@ActivityCreateEditHospitalDocs, getString(R.string.speciality), listSpeciality, object :
                    DropDownDialogCallBack {
                    override fun onConfirm(text: String) {
                        edtSpeciality.setText(text)
                    }
                })
            }
            radioYesOrNo.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.radio_btn_reg_male) {
                    selectedGender = GenderType.MALE.get()
                }
                if (checkedId == R.id.radio_btn_reg_female) {
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
            tvhDisableDoc.setOnClickListener { disableDeleteDoc(UserDisableType.DISABLE.get(), getString(R.string.sure_to_disable_this_doc)) }
            tvhDeleteDoc.setOnClickListener { disableDeleteDoc(UserDisableType.DELETE.get(), getString(R.string.sure_to_delete_this_doc))   }

//            tvWorkFrom.setOnClickListener {
//                if(workFromList.isEmpty()) return@setOnClickListener
//             CommonDialog.showDialogForDropDownList(this@ActivityCreateEditHospitalDocs, getString(R.string.select),
//                    workFromList, object : DropDownDialogCallBack {
//                        override fun onConfirm(text: String) {
//                           if(needToCreateDoc) {
//                                binding?.run {
//                                    if(tvWorkFrom.text.toString().equals(text,true).not()){
//                                        tvWorkFrom.text = text
//
//                                        val mCode = workFromListMap[text].orEmpty()
//                                        edtCc.setText(mCode)
//                                    }
//                                }
//                            }
//
//                        }
//                    })
//            }

        }
    }

    private fun disableDeleteDoc(typeToHit: String,desc:String) {
       CommonDialog.showDialog(this@ActivityCreateEditHospitalDocs, object :
            DialogClickCallback {
            override fun onConfirm() {
                if (isNetworkConnected) {
                    showLoading()
                    val jsonObject = JsonObject().apply {
                        addProperty("user_id", docId)
                    }
                    val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                    when {
                        typeToHit.equals(UserDisableType.DISABLE.get(),true) -> mViewModel?.disableDoc(body)
                        typeToHit.equals(UserDisableType.DELETE.get(),true) -> mViewModel?.deleteDoc(body)
                        else -> hideLoading()
                    }
                } else {
                    showToast(getString(R.string.network_unavailable))
                }

            }
        }, typeToHit, desc)

    }

    private fun fetchProfileData() {
        if (isNetworkConnected) {
            showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", LoginTypes.DOCTOR.type)
                addProperty("id", docId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.apiHospitalDoctorProfile(body)
        } else {
            showToast(getString(R.string.network_unavailable))
        }
    }

    private fun fetchServiceForApi() {
        if (isNetworkConnected) {
            mViewModel?.apiServiceFor()
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    override fun onSuccessCommon(response: CommonResponse?) {
        hideLoading()
        showToast(response?.message ?: "")
        if (response?.code.equals(SUCCESS_CODE)) {
            FragmentManageHospitalDocsLab.NEED_REFRESH_DOCS = true
            finish()
        }
    }

   override fun onSuccessUserProfile(response: ModelUserProfile?) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            response?.result?.let { bindNewLayout(it) } ?: run { showToast(response?.message ?: "") }
        } else {
            showToast(response?.message ?: "")
        }
    }

    override fun onSuccessServiceFor(specialityList: ModelServiceFor?) {
             hideLoading()
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

    override fun successRegistrationResponse(response: RegistrationResponse?) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            FragmentManageHospitalDocsLab.NEED_REFRESH_DOCS = true
            CommonDialog.showDialogForSingleButton(this, object : DialogClickCallback {
                override fun onConfirm() {
                    onBackPressed()
                }
            }, getString(R.string.profile), response?.message ?: "")
        } else {
            CommonDialog.showDialogForSingleButton(this, object : DialogClickCallback {
            }, getString(R.string.profile), response?.message ?: "")
        }
    }

    override fun onSuccessEditProfile(response: ModelUserProfile?) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) FragmentManageHospitalDocsLab.NEED_REFRESH_DOCS = true
        CommonDialog.showDialogForSingleButton(this, object : DialogClickCallback {
            override fun onConfirm() { /* onBackPressed() */ } }, getString(R.string.profile), response?.message ?: "")
    }

    private fun addMoreQualif() {
        try {
            binding?.run {
                adapQualiMore.updateData(
                    arrayListOf(
                        ModelQualificationMore("", getString(R.string.no_attachment), "")
                    )
                )
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun bindNewLayout(response: ModelUserProfile.Result?) {
        binding?.run {
            response?.let {
                fetchSpecialityApi()
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

                if (it.image.isNullOrBlank().not()) imgProfile.setCircularRemoteImage(it.image)

                tvWorkFrom.text = it.work_area?.trim().orEmpty()
                edtCc.setText(it.country_code.orEmpty())

                tvUsername.text = it.first_name?.trim()
                tvhEmailId.text = it.email
                tvhAcname.text = it.display_user_type

                edtRegFirstname.setText(it.first_name)
                edtRegEmailaddress.setText(it.email)
                edtRegPhonenumber.setText(it.phone_number)

                edtAbout.setText(it.description)
                true
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
            val jsonObject = JsonObject().apply { addProperty("service_type", LoginTypes.DOCTOR.type ) }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.apiSpecialityList(body)
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        hideLoading()
        if (throwable?.message != null) {
           showToast(getString(R.string.something_went_wrong))
        }
    }

    var listSpeciality = ArrayList<String?>()
    override fun onSuccessSpeciality(specialityList: ModelIssueTypes?) {
        hideLoading()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == -1) {
                    fileUri = data?.data
                    filePath = fileUri?.path
                    try {
                        val file = fileUri?.let { FileUtil.from(this, it) }
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
            if (checkValidationForRegStepOne()) {

                val docterId = docId.asReqBody()
                val uId = mViewModel?.appSharedPref?.loginUserId?.asReqBody()
                val uType = LoginTypes.DOCTOR.type.asReqBody() // mViewModel?.appSharedPref?.loginUserType?.asReqBody()

                val flName = edtRegFirstname.text?.trim().toString().asReqBody()
                var mobNum = edtRegPhonenumber.text?.trim().toString()

                val cc_ = edtCc.text?.trim().toString().trim()
                mobNum = cc_ + mobNum

                val email = edtRegEmailaddress.text?.trim().toString().asReqBody()
                val dob = txtRegDob.text?.trim().toString().asReqBody()
                val gen = selectedGender.asReqBody()
                val about = edtAbout.text?.trim().toString().asReqBody()
                val pwd = binding?.edtRegPassword?.text?.toString()?.asReqBody()
                val workArea = binding?.tvWorkFrom?.text?.toString()?.asReqBody()


                val idNum = edtDocIdentityNumber.text.toString().asReqBody()
                val spec = edtSpeciality.text.toString().asReqBody()
                val hQualification = edtDocHighestQualification.text.toString().asReqBody()
                val expYears = edtDocYearsExperience.text.toString().asReqBody()
                val scfhsRegistrationId = edtDocScfhs.text.toString().asReqBody()

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


                showLoading()
                hideKeyboard()
                if(needToCreateDoc) {
                    mViewModel?.apiCreateDoc(
                        idImg, qlImg, scfhsImg, uType, flName, email, mobNum.asReqBody(), dob, selectedGender.asReqBody(),
                        pwd ?: "".asReqBody(), idNum, spec,  hQualification, expYears,
                        scfhsRegistrationId, uId ?: "".asReqBody(), workArea ?: "".asReqBody())
                } else {
                    mViewModel?.apiEditHospitalDoctorProfile(
                        workArea ?: "".asReqBody(), docterId, uType, flName, email, mobNum.asReqBody(), dob, gen, hQualification, idNum,
                        spec, expYears, scfhsRegistrationId, about, uImage, idImg, qlImg, scfhsImg)
                }

            }

        }
    }

    private fun checkValidationForRegStepOne(): Boolean {
        val mLt = binding
        return when {
            binding?.edtRegFirstname?.text.isNullOrBlank() -> {
              binding?.edtRegFirstname?.error = getString(R.string.enter_your_name)
              binding?.edtRegFirstname?.requestFocus()
              false
            }
            binding?.tvWorkFrom?.text.isNullOrBlank() -> {
                showToast(getString(R.string.provide_service_area))
                false
            }
            !isValidPassword(binding?.edtRegPhonenumber?.text?.toString()!!) -> {
                binding?.edtRegPhonenumber?.error = getString(R.string.enter_mobile_number)
                binding?.edtRegPhonenumber?.requestFocus()
                false
            }

            binding?.edtRegEmailaddress?.text.isNullOrBlank() -> {
                binding?.edtRegEmailaddress?.error = getString(R.string.enter_email_id)
                binding?.edtRegEmailaddress?.requestFocus()
                false
            }

            !binding?.edtRegEmailaddress?.text?.toString()!!.matches(emailPattern.toRegex()) -> {
                binding?.edtRegEmailaddress?.error = getString(R.string.enter_valid_email)
                binding?.edtRegEmailaddress?.requestFocus()
                false
            }

            binding?.txtRegDob?.text.isNullOrBlank() -> {
                showToast(getString(R.string.select_dob))
                false
            }

            selectedGender.isBlank() -> {
                showToast(getString(R.string.select_gender))
                false
            }

            binding?.edtRegPassword?.text.isNullOrBlank()  && needToCreateDoc -> {
                binding?.edtRegPassword?.error = getString(R.string.enter_password)
                binding?.edtRegPassword?.requestFocus()
                false
            }
            ((binding?.edtRegPassword?.text?.toString()?.length ?: 0) < 8)   && needToCreateDoc -> {
                binding?.edtRegPassword?.error = getString(R.string.must_be_at_least_8_characters)
                binding?.edtRegPassword?.requestFocus()
                false
            }
            binding?.edtRegConfirmPassword?.text.isNullOrBlank()  && needToCreateDoc -> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.enter_cnf_pwd)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }
            ((binding?.edtRegConfirmPassword?.text?.toString()?.length ?: 0) < 8)  && needToCreateDoc-> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.must_be_at_least_8_characters)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }
            (!binding?.edtRegPassword?.text?.toString().equals(binding?.edtRegConfirmPassword?.text?.toString()))  && needToCreateDoc -> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.pwd_not_matched)
                binding?.edtRegConfirmPassword?.requestFocus()
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
            (null == imgIdentity) && needToCreateDoc -> {
                showToast(getString(R.string.provide_id_proof))
                false
            }
            mLt?.edtSpeciality?.text.isNullOrBlank() -> {
                showToast( getString(R.string.provide_speciality))
                false
            }
            mLt?.edtDocHighestQualification?.text.isNullOrBlank() -> {
                mLt?.edtDocHighestQualification?.error =  getString(R.string.provide_qualification)
                mLt?.edtDocHighestQualification?.requestFocus()
                false
            }
            // check for upload certificate
            (null == imgQualification) && needToCreateDoc  -> {
                showToast( getString(R.string.provide_qualification_proof))
                false
            }
            mLt?.edtDocYearsExperience?.text.isNullOrBlank() -> {
                mLt?.edtDocYearsExperience?.error =  getString(R.string.provide_experience)
                mLt?.edtDocYearsExperience?.requestFocus()
                false
            }
            mLt?.edtDocScfhs?.text.isNullOrBlank() -> {
                mLt?.edtDocScfhs?.error =  getString(R.string.provide_scfhs_reg_id)
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
            (null == imgScfhs) && needToCreateDoc -> {
                showToast( getString(R.string.provide_scfhs_proof))
                false
            }
            else -> true
        }
    }
    private fun isStartWithIdExact() =  binding?.edtDocIdentityNumber?.text.toString().trim().startsWith("1") ||
                                        binding?.edtDocIdentityNumber?.text.toString().trim().startsWith("2")

    private fun isStartWithIdExact7() =  binding?.edtDocIdentityNumber?.text.toString().trim().startsWith("7")

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
            val permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            val permissionWriteExternalStorage =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

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
        val builder = AlertDialog.Builder(this)
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
                .start(this)
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
        imageFile = File(externalCacheDir!!.absolutePath,System.currentTimeMillis().toString() + ".jpg"
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
                thumbnail = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                bytes = ByteArrayOutputStream()
                thumbnail?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        imageFile = File(externalCacheDir!!.absolutePath,System.currentTimeMillis().toString() + ".jpg")
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
        /*     im_upload.setImageBitmap(thumbnail);
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

}