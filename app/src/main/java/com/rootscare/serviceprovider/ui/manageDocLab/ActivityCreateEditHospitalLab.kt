package com.rootscare.serviceprovider.ui.manageDocLab

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import com.rootscare.model.ModelServiceFor
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewCreateEditLabUnderHospitalBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.manageDocLab.fragments.FragmentManageHospitalDocsLab
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileNavigator
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileViewModel
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile.Companion.IS_PROFILE_UPDATE_
import com.rootscare.serviceprovider.utilitycommon.*
import com.rootscare.utils.ManagePermissions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.util.HashMap

class ActivityCreateEditHospitalLab : BaseActivity<LayoutNewCreateEditLabUnderHospitalBinding, FragmentNursesProfileViewModel>(),
    FragmentNursesProfileNavigator {

    private val PICKFILE_RESULT_CODE = 4

    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var imageFile: File? = null
    private var imgMohLicence: File? = null
    private var imgRegistration: File? = null

    private var binding: LayoutNewCreateEditLabUnderHospitalBinding? = null
    private var mViewModel: FragmentNursesProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.layout_new_create_edit_lab_under_hospital
    override val viewModel: FragmentNursesProfileViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(
                FragmentNursesProfileViewModel::class.java
            )
            return mViewModel as FragmentNursesProfileViewModel
        }

    companion object {
        var needToCreateLab = true
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

        managePermissions = ManagePermissions(this,listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), PermissionsRequestCode)

        initViews()
        callingAPi()
    }


    private fun callingAPi() {
        if(needToCreateLab) {
            binding?.tvhEd?.text = getString(R.string.create_lab)
            binding?.rlAccountDown?.visibility =  View.GONE
            binding?.rlTopImgEmail?.visibility =  View.GONE
            binding?.cnsCba?.visibility =  View.GONE

            binding?.tilPwd?.visibility = View.VISIBLE
            binding?.tilCnfPwd?.visibility = View.VISIBLE
            binding?.tvnD1?.visibility = View.VISIBLE
            binding?.tvnD2?.visibility = View.VISIBLE

        } else {
            binding?.tvhEd?.text = getString(R.string.edit_lab)
            binding?.rlAccountDown?.visibility = View.GONE
            binding?.rlTopImgEmail?.visibility =  View.VISIBLE
            binding?.cnsCba?.visibility =  View.VISIBLE

            binding?.tilPwd?.visibility = View.GONE
            binding?.tilCnfPwd?.visibility = View.GONE
            binding?.tvnD1?.visibility = View.GONE
            binding?.tvnD2?.visibility = View.GONE

         //  fetchProfileData()
        }
        logModel?.let {
            binding?.run {
                tvWorkFrom.text = it.result?.work_area.orEmpty()
                edtCc.setText(it.result?.country_code.orEmpty())
            }
        }

    }

    private lateinit var noAttchQualiTv: TextView

    private fun initViews() {
        binding?.tvhAcname?.text = HospitalUnder.LAB.get()
        binding?.run {
           imgProfile.setOnClickListener {
            //    openCamera()
                 uploadForType = 3
                if (checkAndRequestPermissionsTest()) {
                  //  pickupImage(PICKFILE_RESULT_CODE)
                captureImage()
                }
           }
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

            profileImageCamera.setOnClickListener { imgProfile.performClick() }
            btnSubmit.setOnClickListener { submitDetailsForEditProfile() }
            tvhDisableLab.setOnClickListener { disableDeleteDoc(UserDisableType.DISABLE.get(), getString(R.string.sure_to_disable_this_lab)) }
            tvhDelLab.setOnClickListener { disableDeleteDoc(UserDisableType.DELETE.get(), getString(R.string.sure_to_delete_this_lab))   }

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

    private fun disableDeleteDoc(typeToHit: String, desc:String) {
        CommonDialog.showDialog(this@ActivityCreateEditHospitalLab, object :
            DialogClickCallback {
            override fun onConfirm() {
                if (isNetworkConnected) {
                    showLoading()
                    val jsonObject = JsonObject().apply {
                        addProperty("user_id", ActivityCreateEditHospitalDocs.docId)
                    }
                 //   val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                 // when {
                    //    typeToHit.equals(UserDisableType.DISABLE.get(),true) -> mViewModel?.disableDoc(body)
                     //   typeToHit.equals(UserDisableType.DELETE.get(),true) -> mViewModel?.deleteDoc(body)
                      //  else -> hideLoading()
                 //  }

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
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("id", mViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.apiHospitalDoctorProfile(body)
        } else {
            showToast(getString(R.string.network_unavailable))
        }
    }

    override fun onSuccessUserProfile(response: ModelUserProfile?) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            response?.result?.let {
                bindNewLayout(it)
            } ?: run { showToast(response?.message ?: "") }
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

    override fun onSuccessEditProfile(response: ModelUserProfile?) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) FragmentManageHospitalDocsLab.NEED_REFRESH_LAB = true
        CommonDialog.showDialogForSingleButton(this, object : DialogClickCallback {
            override fun onConfirm() { /* onBackPressed() */ } }, getString(R.string.profile), response?.message ?: "")
    }

    override fun successRegistrationResponse(response: RegistrationResponse?) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            FragmentManageHospitalDocsLab.NEED_REFRESH_LAB = true
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

    override fun onSuccessCommon(response: CommonResponse?) {
        hideLoading()
        showToast(response?.message ?: "")
        if (response?.code.equals(SUCCESS_CODE)) {
            FragmentManageHospitalDocsLab.NEED_REFRESH_LAB = true
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindNewLayout(response: ModelUserProfile.Result?) {
        binding?.run {
            response?.let {

                if (it.image.isNullOrBlank().not()) imgProfile.setCircularRemoteImage(it.image)
                tvUsername.text = (it.first_name + " " + it.last_name).trim()
                tvhHospName.text = it.email  // hospital name

                edtRegLabname.setText((it.first_name + " " + it.last_name).trim())
                edtRegEmailaddress.setText(it.email)
                edtRegPhonenumber.setText(it.phone_number.orEmpty())

                tvWorkFrom.text = it.work_area.orEmpty()
                edtCc.setText(it.country_code.orEmpty())

                layoutHospital.edtHospMoh.setText(it.hosp_moh_lic_no)
                layoutHospital.tvHospNoAttachmentPhotocopy.text = it.moh_lic_image

                layoutHospital.edtHospRegistrationNo.setText(it.hosp_reg_no)
                layoutHospital.tvHospAttachementCertificate.text = it.hosp_reg_image

                edtAbout.setText(it.description)
                true
            }
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        hideLoading()
        if (throwable?.message != null) {
           showToast(getString(R.string.something_went_wrong))
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


    private var uploadForType = -1 // ie. are 0-id_num,1-qualification,2-SCFHS Registration ID , 3- image

    private fun displayCertificateName(fFile: File) {
        when (uploadForType) {
            0 -> {
                imgMohLicence = fFile
                binding?.layoutHospital?.tvHospNoAttachmentPhotocopy?.text = fFile.name
            }
            1 -> {
                imgRegistration = fFile
                binding?.layoutHospital?.tvHospAttachementCertificate?.text = fFile.name
            }
            3 -> {
                imageFile = fFile
                binding?.imgProfile?.setCircularLocalImage(fFile.absolutePath ?: "")
            }
            else ->Unit
        }

    }


    private fun submitDetailsForEditProfile() {
        binding?.run {
            if (checkValidationForRegStepOne()) {

                val uId = mViewModel?.appSharedPref?.loginUserId?.asReqBody()
                val uType = mViewModel?.appSharedPref?.loginUserType?.asReqBody()

                val labName = binding?.edtRegLabname?.text?.toString().orEmpty()
                val email = binding?.edtRegEmailaddress?.text?.toString().orEmpty()
                var mobNum = binding?.edtRegPhonenumber?.text?.toString().orEmpty()
                val cc_ = binding?.edtCc?.text?.toString().orEmpty()
                mobNum = cc_ + mobNum

                val about = edtAbout.text?.trim().toString().asReqBody()
                val pwd = binding?.edtRegPassword?.text?.toString()?.asReqBody()
                val workArea = binding?.tvWorkFrom?.text?.toString().orEmpty()

                var hspMohLicNum = ""
                var hospRegId = ""

                binding?.layoutHospital?.run {
                    hspMohLicNum = edtHospMoh.text.toString()
                    hospRegId = edtHospRegistrationNo.text.toString()
                }


                var uImage: MultipartBody.Part? = null
                imageFile?.let {
                    val nmg = imageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    uImage = MultipartBody.Part.createFormData("image", imageFile?.name, nmg)
                }
                var mohLicImg: MultipartBody.Part? = null
                imgMohLicence?.let {
                    val idnmg = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgMohLicence!!)
                    mohLicImg = MultipartBody.Part.createFormData("id_image", imgMohLicence?.name, idnmg)
                }
                var regImg: MultipartBody.Part? = null
                imgRegistration?.let {
                    val qmg = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgRegistration!!)
                    regImg = MultipartBody.Part.createFormData("certificate", imgRegistration?.name, qmg)
                }

//                showLoading()
//                 hideKeyboard()
//                if(needToCreateLab){
//                    mViewModel?.apiSignup(
//                        idImg, qlImg, scfhsImg, uType ?: "".asReqBody(), flName,
//                        email, mobNum, dob, selectedGender.asReqBody(),
//                        pwd ?: "".asReqBody(), idNum.asReqBody(), spec.asReqBody(),
//                        hQualification.asReqBody(), expYears.asReqBody(), scfhsRegistrationId.asReqBody()
//                    )
//                } else {
//                    mViewModel?.apiSubmitEditProfile(
//                        uId, uType, flName, email, mobNum, dob, gen,
//                        hQualification.asReqBody(), idNum.asReqBody(),
//                        spec.asReqBody(), expYears.asReqBody(),
//                        scfhsRegistrationId.asReqBody(), about, uImage, idImg, qlImg, scfhsImg
//                    )
//                }


            }

        }
    }

    private fun checkValidationForRegStepOne(): Boolean {
        val mLt = binding
        return when {
            binding?.edtRegLabname?.text.isNullOrBlank() -> {
              binding?.edtRegLabname?.error = getString(R.string.enter_lab_name)
              binding?.edtRegLabname?.requestFocus()
              false
            }

            binding?.edtRegEmailaddress?.text.isNullOrBlank() -> {
                binding?.edtRegEmailaddress?.error = getString(R.string.enter_email_id)
                binding?.edtRegEmailaddress?.requestFocus()
                false
            }
            binding?.tvWorkFrom?.text.isNullOrBlank() -> {
                showToast(getString(R.string.provide_service_area))
                false
            }
            !binding?.edtRegEmailaddress?.text?.toString()!!.matches(emailPattern.toRegex()) -> {
                binding?.edtRegEmailaddress?.error = getString(R.string.enter_valid_email)
                binding?.edtRegEmailaddress?.requestFocus()
                false
            }
            !isValidPassword(binding?.edtRegPhonenumber?.text?.toString()!!) -> {
                binding?.edtRegPhonenumber?.error = getString(R.string.enter_mobile_number)
                binding?.edtRegPhonenumber?.requestFocus()
                false
            }

            binding?.edtRegPassword?.text.isNullOrBlank()  && needToCreateLab-> {
                binding?.edtRegPassword?.error = getString(R.string.enter_password)
                binding?.edtRegPassword?.requestFocus()
                false
            }
            ((binding?.edtRegPassword?.text?.toString()?.length ?: 0) < 8) && needToCreateLab -> {
                binding?.edtRegPassword?.error = getString(R.string.must_be_at_least_8_characters)
                binding?.edtRegPassword?.requestFocus()
                false
            }
            binding?.edtRegConfirmPassword?.text.isNullOrBlank()  && needToCreateLab -> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.enter_cnf_pwd)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }
            ((binding?.edtRegConfirmPassword?.text?.toString()?.length ?: 0) < 8)  && needToCreateLab-> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.must_be_at_least_8_characters)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }
            (!binding?.edtRegPassword?.text?.toString().equals(binding?.edtRegConfirmPassword?.text?.toString()))  && needToCreateLab -> {
                binding?.edtRegConfirmPassword?.error = getString(R.string.pwd_not_matched)
                binding?.edtRegConfirmPassword?.requestFocus()
                false
            }
            binding?.layoutHospital?.edtHospMoh?.text.isNullOrBlank() -> {
                binding?.layoutHospital?.edtHospMoh?.error = getString(R.string.provide_moh_licence_no)
                binding?.layoutHospital?.edtHospMoh?.requestFocus()
                false
            }
              (null == imgMohLicence) && needToCreateLab -> {
                showToast(getString(R.string.provide_moh_proof))
                false
            }
            binding?.layoutHospital?.edtHospRegistrationNo?.text.isNullOrBlank() -> {
                binding?.layoutHospital?.edtHospRegistrationNo?.error = getString(R.string.provide_registration_number)
                binding?.layoutHospital?.edtHospRegistrationNo?.requestFocus()
                false
            }
            (null == imgRegistration) && needToCreateLab  -> {
                showToast(getString(R.string.provide_registration_proof))
                false
            }

            else -> true
        }
    }

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