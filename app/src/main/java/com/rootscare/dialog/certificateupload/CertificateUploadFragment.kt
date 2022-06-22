package com.rootscare.dialog.certificateupload

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.myfilepickesexample.FileUtil
import com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.CertificateUploadDialogBinding
import com.rootscare.serviceprovider.ui.base.BaseDialogFragment
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.io.File
import java.io.IOException


class CertificateUploadFragment :
    BaseDialogFragment<CertificateUploadDialogBinding, CertificateViewModel>(), CertificateNavigator {

    private val PICKFILE_RESULT_CODE = 4
    private var choosenYear = 1980
    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var certificatefileFile: File? = null
    var listener: PassDataCallBack? = null

    interface PassDataCallBack {
        fun onPassData(data: QualificationDataItem)
    }

    private var fragmentLayoutBinding: CertificateUploadDialogBinding? = null
    private var fragmentLocalViewModel: CertificateViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.certificate_upload_dialog
    override val viewModel: CertificateViewModel
        get() {
            fragmentLocalViewModel = ViewModelProviders.of(this).get(
                CertificateViewModel::class.java
            )
            return fragmentLocalViewModel as CertificateViewModel
        }

    companion object {
        fun newInstance(): CertificateUploadFragment {
            val args = Bundle()
            val fragment = CertificateUploadFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
            val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLocalViewModel!!.navigator = this
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLayoutBinding = viewDataBinding

        with(fragmentLayoutBinding!!) {
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvSubmit.setOnClickListener {
                if (checkValidationForRegStepOne()) {
                    val model = QualificationDataItem()
                    model.institute = ediitextInstitute.text?.trim().toString()
                    model.passingYear = textViewPassingYear.text?.trim().toString()
                    model.qualification = ediitextQualification.text?.trim().toString()
                    model.qualificationCertificate = textViewCertificate.text?.trim().toString()
                    model.certificateFileTemporay = certificatefileFile
                    model.isOldData = false
                    listener?.onPassData(model)
                    ediitextInstitute.setText("")
                    ediitextQualification.setText("")
                    textViewCertificate.text = ""
                    textViewPassingYear.text = ""
                    dismiss()
                }
            }

            textViewPassingYear.setOnClickListener {
                val builder = MonthPickerDialog.Builder(
                    activity,
                    { _, selectedYear ->
                        textViewPassingYear.text = selectedYear.toString()
                    },
                    choosenYear,
                    0
                )
                if (!TextUtils.isEmpty(textViewPassingYear.text.toString().trim())) {
                    builder.setActivatedYear(textViewPassingYear.text.toString().trim().toInt())
                }
                builder.showYearOnly()
                    .setYearRange(1980, 2090)
                    .build()
                    .show()
            }

            textViewCertificate.setOnClickListener{
                var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                chooseFile.type = "*/*"
                chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                startActivityForResult(
                    chooseFile,
                    PICKFILE_RESULT_CODE
                )
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                    fragmentLayoutBinding?.textViewCertificate?.text = getFileName(requireActivity(), fileUri!!)
                } catch (e: IOException) {
                    e.printStackTrace()
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


    private fun checkValidationForRegStepOne(): Boolean {
        if (TextUtils.isEmpty(
                fragmentLayoutBinding?.ediitextQualification?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your Qualification!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(
                fragmentLayoutBinding?.ediitextInstitute?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter Institute name!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(
                fragmentLayoutBinding?.textViewPassingYear?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your passing year!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (certificatefileFile == null) {
            Toast.makeText(activity, "Please select your certificate!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}