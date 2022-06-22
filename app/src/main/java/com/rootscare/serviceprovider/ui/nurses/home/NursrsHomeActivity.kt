package com.rootscare.serviceprovider.ui.nurses.home

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.NotificationCountResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityNursrsHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.FragmentHospitalManageNotification
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHome
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointment
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.FragNewAppointmentListing
import com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.FragmentNursesReviewAndRating
import com.rootscare.serviceprovider.ui.pricelistss.PriceListScreen
import com.rootscare.serviceprovider.ui.scheduless.ScheduleActivity
import com.rootscare.serviceprovider.ui.supportmore.SupportAndMore
import com.rootscare.serviceprovider.ui.transactionss.TransactionsMore
import com.rootscare.serviceprovider.utilitycommon.*
import com.rootscare.utils.firebase.Config

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class NursrsHomeActivity : BaseActivity<ActivityNursrsHomeBinding, NursrsHomeActivityViewModel>(), NursrsHomeActivityNavigator {
    private var activityNurseHomeBinding: ActivityNursrsHomeBinding? = null
    private var nurseHomeActivityViewModel: NursrsHomeActivityViewModel? = null

    private var checkForClose = false

    var userFirstName = ""
    var userLastName = ""
    var userEmail = ""
    var userImage = ""
    var loginresponse: ModelUserProfile? = null

    companion object {
        var isNotificationChecked = MutableLiveData<Boolean?>(false)
        var isUnreadNotficationAvailable = MutableLiveData<Boolean?>(null)

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, NursrsHomeActivity::class.java)
        }

        private var fragment_open_container: Int? = null
    }

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_nursrs_home
    override val viewModel: NursrsHomeActivityViewModel
        get() {
            nurseHomeActivityViewModel = ViewModelProviders.of(this).get(NursrsHomeActivityViewModel::class.java)
            return nurseHomeActivityViewModel as NursrsHomeActivityViewModel
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nurseHomeActivityViewModel?.navigator = this
        activityNurseHomeBinding = viewDataBinding

        setupPermissions()

        drawerNavigationMenu()
        setDataAndSelectOptionInDrawerNavigation()
        checkFragmentInBackStackAndOpen(FragmentNurseHome.newInstance())
        observers()
        hitNotificationUnread()
    }

    private fun observers() {
        isNotificationChecked.observe(this) {
            try {
                it?.let {
                    if(it) {
                        hitNotificationUnread()
                        isNotificationChecked.value = false
                    }
                }
            }catch (e: java.lang.Exception){
                println(e)
            }
        }
        isUnreadNotficationAvailable.observe(this) {
            try {
                it?.let {
                    val mTlBar = activityNurseHomeBinding?.appBarHomepage?.toolbarLayout
                    if(it) {
                        mTlBar?.tvUnreadCount?.visibility = View.VISIBLE
                    } else mTlBar?.tvUnreadCount?.visibility = View.GONE
                }
            }catch (e: java.lang.Exception){
                println(e)
            }
        }
    }

    fun init() {
        loginresponse = nurseHomeActivityViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref()
        activityNurseHomeBinding?.inclLayoutProviders?.run {
        loginresponse?.result?.let {
                // setup values to the variable for latter use
                userFirstName = if (it.first_name.isNullOrBlank().not()) { it.first_name ?: "" } else ""
                userLastName = if (it.last_name.isNullOrBlank().not()) { it.last_name ?: "" } else ""
                userEmail = if (it.email.isNullOrBlank().not()) { it.email ?: "" } else ""
                userImage = if (it.image.isNullOrBlank().not()) { it.image ?: "" } else ""

            txtSidemenuName.text = "$userFirstName $userLastName"
            txtSidemenueEmail.text = if(it.hospital_id.isNullOrBlank()) "$userEmail" else "${it.hospital_name}"
            tvhVersion.text = getAppVersionText()

            profileImage.setCircularRemoteImage(userImage)
            activityNurseHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarAddMemberIvBack?.let { it.setHamburgerImage(userImage) }

        }
        }

    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(brForNotificaiton, IntentFilter(Config.PUSH_NOTIFICATION))
        init()
    }

    val brForNotificaiton = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                Config.PUSH_NOTIFICATION -> hitNotificationUnread()
            }
        }
    }

    fun hitNotificationUnread() {
        try {
            val jsonObject = JsonObject().apply {
                addProperty("login_user_id", nurseHomeActivityViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
          nurseHomeActivityViewModel?.apiUnreadNotifications(body)
        } catch (e: Exception) {
            println(e)
        }
    }

    // Change once the api received
    override fun onSuccessUnreadNotification(commonResponse: NotificationCountResponse?) {
        this@NursrsHomeActivity.hideLoading()
        if (commonResponse?.code.equals(SUCCESS_CODE,ignoreCase = true)) {
            isUnreadNotficationAvailable.value = if(null != commonResponse?.result && commonResponse.result != 0) {
                activityNurseHomeBinding?.appBarHomepage?.toolbarLayout?.
                toolbarNotification?.startAnimation(doAnimation(R.anim.shake))
                true } else false
        } else {
            showToast(commonResponse?.message ?: "")
        }
    }


    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(brForNotificaiton)
    }



    private fun drawerNavigationMenu() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(false)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.parent_layout)
        val actionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, activityNurseHomeBinding?.drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)
                    hideKeyboard()
                }

                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    hideKeyboard()
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val moveFactor = activityNurseHomeBinding!!.navView.width * slideOffset
                    constraintLayout.translationX = moveFactor
                }

            }

        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        activityNurseHomeBinding?.drawerLayout?.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        activityNurseHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarAddMemberIvBack?.setOnClickListener {
          toggleDrawer()
        }
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            toggleDrawer()
        }



    }

    private fun setDataAndSelectOptionInDrawerNavigation() {
        activityNurseHomeBinding?.inclLayoutProviders?.run {
            // appointment
            cnsAppointment.setOnClickListener {
                toggleDrawer()
             checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())
            }

            // price-list
            cnsPricelist.setOnClickListener { toggleDrawer();
              navigate<PriceListScreen>()
            }

            // Schedule availability
            cnsSchedule.setOnClickListener { toggleDrawer(); navigate<ScheduleActivity>() }

            // profile setting
            cnsProfileSetting.setOnClickListener { toggleDrawer(); checkFragmentInBackStackAndOpen(FragmentNursesProfile.newInstance()) }
            profileImage.setOnClickListener { cnsProfileSetting.performClick() }
            profileImageCamera.setOnClickListener { cnsProfileSetting.performClick() }

            // transaction history
            cnsTransactionHistory.setOnClickListener { toggleDrawer()
                navigate<TransactionsMore>()
            }

            // Review and rating
            cnsReviewRating.setOnClickListener { toggleDrawer(); checkFragmentInBackStackAndOpen(FragmentNursesReviewAndRating.newInstance()) }

            // Support and more
            cnsSupportMore.setOnClickListener { toggleDrawer(); navigate<SupportAndMore>() }

            // Logout
            llLogout.setOnClickListener { toggleDrawer();  logout() }
        }

    }

    private fun toggleDrawer() {
        if (activityNurseHomeBinding?.drawerLayout?.isDrawerVisible(GravityCompat.START) == true) {
            activityNurseHomeBinding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            activityNurseHomeBinding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (activityNurseHomeBinding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            activityNurseHomeBinding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1 || supportFragmentManager.findFragmentById(R.id.layout_container) is FragmentNurseHome) {
                if (checkForClose) {
                    finish()
                }
                checkForClose = true
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({ checkForClose = false }, 2000)
            } else {
                super.onBackPressed()
                showSelectionOfBottomNavigationItem()
            }
        }
    }

    private fun showSelectionOfBottomNavigationItem() {
        val fragment = supportFragmentManager.findFragmentById(R.id.layout_container)
        showTextInToolbarRelativeToFragment(fragment!!)
    }


    private fun showTextInToolbarRelativeToFragment(fragment: Fragment) {
        val tootbar_text = activityNurseHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle
        val tootbar_notification = activityNurseHomeBinding!!.appBarHomepage.toolbarLayout.toolbarNotification
        val toolbar_back = activityNurseHomeBinding!!.appBarHomepage.toolbarLayout.toolbarBack
        val toolbar_menu = activityNurseHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack

        val appointmentSearch = activityNurseHomeBinding!!.appBarHomepage.toolbarLayout.toolbarSearch
        appointmentSearch.visibility = View.GONE
        appointmentSearch.setOnClickListener(null)

        when (fragment) {
            is FragmentNurseHome -> {
                tootbar_text.text = resources.getString(R.string.dashboard)
                tootbar_notification.visibility = View.VISIBLE
                toolbar_back.visibility = View.GONE
                toolbar_menu.visibility = View.VISIBLE

                tootbar_notification.setOnClickListener { checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance()) }
              }
            is FragmentNursesProfile -> {
                tootbar_text.text = resources.getString(R.string.my_profile)
                tootbar_notification.visibility = View.VISIBLE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener{ onBackPressed() }
                tootbar_notification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })
            }
            is FragmentNursesEditProfile -> {
                tootbar_text.text = resources.getString(R.string.edit_your_profile)
                tootbar_notification.visibility = View.VISIBLE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener(View.OnClickListener {
                    onBackPressed()
                })
                tootbar_notification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })

            }
            is FragmentNursesReviewAndRating -> {
                tootbar_text.text = resources.getString(R.string.review_and_rating)
                tootbar_notification.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener { onBackPressed() }

            }
            is FragmentHospitalManageNotification -> {
                tootbar_text.text = resources.getString(R.string.notification)
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                tootbar_notification.visibility = View.GONE
                toolbar_back.setOnClickListener { onBackPressed() }
            }
            is FragmentNursesMyAppointment -> {
                tootbar_text.text = resources.getString(R.string.my_appointment)
                tootbar_notification.visibility = View.VISIBLE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE

                appointmentSearch.visibility = View.VISIBLE
                appointmentSearch.setOnClickListener { FragNewAppointmentListing.showSearch.value = true }

                toolbar_back.setOnClickListener{ onBackPressed() }
                tootbar_notification.setOnClickListener{ checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())}

            }

        }
    }

    fun checkFragmentInBackStackAndOpen(fragment: Fragment) {

        val nameFragmentInBackStack = fragment.javaClass.name

        if (fragment_open_container == null && activityNurseHomeBinding?.appBarHomepage?.layoutContainer?.id != null) {
            fragment_open_container = activityNurseHomeBinding?.appBarHomepage?.layoutContainer?.id
        }

        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(nameFragmentInBackStack, 0)
        val ft = manager.beginTransaction()
        if (!fragmentPopped && manager.findFragmentByTag(nameFragmentInBackStack) == null) { //fragment not in back stack, create it.
            ft.replace(fragment_open_container!!, fragment, nameFragmentInBackStack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(nameFragmentInBackStack)
            ft.commit()
        } else if (manager.findFragmentByTag(nameFragmentInBackStack) != null) {
            /*String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();*/
            val currentFragment = manager.findFragmentByTag(nameFragmentInBackStack)
            ft.replace(fragment_open_container!!, currentFragment!!, nameFragmentInBackStack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(nameFragmentInBackStack)
            ft.commit()
        }
        showTextInToolbarRelativeToFragment(fragment)
    }


    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CONTACTS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_CONTACTS),
            RECORD_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(activityNurseHomeBinding?.appBarHomepage?.layoutContainer?.id!!)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        CommonDialog.showDialog(this@NursrsHomeActivity, object : DialogClickCallback {
            override fun onConfirm() {
                if (isNetworkConnected) {
                    this@NursrsHomeActivity.showLoading()
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("user_id", nurseHomeActivityViewModel?.appSharedPref?.loginUserId!!)
                    val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                    nurseHomeActivityViewModel?.apiLogout(body)

                } else {
                    Toast.makeText(
                        this@NursrsHomeActivity,
                        getString(R.string.check_network_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        },getString(R.string.logout), getString(R.string.sure_to_logout))
    }

    override fun successLogoutResponse(commonResponse: CommonResponse?) {
        this@NursrsHomeActivity.hideLoading()
        nurseHomeActivityViewModel?.appSharedPref?.deleteLoginModelData()
        nurseHomeActivityViewModel?.appSharedPref?.deleteIsLogINRemember()
        nurseHomeActivityViewModel?.appSharedPref?.deleteLoginUserType()
        nurseHomeActivityViewModel?.appSharedPref?.deleteLoginUserId()
        startActivity(LoginActivity.newIntent(this@NursrsHomeActivity))
        finishAffinity()

        if (commonResponse?.code.equals("200")) {

        } else {
            Toast.makeText(this@NursrsHomeActivity, commonResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorLogoutResponse(throwable: Throwable?) {
        this@NursrsHomeActivity.hideLoading()
        if (throwable?.message != null) {
            showToast(getString(R.string.something_went_wrong))
        }
    }
}
