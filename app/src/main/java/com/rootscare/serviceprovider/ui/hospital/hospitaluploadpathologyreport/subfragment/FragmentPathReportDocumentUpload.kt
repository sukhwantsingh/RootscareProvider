package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentPatReportDocumentUploadBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReport
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.util.*

class FragmentPathReportDocumentUpload : BaseFragment<FragmentPatReportDocumentUploadBinding, FragmentPathReportDocumentUploadViewModel>(),
    FragmentPathReportDocumentUploadNavigator {
    private val GALLERY = 1
    private val CAMERA = 2
    var imageFile: File? = null
    var appointmentId: String? = null
    var patientId: String? = null
    private var fragmentPatReportDocumentUploadBinding: FragmentPatReportDocumentUploadBinding? = null
    private var fragmentPathReportDocumentUploadViewModel: FragmentPathReportDocumentUploadViewModel? = null
    val TAG = FragmentPathReportDocumentUpload::class.java.simpleName
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_pat_report_document_upload
    override val viewModel: FragmentPathReportDocumentUploadViewModel
        get() {
            fragmentPathReportDocumentUploadViewModel = ViewModelProviders.of(this).get(
                FragmentPathReportDocumentUploadViewModel::class.java
            )
            return fragmentPathReportDocumentUploadViewModel as FragmentPathReportDocumentUploadViewModel
        }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(patientId: String, appointmentId: String): FragmentPathReportDocumentUpload {
            val args = Bundle()
            args.putString("patientId", patientId)
            args.putString("appointmentId", appointmentId)
            val fragment = FragmentPathReportDocumentUpload()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPathReportDocumentUploadViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPatReportDocumentUploadBinding = viewDataBinding
        appointmentId = arguments?.getString("appointmentId")
        patientId = arguments?.getString("patientId")
        fragmentPatReportDocumentUploadBinding?.txtDoctorbookingUploadPrescriptionimage?.setOnClickListener {
            showPictureDialog()
        }
        fragmentPatReportDocumentUploadBinding?.btnUpload?.setOnClickListener {
            submitDetailsForInsertPathologyReport()
        }
    }


    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
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
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                        val path = saveImage(bitmap)
                        bitmapToFile(bitmap)
                        Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()


                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                    }

                }

            } else if (requestCode == CAMERA) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                saveImage(thumbnail)
                bitmapToFile(thumbnail)
                Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            activity?.externalCacheDir!!.absolutePath.toString() + IMAGE_DIRECTORY
        )

        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            //     File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");
            f.createNewFile()
            // imageFile=f

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                activity,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)


            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(activity)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        imageFile = file

        fragmentPatReportDocumentUploadBinding?.txtDoctorbookingUploadPrescriptionimage?.text =
            imageFile?.name



        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //updateProfileImageApiCall(imageFile!!)
        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }


    private fun submitDetailsForInsertPathologyReport() {
        with(fragmentPatReportDocumentUploadBinding!!) {
            if (fragmentPathReportDocumentUploadViewModel?.appSharedPref?.loginUserId != null && checkValidationFoImageUpload()) {
                baseActivity?.showLoading()
                baseActivity?.hideKeyboard()

//                val patientId = RequestBody.create(
//                    "multipart/form-data".toMediaTypeOrNull(),
//                    patientId.toString()
//                )
                val patientId: RequestBody = if (patientId == "")
                    "11".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                else {
                    patientId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
                val hospitalId = id.toString().toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull(),
                )
//                val appointmentId = appointmentId!!.trim().toRequestBody(
//                    "multipart/form-data".toMediaTypeOrNull()
//                )
                val appointmentId: RequestBody = if (appointmentId == "")
                    "2".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                else {
                    appointmentId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                }
                val reportNumber = imageFile!!.name.trim().toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                )

                val imageMultipartBody: MultipartBody.Part?
                val image: RequestBody?
                if (imageFile != null) {
                    image = imageFile!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    imageMultipartBody = MultipartBody.Part.createFormData("e_report", imageFile?.name, image)

                    fragmentPathReportDocumentUploadViewModel?.apiHitForReportUpload(
                        patientId, hospitalId, appointmentId, reportNumber, imageMultipartBody
                    )

                }

            }
        }
    }

    private fun checkValidationFoImageUpload(): Boolean {
        return if (imageFile == null) {
            Toast.makeText(activity, "Please upload patient's report to continue", Toast.LENGTH_SHORT).show()
            false
        } else {
            true;
        }
    }

    override fun onSuccessReportUpload(response: CommonResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT)
                .show()
            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(FragmentHospitalUploadPathologyReport.newInstance())
        } else {
            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}