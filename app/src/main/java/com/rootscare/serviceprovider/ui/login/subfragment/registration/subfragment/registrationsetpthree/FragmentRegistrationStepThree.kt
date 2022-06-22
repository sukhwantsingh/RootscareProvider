package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.data.model.response.hospital.HospitalHDepartmentListResponse
import com.rootscare.data.model.response.hospital.HospitalResultItem
import com.rootscare.data.model.response.registrationresponse.RegistrationResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.model.RegistrationModel
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentRegistrationStepthreeBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.utils.ManagePermissions

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class FragmentRegistrationStepThree : BaseFragment<FragmentRegistrationStepthreeBinding, FragmentRegistrationStepThreeViewModel>(),
    FragmentRegistrationStepThreeNavigator {
    private var fragmentRegistrationStepthreeBinding: FragmentRegistrationStepthreeBinding? = null
    private var fragmentRegistrationStepThreeViewModel: FragmentRegistrationStepThreeViewModel? = null
    var departmentList: ArrayList<HospitalResultItem?>? = null
    var departmentId = ""
    var departTitle = ""
    private val REQUEST_CAMERA = 3
    var SELECT_FILE: Int = 4
    var REQUEST_ID_MULTIPLE_PERMISSIONS = 123
    var requested = false
    var imageFile: File? = null
    var thumbnail: Bitmap? = null
    var bytes: ByteArrayOutputStream? = null
    private var imageSelectionModel: AddlabTestImageSelectionModel? = null
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration_stepthree
    override val viewModel: FragmentRegistrationStepThreeViewModel
        get() {
            fragmentRegistrationStepThreeViewModel = ViewModelProviders.of(this).get(FragmentRegistrationStepThreeViewModel::class.java)
            return fragmentRegistrationStepThreeViewModel as FragmentRegistrationStepThreeViewModel
        }

    companion object {
        val TAG = FragmentRegistrationStepThree::class.java.simpleName
        fun newInstance(): FragmentRegistrationStepThree {
            val args = Bundle()
            val fragment = FragmentRegistrationStepThree()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationStepThreeViewModel!!.navigator = this

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationStepthreeBinding = viewDataBinding
/*
        println("onViewCreated FragmentRegistrationStepThree")

        if (AppData.registrationModelData?.userType == "hospital") {
            fragmentRegistrationStepthreeBinding?.edtLicense?.visibility = View.VISIBLE
            fragmentRegistrationStepthreeBinding?.txtRegDepartment?.visibility = View.GONE
            fragmentRegistrationStepthreeBinding?.edtRegFees?.visibility = View.GONE
            fragmentRegistrationStepthreeBinding?.rlLicenseImage?.visibility = View.VISIBLE
        } else {
            fragmentRegistrationStepthreeBinding?.txtRegDepartment?.visibility = View.VISIBLE
            fragmentRegistrationStepthreeBinding?.edtRegFees?.visibility = View.VISIBLE
            fragmentRegistrationStepthreeBinding?.edtLicense?.visibility = View.GONE
            fragmentRegistrationStepthreeBinding?.rlLicenseImage?.visibility = View.GONE

//            if (isNetworkConnected) {
//                baseActivity?.showLoading()
//                val commonUserIdRequest = CommonHospitalId()
//                commonUserIdRequest.hospital_id = fragmentRegistrationStepThreeViewModel?.appSharedPref?.loginUserId
//                fragmentRegistrationStepThreeViewModel?.apidepartmentlist(commonUserIdRequest)
//
//            } else {
//                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
//            }
        }

        fragmentRegistrationStepthreeBinding?.imageViewBack?.setOnClickListener {
            (activity as LoginActivity).onBackPressed()
        }

        fragmentRegistrationStepthreeBinding?.llRegImageSelect?.setOnClickListener {
            if (checkAndRequestPermissionsTest()) {
                captureImage()
            }
        }

        fragmentRegistrationStepthreeBinding?.btnRooscareServiceproviderRegistrationSubmit?.setOnClickListener {

            CommonDialog.showDialog(this.requireActivity(), object : DialogClickCallback {
                override fun onConfirm() {
                    if (checkValidationForRegStepThree()) {
                        AppData.registrationModelData?.description =
                            fragmentRegistrationStepthreeBinding?.edtRegDescription?.text?.toString()
                        AppData.registrationModelData?.experience =
                            fragmentRegistrationStepthreeBinding?.edtRegExperience?.text?.toString()
                        AppData.registrationModelData?.availableTime =
                            fragmentRegistrationStepthreeBinding?.edtRootscareRegistrationAvailableTime?.text?.toString()
                        AppData.registrationModelData?.fees = fragmentRegistrationStepthreeBinding?.edtRegFees?.text?.toString()
                        AppData.registrationModelData?.department =
                            fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text?.toString()
                        AppData.registrationModelData?.license =
                            fragmentRegistrationStepthreeBinding?.edtLicense?.text?.toString()
                        AppData.registrationModelData?.licenseFile = imageFile
                        AppData.registrationModelData?.licenseName = fragmentRegistrationStepthreeBinding?.txtImageSelectName?.text?.toString()
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            registrationApiCall()
                        } else {
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onDismiss() {
                }

            }, "Confirmation", "Are you sure to register?")

        }

        fragmentRegistrationStepthreeBinding?.txtRegDepartment?.setOnClickListener {
            CommonDialog.showDialogForDepartmentDropDownList(this.requireActivity(), departmentList, "Select Department", object :
                OnDepartmentDropDownListItemClickListener {
                override fun onConfirm(departmentList: ArrayList<HospitalResultItem?>?) {
                    departTitle = ""
                    departmentId = ""
                    if (departmentList?.size != null && departmentList.size > 0) {
                        var p = 0
                        for (i in 0 until departmentList.size) {
                            if (departmentList[i]?.isChecked.equals("true")) {
                                if (p == 0) {
                                    departTitle = departmentList[i]?.title!!
                                    departmentId = departmentList[i]?.id!!
                                    p++

//                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
//                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
                                } else {
                                    departTitle = departTitle + "," + departmentList[i]?.title
                                    departmentId = departmentId + "," + departmentList[i]?.id

//                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
//                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
                                }
                            }
                        }
                    }

                    Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- $departTitle")
                    Log.d(FragmentLogin.TAG, "--SELECT ID:-- $departmentId")


                    fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text = departTitle
                }


            })
        }
        */
    }

    private fun checkValidationForRegStepThree(): Boolean {
        if (TextUtils.isEmpty(fragmentRegistrationStepthreeBinding?.edtRegDescription?.text?.toString()?.trim())) {
            Toast.makeText(activity, "Please enter your description!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationStepthreeBinding?.edtRegExperience?.text?.toString().equals("")) {
            Toast.makeText(activity, "Please enter your experience!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationStepthreeBinding?.edtRootscareRegistrationAvailableTime?.text?.toString().equals("")) {
            Toast.makeText(activity, "Please enter your available time in hour!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (AppData.registrationModelData?.userType != "hospital") {
            if (fragmentRegistrationStepthreeBinding?.edtRegFees?.text?.toString().equals("")) {
                Toast.makeText(activity, "Please enter your fees!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
                return false
            }

            if ((activity as LoginActivity).isDepartmentMandatory!! && fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text?.toString()
                    ?.trim().equals("")
            ) {
                Toast.makeText(activity, "Please select your department!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
                return false
            }
        } else {
            if (fragmentRegistrationStepthreeBinding?.edtLicense?.text?.toString()
                    ?.trim().equals("")
            ) {
                Toast.makeText(activity, "Please enter your license no!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
                return false
            }
            if (fragmentRegistrationStepthreeBinding?.txtImageSelectName?.text?.toString().equals("")) {
                Toast.makeText(activity, "Please select your license image!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
                return false
            }
        }
        return true
    }

    override fun successRegistrationResponse(registrationResponse: RegistrationResponse?) {
        baseActivity?.hideLoading()
        if (registrationResponse?.code.equals("200")) {
            //   Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()

            CommonDialog.showDialogForSingleButton(requireActivity(), object : DialogClickCallback {
                override fun onConfirm() {
                    AppData.registrationModelData = RegistrationModel()
                    AppData.boolSForRefreshLayout = true
                    (activity as LoginActivity?)!!.setCurrentItem(0, true)
                }

                override fun onDismiss() {
                }

            }, "Registration", registrationResponse?.message!!)

        } else {
            Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successDepartmentListResponse(departmentListResponse: HospitalHDepartmentListResponse?) {
        baseActivity?.hideLoading()
        if (departmentListResponse?.code.equals("200")) {
            if (departmentListResponse?.result != null && departmentListResponse.result.size > 0) {
                departmentList = ArrayList()
                departmentList = departmentListResponse.result
            }
        }
    }

    override fun errorRegistrationResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    //
    private fun registrationApiCall() {
        baseActivity?.showLoading()

        val user_type = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppData.registrationModelData?.userType?.toLowerCase(Locale.getDefault()).toString()
        )
        val first_name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.firstName.toString())
        val last_name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.lastName.toString())
        val email = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.emailAddress.toString())
        val mobile_number = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppData.registrationModelData?.mobileNumber.toString()
        )
        val dob = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.dob.toString())
        val gender = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.gender.toString())
        val password = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.password.toString())
        val confirm_password = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppData.registrationModelData?.confirmPassword.toString()
        )

        var imageMultipartBody: MultipartBody.Part? = null
        if (AppData.registrationModelData?.imageFile != null) {
            val image = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.imageFile!!)
            imageMultipartBody = MultipartBody.Part.createFormData("image", AppData.registrationModelData?.imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
        }
        var licenseMultipartBody: MultipartBody.Part? = null
        if (AppData.registrationModelData?.licenseFile != null) {
            val image = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.licenseFile!!)
            licenseMultipartBody = MultipartBody.Part.createFormData("licence_image", AppData.registrationModelData?.licenseFile?.name, image)
        }

        /*var certificateMultipartBody:MultipartBody.Part?=null
        if (AppData?.registrationModelData?.certificateFile != null) {
            val certificate = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.certificateFile)
            certificateMultipartBody = MultipartBody.Part.createFormData("certificate", AppData?.registrationModelData?.certificateFile?.name, certificate)

        }*/
        val qualification = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppData.registrationModelData?.qualification.toString()
        )
        val passing_year = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppData.registrationModelData?.passingYear.toString()
        )
        val institute = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.institude.toString())
        val description = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.description.toString())
        val experience = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.experience.toString())
        val available_time = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            AppData.registrationModelData?.availableTime.toString()
        )
        val fees = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.fees.toString())
        val license = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), AppData.registrationModelData?.license.toString())
        var department: RequestBody? = null
        if ((activity as LoginActivity).isDepartmentMandatory!!) {
            department = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), departmentId)
        }
        val certificateMultipartBody: MutableList<MultipartBody.Part> = ArrayList()
        if (AppData.registrationModelData?.qualificationDataList != null && AppData.registrationModelData?.qualificationDataList?.size!! > 0) {
            var index = 0
            for (item in AppData.registrationModelData?.qualificationDataList!!) {
                if (item.certificateFileTemporay != null) {
                    val certificate = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), item.certificateFileTemporay!!)
                    certificateMultipartBody.add(
                        MultipartBody.Part.createFormData("certificate[]",
                            item.certificateFileTemporay?.name, certificate)
                    )
                    index++
                }
            }
        }
        if (AppData.registrationModelData?.userType == "hospital") {
            fragmentRegistrationStepThreeViewModel?.apiHospitalRegistration(
                user_type, first_name, email, mobile_number, password, confirm_password, description,
                experience, available_time, license, imageMultipartBody, licenseMultipartBody
            )
        } else {
            fragmentRegistrationStepThreeViewModel?.apieditpatientprofilepersonal(
                user_type, first_name, last_name, email, mobile_number, dob, gender, password, confirm_password,
                imageMultipartBody, certificateMultipartBody, qualification, passing_year, institute, description,
                experience, available_time, fees, department
            )
        }

    }

    override fun onResume() {
        super.onResume()
        if (AppData.boolSForRefreshLayout) {
            fragmentRegistrationStepthreeBinding?.edtRegDescription?.setText("")
            fragmentRegistrationStepthreeBinding?.edtRegExperience?.setText("")
            fragmentRegistrationStepthreeBinding?.edtRootscareRegistrationAvailableTime?.setText("")
            fragmentRegistrationStepthreeBinding?.edtRegFees?.setText("")
            fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text = ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("requestCode $requestCode resultCode $resultCode data $data")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                try {
                    onCaptureImageResult(data!!)
                } catch (e: Exception) {
                    println("Exception===>$e")
                }

            } else if (requestCode == SELECT_FILE) {
                if (data != null) {
                    onSelectFromGalleryResult(data)
                }

            }

        }
    }

// End of Image Selection and Save Image Path


    // Receive the permissions request result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    captureImage()
                    Toast.makeText(activity, "Permissions granted.", Toast.LENGTH_SHORT).show()
                    //  toast("Permissions granted.")
                } else {
                    Toast.makeText(activity, "Permissions denied.", Toast.LENGTH_SHORT).show()
                    // toast("Permissions denied.")
                }
                return
            }
        }
    }

    //image upload*****************************************************
    private fun checkAndRequestPermissionsTest(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            val permissionText = " "
            val permissionCamera = ContextCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.CAMERA
            )
            val permissionWriteExternalStorage = ContextCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val listPermissionsNeeded: MutableList<String> =
                java.util.ArrayList()
            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (listPermissionsNeeded.isNotEmpty()) {
                requested = true
                requestPermissions(
                    listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS
                )
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
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder =
            AlertDialog.Builder(context)
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
        if (!TextUtils.isEmpty(imageFile?.path) && File(imageFile?.path)
                .exists()
        ) {
//            CropImage.activity(Uri.fromFile(File(imageFile?.path)))
//                .setCropShape(
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) CropImageView.CropShape.RECTANGLE
//                    else (CropImageView.CropShape.OVAL)
//                )
//                .setInitialCropWindowPaddingRatio(0F)
//                .setAspectRatio(1, 1)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setActivityTitle("Crop")
//                .setOutputCompressQuality(10)
//                .start(requireActivity())
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        thumbnail = data.extras!!["data"] as Bitmap?
        bytes = ByteArrayOutputStream()

        thumbnail?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        imageFile = File(
            activity?.externalCacheDir!!.absolutePath,
            System.currentTimeMillis().toString() + ".jpg"
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
        imageFile = File(
            activity?.externalCacheDir!!.absolutePath,
            System.currentTimeMillis().toString() + ".jpg"
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
}