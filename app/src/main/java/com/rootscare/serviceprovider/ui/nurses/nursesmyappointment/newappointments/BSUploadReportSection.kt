package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewBsReportUploadingBinding
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter.AdapterReportUploading
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelReportUploading
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.OnBottomSheetCallbacks
import com.rootscare.serviceprovider.ui.supportmore.bottomsheet.DIALOG_BACK_RANGE
import java.io.File

class BSUploadReportSection : DialogFragment() {

    private lateinit var mContext: Context
    lateinit var binding: LayoutNewBsReportUploadingBinding

    private val adapterReports: AdapterReportUploading by lazy { AdapterReportUploading() }

    companion object {
        var mCallback: OnBottomSheetCallbacks? = null

        fun newInstance(clb: OnBottomSheetCallbacks?): BSUploadReportSection {
            this.mCallback = clb
            return BSUploadReportSection()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_new_bs_report_uploading, container, false)
        binding.lifecycleOwner = this
        dialog?.window?.let {
            with(it) {
                setBackgroundDrawableResource(android.R.color.transparent)
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                setDimAmount(DIALOG_BACK_RANGE)
            }
        }

        try {
            binding.run {
                rvAttachments.adapter = adapterReports

                ivCross.setOnClickListener { dismiss() }
                relTap.setOnClickListener { mCallback?.onPickImage() }
                btnUpload.setOnClickListener { _ ->
                    val taskIdList = ArrayList<File?>()
                    val mList = adapterReports.updatedArrayList
                    mList.forEach {
                        taskIdList.add(it?.reportFile)
                    }
                    if (taskIdList.isEmpty()) {
                        Toast.makeText(requireContext(), "Please choose report", Toast.LENGTH_SHORT).show()
                    } else {
                        mCallback?.onSubmitReports(taskIdList)
                        dismiss()
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            println(e)
        }

        return binding.root
    }


    fun updateReports(file: File?) {
        val model = ModelReportUploading(file, file?.name.orEmpty())
        setReportListRecycle(model)
    }

    private fun setReportListRecycle(mNode: ModelReportUploading?) {
        binding.run {
            mNode?.let { adapterReports.loadDataNodeIntoList(it) }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        try {
            dialog.setCanceledOnTouchOutside(false)
        } catch (e: Exception) {
            println(e)
        }
        return dialog
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
    }

    override fun onStart() {
        super.onStart()
        try {
            dialog?.window?.let {
                with(it) {
                    val metrics = mContext.resources.displayMetrics
                    val width = metrics.widthPixels
                    //  val heigh = metrics.heightPixels
                    setLayout((width), ViewGroup.LayoutParams.WRAP_CONTENT)
                    //   setLayout(width, heigh)
                    setGravity(Gravity.BOTTOM)
                    setWindowAnimations(R.style.DialogAnimation)
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

}
