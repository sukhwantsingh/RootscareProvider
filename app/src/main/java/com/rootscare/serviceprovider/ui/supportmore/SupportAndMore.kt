package com.rootscare.serviceprovider.ui.supportmore

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivitySupportAndMoreBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.manageDocLab.ActivityCreateEditHospitalDocs
import com.rootscare.serviceprovider.utilitycommon.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SupportAndMore : BaseActivity<ActivitySupportAndMoreBinding, CommonViewModel>() {
    private var binding: ActivitySupportAndMoreBinding? = null
    private var mViewModel: CommonViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_support_and_more
    override val viewModel: CommonViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
            return mViewModel as CommonViewModel
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        binding?.topToolbar?.tvHeader?.text = getString(R.string.support_amp_more)
        binding?.topToolbar?.btnBack?.setOnClickListener { finish() }

        initViews()
    }

    private fun initViews() {

        setLanguagePrefernce()
        binding?.run {
            tvVersion.text = getAppVersionText()

            tvhEng.setOnClickListener {
                mViewModel?.appSharedPref?.languagePref = LanguageModes.ENG.get()
                tvhEng.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.lang_pref_select))
                tvhAr.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.white))
            }
            tvhAr.setOnClickListener {
                mViewModel?.appSharedPref?.languagePref = LanguageModes.AR.get()
                tvhAr.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.lang_pref_select))
                tvhEng.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.white))
            }

            tvhTermsnConditions.setOnClickListener { navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_TERMS))) }
            tvhAboutRoot.setOnClickListener { navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_ABOUT))) }
            tvhPrivacyp.setOnClickListener { navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_PRIVACY))) }

            tvhNeedsupport.setOnClickListener { navigate<NeedSupportScreen>() }

//            tvhDelAcc.setOnClickListener {
//                deleteDoc(UserDisableType.DELETE.get(), getString(R.string.sure_to_delete_account))
//            }

        }

    }

    private fun deleteDoc(typeToHit:String, desc:String) {
        CommonDialog.showDialog(this@SupportAndMore, object :
            DialogClickCallback {
            override fun onConfirm() {
                if (isNetworkConnected) {
                 //   val jsonObject = JsonObject().apply {
                  //      addProperty("user_id", ActivityCreateEditHospitalDocs.docId)
                //    }

                  // showLoading()
                  //  val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
//                    when {
//                        typeToHit.equals(UserDisableType.DELETE.get(),true) -> mViewModel?.deleteDoc(body)
//                        else -> hideLoading()
//                    }
                } else {
                    showToast(getString(R.string.network_unavailable))
                }

            }
        }, typeToHit, desc)

    }

   private fun setLanguagePrefernce() {
        if (mViewModel?.appSharedPref?.languagePref.equals(LanguageModes.ENG.get(), ignoreCase = true)) {
            binding?.tvhEng?.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.lang_pref_select))
            binding?.tvhAr?.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.white))
        } else {
            binding?.tvhAr?.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.lang_pref_select))
            binding?.tvhEng?.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.white))
        }
    }

}