package com.rootscare.serviceprovider.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.dialog.CommonDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rootscare.adapter.DrawerAdapter
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClickListener
import com.rootscare.model.DrawerDatatype
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment.FragmentPastAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment.FragmentRequestedAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.FragmentTodaysAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.FragmentDoctorMyschedule
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.FragmentdoctorManageSchedule
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime.FragmentAddDoctorScheduleTime
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.FragmentDoctorPaymentHistory
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRating
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfile
import com.rootscare.serviceprovider.ui.doctor.profile.bankDetails.FragmentBankDetails
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHome
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.FragmentHospitalManageNotification
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import com.rootscare.utils.BottomNavigationViewHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.*

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(), HomeActivityNavigator {
    private var activityHomeBinding: ActivityHomeBinding? = null
    private var homeViewModel: HomeActivityViewModel? = null
    private var drawerAdapter: DrawerAdapter? = null
    private var checkForClose = false

    var userFirstName = ""
    var userLastName = ""
    var userEmail = ""
    var userImage = ""
    var loginresponse: LoginResponse? = null

    companion object {

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, HomeActivity::class.java)
        }


        private var fragment_open_container: Int? = null
    }

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_home

    override val viewModel: HomeActivityViewModel
        get() {
            homeViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)
            return homeViewModel as HomeActivityViewModel
        }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
//            R.id.navigation_home -> {
//                checkFragmentInBackstackAndOpen(HomeFragment.newInstance())
//                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
//                return@OnNavigationItemSelectedListener true
//            }
//
//
//
//            R.id.navigation_booking -> {
//                checkFragmentInBackstackAndOpen(FragmentBookingAppointment.newInstance())
//                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_profile -> {
//                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
//                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
//                return@OnNavigationItemSelectedListener true
//            }
//
//            R.id.navigation_cart -> {
//                // checkFragmentInBackstackAndOpen(FragmentContact.newInstance())
//                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_media -> return@OnNavigationItemSelectedListener true
//            R.id.navigation_message -> return@OnNavigationItemSelectedListener true
//            R.id.navigation_profile -> return@OnNavigationItemSelectedListener true
//            R.id.navigation_contact -> return@OnNavigationItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logSentFriendRequestEvent()
        homeViewModel?.navigator = this
        activityHomeBinding = viewDataBinding
        setupPermissions()

        //        activityHomeBinding.navigation.enableAnimation(false);
        //        activityHomeBinding.navigation.enableShiftingMode(false);
        //        activityHomeBinding.navigation.enableItemShiftingMode(false);
        val gson = Gson()
        val loginmodeldataString: String = homeViewModel?.appSharedPref?.loginmodeldata!!
        loginresponse = gson.fromJson(loginmodeldataString, LoginResponse::class.java)
        BottomNavigationViewHelper.removeShiftMode(activityHomeBinding!!.navigation)

        drawerNavigationMenu()
        setDataAndSelectOptionInDrawerNavigation()
        val bottomNavigationView = activityHomeBinding!!.navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //        activityHomeBinding.navigation.setSelectedItemId(R.id.navigation_home);

        // Open Default fragment when app is open ed for 1st time
        //checkFragmentInBackstackAndOpen(AdmissionFragmentPageOne.newInstance(), activityHomeBinding!!.appBarHomepage.layoutContainer.id)
        checkFragmentInBackStackAndOpen(FragmentHome.newInstance())

    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    fun logSentFriendRequestEvent() {
       logger.logEvent("reachedHomeScreen")
    }

    private fun drawerNavigationMenu() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle.text = resources.getString(R.string.roots_care)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(false)
        }

        val constraintLayout = findViewById<ConstraintLayout>(R.id.parent_layout)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, activityHomeBinding!!.drawerLayout,
            toolbar, R.string.app_name, R.string.app_name
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)

                hideKeyboard()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                // To get the data
//                        val json: String = mPrefs.getString("MyObject", "")

                userFirstName = if (loginresponse?.result?.firstName != null && !loginresponse?.result?.firstName.equals("")) {
                    loginresponse?.result?.firstName!!
                } else {
                    ""
                }

                userLastName = if (loginresponse?.result?.lastName != null && !loginresponse?.result?.lastName.equals("")) {
                    loginresponse?.result?.lastName!!
                } else {
                    ""
                }

                userEmail = if (loginresponse?.result?.email != null && !loginresponse?.result?.email.equals("")) {
                    loginresponse?.result?.email!!
                } else {
                    ""
                }

                userImage = if (loginresponse?.result?.image != null && !loginresponse?.result?.image.equals("")) {
                    loginresponse?.result?.image!!
                } else {
                    ""
                }
                activityHomeBinding?.txtSidemenuName?.text = "$userFirstName $userLastName"
                activityHomeBinding?.txtSidemenueEmail?.text = (userEmail)

                val options: RequestOptions =
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.profile_no_image)
                        .priority(Priority.HIGH)
                Glide
                    .with(this@HomeActivity)
                    .load(BaseMediaUrls.USERIMAGE.url + userImage)
                    .apply(options)
                    .into(activityHomeBinding?.profileImage!!)

                hideKeyboard()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val moveFactor = activityHomeBinding!!.navView.width * slideOffset
                constraintLayout.translationX = moveFactor
            }

        }

        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        //        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.header_menu, HomeActivity.this.getTheme());
        //        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        activityHomeBinding!!.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack.setOnClickListener {
            if (activityHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            if (activityHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }


    //Set up drawer side menu item

    private fun setDataAndSelectOptionInDrawerNavigation() {
        // updateNavigationDrawerprofile();
        val linearLayoutManager = LinearLayoutManager(this@HomeActivity)
        activityHomeBinding!!.navigationDrawerRecyclerview.layoutManager = linearLayoutManager
        activityHomeBinding!!.navigationDrawerRecyclerview.setHasFixedSize(true)
        //     SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(HomeActivity.this);
        //  NotificationDatatype notificationDatatype = new Gson().fromJson(sharedPrefManager.getNotification(), NotificationDatatype.class);
        val strings = LinkedList<DrawerDatatype>()

        strings.add(DrawerDatatype("My Appointment", 0, R.drawable.my_appointment_side))
        strings.add(DrawerDatatype("My Schedule", 1, R.drawable.appointment_history))
        strings.add(DrawerDatatype("Profile", 2, R.drawable.cancel_appointment))
        strings.add(DrawerDatatype("Payment History", 3, R.drawable.payment_history))
//        strings.add(DrawerDatatype("Student LIVE Status", 6, 0))
        strings.add(DrawerDatatype("Review and Rating", 4, R.drawable.review_and_rating))
        strings.add(DrawerDatatype("Logout", 5, R.drawable.logout_icon))
//        strings.add(DrawerDatatype("Setting", 5, R.drawable.checked))
        drawerAdapter = DrawerAdapter(this@HomeActivity, strings)
        activityHomeBinding!!.navigationDrawerRecyclerview.adapter = drawerAdapter
        drawerAdapter!!.setonClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val menu = activityHomeBinding!!.navigation.menu
                for (i in 0 until menu.size()) {
                    val menuItem = menu.getItem(i)
                    /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
                    menuItem.isCheckable = false
                    menuItem.isChecked = false
                }
//                menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_deselect)
//                menu.findItem(R.id.navigation_booking).setIcon(R.drawable.booking_deselect)
//                menu.findItem(R.id.navigation_cart).setIcon(R.drawable.cart_deselect)
//                menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_deselect)
                //  menu.findItem(R.id.navigation_contact).setIcon(R.drawable.profile_deselect)
                if (activityHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    activityHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
                }
                when (position) {

                    0 -> checkFragmentInBackStackAndOpen(FragmentMyAppointment.newInstance())
                    1 -> checkFragmentInBackStackAndOpen(FragmentDoctorMyschedule.newInstance())
                    2 -> checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                    3 -> checkFragmentInBackStackAndOpen(FragmentDoctorPaymentHistory.newInstance())
                    4 -> checkFragmentInBackStackAndOpen(FragmentReviewAndRating.newInstance())
                    5 -> logout()


                }
            }

        })
    }


    override fun onBackPressed() {
        if (activityHomeBinding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1 || supportFragmentManager.findFragmentById(R.id.layout_container) is FragmentHome) {
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
        // Uncheck all menu item
        val menu = activityHomeBinding!!.navigation.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
            //            menuItem.setCheckable(false);
            menuItem.isChecked = false
        }
        showTextInToolbarRelativeToFragment(fragment!!)
    }


    private fun showTextInToolbarRelativeToFragment(fragment: Fragment) {

        val tootbar_text = activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle
        val tootbar_profile = activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarProfile
        val tootbar_notification = activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarNotification
        val tootbar_logout = activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarLogout
        val toolbar_back = activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarBack
        val toolbar_menu = activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack

        when (fragment) {
            is FragmentHome -> {
                tootbar_text.text = resources.getString(R.string.home)
           //     tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
                //            tootbar_text.setTextColor(resources.getColor(android.R.color.white))
                //   drawerAdapter!!.selectItem(-1)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.GONE
                toolbar_back.visibility = View.GONE
                toolbar_menu.visibility = View.VISIBLE

                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
                drawerAdapter?.setSelection(-1)
            }
            is FragmentDoctorProfile -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.my_profile)
                tootbar_profile.visibility = View.GONE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE

                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }

                //            tootbar_profile?.setOnClickListener(View.OnClickListener {
                //                checkFragmentInBackstackAndOpen(FragmentDoctorProfileHospital.newInstance())
                //            })
                tootbar_notification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })
                tootbar_logout.setOnClickListener(View.OnClickListener {
                    logout()
                })
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener(View.OnClickListener {
                    onBackPressed()
                })
            //    tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentEditDoctorProfile -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.edit_your_profile)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener({
                    onBackPressed()
                })
                tootbar_profile.setOnClickListener({
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                })
                tootbar_notification.setOnClickListener({
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })
                tootbar_logout.setOnClickListener({
                    logout()
                })
        //        tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentReviewAndRating -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.review_and_rating)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener({
                    onBackPressed()
                })
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
   //             tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentMyAppointment -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.my_appointment)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_profile.setOnClickListener {

                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })
                tootbar_logout.setOnClickListener(View.OnClickListener {
                    logout()
                })
 //               tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentUpcommingAppointment -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.upcoming_appointment)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener(View.OnClickListener {
                    onBackPressed()
                })
                tootbar_profile.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                })
                tootbar_notification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })
                tootbar_logout.setOnClickListener(View.OnClickListener {
                    logout()
                })
//                tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentTodaysAppointment -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.today_appointment)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener({
                    onBackPressed()
                })
                tootbar_profile.setOnClickListener({
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                })
                tootbar_notification.setOnClickListener({
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })
                tootbar_logout.setOnClickListener({
                    logout()
                })
     //           tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentRequestedAppointment -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.new_appointment)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
 //               tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentPastAppointment -> {
                tootbar_text.text = resources.getString(R.string.appointment_history)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
//                tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentDoctorMyschedule -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.my_schedule)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_notification.setOnClickListener(View.OnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                })
                tootbar_logout.setOnClickListener(View.OnClickListener {
                    logout()
                })
 //               tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentdoctorManageSchedule -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.manage_schedule)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
 //               tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentAddDoctorScheduleTime -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.add_schedule)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
  //              tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentDoctorPaymentHistory -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = resources.getString(R.string.payment_history)
                tootbar_profile.visibility = View.VISIBLE
                tootbar_notification.visibility = View.VISIBLE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }
                tootbar_profile.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentDoctorProfile.newInstance())
                }
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
//                tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
            is FragmentBankDetails -> {
                //   drawerAdapter!!.selectItem(0)
                tootbar_text.text = "Bank Details"
                tootbar_profile.visibility = View.GONE
                tootbar_notification.visibility = View.GONE
                tootbar_logout.visibility = View.GONE
                toolbar_back.visibility = View.VISIBLE
                toolbar_menu.visibility = View.GONE
                toolbar_back.setOnClickListener {
                    onBackPressed()
                }

                //            tootbar_profile?.setOnClickListener(View.OnClickListener {
                //                checkFragmentInBackstackAndOpen(FragmentDoctorProfileHospital.newInstance())
                //            })
                tootbar_notification.setOnClickListener {
                    checkFragmentInBackStackAndOpen(FragmentHospitalManageNotification.newInstance())
                }
                tootbar_logout.setOnClickListener {
                    logout()
                }
//                tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            }
        }

    }

    fun checkFragmentInBackStackAndOpen(fragment: Fragment) {

        val nameFragmentInBackstack = fragment.javaClass.name

        if (fragment_open_container == null) {
            fragment_open_container = activityHomeBinding!!.appBarHomepage.layoutContainer.id
        }

        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(nameFragmentInBackstack, 0)
        val ft = manager.beginTransaction()
        if (!fragmentPopped && manager.findFragmentByTag(nameFragmentInBackstack) == null) { //fragment not in back stack, create it.
            ft.replace(fragment_open_container!!, fragment, nameFragmentInBackstack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(nameFragmentInBackstack)
            ft.commit()
        } else if (manager.findFragmentByTag(nameFragmentInBackstack) != null) {
            /*String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();*/
            val currentFragment = manager.findFragmentByTag(nameFragmentInBackstack)
            ft.replace(fragment_open_container!!, currentFragment!!, nameFragmentInBackstack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(nameFragmentInBackstack)
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
        val fragment = supportFragmentManager.findFragmentById(activityHomeBinding?.appBarHomepage?.layoutContainer?.id!!)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        CommonDialog.showDialog(this@HomeActivity, object :
            DialogClickCallback {
            override fun onDismiss() {
            }

            override fun onConfirm() {
                if (isNetworkConnected) {
                    this@HomeActivity.showLoading()
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("user_id", homeViewModel?.appSharedPref?.loginUserId!!)
                    val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                    homeViewModel?.apiLogout(body)

                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        "Please check your network connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }, getString(R.string.logout), getString(R.string.sure_to_logout))
    }

    override fun successLogoutResponse(commonResponse: CommonResponse?) {
        this@HomeActivity.hideLoading()
        if (commonResponse?.code.equals("200")) {
//            homeViewModel?.appSharedPref?.deleteUserId()
            homeViewModel?.appSharedPref?.deleteLoginModelData()
            homeViewModel?.appSharedPref?.deleteIsLogINRemember()
            homeViewModel?.appSharedPref?.deleteLoginUserType()
            homeViewModel?.appSharedPref?.deleteLoginUserId()
            println("homeViewModel.loginUserType ${homeViewModel?.appSharedPref?.loginUserType}")
            startActivity(LoginActivity.newIntent(this@HomeActivity))
            finishAffinity()
        } else {
            Toast.makeText(this@HomeActivity, commonResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorLogoutResponse(throwable: Throwable?) {
        this@HomeActivity.hideLoading()
        if (throwable?.message != null) {
            Log.d(TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(this@HomeActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}
