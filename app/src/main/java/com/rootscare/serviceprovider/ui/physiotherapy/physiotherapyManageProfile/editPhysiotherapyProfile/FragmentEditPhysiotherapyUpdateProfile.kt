package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyManageProfile.editPhysiotherapyProfile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.myfilepickesexample.FileUtil
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.response.deaprtmentlist.ResultItem
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.dialog.certificateupload.CertificateUploadFragmentCaregiver
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorEditProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyManageProfile.editPhysiotherapyProfile.adapter.PhysiotherapyCertificateListAdapter
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import com.rootscare.serviceprovider.utilitycommon.GenderType
import kotlinx.android.synthetic.main.fragment_doctor_edit_profile.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FragmentEditPhysiotherapyUpdateProfile :
    BaseFragment<FragmentDoctorEditProfileBinding, FragmentEditPhysiotherapyUpdateProfileViewModel>(),
    FragmentEditPhysiotherapyUpdateProfileNavigator {

    private var imageSelectionModel: AddlabTestImageSelectionModel? = null
    private val TAG = "FragmentEditDoctorProfile"
    private var certificateListAdapter: PhysiotherapyCertificateListAdapter? = null

    private val IMAGE_DIRECTORY = "/demonuts"
    private val PICKFILE_RESULT_CODE = 4
    private val GALLERY = 1
    private val CAMERA = 2

    private var departmentList: ArrayList<ResultItem?>? = ArrayList()
    private var departmentId = ""
    private var departTitle = ""
    private var choosenYear = 1980
    private var selectedGender = ""

    private var monthOfDob: String = ""
    private var dayOfDob: String = ""

    //    private var yearOfPassingToSubmit: String? = null
//    private var dobToSubmit: String? = null
    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var certificatefileFile: File? = null
    private var imageFile: File? = null
    private var userType: String? = null

    private var fragmentDoctorEditProfileBinding: FragmentDoctorEditProfileBinding? = null
    private var fragmentEditDoctorProfileViewModel: FragmentEditPhysiotherapyUpdateProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_edit_profile
    override val viewModel: FragmentEditPhysiotherapyUpdateProfileViewModel
        get() {
            fragmentEditDoctorProfileViewModel = ViewModelProviders.of(this).get(
                FragmentEditPhysiotherapyUpdateProfileViewModel::class.java
            )
            return fragmentEditDoctorProfileViewModel as FragmentEditPhysiotherapyUpdateProfileViewModel
        }

    companion object {
        fun newInstance(userType: String): FragmentEditPhysiotherapyUpdateProfile {
            val args = Bundle()
            args.putString("userType", userType)
            val fragment = FragmentEditPhysiotherapyUpdateProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentEditDoctorProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorEditProfileBinding = viewDataBinding
        fragmentDoctorEditProfileBinding?.llDepartment?.visibility = View.GONE
        fragmentDoctorEditProfileBinding?.llFees?.visibility = View.VISIBLE

        userType = arguments?.getString("userType")

        with(fragmentDoctorEditProfileBinding!!) {

            addCertificatePortionsSetUp()


//            textViewDepartment.setOnClickListener {
//                CommonDialog.showDialogForDepartmentDropDownList(
//                    activity!!,
//                    departmentList,
//                    "Select Department",
//                    object :
//                        OnDepartmentDropDownListItemClickListener {
//                        override fun onConfirm(departmentList: ArrayList<ResultItem?>?) {
//                            departTitle = ""
//                            departmentId = ""
//                            var p = 0
//                            for (i in 0 until departmentList?.size!!) {
//                                if (departmentList[i]?.isChecked.equals("true")) {
//                                    if (p == 0) {
//                                        departTitle = departmentList[i]?.title!!
//                                        departmentId = departmentList[i]?.id!!
//                                        p++
//
////                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
////                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
//                                    } else {
//                                        departTitle =
//                                            departTitle + "," + departmentList[i]?.title
//                                        departmentId =
//                                            departmentId + "," + departmentList[i]?.id
//
////                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
////                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
//                                    }
//                                }
//                            }
//
//                            Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- $departTitle")
//                            Log.d(FragmentLogin.TAG, "--SELECT ID:-- $departmentId")
//
//                            textViewDepartment.text = departTitle
//                        }
//
//                    })
//            }

            /*textViewPassingYear.setOnClickListener(View.OnClickListener {
                val builder = MonthPickerDialog.Builder(
                    activity,
                    MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
                        textViewPassingYear.setText(selectedYear.toString())
                    },
                    choosenYear,
                    0
                );
                builder.showYearOnly()
                    .setYearRange(1980, 2090)
                    .build()
                    .show();
            })*/


            textViewDOB.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    requireActivity(),
                    { _, year, monthOfYear, dayOfMonth ->

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
//                        dobToSubmit = "" + year + "-" + monthOfDob + "-" + dayOfDob
                        textViewDOB.text = "$year-$monthOfDob-$dayOfDob"
                    },
                    year,
                    month,
                    day
                )
                var yearForReopen: Int? = null
                var monthForReopen: Int? = null
                var dayForReopen: Int? = null
                if (!TextUtils.isDigitsOnly(textViewDOB.text.toString().trim())) {
                    val strings = textViewDOB.text.toString().split("-")
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
                dpd.show()
                dpd.datePicker.maxDate = System.currentTimeMillis()
            }


            //File Selection Button Click
//            textViewCertificate.setOnClickListener(View.OnClickListener {
//                var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
//                chooseFile.type = "*/*"
//                chooseFile = Intent.createChooser(chooseFile, "Choose a file")
//                startActivityForResult(
//                    chooseFile,
//                    PICKFILE_RESULT_CODE
//                )
//            })

            //Image Selection Button Click
            imgDoctorProfile.setOnClickListener {
                openCamera()
            }

            radioYesOrNo.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.radioBtnMale) {
                    selectedGender = GenderType.MALE.get()
                }
                if (checkedId == R.id.radioBtnFemale) {
                    selectedGender =  GenderType.FEMALE.get()
                }
                /*if (checkedId == R.id.radioBtnOther) {
                                selectedGender = "Other"
                            }*/
            }
            radioYesOrNo.radioBtnMale.isChecked = true

            btnSubmit.setOnClickListener {
                submitDetailsForEditProfile()
            }

        }


//        if (isNetworkConnected) {
//            baseActivity?.showLoading()
//            fragmentEditDoctorProfileViewModel?.apiDepartmentList()
//        } else {
//            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
//                .show()
//        }
        fetchDOCUserDetails()

    }

    private fun fetchDOCUserDetails() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val commonUserIdRequest = CommonUserIdRequest()
            commonUserIdRequest.id = fragmentEditDoctorProfileViewModel?.appSharedPref?.loginUserId
            fragmentEditDoctorProfileViewModel!!.apiDoctorProfile(commonUserIdRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?) {
        baseActivity?.hideLoading()
        if (departmentListResponse?.code.equals("200")) {
            if (departmentListResponse?.result != null && departmentListResponse.result.size > 0) {
                departmentList = ArrayList()
                departmentList = departmentListResponse.result
            }
        }
    }

    override fun onSuccessEditProfile(response: LoginResponse) {
//        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            certificateListAdapter?.qualificationDataList?.clear()
            Handler().postDelayed({ fetchDOCUserDetails() }, 500)
        } else {
            baseActivity?.hideLoading()
            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?) {
        baseActivity?.hideLoading()
        if (getDoctorProfileResponse?.code.equals("200")) {
            if (getDoctorProfileResponse?.result != null) {
                with(fragmentDoctorEditProfileBinding!!) {
                    if (getDoctorProfileResponse.result.firstName != null && getDoctorProfileResponse.result.firstName != "") {
                        ediitextFirstName.setText(getDoctorProfileResponse.result.firstName)
                    } else {
                        ediitextFirstName.setText("")
                    }
                    if (getDoctorProfileResponse.result.lastName != null && getDoctorProfileResponse.result.lastName != "") {
                        ediitextLastName.setText(getDoctorProfileResponse.result.lastName)
                    } else {
                        ediitextLastName.setText("")
                    }
                    if (getDoctorProfileResponse.result.firstName != null && getDoctorProfileResponse.result.firstName != "" && getDoctorProfileResponse.result.lastName != null
                        && getDoctorProfileResponse.result.lastName != ""
                    ) {
                        textViewDocNameTilte.text = getDoctorProfileResponse.result.firstName + " " + getDoctorProfileResponse.result.lastName
                    } else {
                        textViewDocNameTilte.text = ""
                    }

                    if (getDoctorProfileResponse.result.email != null && getDoctorProfileResponse.result.email != "") {
                        ediitextEmail.setText(getDoctorProfileResponse.result.email)
                        textViewDocEmailTilte.text = getDoctorProfileResponse.result.email
                    } else {
                        ediitextEmail.setText("")
                        textViewDocEmailTilte.text = ""
                    }
                    if (getDoctorProfileResponse.result.image != null && getDoctorProfileResponse.result.image != "") {
                        val options: RequestOptions =
                            RequestOptions()
                                .centerCrop()
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.profile_no_image)
                                .priority(Priority.HIGH)
                        Glide
                            .with(requireActivity())
                            .load(BaseMediaUrls.USERIMAGE.url + getDoctorProfileResponse.result.image)
                            .apply(options)
                            .into(imgDoctorProfile)
                    }
                    if (getDoctorProfileResponse.result.dob != null && getDoctorProfileResponse.result.dob != "") {
                        textViewDOB.text = getDoctorProfileResponse.result.dob
                    } else {
                        textViewDOB.text = ""
                    }
                    if (getDoctorProfileResponse.result.phoneNumber != null && getDoctorProfileResponse.result.phoneNumber != "") {
                        ediitextMobileNumber.setText(getDoctorProfileResponse.result.phoneNumber)
                    } else {
                        ediitextMobileNumber.setText("")
                    }
                    if (getDoctorProfileResponse.result.gender != null && getDoctorProfileResponse.result.gender != "") {
                        if (getDoctorProfileResponse.result.gender.trim().toLowerCase(Locale.ROOT) == "male") {
                            radioYesOrNo.check(R.id.radioBtnMale)
                            selectedGender =  GenderType.MALE.get()
                        } else if (getDoctorProfileResponse.result.gender.trim().toLowerCase(Locale.ROOT) == "female") {
                            radioYesOrNo.check(R.id.radioBtnFemale)
                            selectedGender =  GenderType.FEMALE.get()
                        }/*else{
                            radioYesOrNo.check(R.id.radioBtnOther)
                            selectedGender = "Other"
                        }*/
                    }
                    /*if(getDoctorProfileResponse.result.qualification!=null && !getDoctorProfileResponse.result.qualification.equals("")){
                        ediitextQualification.setText(getDoctorProfileResponse.result.qualification)
                    }else{
                        ediitextQualification.setText("")
                    }
                    if(getDoctorProfileResponse.result.passingYear!=null && !getDoctorProfileResponse.result.passingYear.equals("")){
                        textViewPassingYear.setText(getDoctorProfileResponse.result.passingYear)
                    }else{
                        textViewPassingYear.setText("")
                    }
                    if(getDoctorProfileResponse.result.institute!=null && !getDoctorProfileResponse.result.institute.equals("")){
                        ediitextInstitute.setText(getDoctorProfileResponse.result.institute)
                    }else{
                        ediitextInstitute.setText("")
                    }*/
                    if (getDoctorProfileResponse.result.description != null && getDoctorProfileResponse.result.description != "") {
                        ediitextDescription.setText(getDoctorProfileResponse.result.description)
                    } else {
                        ediitextDescription.setText("")
                    }
                    if (getDoctorProfileResponse.result.experience != null && getDoctorProfileResponse.result.experience != "") {
                        ediitextExperience.setText(getDoctorProfileResponse.result.experience)
                    } else {
                        ediitextExperience.setText("")
                    }
                    if (getDoctorProfileResponse.result.availableTime != null && getDoctorProfileResponse.result.availableTime != "") {
                        ediitexAvailableTime.setText(getDoctorProfileResponse.result.availableTime)
                    } else {
                        ediitexAvailableTime.setText("")
                    }
                    if (userType.equals("nurse")) {
                        if (getDoctorProfileResponse.result.dailyRate != null && getDoctorProfileResponse.result.dailyRate != "") {
                            ediitextFees.setText(getDoctorProfileResponse.result.dailyRate)
                        } else {
                            ediitextFees.setText("")
                        }
                    } else {
                        if (getDoctorProfileResponse.result.fees != null && getDoctorProfileResponse.result.fees != "") {
                            ediitextFees.setText(getDoctorProfileResponse.result.fees)
                        } else {
                            ediitextFees.setText("")
                        }
                    }

                    if (getDoctorProfileResponse.result.department != null && getDoctorProfileResponse.result.department == "") {
                        textViewDepartment.text = "No Department"
                        departTitle = ""
                        departmentId = "1"
                        /*for (i in 0 until getDoctorProfileResponse.result.department.size) {
                            if (getDoctorProfileResponse.result.department[i]?.title != null &&
                                getDoctorProfileResponse.result.department[i]?.id != null
                            ) {
                                if (i == 0) {
                                    departTitle += getDoctorProfileResponse.result.department[i]?.title
                                    departmentId += getDoctorProfileResponse.result.department[i]?.id
                                } else {
                                    departTitle += "," + getDoctorProfileResponse.result.department[i]?.title
                                    departmentId += "," + getDoctorProfileResponse.result.department[i]?.id
                                }
                            }
                            textViewDepartment.setText(departTitle)
                            for (j in 0 until departmentList?.size!!) {
                                if (getDoctorProfileResponse.result.department[i]?.id.equals(departmentList!![j]?.id)) {
                                    departmentList!![j]?.isChecked = "true"
                                }
                            }
                        }*/
                    } else {
                        textViewDepartment.text = "No Department"
                    }
                    /*if(getDoctorProfileResponse.result.qualificationCertificate!=null && !getDoctorProfileResponse.result.qualificationCertificate.equals("")){
                        textViewCertificate.setText(getDoctorProfileResponse.result.qualificationCertificate)
                    }else{
                        textViewCertificate.setText("")
                    }*/

                    if (getDoctorProfileResponse.result.qualificationData != null && getDoctorProfileResponse.result.qualificationData.size > 0) {
                        for (item in getDoctorProfileResponse.result.qualificationData) {
                            item.isOldData = true
                        }
                        certificateListAdapter?.qualificationDataList?.addAll(getDoctorProfileResponse.result.qualificationData)
                        certificateListAdapter?.notifyDataSetChanged()
                    }

                }

            }
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
//            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    /*private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }*/

    /*fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            /*if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data!!.data
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                        val path = saveImage(bitmap)
                        bitmapToFile(bitmap)
                        Glide.with(activity!!).load(bitmap).apply(RequestOptions.circleCropTransform())
                            .into(fragmentDoctorEditProfileBinding!!.imgDoctorProfile)
//                    Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()

                    } catch (e: IOException) {
                        e.printStackTrace()
//                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                    }

                }


            } else if (requestCode == CAMERA) {
//            val contentURI = data!!.data
                val thumbnail = data?.extras?.get("data") as Bitmap
                saveImage(thumbnail)
                bitmapToFile(thumbnail)
                Glide.with(activity!!).load(thumbnail).apply(RequestOptions.circleCropTransform())
                    .into(fragmentDoctorEditProfileBinding!!.imgDoctorProfile)
//            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()


            } else*/
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == -1) {
                    fileUri = data!!.data
                    filePath = fileUri!!.path
                    // tvItemPath.setText(filePath)
                    try {
                        val file = FileUtil.from(this.requireActivity(), fileUri!!)
                        certificatefileFile = file
                        Log.d(
                            "file",
                            "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists()
                        )
                        /*fragmentDoctorEditProfileBinding?.textViewCertificate?.setText(
                        getFileName(this!!.activity!!, fileUri!!)
                    )*/
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }


    private fun submitDetailsForEditProfile() {
        with(fragmentDoctorEditProfileBinding!!) {
            if (fragmentEditDoctorProfileViewModel?.appSharedPref?.loginUserId != null && checkValidationForRegStepOne()) {
                baseActivity?.showLoading()
                baseActivity?.hideKeyboard()


                var startTime: RequestBody? = null
                var endTime: RequestBody? = null

                val user_id = fragmentEditDoctorProfileViewModel?.appSharedPref?.loginUserId!!
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val first_name = ediitextFirstName.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val last_name = ediitextLastName.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val email = ediitextEmail.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val mobile_number = ediitextMobileNumber.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val dob = textViewDOB.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val gender = selectedGender
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                /*val qualification = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextQualification.text?.trim().toString()
                )
                val passingYear = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    textViewPassingYear.text?.trim().toString()
                )
                val institute = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextInstitute.text?.trim().toString()
                )*/
                var qualificationStr = ""
                var passingYearStr = ""
                var instituteStr = ""
                if (certificateListAdapter?.qualificationDataList != null && certificateListAdapter?.qualificationDataList!!.size > 0) {
                    val tempList: ArrayList<com.rootscare.data.model.response.caregiver.profileresponse.QualificationDataItem> =
                        ArrayList()
                    for (i in 0 until certificateListAdapter?.qualificationDataList?.size!!) {
                        if (!certificateListAdapter?.qualificationDataList!![i].isOldData) {
                            tempList.add(certificateListAdapter?.qualificationDataList!![i])
                        }
                    }
                    for (i in 0 until tempList.size) {
                        if (i == 0) {
                            qualificationStr += tempList[i].qualification!!
                            passingYearStr += tempList[i].passingYear!!
                            instituteStr += tempList[i].institute!!
                        } else {
                            qualificationStr += ",${tempList[i].qualification!!}"
                            passingYearStr += ",${tempList[i].passingYear!!}"
                            instituteStr += ",${tempList[i].institute!!}"
                        }

                    }
                    /*for (i in 0 until certificateListAdapter?.qualificationDataList!!.size) {
                        if (i == 0 ) {
                            qualificationStr += certificateListAdapter?.qualificationDataList!![i].qualification!!
                            passingYearStr += certificateListAdapter?.qualificationDataList!![i].passingYear!!
                            instituteStr += certificateListAdapter?.qualificationDataList!![i].institute!!
                        } else {
                            qualificationStr += ",${certificateListAdapter?.qualificationDataList!![i].qualification!!}"
                            passingYearStr += ",${certificateListAdapter?.qualificationDataList!![i].passingYear!!}"
                            instituteStr += ",${certificateListAdapter?.qualificationDataList!![i].institute!!}"
                        }

                    }*/
                }
                var qualification: RequestBody? = null
                if (!TextUtils.isEmpty(qualificationStr.trim())) {
                    qualification = qualificationStr
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
                var passingYear: RequestBody? = null
                if (!TextUtils.isEmpty(qualificationStr.trim())) {
                    passingYear = passingYearStr
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
                var institute: RequestBody? = null
                if (!TextUtils.isEmpty(qualificationStr.trim())) {
                    institute = instituteStr
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
                val description = ediitextDescription.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val experience = ediitextExperience.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val availableTime = ediitexAvailableTime.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val fees = ediitextFees.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val department = departmentId.trim()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())

                startTime = edtNurseFromTime.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                endTime = edtNurseToTime.text?.trim().toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                var imageMultipartBody: MultipartBody.Part? = null
                var image: RequestBody?
                if (imageFile != null) {
                    image = imageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    imageMultipartBody = MultipartBody.Part.createFormData("image", imageFile?.name, image)
//                } else {
//                    image = RequestBody.create(MediaType.parse("multipart/form-data"), "")
//                    imageMultipartBody = MultipartBody.Part.createFormData("image", "", image)
                }

                var certificateMultipartBody: ArrayList<MultipartBody.Part>? = null
                if ((certificateListAdapter?.qualificationDataList != null && certificateListAdapter?.qualificationDataList?.size!! > 0)) {
                    val tempList: ArrayList<com.rootscare.data.model.response.caregiver.profileresponse.QualificationDataItem> =
                        ArrayList()
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

                fragmentEditDoctorProfileViewModel?.apiHitForUpdateProfileWithProfileAndCertificationImage(
                    user_id,
                    first_name,
                    last_name,
                    email,
                    mobile_number,
                    dob,
                    gender,
                    qualification,
                    passingYear,
                    institute,
                    description,
                    experience,
                    availableTime,
                    fees,
                    department,
                    startTime ?: "".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    endTime ?: "".toRequestBody("multipart/form-data".toMediaTypeOrNull()),

                    imageMultipartBody,
                    certificateMultipartBody
                )
            }
        }
    }

    //Start of All field Validation
    private fun checkValidationForRegStepOne(): Boolean {
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextFirstName?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your first name!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextLastName?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your last name!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextEmail?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your email!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val email = fragmentDoctorEditProfileBinding?.ediitextEmail?.text?.toString()
        if (!email!!.matches(emailPattern.toRegex())) {
//            activityLoginBinding?.edtEmailOrPhone?.setError("Please enter valid email id")
            Toast.makeText(activity, "Please enter valid email id!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.textViewDOB?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please select your dob!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (TextUtils.isEmpty(selectedGender.trim())) {
            Toast.makeText(activity, "Please select gender!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextMobileNumber?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your Mobile Number!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextDescription?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your description!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextExperience?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your experience!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitexAvailableTime?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter available time!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextFees?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your fees!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(departmentId.trim())) {
            Toast.makeText(activity, "Please enter your department!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        return true
    }


    private fun addCertificatePortionsSetUp() {
        with(fragmentDoctorEditProfileBinding!!) {
            recyclerViewCertificates.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
            certificateListAdapter = PhysiotherapyCertificateListAdapter(requireActivity())
            recyclerViewCertificates.isNestedScrollingEnabled = false
            recyclerViewCertificates.setHasFixedSize(false)
            recyclerViewCertificates.adapter = certificateListAdapter
            val fragment = CertificateUploadFragmentCaregiver.newInstance()
            fragment.listener = object : CertificateUploadFragmentCaregiver.PassDataCallBack {
                override fun onPassData(data: com.rootscare.data.model.response.caregiver.profileresponse.QualificationDataItem) {
                    certificateListAdapter?.qualificationDataList?.add(data)
                    certificateListAdapter?.notifyDataSetChanged()
                }

            }
            imageViewaddCertificate.setOnClickListener {
                baseActivity?.openDialogFragment(fragment)
            }
        }
    }

    private fun openCamera() {
//        mCameraIntentHelper!!.startCameraIntent()
//        CropImage.activity()
//            .setCropShape(
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    CropImageView.CropShape.RECTANGLE
//                } else {
//                    CropImageView.CropShape.OVAL
//                }
//            )
////            .setCropShape(CropImageView.CropShape.OVAL)
//            .setInitialCropWindowPaddingRatio(0F)
//            .setAspectRatio(1, 1)
//            .setGuidelines(CropImageView.Guidelines.ON)
//            .setActivityTitle("Crop")
//            .setOutputCompressQuality(10)
//            .start(requireActivity())
    }
}

