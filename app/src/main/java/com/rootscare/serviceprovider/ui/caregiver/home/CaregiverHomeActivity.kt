package com.rootscare.serviceprovider.ui.caregiver.home

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
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.rootscare.adapter.DrawerAdapter
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.NotificationCountResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityCaregiverHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.FragmentCaregiverUpdateProfile
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHome
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.FragmentHospitalManageNotification
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivityNavigator
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivityViewModel
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

class CaregiverHomeActivity : BaseActivity<ActivityCaregiverHomeBinding, NursrsHomeActivityViewModel>(),
    NursrsHomeActivityNavigator {
    private var activityCaregiverHomeBinding: ActivityCaregiverHomeBinding? = null
    private var caregiverHomeActivityViewModel: NursrsHomeActivityViewModel? = null

    private var drawerAdapter: DrawerAdapter? = null
    private var checkForClose = false

    var userFirstName = ""
    var userLastName = ""
    var userEmail = ""
    var userImage = ""
    var loginresponse: ModelUserProfile? = null

    companion object {

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, CaregiverHomeActivity::class.java)
        }

        private var fragment_open_container: Int? = null
    }

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_caregiver_home
    override val viewModel: NursrsHomeActivityViewModel
        get() {
            caregiverHomeActivityViewModel = ViewModelProviders.of(this).get(
                NursrsHomeActivityViewModel::class.java
            )
            return caregiverHomeActivityViewModel as NursrsHomeActivityViewModel
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        caregiverHomeActivityViewModel?.navigator = this
        activityCaregiverHomeBinding = viewDataBinding


        setupPermissions()

        drawerNavigationMenu()
        setDataAndSelectOptionInDrawerNavigation()
        checkFragmentInBackStackAndOpen(FragmentCaregiverHome.newInstance())
        observers()
        hitNotificationUnread()
    }

    private fun observers() {
        NursrsHomeActivity.isNotificationChecked.observe(this) {
            try {
                it?.let {
                    if(it) {
                        hitNotificationUnread()
                        NursrsHomeActivity.isNotificationChecked.value = false
                    }
                }
            }catch (e: java.lang.Exception){
                println(e)
            }
        }
        NursrsHomeActivity.isUnreadNotficationAvailable.observe(this) {
            try {
                it?.let {
                    val mTlBar = activityCaregiverHomeBinding?.appBarHomepage?.toolbarLayout
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
        loginresponse = caregiverHomeActivityViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref()
        loginresponse?.result?.let {
            // setup values to the variable for latter use
            userFirstName = if (it.first_name.isNullOrBlank().not()) { it.first_name ?: "" } else ""
            userLastName = if (it.last_name.isNullOrBlank().not()) { it.last_name ?: ""  } else ""
            userEmail = if (it.email.isNullOrBlank().not()) { it.email ?: "" } else ""
            userImage = if (it.image.isNullOrBlank().not()) { it.image ?: "" } else ""
        }
        activityCaregiverHomeBinding?.inclLayoutProviders?.run {
            txtSidemenuName.text = "$userFirstName $userLastName"
            txtSidemenueEmail.text = "$userEmail"
            tvhVersion.text = getAppVersionText()

            profileImage.setCircularRemoteImage(userImage)
            activityCaregiverHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarAddMemberIvBack?.let { it.setHamburgerImage(userImage) }

        }
    }

    override fun onStart() {
        super.onStart()
        hitNotificationUnread()
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
                addProperty("login_user_id", caregiverHomeActivityViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
         caregiverHomeActivityViewModel?.apiUnreadNotifications(body)
        } catch (e: Exception) {
            println(e)
        }
    }

    // Change once the api received
    override fun onSuccessUnreadNotification(commonResponse: NotificationCountResponse?) {
          hideLoading()
        if (commonResponse?.code.equals(SUCCESS_CODE,ignoreCase = true)) {
            NursrsHomeActivity.isUnreadNotficationAvailable.value = if(commonResponse?.result != null && commonResponse.result == 0) {
                activityCaregiverHomeBinding?.appBarHomepage?.toolbarLayout?.
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
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, activityCaregiverHomeBinding?.drawerLayout, toolbar, R.string.app_name, R.string.app_name ) {
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
                val moveFactor = activityCaregiverHomeBinding!!.navView.width * slideOffset
                constraintLayout.translationX = moveFactor
            }

        }



        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        //        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.header_menu, HomeActivity.this.getTheme());
        //        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        activityCaregiverHomeBinding!!.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack.setOnClickListener {
            if (activityCaregiverHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityCaregiverHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityCaregiverHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            if (activityCaregiverHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityCaregiverHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityCaregiverHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }

    private fun setDataAndSelectOptionInDrawerNavigation() {
        activityCaregiverHomeBinding?.inclLayoutProviders?.run {
            // appointment
            cnsAppointment.setOnClickListener { toggleDrawer()
                checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())}
            // price-list
            cnsPricelist.setOnClickListener { toggleDrawer();
//                checkFragmentInBackStackAndOpen(FragmentCaregiverUpdateManageRate.newInstance())
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
        if (activityCaregiverHomeBinding?.drawerLayout?.isDrawerVisible(GravityCompat.START) == true) {
            activityCaregiverHomeBinding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            activityCaregiverHomeBinding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }


    override fun onBackPressed() {
        if (activityCaregiverHomeBinding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityCaregiverHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1 || supportFragmentManager.findFragmentById(R.id.layout_container) is FragmentCaregiverHome) {
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

        val tootbar_text = activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle

        val tootbar_profile = activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarProfile
        val tootbar_notification = activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarNotification
        val tootbar_logout = activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarLogout
        val toolbar_back = activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarBack
        val toolbar_menu = activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack

        val appointmentSearch = activityCaregiverHomeBinding!!.appBarHomepage.toolbarLayout.toolbarSearch
        appointmentSearch.visibility = View.GONE
        appointmentSearch.setOnClickListener(null)

        when (fragment) {
            is FragmentCaregiverHome -> {
                tootbar_text.text = resources.getString(R.string.dashboard)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.GONE
                toolbar_menu.visibility = View.VISIBLE
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentCaregiverUpdateProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
                //  drawerAdapter!!.setSelection(-1)
            }
            is FragmentNursesProfile -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.my_profile)
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
            is FragmentNursesEditProfile -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.edit_your_profile)
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener(View.OnClickListener {
                    onBackPressed()
                })
                tootbar_notification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })

            }
            is FragmentNursesMyAppointment -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.my_appointment)
                tootbar_notification.visibility = View.VISIBLE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE

                appointmentSearch.visibility = View.VISIBLE
                appointmentSearch.setOnClickListener { FragNewAppointmentListing.showSearch.value = true }

                toolbar_back.setOnClickListener {onBackPressed() }
                tootbar_notification.setOnClickListener { checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())}

            }
            is FragmentHospitalManageNotification -> {
                tootbar_text.text = resources.getString(R.string.notification)
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                tootbar_notification.visibility = View.GONE
                toolbar_back.setOnClickListener { onBackPressed() }
            }
            is FragmentNursesReviewAndRating -> {
                tootbar_text.text = getString(R.string.review_amp_rating)
                tootbar_notification.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener { onBackPressed() }
//                tootbar_notification.setOnClickListener {
//                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
//                }

            }


        }
    }

    fun checkFragmentInBackStackAndOpen(fragment: Fragment) {

        val name_fragment_in_backstack = fragment.javaClass.name

        if (fragment_open_container == null) {
            fragment_open_container = activityCaregiverHomeBinding!!.appBarHomepage.layoutContainer.id
        }

        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(name_fragment_in_backstack, 0)
        val ft = manager.beginTransaction()
        if (!fragmentPopped && manager.findFragmentByTag(name_fragment_in_backstack) == null) { //fragment not in back stack, create it.
            ft.replace(fragment_open_container!!, fragment, name_fragment_in_backstack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(name_fragment_in_backstack)
            ft.commit()
        } else if (manager.findFragmentByTag(name_fragment_in_backstack) != null) {
            /*String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();*/
            val currentFragment = manager.findFragmentByTag(name_fragment_in_backstack)
            ft.replace(fragment_open_container!!, currentFragment!!, name_fragment_in_backstack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(name_fragment_in_backstack)
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
        val fragment = supportFragmentManager.findFragmentById(activityCaregiverHomeBinding?.appBarHomepage?.layoutContainer?.id!!)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        CommonDialog.showDialog(this@CaregiverHomeActivity, object :
            DialogClickCallback {
            override fun onDismiss() {
            }

            override fun onConfirm() {
                if (isNetworkConnected) {
                    this@CaregiverHomeActivity.showLoading()
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("user_id", caregiverHomeActivityViewModel?.appSharedPref?.loginUserId!!)
                    val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                    caregiverHomeActivityViewModel?.apiLogout(body)

                } else {
                    Toast.makeText(
                        this@CaregiverHomeActivity,
                        "Please check your network connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }, "Logout", "Are you sure you want to logout?")
    }

    override fun successLogoutResponse(commonResponse: CommonResponse?) {
        this@CaregiverHomeActivity.hideLoading()
        if (commonResponse?.code.equals("200")) {
//                homeViewModel?.appSharedPref?.deleteUserId()
            caregiverHomeActivityViewModel?.appSharedPref?.deleteLoginModelData()
            caregiverHomeActivityViewModel?.appSharedPref?.deleteIsLogINRemember()
            caregiverHomeActivityViewModel?.appSharedPref?.deleteLoginUserType()
            caregiverHomeActivityViewModel?.appSharedPref?.deleteLoginUserId()
            startActivity(LoginActivity.newIntent(this@CaregiverHomeActivity))
            finishAffinity()
        } else {
            Toast.makeText(this@CaregiverHomeActivity, commonResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorLogoutResponse(throwable: Throwable?) {
        this@CaregiverHomeActivity.hideLoading()
        if (throwable?.message != null) {
            Log.d(TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(this@CaregiverHomeActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}
