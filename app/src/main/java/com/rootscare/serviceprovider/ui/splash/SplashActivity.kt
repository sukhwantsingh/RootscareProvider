package com.rootscare.serviceprovider.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProviders
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivitySplashBinding
import com.rootscare.serviceprovider.ui.babySitter.home.BabySitterHomeActivity
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.labtechnician.home.LabTechnicianHomeActivity
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import com.rootscare.serviceprovider.utilitycommon.openWebLink
import io.branch.referral.Branch
import io.branch.referral.BranchError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashActivityViewModel>(), SplashActivityNavigator {
    private var activitySplashBinding: ActivitySplashBinding? = null
    private var splashViewModel: SplashActivityViewModel? = null

    //    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    private val SPLASH_DISPLAY_LENGTH = 1000
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_splash

    override val viewModel: SplashActivityViewModel
        get() {
            splashViewModel = ViewModelProviders.of(this).get(SplashActivityViewModel::class.java)
            return splashViewModel as SplashActivityViewModel
        }

    companion object {
        val TAG = SplashActivity::class.java.simpleName

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, SplashActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel?.navigator = this
        activitySplashBinding = viewDataBinding

        Handler(Looper.getMainLooper()).postDelayed({ redirectToLogin() }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun redirectToLogin() {
        if (splashViewModel?.appSharedPref?.loginUserType != null && !splashViewModel?.appSharedPref?.loginUserType.equals("")) {
            if (splashViewModel?.appSharedPref?.isLoggedIn == true) {
                val loginResponse = splashViewModel?.appSharedPref?.loginUserType
                when {
                    loginResponse?.lowercase(Locale.ROOT).equals(LoginTypes.NURSE.type) -> {
                        startActivity(NursrsHomeActivity.newIntent(this@SplashActivity))
                    }
                    loginResponse?.lowercase().equals(LoginTypes.CAREGIVER.type) -> {
                        startActivity(CaregiverHomeActivity.newIntent(this@SplashActivity))
                    }
                    loginResponse?.lowercase().equals(LoginTypes.BABYSITTER.type) -> {
                        startActivity(BabySitterHomeActivity.newIntent(this@SplashActivity))
                    }
                    loginResponse?.lowercase().equals(LoginTypes.PHYSIOTHERAPY.type) -> {
                        startActivity(PhysiotherapyHomeActivity.newIntent(this@SplashActivity))
                    }
                    loginResponse?.lowercase().equals(LoginTypes.DOCTOR.type) ||
                            loginResponse?.lowercase().equals(LoginTypes.HOSPITAL_DOCTOR.type) -> {
//                        startActivity(HomeActivity.newIntent(this@SplashActivity))
                        startActivity(NursrsHomeActivity.newIntent(this@SplashActivity))
                    }
                    loginResponse?.lowercase().equals(LoginTypes.HOSPITAL.type) -> {
                        startActivity(HospitalHomeActivity.newIntent(this@SplashActivity))
                    }
                    loginResponse?.lowercase().equals(LoginTypes.LAB_TECHNICIAN.type) -> {
                        startActivity(LabTechnicianHomeActivity.newIntent(this@SplashActivity))
                    }

                }
                finish()
            } else {
                startActivity(LoginActivity.newIntent(this@SplashActivity))
                finish()
            }
        } else {
            startActivity(LoginActivity.newIntent(this@SplashActivity))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        // Branch init
        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
       // apiVersionCheck()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent

        // if activity is in foreground (or in backstack but partially visible) launch the same
        // activity will skip onStart, handle this case with reInit
        if (intent != null &&
            intent.hasExtra("branch_force_new_session") &&
            intent.getBooleanExtra("branch_force_new_session", false)
        ) {
            Branch.sessionBuilder(this).withCallback(branchListener).reInit()

        }
    }

    object branchListener : Branch.BranchReferralInitListener {
        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                Log.wtf("BRANCH SDK", referringParams.toString())
                // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
            } else {
                Log.wtf("BRANCH SDK", error.message)
            }
        }
    }

    override fun onSuccessVersion(response: CommonResponse?) {
        try {
            if (response?.code.equals(SUCCESS_CODE)) {
                response?.result?.let {
                    try {
                      //  if (BuildConfig.VERSION_CODE >= mRes.appVer?.appVer ?: 1) {
                            redirectToLogin()
                       // } else {
                            showUpdateDialogBeforeLogin(response)
                      //  }

                    } catch (e: java.lang.Exception) {

                    }

                }
            }
        } catch (e: Exception) {
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        hideLoading()
    }

    private fun apiVersionCheck() {
        if (isNetworkConnected) { splashViewModel?.apiVersionCheck() }
    }

    private fun showUpdateDialogBeforeLogin(mNode: CommonResponse?) {
        val view = layoutInflater.inflate(R.layout.layout_dialog_version_check, null, false)

        val mTxtTopHeader = view.findViewById<AppCompatTextView>(R.id.txt_welcome)
        val mTxtContent = view.findViewById<AppCompatTextView>(R.id.txt_update_content)

        val mTxtHelp = view.findViewById<AppCompatTextView>(R.id.tv_help)
        val mViewLine = view.findViewById<View>(R.id.view_line)

        val mBtnNoThankx = view.findViewById<AppCompatTextView>(R.id.btn_no_thanks)
        val mBtnUpdate = view.findViewById<AppCompatTextView>(R.id.btn_update)

        GlobalScope.launch(Dispatchers.Main + SupervisorJob()) {
          //  mNode?.appVer?.let {
             mTxtTopHeader.text = getString(R.string.update_patient)

              //  if (it.skipFlag == true) {
                   mBtnNoThankx.visibility = View.VISIBLE
              //  } else {
               //     mBtnNoThankx.visibility = View.INVISIBLE
             //   }

            //    mTxtContent.text = "".parseAsHtml()  // it.updText

             //   if (it.showHelp == true) {
                    mViewLine.visibility = View.VISIBLE; mTxtHelp.visibility = View.VISIBLE
                    mTxtHelp.text = "Help https://www.rootscare.com"     // "${it.helpTitle}: ${it.helpUrl}"
             //   } else {
                 //   mViewLine.visibility = View.GONE; mTxtHelp.visibility = View.GONE
               // }
           // }

        }

        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setView(view).setCancelable(false).create()
            dialog.show()
        mBtnUpdate.setOnClickListener {
           // openWebLink(mNode?.appVer?.rdrUrl)
           openWebLink("https://play.google.com/store/apps/details?id=com.rootscare")
            dialog.cancel()
            finish()

        }
        mBtnNoThankx.setOnClickListener {
            redirectToLogin()
            dialog.dismiss()
        }


    }
}

