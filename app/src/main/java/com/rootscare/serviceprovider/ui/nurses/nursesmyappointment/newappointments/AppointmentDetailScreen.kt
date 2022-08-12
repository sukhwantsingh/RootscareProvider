package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.myfilepickesexample.FileUtil
import com.rootscare.data.model.request.videoPushRequest.VideoPushRequest
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.videoPushResponse.VideoPushResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter.AdapterPaymentSplitting
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter.AdapterReportUploadeds
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter.OnLabReportsCallback
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelAppointmentDetails
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelLabReports
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.OnBottomSheetCallbacks
import com.rootscare.serviceprovider.utilitycommon.*
import com.rootscare.twilio.VideoCallActivity
import com.rootscare.utils.ManagePermissions
import kotlinx.android.synthetic.main.layout_new_appointment_details.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

private const val IMAGE_DIRECTORY = "/demonuts"
class AppointmentDetailScreen : BaseActivity<LayoutNewAppointmentDetailsBinding, ViewModelMyAppointments>(),
    AppointmentNavigator {
    private var binding: LayoutNewAppointmentDetailsBinding? = null
    private var mViewModel: ViewModelMyAppointments? = null

    companion object{
        var appointmentId: String? = null
    }

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.layout_new_appointment_details
    override val viewModel: ViewModelMyAppointments
        get() {
            mViewModel = ViewModelProviders.of(this).get(ViewModelMyAppointments::class.java)
            return mViewModel as ViewModelMyAppointments
        }

    private lateinit var mAdapterPayment: AdapterPaymentSplitting

    private var detailModel :ModelAppointmentDetails.Result? = null
    private var accRejStatus = ""
    private var patientContact = ""
    private var myExoPlayer: MyExoPlayer? = null

    private lateinit var managePermissions: ManagePermissions
    private val PermissionsRequestCode = 123

    private val GALLERY = 1
    private val CAMERA = 2
    val PICKFILE_RESULT_CODE = 4
    private var fileUri: Uri? = null
    private var filePath: String? = null

    var imageFile: File? = null
    private var prescriptionimage: RequestBody? = null
    private var imageMulitpart: MultipartBody.Part? = null
    private var hospitalId = ""

    private val adapterReports: AdapterReportUploadeds by lazy { AdapterReportUploadeds() }

    var mBsReportsUploading : BSUploadReportSection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapterPayment = AdapterPaymentSplitting(this)
        mViewModel?.navigator = this
        binding = viewDataBinding
        initViews()
        fetchTasksDetailApi()
    }

    private fun initViews() {
        binding?.apply {
            topToolbar.tvHeader.text = getString(R.string.appointment_details)
            topToolbar.btnBack.setOnClickListener { finish() }
            rvPayments.adapter = mAdapterPayment
            rvReports.adapter = adapterReports
            rvReports.addItemDecoration(DividerItemDecoration(this@AppointmentDetailScreen,LinearLayoutManager.VERTICAL))
            adapterReports.mCallback = object: OnLabReportsCallback{
                override fun onDownloads(mFileName: String) {
                    initializeDownloadManager()
                    downloadFile(mFileName)
                }
            }
            tvhCall.setOnClickListener { openDialer(patientContact) }
            tvhOpenGmap.setOnClickListener { openGoogleMapWithDirectNavigationStart(mPatientLat,mPatientLng) }

            ivSendOtp.setOnClickListener {
                val ot = edtOtp.text.toString().trim()
                if (ot.isBlank()) {
                   showToast("Please provide OTP")
                } else {
                    apiMarkAsComplete(ot)
                }
            }
            btnAccept.setOnClickListener {
                if(hospitalId.isNotBlank() && mViewModel?.appSharedPref?.loginUserType.equals("lab",ignoreCase = true)){
                    showToast("Not yet implemented!")
                    return@setOnClickListener
                }

                CommonDialog.showDialog(this@AppointmentDetailScreen, object : DialogClickCallback {
                    override fun onConfirm() {
                        accRejStatus = TransactionStatus.ACCEPTED.get()
                         apiRejAccept(appointmentId ?: "", "Accept")
                    }   }, getString(R.string.confirmation), getString(R.string.do_realy_want_to_accept_appointment))
            }
            ibcross.setOnClickListener {
                if(hospitalId.isNotBlank() && mViewModel?.appSharedPref?.loginUserType.equals("lab",ignoreCase = true)){
                    showToast("Not yet implemented!")
                    return@setOnClickListener
                }

                CommonDialog.showDialog(this@AppointmentDetailScreen, object : DialogClickCallback {
                    override fun onConfirm() {
                        accRejStatus = TransactionStatus.CANCELLED.get()
                     apiRejAccept(appointmentId ?: "", "Reject")
                    }
                }, getString(R.string.confirmation), getString(R.string.do_realy_want_to_reject_appointment))
            }
            imgPatientArrowDown.setOnClickListener {
                imgPatientArrowDown.rotation = if(grpPatientDetails.isShown){
                   grpPatientDetails.visibility = View.GONE; 0f
                } else {
                   grpPatientDetails.visibility = View.VISIBLE; 180f
                }

            }
       }
    }

    private fun fetchTasksDetailApi() {
        if (isNetworkConnected) {
            showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("id", appointmentId?:"")
            }
         val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.apiAppointmentDetails(body)
//           mViewModel?.apiAppointmentDetails((mViewModel?.appSharedPref?.loginUserType?:"").asReqBody(), (appointmentId?:"").asReqBody())
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    private fun apiMarkAsComplete(otp_:String) {
        if (isNetworkConnected) {
            showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("id", appointmentId)
                addProperty("otp", otp_)
            }

           val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
           mViewModel?.apiMarkAsComplete(body)

      //      mViewModel?.apiMarkAsComplete((mViewModel?.appSharedPref?.loginUserType ?: "").asReqBody(), appointmentId?.asReqBody(), otp_.asReqBody() )
        } else {
            CommonDialog.showDialogForSingleButton(this, object : DialogClickCallback {
                override fun onConfirm() { finish() }
            }, getString(R.string.netork_status), getString(R.string.check_network_connection)) }
    }

    private fun apiRejAccept(id: String, action: String) {
        if (isNetworkConnected) {
            showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("id", id)
                addProperty("acceptance_status", action)
            }
           val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
           mViewModel?.apiMarkAs(body, -1)
        //    mViewModel?.apiMarkAs((mViewModel?.appSharedPref?.loginUserType?:"").asReqBody(),id.asReqBody(), action.asReqBody(),-12)
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    override fun onMarkComplete(response: ModelAppointmentDetails?) {
        hideLoading()
        try {
            if (response?.code.equals(SUCCESS_CODE)) {
             //   bindViews(response?.result)
              afterCompleteAppointment()
             }else  showToast(response?.message ?: getString(R.string.something_went_wrong))

        } catch (e: Exception) {
            hideLoading()
            println(e)
        }
    }

    override fun onAppointmentDetail(response: ModelAppointmentDetails?) {
          hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
           bindViews(response?.result)
        } else {
          CommonDialog.showDialogForSingleButton(this, object : DialogClickCallback {
            override fun onConfirm() { finish() }
        }, getString(R.string.appointment_details), response?.message ?: "") }

    }

    private var mPatientLat:String = "0.0"
    private var mPatientLng:String = "0.0"
    private var mPatientId: String = ""

    private fun bindViews(response: ModelAppointmentDetails.Result?) {
       response?.let {
           binding?.run {
               data = it
               patientContact = it.patient_contact?:""
               mPatientLat = it.patient_lat ?: "0.0"
               mPatientLng = it.patient_long ?: "0.0"
               hospitalId = it.hospital_id.orEmpty()
               mPatientId = it.patient_id.orEmpty()

               if(it.service_type.equals(LoginTypes.DOCTOR.type, ignoreCase = true)) {
                   detailModel = it
                   btnVideoCall.visibility = View.GONE
                   showDoctorUI(response)
                   when {
                       it.acceptance_status.equals(TransactionStatus.PENDING.get(), ignoreCase = true) -> {
                           grpAcceptCancel.visibility = View.VISIBLE
                       }
                       else -> {
                           grpAcceptCancel.visibility = View.GONE
                           when {
                               it.acceptance_status.equals(TransactionStatus.REJECTED.get(), ignoreCase = true) ||
                               it.acceptance_status.equals(TransactionStatus.CANCELLED.get(), ignoreCase = true) -> {
                                   btnUploadPresc.visibility = View.GONE
                               }
                               it.acceptance_status.equals(TransactionStatus.ACCEPTED.get(), ignoreCase = true) -> {
                                if(it.booking_type.equals("online_task",ignoreCase = true)) {
                                  needToShowVideoCall(it.app_date, it.from_time, btnVideoCall)
                                   btnVideoCall.setOnClickListener { hitVideoCall()}
                                  } else btnVideoCall.visibility = View.GONE

                                 //  btnVideoCall.visibility = View.VISIBLE
                                 //  btnUploadPresc.visibility = View.VISIBLE
                                   val list = listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                                   // Initialize a new instance of ManagePermissions class
                                   managePermissions = ManagePermissions(this@AppointmentDetailScreen, list, PermissionsRequestCode)
                                   //check permissions states
                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                       managePermissions.checkPermissions()
                                   }
                                   needToShowUploadPrescription(it.app_date, it.from_time, btnUploadPresc)
                                   btnUploadPresc.setOnClickListener { showPictureDialog() }
                               }

                               it.acceptance_status.equals(TransactionStatus.COMPLETED.get(), ignoreCase = true) -> {
                                       showRatingOrNot(it)
                                       btnUploadPresc.visibility = View.GONE
                                       grpPresc.visibility = if (it.provider_prescription.isNullOrBlank().not()) {
                                           tvPrescDowonload.setOnClickListener { _ ->
                                               initializeDownloadManager()
                                               downloadFile(it.provider_prescription.orEmpty())
                                           }
                                          tvPrescReUpload.setOnClickListener { showPictureDialog() }
                                           View.VISIBLE
                                       } else View.GONE
                              }

                               }
                           }
                       }
               }
               else if(it.service_type.equals(LoginTypes.LAB.type, ignoreCase = true)) {
                   detailModel = it
                   btnVideoCall.visibility = View.GONE
                   when {
                       it.acceptance_status.equals(TransactionStatus.PENDING.get(), ignoreCase = true) -> {
                           grpAcceptCancel.visibility = View.VISIBLE
                       }
                       else -> {
                           grpAcceptCancel.visibility = View.GONE
                           when {
                               it.acceptance_status.equals(TransactionStatus.REJECTED.get(), ignoreCase = true) ||
                               it.acceptance_status.equals(TransactionStatus.CANCELLED.get(), ignoreCase = true) -> { btnUploadPresc.visibility = View.GONE }
                               it.acceptance_status.equals(TransactionStatus.ACCEPTED.get(), ignoreCase = true) -> {
                                   val list = listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                                   managePermissions = ManagePermissions(this@AppointmentDetailScreen, list, PermissionsRequestCode)
                                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { managePermissions.checkPermissions() }

                                   needToShowUploadPrescription(it.app_date, it.from_time, relLabReportSection)
                                   tvUpload.setOnClickListener { uploadReportClick() }
                               }
                               it.acceptance_status.equals(TransactionStatus.COMPLETED.get(), ignoreCase = true) -> {
                                    showRatingOrNot(it)
                                    relLabReportSection.visibility = View.VISIBLE
                                    needToShowUploadLabReport(it.app_date, it.from_time, tvUpload)
                                    needToShowUploadLabReport(it.app_date, it.from_time, vrepLast)

                                    rvReports.visibility = if(it.report.isNullOrEmpty()) View.GONE else {
                                        adapterReports.submitList(it.report); View.VISIBLE
                                    }
                                   tvUpload.setOnClickListener { uploadReportClick() }
                                  }
                               }
                           }
                       }
                   }
               else {
                   when {
                           it.acceptance_status.equals(TransactionStatus.PENDING.get(), ignoreCase = true) -> {
                               grpAcceptCancel.visibility = View.VISIBLE
                           }
                           else -> {
                               grpAcceptCancel.visibility = View.GONE
                               when {
                                   it.acceptance_status.equals(TransactionStatus.REJECTED.get(), ignoreCase = true) ||
                                   it.acceptance_status.equals(TransactionStatus.CANCELLED.get(), ignoreCase = true) -> {
                                       grpEnterOtp.visibility = View.GONE
                                       grpOtpSuccess.visibility = View.GONE

                                   }
                                   it.acceptance_status.equals(TransactionStatus.ACCEPTED.get(), ignoreCase = true) -> {
                                       grpEnterOtp.visibility = View.VISIBLE
                                       grpOtpSuccess.visibility = View.GONE

                                   }
                                   it.acceptance_status.equals(TransactionStatus.COMPLETED.get(), ignoreCase = true) -> {
                                       showRatingOrNot(it)
                                       grpEnterOtp.visibility = View.GONE
                                       grpOtpSuccess.visibility = if (it.OTP.isNullOrBlank().not()) {
                                           View.VISIBLE
                                       } else View.GONE
                                   }
                               }

                           }
                       }
               }

              // set the recyclerview
               if(it.task_details.isNullOrEmpty().not()){
                   relPayment.visibility = View.VISIBLE
                   mAdapterPayment.submitList(it.task_details)
               }else {
                   relPayment.visibility = View.GONE
               }

         }
        }
    }
    private fun afterCompleteAppointment(){
        FragNewAppointmentListing.icCompleted = true
        FragNewAppointmentListing.isAcceptedOrRejcted = false
        fetchTasksDetailApi()
    }

    private fun uploadReportClick(){
        mBsReportsUploading = BSUploadReportSection.newInstance(object :OnBottomSheetCallbacks{
            override fun onSubmitReports(data: ArrayList<File?>?) {
                // hit the api from here for the lab
                apiUploadLabReports(data)
            }
            override fun onPickImage() {
                if (checkAndRequestPermissionsTest()) {
                    uplaodCertificate(PICKFILE_RESULT_CODE)
                }
            }
        })
        mBsReportsUploading?.show(supportFragmentManager, "RescheduleDialog")
    }

    private fun apiUploadLabReports(repoFiles: ArrayList<File?>?) {
        if (isNetworkConnected) {
            val repoLst = ArrayList<MultipartBody.Part?>()
            repoFiles?.let { it.forEach { fls ->
            val rqb = fls?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                repoLst.add(MultipartBody.Part.createFormData("report[]", fls?.name.orEmpty(), rqb!!))
            }
          }

         val reqApptId = (appointmentId.orEmpty()).asReqBody()
         val reqPatientId = mPatientId.asReqBody()
         val reqHospId = hospitalId.asReqBody()

             showLoading()
             mViewModel?.apiUploadLabReports(reqApptId,reqPatientId,reqHospId,repoLst)
        }
    }


    private fun hitVideoCall(){
        if (isNetworkConnected) {
            showLoading()
            val videoPushRequest = VideoPushRequest()
            videoPushRequest.fromUserId = mViewModel?.appSharedPref?.loginUserId.orEmpty() // doctor_id
            videoPushRequest.toUserId = detailModel?.patient_id.orEmpty()  // patient_id
            roomName = "rootvideo_room_" + videoPushRequest.toUserId + "_" + videoPushRequest.fromUserId
            videoPushRequest.orderId = detailModel?.order_id
            videoPushRequest.roomName = "rootvideo_room_" + videoPushRequest.toUserId + "_" + videoPushRequest.fromUserId
            videoPushRequest.type = "doctor_to_patient_video_call"
            videoPushRequest.fromUserName = detailModel?.provider_name.orEmpty().trim()
            videoPushRequest.toUserName = detailModel?.patient_name.orEmpty().trim()

            mViewModel?.apiSendVideoPushNotification(videoPushRequest)

        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    private fun showDoctorUI(response: ModelAppointmentDetails.Result?) {
        response?.let {
            binding?.run {
                if(it.slot_booking_id?.equals("ONLINE_BOOKING",true) == true){
                    tvhDisFare.visibility = View.GONE
                    tvDisFare.visibility = View.GONE
                }

                if(it.symptom_text.isNullOrBlank().not()){
                    tSyp.visibility = View.VISIBLE
                    tvSympDesc.visibility = View.VISIBLE
                } else {
                    tSyp.visibility = View.GONE
                    tvSympDesc.visibility = View.GONE
                }

                if(it.patient_prescription.isNullOrBlank().not()) {
                    tvUploadedSumptom.visibility = View.VISIBLE
                    tvUpPresView.visibility = View.VISIBLE
                    tvUpPresView.setOnClickListener { _ ->
                        viewFileEnlarge( BaseMediaUrls.USERIMAGE.url+ it.patient_prescription.orEmpty())
                    }
                } else {
                    tvUploadedSumptom.visibility = View.GONE
                    tvUpPresView.visibility = View.GONE
                }
                if(it.symptom_recording.isNullOrBlank().not()){
                    playerView.visibility = View.VISIBLE
                    myExoPlayer = MyExoPlayer(playerView, applicationContext, BaseMediaUrls.AUDIO.url + it.symptom_recording.orEmpty())
                    onStart(); onResume()
                } else playerView.visibility = View.GONE

                if(it.symptom_text.isNullOrBlank() && it.patient_prescription.isNullOrBlank() && it.symptom_recording.isNullOrBlank()){
                    cnsPatientSyptm.visibility = View.GONE
                } else cnsPatientSyptm.visibility = View.VISIBLE
            }
        }

    }

    private var downloadManager: DownloadManager? = null
    private var downLoadId: Long? = null
    private fun initializeDownloadManager() {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    }
    private fun downloadFile(fName: String) {
        try{
            val request = DownloadManager.Request(Uri.parse(BaseMediaUrls.USERIMAGE.url + fName))
//          val request = DownloadManager.Request(Uri.parse("https://teq-dev-var19.co.in/rootscare/uploads/images/Kalimba.mp3"))
            request.setTitle(fName).setDescription("File is downloading...")
                .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, fName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //Enqueue the download.The download will start automatically once the download manager is ready
            // to execute it and connectivity is available.
            downLoadId = downloadManager?.enqueue(request)
            showToast(getString(R.string.prescription_downloaded_success))
        } catch (e:Exception){
            showToast(getString(R.string.something_went_wrong))
        }

    }

    private fun showRatingOrNot(it: ModelAppointmentDetails.Result) {
        binding?.run {
            if(it.avg_rating.isNullOrBlank().not() && it.avg_rating?.toInt() != 0){
                ratBar.visibility = View.VISIBLE
                tvhRated.visibility = View.VISIBLE
                ratBar.rating = it.avg_rating?.toFloat() ?: 0.0f
                tvhRated.text = getString(R.string.rated)
            } else {
                ratBar.visibility = View.GONE
                tvhRated.visibility = View.VISIBLE
                tvhRated.text = getString(R.string.not_rated_yet)
            }
        }
    }

    override fun onMarkAcceptReject(response: ModelAppointmentsListing?, position: Int) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            FragNewAppointmentListing.isAcceptedOrRejcted = true
            fetchTasksDetailApi()
        } else showToast(response?.message?:"")
    }

     override fun errorInApi(throwable: Throwable?) {
        hideLoading()
        if (throwable?.message != null) {
            showToast(throwable.message ?: getString(R.string.something_went_wrong))
        }
    }

    override fun onResume() {
        super.onResume()
        myExoPlayer?.onResume()
    }

    override fun onStart() {
        super.onStart()
        myExoPlayer?.onStart()
    }

    override fun onPause() {
        super.onPause()
        myExoPlayer?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        myExoPlayer?.onStop()
    }

    //IMAGE SELECTION AND GET IMAGE PATH
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.select_action))
        val pictureDialogItems = arrayOf(getString(R.string.select_photo_from_gallary), getString(R.string.capture_photo))
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if (resultCode != Activity.RESULT_CANCELED) {
             if (requestCode == PICKFILE_RESULT_CODE) {
                 if (resultCode == -1) {
                     fileUri = data?.data
                     filePath = fileUri?.path
                     // tvItemPath.setText(filePath)
                     try {
                         val file = FileUtil.from(this, fileUri!!)
                         Log.wtf("file_", "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists())
                         if(isSelectedFileSizeNotEligible(file.path , 10)){
                             showLongToast(getString(R.string.size_below_10mb))
                         } else {
                             displayCertificateName(getFileName(this, fileUri!!), file)
                         }
                     } catch (e: IOException) {
                         e.printStackTrace()
                     }
                 }
             } else if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
                        val path = saveImage(bitmap)
                         bitmapToFile(bitmap)
                     //   Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()

//                    fra?.imgRootscareProfileImage?.setImageBitmap(bitmap)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                    }

                }

            } else if (requestCode == CAMERA) {

                try {
                    val thumbnail = data!!.extras!!.get("data") as Bitmap
                    //    fragmentProfileBinding?.imgRootscareProfileImage?.setImageBitmap(thumbnail)
                    saveImage(thumbnail)
                    bitmapToFile(thumbnail)
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    println("Exception===>$e")
                }

            }
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

    private fun displayCertificateName(fname: String?, fFile: File) {
        Log.wtf(FragmentRegistration.TAG, "displayCertificateName: $fname \n ${fFile.name}")
        mBsReportsUploading?.updateReports(fFile)
    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(externalCacheDir?.absolutePath?.toString() + IMAGE_DIRECTORY)

        // have the object build the directory structure, if needed.
        Log.wtf("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            Log.wtf("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance().timeInMillis).toString() + ".jpg"))
            //     File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");
            f.createNewFile()
            // imageFile=f
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.wtf("TAG", "File Saved::--->" + f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(this)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        imageFile = file

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //  binding?.tvUpload?.text = imageFile?.name
         apiUploadPrescription()

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    private fun apiUploadPrescription() {
        if (isNetworkConnected) {
           if (imageFile != null) {
                prescriptionimage =
                imageFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                imageMulitpart = MultipartBody.Part.createFormData("provider_prescription", imageFile?.name, prescriptionimage!!)
            } else {
                prescriptionimage = "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                imageMulitpart = MultipartBody.Part.createFormData("provider_prescription", "", prescriptionimage!!)
            }

            val reqApptId = (appointmentId ?: "").asReqBody()
            showLoading()
           mViewModel?.apiUploadPrescription(reqApptId,imageMulitpart)
        }
    }

    override fun onPrescriptionUploaded(response: CommonResponse?) {
        hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            afterCompleteAppointment()
        } else showLongToast(response?.message.orEmpty())
    }


    private var roomName: String? = null
    override fun successVideoPushResponse(videoPushResponse: VideoPushResponse?) {
        hideLoading()
        if (videoPushResponse?.status == true && videoPushResponse.code.equals(SUCCESS_CODE)) {
            val bundle = Bundle().apply {
                putString("roomName", roomName)
                putString("fromUserId", mViewModel?.appSharedPref?.loginUserId.orEmpty()) // doctor_id
                putString("fromUserName", detailModel?.provider_name.orEmpty().trim())
                putString("toUserId", detailModel?.patient_id.orEmpty().trim())
                putString("toUserName", detailModel?.patient_name.orEmpty().trim())
                putString("orderId",  detailModel?.order_id.orEmpty().trim())
                putBoolean("isDoctorCalling", true)
            }

            val intent = Intent(this, VideoCallActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
//            activity?.finish()
        } else {
            Toast.makeText(this, videoPushResponse?.message ?: getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,  grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions.processPermissionsResult(grantResults)

                if (isPermissionsGranted) {
                    showPictureDialog(); showToast(getString(R.string.permission_granted))
                } else { showToast(getString(R.string.permission_denied)) }
                return
            }
        }

    }

}