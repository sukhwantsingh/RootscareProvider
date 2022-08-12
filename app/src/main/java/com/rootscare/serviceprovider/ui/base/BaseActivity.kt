package com.rootscare.serviceprovider.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.facebook.appevents.AppEventsLogger
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.getAppLocale
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
import com.rootscare.utils.CommonUtils
import com.rootscare.utils.NetworkUtils
import java.util.*


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(), BaseFragment.Callback {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private var mProgressDialog: ProgressDialog? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    lateinit var logger: AppEventsLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger = AppEventsLogger.Companion.newLogger(this)
        performDataBinding()

    }


    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding?.setVariable(bindingVariable, mViewModel)
        viewDataBinding?.executePendingBindings()
    }

    fun openDialogFragment(dialogFragment: DialogFragment) {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(dialogFragment.javaClass.name)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragment.show(ft, dialogFragment.javaClass.name)
    }

    fun allowProvider(): Boolean {
        val mModel = viewModel.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()
        return when {
            mModel?.result?.user_type.equals(LoginTypes.LAB.type, ignoreCase = true) &&
            mModel?.result?.hospital_id.isNullOrBlank().not() -> {
                showToast("Has not implemented yet!"); false
            }
            else -> true
        }
    }

    override fun attachBaseContext(newBase: Context) {
        // Log.e("langMode", ": ${getAppLocale()}")
        super.attachBaseContext(LangContextWrapper.wrap(newBase, getAppLocale()))
    }

}

class LangContextWrapper private constructor(base: Context) : ContextWrapper(base) {

    companion object {

        private val enLocale = Locale("en", "US")
        private val arLocale = Locale("ar", "SA")

        fun wrap(baseContext: Context, language: String): ContextWrapper {
            var wrappedContext = baseContext
            val config = Configuration(baseContext.resources.configuration)

            if (language.isNotBlank()) {
                val locale = returnOrCreateLocale(language)
                Locale.setDefault(locale)
                config.setLocale(locale)
                config.setLayoutDirection(locale)
                wrappedContext = baseContext.createConfigurationContext(config)
            }
            return LangContextWrapper(wrappedContext)
        }

        private fun returnOrCreateLocale(language: String): Locale {
            return when (language) {
                "en" -> enLocale
                "ar" -> arLocale
                else -> Locale(language)
            }
        }
    }

}

