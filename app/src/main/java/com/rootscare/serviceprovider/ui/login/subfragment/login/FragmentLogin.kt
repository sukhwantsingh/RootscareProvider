package com.rootscare.serviceprovider.ui.login.subfragment.login

import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.data.model.request.loginrequest.LoginRequest
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLoginBinding
import com.rootscare.serviceprovider.ui.babySitter.home.BabySitterHomeActivity
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.utilitycommon.*
import java.util.regex.Pattern


class FragmentLogin : BaseFragment<FragmentLoginBinding, FragmentLoginViewModel>(), FragmentLoginNavigator {
    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private var fragmentLoginViewModel: FragmentLoginViewModel? = null
    private var androidId: String? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_login
    override val viewModel: FragmentLoginViewModel
        get() {
            fragmentLoginViewModel = ViewModelProviders.of(this).get(FragmentLoginViewModel::class.java)
            return fragmentLoginViewModel as FragmentLoginViewModel
        }

    companion object {
        val TAG = FragmentLogin::class.java.simpleName
        fun newInstance(): FragmentLogin {
            val args = Bundle()
            val fragment = FragmentLogin()
            fragment.arguments = args
            return fragment
        }
    }

    private var isRemChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLoginViewModel?.navigator = this
        androidId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
         Log.wtf(TAG, "onCreate: ${fragmentLoginViewModel?.appSharedPref?.accessToken}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLoginBinding = viewDataBinding
         initViews()
    }

    private fun initViews() {
 //       setLanguagePrefernce()
        setupRemember()
        fragmentLoginBinding?.checkboxLoginremember?.setOnClickListener {
            isRemChecked = fragmentLoginBinding?.checkboxLoginremember?.isChecked ?: false
        }
        fragmentLoginBinding?.txtForgotPassword?.setOnClickListener {
            (activity as LoginActivity?)?.setCurrentItem(4, true)
        }
        fragmentLoginBinding?.txtRootscareLoginUserType?.setOnClickListener {
            CommonDialog.showDialogForDropDownList(this.requireActivity(), getString(R.string.select_provider_type), enableProviders,
                object : DropDownDialogCallBack {
                    override fun onConfirm(text: String) {
                        fragmentLoginBinding?.txtRootscareLoginUserType?.text = text
                    }
                })
        }
        fragmentLoginBinding?.btnRootscareLogin?.setOnClickListener {
//        val mtyp = fragmentLoginBinding?.txtRootscareLoginUserType?.text?.toString().orEmpty()
//            if(mtyp.equals(LoginTypes.HOSPITAL.displayName,true)){
//                startActivity(activity?.let { HospitalHomeActivity.newIntent(it) })
//                activity?.finish()
//            return@setOnClickListener
//            }

            val userEmail = fragmentLoginBinding?.edtRootscareProviderEmail?.text?.toString().orEmpty()
            val password = fragmentLoginBinding?.edtRootscareProviderPassword?.text?.toString().orEmpty()

            if (checkValidation(userEmail, password)) {
                val requestUserLogin = LoginRequest()
                requestUserLogin.userType = when {
//                    fragmentLoginBinding?.txtRootscareLoginUserType?.text?.toString()?.lowercase() == "labdetails-technician" -> {
//                        "lab-technician"
//                    }
                    fragmentLoginBinding?.txtRootscareLoginUserType?.text?.toString()?.equals(LoginTypes.CAREGIVER.displayName, ignoreCase = true) == true -> {
                        LoginTypes.CAREGIVER.type
                    }
                    else -> fragmentLoginBinding?.txtRootscareLoginUserType?.text?.toString()?.lowercase()
                }

                requestUserLogin.email = userEmail
                requestUserLogin.password = password
                requestUserLogin.fcm_token = fragmentLoginViewModel?.appSharedPref?.accessToken
                requestUserLogin.device_type = "android"
                hideKeyboard()
                if (isNetworkConnected) {
                    baseActivity?.showLoading()
                    fragmentLoginViewModel?.apiserviceproviderlogin(requestUserLogin)
                } else {
                    showToast(getString(R.string.check_network_connection))
                }
//                } else {
//                    Toast.makeText(activity, "Please Tick remember button.", Toast.LENGTH_SHORT).show()

//                }


            }


        }
        fragmentLoginBinding?.tvBtnCreateAcc?.setOnClickListener {
            (activity as? LoginActivity)?.setCurrentItem(1,true)
        }

    }

    private fun setLanguagePreference() {
        fragmentLoginBinding?.run {
            if (fragmentLoginViewModel?.appSharedPref?.languagePref.equals(LanguageModes.ENG.get(), ignoreCase = true)) {
                tvhEng.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lang_pref_select))
                tvhAr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                tvhAr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lang_pref_select))
                tvhEng.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            tvhEng.setOnClickListener {
                try {
                    changeLabgPreference(LanguageModes.ENG.get())
                } catch (e:Exception){
                    println(e)
                }

            }
            tvhAr.setOnClickListener {
                try {
                    changeLabgPreference(LanguageModes.AR.get())
                } catch (e:Exception){
                    println(e)
                }
            }
        }
    }

    private fun changeLabgPreference(lgchg: String){
        fragmentLoginViewModel?.appSharedPref?.languagePref = lgchg
        navigate<LoginActivity>()
        activity?.finish()

    }

    private fun setupRemember() {
        isRemChecked = fragmentLoginViewModel?.appSharedPref?.isloginremember ?: false
        fragmentLoginBinding?.checkboxLoginremember?.isChecked = isRemChecked

        if(isRemChecked) {
            fragmentLoginBinding?.edtRootscareProviderEmail?.setText(fragmentLoginViewModel?.appSharedPref?.remUsername)
            fragmentLoginBinding?.edtRootscareProviderPassword?.setText(fragmentLoginViewModel?.appSharedPref?.remPwd)
        } else {
            fragmentLoginBinding?.edtRootscareProviderEmail?.setText("")
            fragmentLoginBinding?.edtRootscareProviderPassword?.setText("")
        }

    }

    // Validation checking for email and password
    private fun checkValidation(email: String, password: String): Boolean {
        if (fragmentLoginBinding?.txtRootscareLoginUserType?.text?.toString().equals("")) {
            Toast.makeText(activity, "Please select user type for login!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email == "") {
            Toast.makeText(activity, "Please enter your email!", Toast.LENGTH_SHORT).show()
            return false
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
//            activityLoginBinding?.edtEmailOrPhone?.setError("Please enter valid email id")
            Toast.makeText(activity, "Please enter valid email id!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidPassword(password)) {
            Toast.makeText(activity, "Please enter Password!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        return true
    }

    // Password validation method
    private fun isValidPassword(s: String): Boolean {
        val PASSWORD_PATTERN = Pattern.compile(
            "[a-zA-Z0-9!@#$]{8,24}"
        )

//        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches()

        return !TextUtils.isEmpty(s)
    }

    override fun successLoginResponse(loginResponse: ModelUserProfile?) {
        baseActivity?.hideLoading()
        if (loginResponse?.code.equals(SUCCESS_CODE)) {
            if (null != loginResponse?.result) {
                fragmentLoginViewModel?.appSharedPref?.isLoggedIn = true
                fragmentLoginViewModel?.appSharedPref?.isloginremember = isRemChecked
                fragmentLoginViewModel?.appSharedPref?.plcKey = loginResponse.result.place_key

                if(isRemChecked) {
                    fragmentLoginViewModel?.appSharedPref?.remUsername = fragmentLoginBinding?.edtRootscareProviderEmail?.text?.toString()
                    fragmentLoginViewModel?.appSharedPref?.remPwd = fragmentLoginBinding?.edtRootscareProviderPassword?.text?.toString()
                }

                when {
                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.NURSE.type) ||
                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.DOCTOR.type) ||
                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.LAB.type) ||
                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.HOSPITAL_DOCTOR.type) -> {
                        startActivity(activity?.let { NursrsHomeActivity.newIntent(it) })
                        activity?.finish()
                    }
                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.CAREGIVER.type) -> {
                        startActivity(activity?.let { CaregiverHomeActivity.newIntent(it) })
                        activity?.finish()
                    }
                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.BABYSITTER.type) -> {
                        startActivity(activity?.let { BabySitterHomeActivity.newIntent(it) })
                        activity?.finish()
                    }
                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.PHYSIOTHERAPY.type) -> {
                        startActivity(activity?.let { PhysiotherapyHomeActivity.newIntent(it) })
                        activity?.finish()
                    }

                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.HOSPITAL.type) -> {
                        startActivity(activity?.let { HospitalHomeActivity.newIntent(it) })
                        activity?.finish()
                    }
//                    loginResponse.result.user_type?.lowercase().equals(LoginTypes.LAB.type) -> {
//                        startActivity(activity?.let { LabTechnicianHomeActivity.newIntent(it) })
//                        activity?.finish()
//                    }

                }
            } else {
                showToast(loginResponse?.message?:"")
            }


        } else {
            CommonDialog.showDialogForSingleButton(requireActivity(), object : DialogClickCallback {
            }, null, loginResponse?.message?:"")
        }
    }

    override fun errorLoginResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        throwable?.message?.let { showToast(it)}
    }





}