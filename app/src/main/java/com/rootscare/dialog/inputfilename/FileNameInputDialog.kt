package com.rootscare.dialog.inputfilename

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FileInputDialogLayoutBinding
import com.rootscare.serviceprovider.ui.base.BaseDialog
import com.sihatku.commondialog.inputfilename.FileNameNavigator
import com.sihatku.commondialog.inputfilename.FileNameViewModel


class FileNameInputDialog(internal var activity: Activity, callbackAfterDateTimeSelect: CallbackAfterDateTimeSelect) :
        BaseDialog(), FileNameNavigator, View.OnClickListener {



    interface CallbackAfterDateTimeSelect {
        fun selectDateTime(dateTime: String)
    }

    companion object {
        private val TAG = FileNameInputDialog::class.java.simpleName
    }

    private var callbackAfterDateTimeSelect: CallbackAfterDateTimeSelect? = callbackAfterDateTimeSelect
    private var fileName = ""
    private lateinit var layoutBinding: FileInputDialogLayoutBinding
    private var forgotPasswordViewModel: FileNameViewModel? = null


    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.file_input_dialog_layout, null, false)
        // setting viewmodel to layout bind
        forgotPasswordViewModel = ViewModelProviders.of(this).get(FileNameViewModel::class.java)
        layoutBinding.setVariable(BR.viewModel, forgotPasswordViewModel)
        forgotPasswordViewModel?.navigator = this
        isCancelable = false

        with(layoutBinding) {
            tvTitle.text = activity.resources?.getString(R.string.create_prescription_name)
            edittextFilename.setLines(1)
            edittextFilename.hint = activity.resources?.getString(R.string.choose_name_for_this_file)
            edittextFilename.maxLines = 1
            tvSubmit.setOnClickListener(this@FileNameInputDialog)
            tvCancel.setOnClickListener(this@FileNameInputDialog)

            edittextFilename.addTextChangedListener {
                fileName = it.toString().trim()
                edittextFilename.error = null
            }
        }

        return layoutBinding.root
    }


    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onClick(v: View?) {
        with(layoutBinding) {
            if (v!!.id == tvSubmit.id) {
                if (!TextUtils.isEmpty(fileName.trim())){
                    callbackAfterDateTimeSelect?.selectDateTime(fileName)
                    dismiss()
                }else{
                    edittextFilename.error = activity.resources.getString(R.string.create_prescription_name)
                    edittextFilename.requestFocus()
                }
            }
            if (v.id == tvCancel.id) {
                dismiss()
            }
        }
    }
}