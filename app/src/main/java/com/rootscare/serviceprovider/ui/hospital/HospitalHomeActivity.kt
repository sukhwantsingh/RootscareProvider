package com.rootscare.serviceprovider.ui.hospital

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
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
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.model.DrawerDatatype
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityHospitalHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.doctor.profile.bankDetails.FragmentBankDetails
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.FragmentHospitalManageAppointments
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.FragmentHospitalManageDepartment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctor
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.FragmentHospitalManageNotification
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.FragmentHospitalOrderManagement
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.subfragment.FragmentHospitalOrderDetails
import com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction.FragmentHospitalPaymentTransaction
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollection
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment.FragmentHospitalSampleCollectionDetails
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReport
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment.FragmentPathReportDocumentUpload
import com.rootscare.serviceprovider.ui.hospital.profiledoctorhospital.FragmentDoctorProfileHospital
import com.rootscare.serviceprovider.ui.hospital.subfragment.FragmentHospitalHome
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.manageDocLab.ManageDocAndLab
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointment
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.FragNewAppointmentListing
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule.FragmentNursesManageRate
import com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.FragmentNursesReviewAndRating
import com.rootscare.serviceprovider.ui.supportmore.SupportAndMore
import com.rootscare.serviceprovider.ui.transactionss.TransactionsMore
import com.rootscare.serviceprovider.utilitycommon.*
import com.rootscare.utils.firebase.Config
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class HospitalHomeActivity : BaseActivity<ActivityHospitalHomeBinding, HospitalHomeActivityViewModel>(),
    HospitalHomeActivityNavigator {

    private var activityHospitalHomeBinding: ActivityHospitalHomeBinding? = null
    private var hospitalHomeActivityViewModel: HospitalHomeActivityViewModel? = null

    private var drawerAdapter: DrawerAdapter? = null
    private var checkForClose = false

    var userFirstName = ""
    var userEmail = ""
    var userImage = ""
    var loginresponse: LoginResponse? = null

    companion object {
        fun newIntent(activity: Activity): Intent {
            return Intent(activity, HospitalHomeActivity::class.java)
        }
       private var fragment_open_container: Int? = null
    }

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_hospital_home
    override val viewModel: HospitalHomeActivityViewModel
        get() {
            hospitalHomeActivityViewModel = ViewModelProviders.of(this).get(HospitalHomeActivityViewModel::class.java)
            return hospitalHomeActivityViewModel as HospitalHomeActivityViewModel
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hospitalHomeActivityViewModel?.navigator = this
        activityHospitalHomeBinding = viewDataBinding
        setupPermissions()

        drawerNavigationMenu()
        setDataAndSelectOptionInDrawerNavigation()
        checkFragmentInBackStackAndOpen(FragmentHospitalHome.newInstance())

        observers()
      // hitNotificationUnread()
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
                    val mTlBar = activityHospitalHomeBinding?.appBarHomepage?.toolbarLayout
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
        loginresponse = hospitalHomeActivityViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref()
        loginresponse?.result?.let {
            userFirstName = if (it.firstName.isNullOrBlank().not()) { it.firstName.orEmpty()  } else ""
            userEmail = if (it.email.isNullOrBlank().not()) { it.email.orEmpty()  } else ""
            userImage = if (it.image.isNullOrBlank().not()) { it.image.orEmpty() } else ""
        }
        activityHospitalHomeBinding?.inclLayoutHospital?.run {
            txtSidemenuName.text = "$userFirstName".trim()
            txtSidemenueEmail.text = "$userEmail"
            tvhVersion.text = getAppVersionText()

            profileImage.setCircularRemoteImage(userImage)
            activityHospitalHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarAddMemberIvBack?.let {
                it.setHamburgerImage(userImage)
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
                addProperty("login_user_id", viewModel.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            viewModel.apiUnreadNotifications(body)
        } catch (e: Exception) {
            println(e)
        }
    }
    override fun onSuccessUnreadNotification(commonResponse: NotificationCountResponse?) {
        this@HospitalHomeActivity.hideLoading()
        if (commonResponse?.code.equals(SUCCESS_CODE,ignoreCase = true)) {
            NursrsHomeActivity.isUnreadNotficationAvailable.value = if(null!= commonResponse?.result && commonResponse.result != 0) {
                activityHospitalHomeBinding?.appBarHomepage?.toolbarLayout?.
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
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, activityHospitalHomeBinding!!.drawerLayout,  toolbar, R.string.app_name, R.string.app_name) {
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
                val moveFactor = activityHospitalHomeBinding!!.navView.width * slideOffset
                constraintLayout.translationX = moveFactor
            }

        }

        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        activityHospitalHomeBinding!!.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack.setOnClickListener {
            toggleDrawer()
        }
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            toggleDrawer()
        }

    }

    private fun toggleDrawer() {
        if (activityHospitalHomeBinding?.drawerLayout?.isDrawerVisible(GravityCompat.START) == true) {
            activityHospitalHomeBinding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            activityHospitalHomeBinding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }


    private fun setDataAndSelectOptionInDrawerNavigation() {
        val strings = LinkedList<DrawerDatatype>()

//        strings.add(DrawerDatatype("Manage Appointment", 0, R.drawable.manageappointment))
//        strings.add(DrawerDatatype("Manage Doctor", 1, R.drawable.managedoctor))
//        strings.add(DrawerDatatype("Manage Department", 2, R.drawable.managedepartment))
//        strings.add(DrawerDatatype("Order Management", 3, R.drawable.ordermanagement))
////        strings.add(DrawerDatatype("Student LIVE Status", 6, 0))
//        strings.add(DrawerDatatype("Sample Collection", 4, R.drawable.samplecollection))
//        strings.add(DrawerDatatype("Upload Pathology Report", 5, R.drawable.uploadpathologyreport))
//        strings.add(DrawerDatatype("Payment Transaction", 6, R.drawable.payment_history))
//        strings.add(DrawerDatatype("Bank Details", 7, R.drawable.payment_history))
//        strings.add(DrawerDatatype("Manage Rate", 8, R.drawable.payment_history))
//        strings.add(DrawerDatatype("Logout", 9, R.drawable.logout_icon))
//
////        strings.add(DrawerDatatype("Setting", 5, R.drawable.checked))
//        drawerAdapter = DrawerAdapter(this@HospitalHomeActivity, strings)
//        drawerAdapter!!.setonClickListener(object : OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//                when (position) {
//
//                    0 -> checkFragmentInBackStackAndOpen(FragmentHospitalManageAppointments.newInstance())
//                    1 -> checkFragmentInBackStackAndOpen(FragmenthospitalManageDoctor.newInstance())
//                    2 -> checkFragmentInBackStackAndOpen(FragmentHospitalManageDepartment.newInstance())
//                    3 -> checkFragmentInBackStackAndOpen(FragmentHospitalOrderManagement.newInstance())
//                    4 -> checkFragmentInBackStackAndOpen(FragmentHospitalSampleCollection.newInstance())
//                    5 -> checkFragmentInBackStackAndOpen(FragmentHospitalUploadPathologyReport.newInstance())
//                    6 -> checkFragmentInBackStackAndOpen(FragmentHospitalPaymentTransaction.newInstance())
//                    7 -> checkFragmentInBackStackAndOpen(FragmentBankDetails.newInstance())
//                    8 -> checkFragmentInBackStackAndOpen(FragmentNursesManageRate.newInstance("lab"))
//                    9 -> logout()
//
//
//                }
//            }
//        })

        // New impl..........
        activityHospitalHomeBinding?.inclLayoutHospital?.run {
            // appointment Doctor
            cnsAppointment.setOnClickListener {
             toggleDrawer()
             checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())
            }

            // appointment Lab
            cnsLabAppointment.setOnClickListener {
                toggleDrawer()
              checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())
            }

            // Manage Doctor and lab
            cnsManageDocLab.setOnClickListener {
                toggleDrawer()
                navigate<ManageDocAndLab>()
            }

            // profile setting
            cnsProfileSetting.setOnClickListener {
               toggleDrawer()
               checkFragmentInBackStackAndOpen(FragmentNursesProfile.newInstance())
            }
            profileImage.setOnClickListener { cnsProfileSetting.performClick() }
            profileImageCamera.setOnClickListener { cnsProfileSetting.performClick() }

            cnsTransactionHistory.setOnClickListener { toggleDrawer()
              navigate<TransactionsMore>()
            }

            cnsReviewRating.setOnClickListener {
                toggleDrawer()
             checkFragmentInBackStackAndOpen(FragmentNursesReviewAndRating.newInstance())
            }

            cnsSupportMore.setOnClickListener { toggleDrawer();
              navigate<SupportAndMore>()
            }

            llLogout.setOnClickListener { toggleDrawer();  logout() }
        }
    }

    override fun onBackPressed() {
        if (activityHospitalHomeBinding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHospitalHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1 || supportFragmentManager.findFragmentById(R.id.layout_container) is FragmentHospitalHome) {
                if (checkForClose) {
                    finish()
                }
                checkForClose = true
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ checkForClose = false }, 2000)
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

        val toolbarText = activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle
        val toolbarProfile = activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarProfile
        val toolbarNotification = activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarNotification
        val toolbarLogout = activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarLogout
        val toolbarBack = activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarBack
        val toolbarMenu = activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack

        val appointmentSearch = activityHospitalHomeBinding!!.appBarHomepage.toolbarLayout.toolbarSearch
        appointmentSearch.visibility = View.GONE
        appointmentSearch.setOnClickListener(null)

        when (fragment) {
            is FragmentNursesProfile -> {
                toolbarText.text = resources.getString(R.string.hospital)
                toolbarNotification.visibility = View.VISIBLE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener{ onBackPressed() }
                toolbarNotification.setOnClickListener{
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
            }
            is FragmentNursesEditProfile -> {
                toolbarText.text = resources.getString(R.string.edit_hospital)
                toolbarNotification.visibility = View.VISIBLE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener(View.OnClickListener {
                    onBackPressed()
                })
                toolbarNotification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })

            }
            is FragmentNursesReviewAndRating -> {
                toolbarText.text = resources.getString(R.string.review_and_rating)
                toolbarNotification.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener { onBackPressed() }

            }
            is FragmentHospitalManageNotification -> {
                toolbarText.text = resources.getString(R.string.notification)
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarNotification.visibility = View.GONE
                toolbarBack.setOnClickListener { onBackPressed() }
            }
            is FragmentNursesMyAppointment -> {
                toolbarText.text = resources.getString(R.string.my_appointment)
                toolbarNotification.visibility = View.VISIBLE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE

                appointmentSearch.visibility = View.VISIBLE
                appointmentSearch.setOnClickListener { FragNewAppointmentListing.showSearch.value = true }

                toolbarBack.setOnClickListener{ onBackPressed() }
                toolbarNotification.setOnClickListener(View.OnClickListener { checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance()) })

            }


            is FragmentHospitalHome -> {
                toolbarText.text = resources.getString(R.string.dashboard)
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE

                toolbarBack.visibility = View.GONE
                toolbarMenu.visibility = View.VISIBLE

                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }

            }
            is FragmentHospitalManageDepartment -> {
                toolbarText.text = "Address & Specialties"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE

                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }

                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }

            }
            is FragmenthospitalManageDoctor -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Manage Doctors"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }

                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentDoctorProfileHospital -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Doctor Profile"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }

                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalManageAppointments -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Appointment List"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }

            is FragmentHospitalOrderManagement -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Order Lists"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }

                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalOrderDetails -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Order Details"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }

                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalSampleCollection -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Sample Collection List"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalSampleCollectionDetails -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Booking Details"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalSampleCollection -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Pathology Report"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentPathReportDocumentUpload -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Upload Report"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }

                 toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalPaymentTransaction -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Payment Transaction"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalManageAppointments -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "All Booking"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.VISIBLE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentBankDetails -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Bank Details"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.GONE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentHospitalUploadPathologyReport -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Upload Pathology Report"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.GONE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }


                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }
            is FragmentNursesManageRate -> {
                //   drawerAdapter!!.selectItem(0)
                toolbarText.text = "Manage Rate"
                toolbarProfile.visibility = View.GONE
                toolbarNotification.visibility = View.GONE
                toolbarLogout.visibility = View.GONE
                toolbarBack.visibility = View.VISIBLE
                toolbarMenu.visibility = View.GONE
                toolbarBack.setOnClickListener {
                    onBackPressed()
                }

                toolbarNotification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                toolbarLogout.setOnClickListener {
                    logout()
                }
            }


        }


    }

    fun checkFragmentInBackStackAndOpen(fragment: Fragment) {
        val nameFragmentInBackStack = fragment.javaClass.name

        if (fragment_open_container == null) {
            fragment_open_container = activityHospitalHomeBinding!!.appBarHomepage.layoutContainer.id
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
        val fragment = supportFragmentManager.findFragmentById(activityHospitalHomeBinding?.appBarHomepage?.layoutContainer?.id!!)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        CommonDialog.showDialog(this@HospitalHomeActivity, object :
            DialogClickCallback {
            override fun onDismiss() {
            }

            override fun onConfirm() {
                if (isNetworkConnected) {
                    this@HospitalHomeActivity.showLoading()
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("user_id", hospitalHomeActivityViewModel?.appSharedPref?.loginUserId!!)
                    val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                    hospitalHomeActivityViewModel?.apiLogout(body)

                } else {
                    Toast.makeText(
                        this@HospitalHomeActivity,
                        getString(R.string.check_network_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

        }, getString(R.string.logout), getString(R.string.sure_to_logout))
    }

    override fun successLogoutResponse(commonResponse: CommonResponse?) {
        this@HospitalHomeActivity.hideLoading()
        hospitalHomeActivityViewModel?.appSharedPref?.deleteLoginModelData()
        hospitalHomeActivityViewModel?.appSharedPref?.deleteIsLogINRemember()
        hospitalHomeActivityViewModel?.appSharedPref?.deleteLoginUserType()
        hospitalHomeActivityViewModel?.appSharedPref?.deleteLoginUserId()
        startActivity(LoginActivity.newIntent(this@HospitalHomeActivity))
        finishAffinity()

        if (commonResponse?.code.equals("200")) {

        } else {
            Toast.makeText(this@HospitalHomeActivity, commonResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorLogoutResponse(throwable: Throwable?) {
        this@HospitalHomeActivity.hideLoading()
        if (throwable?.message != null) {
            showToast(getString(R.string.something_went_wrong))
        }
    }
}
