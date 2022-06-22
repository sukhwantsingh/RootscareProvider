package com.rootscare.serviceprovider.ui.supportmore.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutBsThankyouSuccessBinding

const val DIALOG_BACK_RANGE = 0.8f
class DialogThankyou : DialogFragment() {

    private lateinit var mContext: Context
    lateinit var binding: LayoutBsThankyouSuccessBinding

    companion object {
 //   private var internalModel: ModelDoEkyc? = null
      private var mCallback: BottomSheetCallback? = null

        fun newInstance(data: Any? = null, clb: BottomSheetCallback? = null): DialogThankyou {
        //    this.internalModel = filterData
            this.mCallback = clb
            return DialogThankyou()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_bs_thankyou_success, container, false)
        binding.lifecycleOwner = this
        dialog?.window?.let {
            with(it) {
                setBackgroundDrawableResource(android.R.color.transparent)
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                setDimAmount(DIALOG_BACK_RANGE)
            }

        }
        try {
            binding.tvClose.setOnClickListener {
               mCallback?.onCloseBottomSheet()
                dismiss()
            }

        } catch (e: java.lang.Exception) {
           println(e)
        }

        return binding.root
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
                    setLayout((width), ViewGroup.LayoutParams.WRAP_CONTENT)
                    setGravity(Gravity.BOTTOM)
                    setWindowAnimations(R.style.DialogAnimation)
                }
            }
        } catch (e: Exception) {
           println(e)
        }
    }
}
