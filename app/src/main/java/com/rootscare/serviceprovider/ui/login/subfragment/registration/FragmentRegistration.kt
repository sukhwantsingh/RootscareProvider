package com.rootscare.serviceprovider.ui.login.subfragment.registration

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.myfilepickesexample.FileUtil
import com.rootscare.data.model.response.registrationresponse.RegistrationResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.model.ModelServiceFor
import com.rootscare.model.RegistrationModel
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentRegistrationBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree.FragmentRegistrationStepThreeNavigator
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree.FragmentRegistrationStepThreeViewModel
import com.rootscare.serviceprovider.ui.supportmore.*
import com.rootscare.serviceprovider.ui.supportmore.models.ModelIssueTypes
import com.rootscare.serviceprovider.utilitycommon.*
import com.rootscare.utils.ManagePermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FragmentRegistration : BaseFragment<FragmentRegistrationBinding, FragmentRegistrationStepThreeViewModel>(),  FragmentRegistrationStepThreeNavigator {
    private var binding: FragmentRegistrationBinding? = null
    private var mViewModel: FragmentRegistrationStepThreeViewModel? = null
    private var yearForReopen: Int? = null
    private var monthForReopen: Int? = null
    private var dayForReopen: Int? = null
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    val PICKFILE_RESULT_CODE = 4
    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var selectedGender = ""
    var monthOfDob: String = ""
    var dayOfDob: String = ""
    var choosenYear = 1990
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration
    override val viewModel: FragmentRegistrationStepThreeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(FragmentRegistrationStepThreeViewModel::class.java)
            return mViewModel as FragmentRegistrationStepThreeViewModel
        }

    private var imgIdentity: File? = null
    private var imgQualification: File? = null
    private var imgScfhs: File? = null
    private var uploadForType = -1 // ie. are 0-id_num,1-qualification,2-SCFHS Registration ID
    private val workFromList : ArrayList<String?> by lazy { ArrayList() }
    private val workFromListMap : HashMap<String?, String?> by lazy { HashMap() }

    companion object {
        val TAG = FragmentRegistration::class.java.simpleName
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(): FragmentRegistration {
            val args = Bundle()
            val fragment = FragmentRegistration()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        initViews()
        initApicalling()
    }

    private fun initApicalling() {
        fetchServiceForApi()
    }

    private fun initViews() {
        val termsPolicyText = "${getString(R.string.by_creating_agree_)} <font color='#17181A'><b>${getString(R.string.terms_of_service)}</b></font> and <font color='#17181A'><b>${getString(R.string.privacy_policy)}</b></font>"
        setClickableString(termsPolicyText.parseAsHtml().toString(), binding?.tvTermsPolicy,
            arrayOf(getString(R.string.terms_of_service), getString(R.string.privacy_policy)), arrayOf(termsCallback, privacyCallback))

        managePermissions = ManagePermissions(
            this.requireActivity(),
            listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            PermissionsRequestCode
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) managePermissions.checkPermissions()

        binding?.tvWorkFrom?.setOnClickListener {
        CommonDialog.showDialogForDropDownList(this.requireActivity(), getString(R.string.select),
                workFromList, object : DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    binding?.run {
                        if(tvWorkFrom.text.toString().equals(text,true).not()){
                            tvWorkFrom.text = text

                            val mCode = workFromListMap[text].orEmpty()
                            edtCc.setText(mCode)

                        }
                    }
                }
            })
        }

        binding?.tvSelectUser?.setOnClickListener {
            CommonDialog.showDialogForDropDownList(this.requireActivity(), getString(R.string.select_provider_type), enableProviders, object :
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    if (binding?.tvSelectUser?.text.toString() != text) {
                        binding?.tvSelectUser?.text = text

                        if (text == LoginTypes.HOSPITAL.displayName) {
                            binding?.run {
                                tilDob.visibility = View.GONE
                                llGender.visibility = View.GONE
                                tilFirstname.hint = getString(R.string.hospital_name)
                            }
                        } else if (text == LoginTypes.LAB.displayName) {
                            binding?.run {
                                tilDob.visibility = View.GONE
                                llGender.visibility = View.GONE
                                tilFirstname.hint = getString(R.string.lab_name)
                            }
                        } else {
                            binding?.run {
                                tilDob.visibility = View.VISIBLE
                                llGender.visibility = View.VISIBLE
                                tilFirstname.hint = getString(R.string.full_name)
                            }

                        }

                        resetAttachments()
                        fetchSpecialityApi(binding?.tvSelectUser?.text.toString())

                        // show appropriate layout for signup
                        displayRequiredSignUpForProviders(text.trim())
                    }
                }
            })
        }
            binding?.imageViewBack?.setOnClickListener { (activity as LoginActivity).onBackPressed() }
            binding?.tvhLoginHere?.setOnClickListener { (activity as LoginActivity).onBackPressed() }

            binding?.radioBtnRegFemale?.setOnClickListener { selectedGender = GenderType.FEMALE.get() }
            binding?.radioBtnRegMale?.setOnClickListener { selectedGender = GenderType.MALE.get() }
            binding?.txtRegDob?.setOnClickListener {

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this.requireActivity(), { _, yearLocal, monthOfYear, dayOfMonth ->
                    yearForReopen = yearLocal
                    monthForReopen = monthOfYear
                    dayForReopen = dayOfMonth
                    // Display Selected date in textbox
                    // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                    monthOfDob = if ((monthOfYear + 1) < 10) {
                        "0" + (monthOfYear + 1)
                    } else {
                        (monthOfYear + 1).toString()
                    }

                    dayOfDob = if (dayOfMonth < 10) {
                        "0$dayOfMonth"

                    } else {
                        dayOfMonth.toString()
                    }
                    binding?.txtRegDob?.setText("$yearLocal-$monthOfDob-$dayOfDob")
                }, year, month, day)

                if (yearForReopen != null && monthForReopen != null && dayForReopen != null) {
                    dpd.updateDate(yearForReopen!!, monthForReopen!!, dayForReopen!!)
                }
                val date = Date()
                date.year = Date().year - 5
                dpd.datePicker.maxDate = date.time // for 5 years
                dpd.show()
            }
            binding?.btnCreateAcc?.setOnClickListener { hitSignupApi() }

            // nurse / doc / physio
            binding?.layoutDoctorNursePhy?.edtSpeciality?.setOnClickListener {
                if (binding?.tvSelectUser?.text.isNullOrBlank().not()) {
                    CommonDialog.showDialogForDropDownList(this.requireActivity(), getString(R.string.speciality), listSpeciality, object :
                        DropDownDialogCallBack {
                        override fun onConfirm(text: String) {
                            binding?.layoutDoctorNursePhy?.edtSpeciality?.text = text
                        }
                    })
                } else showToast(getString(R.string.select_user_type))


            }
            binding?.layoutDoctorNursePhy?.tvhDocUploadPhotocopy?.setOnClickListener {
                uploadForType = 0
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
            binding?.layoutDoctorNursePhy?.tvhDocUploadCertificate?.setOnClickListener {
                uploadForType = 1
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
            binding?.layoutDoctorNursePhy?.tvhScfhsUploadCertificate?.setOnClickListener {
                uploadForType = 2
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }

            // babysiter/caregiver
            binding?.layoutBc?.tvhBcUploadPhotocopy?.setOnClickListener {
                uploadForType = 0
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
            binding?.layoutBc?.tvhBcUploadCertificate?.setOnClickListener {
                uploadForType = 1
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }

            // Hospital
            binding?.layoutHospital?.tvhHospUploadPhotocopy?.setOnClickListener {
                uploadForType = 0
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
            binding?.layoutHospital?.tvhHospUploadCertificate?.setOnClickListener {
                uploadForType = 1
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }

        }


    // click over Terms
    private var termsCallback: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
             // open terms and conditions
            try {
                navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_TERMS)))
            } catch (e: Exception) {
                println(e)
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.typeface = Typeface.create("font/rubik_medium.ttf", Typeface.BOLD)
            ds.isUnderlineText = false
        }
    }

    // click over Privacy
    private var privacyCallback: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            // open PP
        try {
            navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_PRIVACY)))
            } catch (e: Exception) {
                println(e)
            }
        }
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.typeface = Typeface.create("font/rubik_medium.ttf", Typeface.BOLD)
            ds.isUnderlineText = false
        }
    }

    private fun hitSignupApi() {
     if (isNetworkConnected) {
            if (checkValidationForRegStepOne()) {
                val fN = binding?.edtRegFirstname?.text?.toString().orEmpty()
                val em = binding?.edtRegEmailaddress?.text?.toString().orEmpty()
                var phn = binding?.edtRegPhonenumber?.text?.toString().orEmpty()
                val cc_ = binding?.edtCc?.text?.toString().orEmpty()
                    phn = cc_ + phn

                val db = binding?.txtRegDob?.text?.toString().orEmpty()
                val pwd = binding?.edtRegPassword?.text?.toString().orEmpty()
                val workArea = binding?.tvWorkFrom?.text?.toString().orEmpty()

                var idNum = ""
                var spec = ""
                var hQualification = ""
                var expYears = ""
                var scfhsRegistrationId = ""
                var hspMohLicNum = ""
                var hospRegId = ""


                val apiUserType = when {
                    binding?.tvSelectUser?.text?.toString()?.equals(LoginTypes.CAREGIVER.displayName, ignoreCase = true) == true -> {
                        LoginTypes.CAREGIVER.type.lowercase(Locale.ROOT)
                    }
                    else -> binding?.tvSelectUser?.text?.toString()?.lowercase(Locale.ROOT)
                }

                when (binding?.tvSelectUser?.text.toString()) {
                    LoginTypes.NURSE.displayName, LoginTypes.DOCTOR.displayName, LoginTypes.PHYSIOTHERAPY.displayName -> {
                        binding?.layoutDoctorNursePhy?.run {
                            idNum = edtDocIdentityNumber.text.toString()
                            spec = edtSpeciality.text.toString()
                            hQualification = edtDocHighestQualification.text.toString()
                            expYears = edtDocYearsExperience.text.toString()
                            scfhsRegistrationId = edtDocScfhs.text.toString()
                        }
                    }
                    LoginTypes.BABYSITTER.displayName, LoginTypes.CAREGIVER.displayName -> {
                        binding?.layoutBc?.run {
                            idNum = edtBcIdentityNumber.text.toString()
                            hQualification = edtHighestQualification.text.toString()
                            expYears = edtExpeYears.text.toString()
                        }

                    }
                    LoginTypes.HOSPITAL.displayName , LoginTypes.LAB.displayName -> {
                        binding?.layoutHospital?.run {
                            hspMohLicNum = edtHospMoh.text.toString()
                            hospRegId = edtHospRegistrationNo.text.toString()
                        }
                    }
                    else -> Unit
                }

                var idImg: MultipartBody.Part? = null
                var qlImg: MultipartBody.Part? = null
                var scfhsImg: MultipartBody.Part? = null

                imgIdentity?.let {
                    val idnmg = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgIdentity!!)
                    idImg = MultipartBody.Part.createFormData("id_image", imgIdentity?.name, idnmg)
                }
                imgQualification?.let {
                    val qmg = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgQualification!!)
                    qlImg = MultipartBody.Part.createFormData("certificate", imgQualification?.name, qmg)
                }
                imgScfhs?.let {
                    val mscfhs = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgScfhs!!)
                    scfhsImg = MultipartBody.Part.createFormData("scfhs_image", imgScfhs?.name, mscfhs)
                }

                // call api
                baseActivity?.showLoading()
                mViewModel?.apiSignup(
                    idImg, qlImg, scfhsImg, apiUserType?.asReqBody()!!, fN.asReqBody(),
                    em.asReqBody(), phn.asReqBody(), db.asReqBody(), selectedGender.asReqBody(),
                    pwd.asReqBody(), idNum.asReqBody(), spec.asReqBody(),
                    hQualification.asReqBody(), expYears.asReqBody(), scfhsRegistrationId.asReqBody(),
                    hspMohLicNum.asReqBody(), hospRegId.asReqBody(),workArea.asReqBody()
                )
            }
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    private fun fetchSpecialityApi(type: String) {
        val apiType = when (type) {
            LoginTypes.NURSE.displayName -> LoginTypes.NURSE.type
            LoginTypes.DOCTOR.displayName -> LoginTypes.DOCTOR.type
            LoginTypes.HOSPITAL_DOCTOR.displayName -> LoginTypes.HOSPITAL_DOCTOR.type
            LoginTypes.PHYSIOTHERAPY.displayName -> LoginTypes.PHYSIOTHERAPY.type
            else -> ""
        }

        if (apiType.isNotBlank()) {
            if (isNetworkConnected) {
                val jsonObject = JsonObject().apply { addProperty("service_type", apiType) }
                val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                //    baseActivity?.showLoading()
                mViewModel?.apiSpecialityList(body)
            } else {
                showToast(getString(R.string.check_network_connection))
            }
        }
    }

    private fun fetchServiceForApi() {
        if (isNetworkConnected) {
            mViewModel?.apiServiceFor()
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    var listSpeciality = ArrayList<String?>()
    override fun onSuccessSpeciality(specialityList: ModelIssueTypes?) {
        baseActivity?.hideLoading()
        listSpeciality.clear()
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

    override fun successRegistrationResponse(registrationResponse: RegistrationResponse?) {
        baseActivity?.hideLoading()
        if (registrationResponse?.code.equals(SUCCESS_CODE)) {
          CommonDialog.showDialogForSingleButton(requireActivity(), object : DialogClickCallback {
                override fun onConfirm() {
                    AppData.registrationModelData = RegistrationModel()
                    AppData.boolSForRefreshLayout = true
                    restForm()
                    (activity as? LoginActivity)?.setCurrentItem(0, true)
                }
            }, getString(R.string.registration), registrationResponse?.message?:"")
        } else {
            CommonDialog.showDialogForSingleButton(requireActivity(), object : DialogClickCallback {
            }, getString(R.string.registration), registrationResponse?.message?:"")
        }

    }

    private fun displayRequiredSignUpForProviders(typeSelected: String) {
        binding?.run {
            layoutBc.llTop.visibility = View.GONE
            layoutDoctorNursePhy.llTop.visibility = View.GONE
            layoutHospital.llTop.visibility = View.GONE

            when {
                typeSelected.equals(LoginTypes.NURSE.displayName, ignoreCase = true) ||
                        typeSelected.equals(LoginTypes.PHYSIOTHERAPY.displayName, ignoreCase = true) ||
                        typeSelected.equals(LoginTypes.DOCTOR.displayName, ignoreCase = true) -> {
                    layoutDoctorNursePhy.llTop.visibility = View.VISIBLE
                }

                typeSelected.equals(LoginTypes.BABYSITTER.displayName, ignoreCase = true) ||
                typeSelected.equals(LoginTypes.CAREGIVER.displayName, ignoreCase = true) -> {
                    layoutBc.llTop.visibility = View.VISIBLE
                }

                typeSelected.equals(LoginTypes.HOSPITAL.displayName, ignoreCase = true) ||
                typeSelected.equals(LoginTypes.LAB.displayName, ignoreCase = true) -> {
                    layoutHospital.llTop.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun checkValidationForRegStepOne(): Boolean {
        return when {
            binding?.tvSelectUser?.text.isNullOrBlank() -> {
                showToast(getString(R.string.select_user_type))
                false
            }
            binding?.edtRegFirstname?.text.isNullOrBlank() -> {
                binding?.edtRegFirstname?.error = if (binding?.tvSelectUser?.text == LoginTypes.HOSPITAL.displayName) (getString(R.string.enter_hospital_name))
                else if (binding?.tvSelectUser?.text == LoginTypes.LAB.displayName) getString(R.string.enter_lab_name)
                else getString(R.string.enter_your_name)

                binding?.edtRegFirstname?.requestFocus()
                false
            }
            binding?.edtRegEmailaddress?.text.isNullOrBlank() -> {
                binding?.edtRegEmailaddress?.error = getString(R.string.provide_email)
                binding?.edtRegEmailaddress?.requestFocus()
                false
            }
            !binding?.edtRegEmailaddress?.text?.toString()!!.matches(emailPattern.toRegex()) -> {
                binding?.edtRegEmailaddress?.error = getString(R.string.enter_valid_email)
                binding?.edtRegEmailaddress?.requestFocus()
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
            (binding?.edtRegPhonenumber?.toString()?.length ?: 0) < 9 -> {
                binding?.edtRegPhonenumber?.error = getString(R.string.use_mobile_number_note)
                binding?.edtRegPhonenumber?.requestFocus()
                false
            }

            binding?.tvSelectUser?.text != LoginTypes.HOSPITAL.displayName &&
            binding?.tvSelectUser?.text != LoginTypes.LAB.displayName && binding?.txtRegDob?.text.isNullOrBlank() -> {
                showToast(getString(R.string.select_dob))
                false
            }
            binding?.tvSelectUser?.text != LoginTypes.HOSPITAL.displayName &&
            binding?.tvSelectUser?.text != LoginTypes.LAB.displayName && selectedGender.isBlank() -> {
                showToast(getString(R.string.select_gender))
                false
            }

            binding?.edtRegPassword?.text.isNullOrBlank() -> {
                binding?.edtRegPassword?.error = getString(R.string.enter_password)
                binding?.edtRegPassword?.requestFocus()
                false
            }
            (binding?.edtRegPassword?.text?.toString()?.length ?: 0) < 8 -> {
                binding?.edtRegPassword?.error = getString(R.string.must_be_at_least_8_characters)
                binding?.edtRegPassword?.requestFocus()
                false
            }
            binding?.edtRegConfirmPassword?.text.isNullOrBlank() -> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.enter_cnf_pwd)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }
            (binding?.edtRegConfirmPassword?.text?.toString()?.length ?: 0) < 8 -> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.must_be_at_least_8_characters)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }
            !binding?.edtRegPassword?.text?.toString().equals(binding?.edtRegConfirmPassword?.text?.toString()) -> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.pwd_not_matched)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }

            binding?.tvSelectUser?.text?.equals(LoginTypes.NURSE.displayName) == true ||
            binding?.tvSelectUser?.text?.equals(LoginTypes.DOCTOR.displayName) == true ||
            binding?.tvSelectUser?.text?.equals(LoginTypes.PHYSIOTHERAPY.displayName) == true -> {
                val mLt = binding?.layoutDoctorNursePhy
                when {
                    mLt?.edtDocIdentityNumber?.text.isNullOrBlank() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.provide_id_number)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }
                    binding?.edtCc?.text?.toString()?.trim().equals(CurrencyTypes.SAR.getCc(), ignoreCase = true) &&
                            isStartWithIdExact().not() -> {
                        mLt?.edtDocIdentityNumber?.error = getString(R.string.id_must_start_from_1_or_2)
                        mLt?.edtDocIdentityNumber?.requestFocus()
                        false
                    }

                   binding?.edtCc?.text?.toString()?.trim().equals(CurrencyTypes.AED.getCc(), ignoreCase = true) &&
                            isStartWithIdExact7().not() -> {
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
                   null == imgIdentity-> {
                        showToast(getString(R.string.provide_id_proof))
                        false
                    }
                    mLt?.edtSpeciality?.text.isNullOrBlank() -> {
                        showToast(getString(R.string.provide_speciality))
                        false
                    }
                    mLt?.edtDocHighestQualification?.text.isNullOrBlank() -> {
                        mLt?.edtDocHighestQualification?.error = getString(R.string.provide_qualification)
                        mLt?.edtDocHighestQualification?.requestFocus()
                        false
                    }
                    // check for upload certificate
                    null == imgQualification -> {
                        showToast(getString(R.string.provide_qualification_proof))
                        false
                    }
                    mLt?.edtDocYearsExperience?.text.isNullOrBlank() -> {
                        mLt?.edtDocYearsExperience?.error = getString(R.string.provide_experience)
                        mLt?.edtDocYearsExperience?.requestFocus()
                        false
                    }
                    mLt?.edtDocScfhs?.text.isNullOrBlank() -> {
                        mLt?.edtDocScfhs?.error = getString(R.string.provide_scfhs_reg_id)
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
                    null == imgScfhs -> {
                       showToast(getString(R.string.provide_scfhs_proof))
                       false
                    }
                    else -> true
                }
            }

            binding?.tvSelectUser?.text?.equals(LoginTypes.BABYSITTER.displayName) == true ||
            binding?.tvSelectUser?.text?.equals(LoginTypes.CAREGIVER.displayName) == true -> {
                val mLt = binding?.layoutBc
                when {
                    mLt?.edtBcIdentityNumber?.text.isNullOrBlank() -> {
                        mLt?.edtBcIdentityNumber?.error = getString(R.string.provide_id_number)
                        mLt?.edtBcIdentityNumber?.requestFocus()
                        false
                    }

                   binding?.edtCc?.text?.toString()?.trim().equals(CurrencyTypes.SAR.getCc(), ignoreCase = true) && isStartWithIdExact2().not() -> {
                        mLt?.edtBcIdentityNumber?.error = getString(R.string.id_must_start_from_1_or_2)
                        mLt?.edtBcIdentityNumber?.requestFocus()
                        false
                    }

                    binding?.edtCc?.text?.toString()?.trim().equals(CurrencyTypes.AED.getCc(), ignoreCase = true) && isStartWithIdExact7_2().not() -> {
                        mLt?.edtBcIdentityNumber?.error = getString(R.string.id_must_start_from_7)
                        mLt?.edtBcIdentityNumber?.requestFocus()
                        false
                    }

                    ((mLt?.edtBcIdentityNumber?.text?.toString()?.length ?: 0) < 10) || ((mLt?.edtBcIdentityNumber?.text?.toString()?.length ?: 0) > 15) -> {
                        mLt?.edtBcIdentityNumber?.error = getString(R.string.id_number_must_be_10)
                        mLt?.edtBcIdentityNumber?.requestFocus()
                        false
                    }


                    // check for upload certificate
                    null == imgIdentity -> {
                        showToast(getString(R.string.provide_id_proof))
                        false
                    }

                    mLt?.edtHighestQualification?.text.isNullOrBlank() -> {
                        mLt?.edtHighestQualification?.error = getString(R.string.provide_qualification)
                        mLt?.edtHighestQualification?.requestFocus()
                        false
                    }
                    // check for upload certificate
                    null == imgQualification -> {
                        showToast(getString(R.string.provide_qualification_proof))
                        false
                    }
                    mLt?.edtExpeYears?.text.isNullOrBlank() -> {
                        mLt?.edtExpeYears?.error = getString(R.string.provide_experience)
                        mLt?.edtExpeYears?.requestFocus()
                        false
                    }
                    else -> true
                }
            }

            binding?.tvSelectUser?.text?.equals(LoginTypes.HOSPITAL.displayName) == true ||
            binding?.tvSelectUser?.text?.equals(LoginTypes.LAB.displayName) == true-> {
                val mLt = binding?.layoutHospital
                when {
                    mLt?.edtHospMoh?.text.isNullOrBlank() -> {
                        mLt?.edtHospMoh?.error = getString(R.string.provide_moh_licence_no)
                        mLt?.edtHospMoh?.requestFocus()
                        false
                    }
                    // check for upload certificate
                    null == imgIdentity -> {
                        showToast(getString(R.string.provide_moh_proof))
                        false
                    }
                    mLt?.edtHospRegistrationNo?.text.isNullOrBlank() -> {
                        mLt?.edtHospRegistrationNo?.error = getString(R.string.provide_registration_number)
                        mLt?.edtHospRegistrationNo?.requestFocus()
                        false
                    }

                    // Check for upload certificate
                    null == imgQualification -> {
                        showToast(getString(R.string.provide_registration_proof))
                        false
                    }
                  else -> true
                }
            }
            else -> true
        }
    }

    private fun isStartWithIdExact() =  binding?.layoutDoctorNursePhy?.edtDocIdentityNumber?.text.toString().startsWith("1") || binding?.layoutDoctorNursePhy?.edtDocIdentityNumber?.text.toString().startsWith("2")
    private fun isStartWithIdExact2() = binding?.layoutBc?.edtBcIdentityNumber?.text.toString().startsWith("1") || binding?.layoutBc?.edtBcIdentityNumber?.text.toString().startsWith("2")

    private fun isStartWithIdExact7() =  binding?.layoutDoctorNursePhy?.edtDocIdentityNumber?.text.toString().startsWith("7")
    private fun isStartWithIdExact7_2() = binding?.layoutBc?.edtBcIdentityNumber?.text.toString().startsWith("7")

    override fun errorRegistrationResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (null != throwable?.message) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            showToast(getString(R.string.something_went_wrong))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == -1) {
                fileUri = data?.data
                filePath = fileUri?.path
                // tvItemPath.setText(filePath)
                try {
                    val file = FileUtil.from(this.requireActivity(), fileUri!!)
                     Log.d("file", "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists())
                    displayCertificateName(getFileName(requireActivity(), fileUri!!), file, binding?.tvSelectUser?.text.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun displayCertificateName(fname: String?, fFile: File, uType: String) {
        Log.wtf(TAG, "displayCertificateName: $fname \n ${fFile.name}")
        val mTextView = when (uploadForType) {
            0 -> {
                imgIdentity = fFile
                when (uType) {
                    LoginTypes.CAREGIVER.displayName, LoginTypes.BABYSITTER.displayName -> {
                        binding?.layoutBc?.tvBcNaId
                    }
                    LoginTypes.HOSPITAL.displayName, LoginTypes.LAB.displayName -> {
                        binding?.layoutHospital?.tvHospNoAttachmentPhotocopy
                    }
                    else -> binding?.layoutDoctorNursePhy?.tvDocNoAttachPhotocpy
                }
            }
            1 -> {
                imgQualification = fFile
                when (uType) {
                    LoginTypes.CAREGIVER.displayName, LoginTypes.BABYSITTER.displayName -> {
                        binding?.layoutBc?.tvBcAttachementQuali
                    }
                    LoginTypes.HOSPITAL.displayName, LoginTypes.LAB.displayName -> {
                        binding?.layoutHospital?.tvHospAttachementCertificate
                    }
                    else -> binding?.layoutDoctorNursePhy?.tvDocAttachementCertificate
                }
            }
            2 -> {
                imgScfhs = fFile
                binding?.layoutDoctorNursePhy?.tvScfhsAttachementCertificate
            }
            else -> {
                imgIdentity = fFile
                when (uType) {
                    LoginTypes.CAREGIVER.displayName, LoginTypes.BABYSITTER.displayName -> {
                        binding?.layoutBc?.tvBcNaId
                    }
                    LoginTypes.HOSPITAL.displayName, LoginTypes.LAB.displayName -> {
                        binding?.layoutHospital?.tvHospNoAttachmentPhotocopy
                    }
                    else -> binding?.layoutDoctorNursePhy?.tvDocNoAttachPhotocpy
                }
            }
        }

        mTextView?.text = fFile.name
    }

    private fun resetAttachments() {
        imgIdentity = null; imgQualification = null; imgScfhs = null
        binding?.layoutDoctorNursePhy?.run {
            edtSpeciality.text = ""
            tvDocNoAttachPhotocpy.text = getString(R.string.no_attachment)
            tvDocAttachementCertificate.text = getString(R.string.no_attachment)
            tvScfhsAttachementCertificate.text = getString(R.string.no_attachment)
        }
        // reset baby sitter/caregiver
        binding?.layoutBc?.run {
            tvBcNaId.text = getString(R.string.no_attachment)
            tvBcAttachementQuali.text = getString(R.string.no_attachment)
        }
        // reset Hospital
        binding?.layoutHospital?.run {
            tvHospNoAttachmentPhotocopy.text = getString(R.string.no_attachment)
            tvHospAttachementCertificate.text = getString(R.string.no_attachment)
        }

    }

     private fun restForm() {
        imgIdentity = null; imgQualification = null; imgScfhs = null
        selectedGender = ""
         binding?.apply {
             tvSelectUser.text = ""
             edtRegFirstname.setText("")
             edtRegEmailaddress.setText("")
             tvWorkFrom.text = ""
             edtRegPhonenumber.setText("")
             txtRegDob.setText("")
             edtRegPassword.setText("")
             edtRegConfirmPassword.setText("")
         }

        binding?.layoutDoctorNursePhy?.run {
            edtSpeciality.text = ""
            edtDocHighestQualification.setText("")
            edtDocYearsExperience.setText("")
            edtDocScfhs.setText("")
            edtDocIdentityNumber.setText("")

            tvDocNoAttachPhotocpy.text = getString(R.string.no_attachment)
            tvDocAttachementCertificate.text = getString(R.string.no_attachment)
            tvScfhsAttachementCertificate.text = getString(R.string.no_attachment)
        }

        // reset baby sitter/caregiver
        binding?.layoutBc?.run {
            edtExpeYears.setText("")
            edtHighestQualification.setText("")
            edtBcIdentityNumber.setText("")

            tvBcNaId.text = getString(R.string.no_attachment)
            tvBcAttachementQuali.text = getString(R.string.no_attachment)
        }

        // reset Hospital
        binding?.layoutHospital?.run {
            edtHospMoh.setText("")
            edtHospRegistrationNo.setText("")

            tvHospNoAttachmentPhotocopy.text = getString(R.string.no_attachment)
            tvHospAttachementCertificate.text = getString(R.string.no_attachment)
        }

    }

    private fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor =
                context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    uplaodCertificate(PICKFILE_RESULT_CODE)
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


}