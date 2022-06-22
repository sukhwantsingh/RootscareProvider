package com.rootscare.serviceprovider.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.rootscare.adapter.MyAdapter
import com.rootscare.model.RegistrationModel
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityLoginBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>(),
    LoginActivityNavigator {
    private var activityLoginBinding: ActivityLoginBinding? = null
    private var loginActivityViewModel: LoginActivityViewModel? = null
     var mAdapter: MyAdapter? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginActivityViewModel
        get() {
            loginActivityViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
            return loginActivityViewModel as LoginActivityViewModel
        }

    companion object {
        val TAG = LoginActivity::class.java.simpleName

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, LoginActivity::class.java)
        }

        private var fragment_open_container: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("loginActivityViewModel.loginUserType ${loginActivityViewModel?.appSharedPref?.loginUserType}")
        loginActivityViewModel!!.navigator = this
        activityLoginBinding = viewDataBinding

        mAdapter = MyAdapter(supportFragmentManager)
        activityLoginBinding?.viewPager?.adapter = mAdapter
        AppData.registrationModelData = RegistrationModel()
        setUpViewPager()
    }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        activityLoginBinding?.viewPager?.setCurrentItem(item, smoothScroll)
    }

    override fun onBackPressed() {
        if (activityLoginBinding?.viewPager?.currentItem == 0) {
            super.onBackPressed()
        } else {
            this.setCurrentItem(0, true)
        }
    }


    var isDepartmentMandatory: Boolean? = null
    private fun setUpViewPager() {
        with(activityLoginBinding!!) {
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    if (position == 0) {
                        viewPager.setPagingEnabled(true)
                    } else {
                        viewPager.setPagingEnabled(false)
                    }
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
        /*var fragment=supportFragmentManager.findFragmentById(ativityLoginBinding?.appBarHomepage?.layoutContainer?.id!!)
        fragment?.onActivityResult(requestCode, resultCode, data)*/
    }
}