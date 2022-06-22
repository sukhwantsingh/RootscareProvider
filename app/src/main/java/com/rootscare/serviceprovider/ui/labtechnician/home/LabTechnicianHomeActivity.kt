package com.rootscare.serviceprovider.ui.labtechnician.home

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
import com.dialog.CommonDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import com.rootscare.adapter.DrawerAdapter
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClickListener
import com.rootscare.model.DrawerDatatype
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityLabTechnicianHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRating
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.FragmentHospitalManageNotification
import com.rootscare.serviceprovider.ui.labtechnician.home.subfragment.FragmentLabTechnicianHome
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmanageprofile.FragmentLabtechnicianManageProfile
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.FragmentLabtechnicianMyAppointment
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.subfragment.FragmentLabTechnicianMyAppointmentDetails
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyspeciality.FragmentLabTechnicianMySpeciality
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.utils.BottomNavigationViewHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.*

class LabTechnicianHomeActivity : BaseActivity<ActivityLabTechnicianHomeBinding, LabTechnicianHomeActivityViewModel>(),
    LabTechnicianHomeActivityNavigator {
    private var activityLabTechnicianHomeBinding: ActivityLabTechnicianHomeBinding? = null
    private var labTechnicianHomeActivityViewModel: LabTechnicianHomeActivityViewModel? = null
    private var drawerAdapter: DrawerAdapter? = null
    private var check_for_close = false

    private var studentName: String = ""
    private var studentEmail: String = ""
    private var studentPrifileImage: String = ""

    companion object {

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, LabTechnicianHomeActivity::class.java)
        }


        private var fragment_open_container: Int? = null
    }

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_lab_technician_home

    override val viewModel: LabTechnicianHomeActivityViewModel
        get() {
            labTechnicianHomeActivityViewModel = ViewModelProviders.of(this).get(LabTechnicianHomeActivityViewModel::class.java)
            return labTechnicianHomeActivityViewModel as LabTechnicianHomeActivityViewModel
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
        labTechnicianHomeActivityViewModel!!.navigator = this
        activityLabTechnicianHomeBinding = viewDataBinding
        setupPermissions()

        //        activityLabTechnicianHomeBinding.navigation.enableAnimation(false);
        //        activityLabTechnicianHomeBinding.navigation.enableShiftingMode(false);
        //        activityLabTechnicianHomeBinding.navigation.enableItemShiftingMode(false);

        BottomNavigationViewHelper.removeShiftMode(activityLabTechnicianHomeBinding!!.navigation)

        drawerNavigationMenu()
        setDataAndSelectOptionInDrawerNavigation()
        val bottomNavigationView = activityLabTechnicianHomeBinding!!.navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //        activityLabTechnicianHomeBinding.navigation.setSelectedItemId(R.id.navigation_home);

        // Open Default fragment when app is open ed for 1st time
        //checkFragmentInBackstackAndOpen(AdmissionFragmentPageOne.newInstance(), activityLabTechnicianHomeBinding!!.appBarHomepage.layoutContainer.id)
        checkFragmentInBackstackAndOpen(FragmentLabTechnicianHome.newInstance())

//
//        activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarProfile.setOnClickListener {
//            //a checkFragmentInBackstackAndOpen(ProfileFragment.newInstance())
//            CommonDialog.showDialog(this, object : DialogClickCallback {
//                override fun onDismiss() {
//                }
//
//                override fun onConfirm() {
//                    homeViewModel?.appSharedPref?.deleteUserId()
//                    homeViewModel?.appSharedPref?.deleteUserName()
//                    homeViewModel?.appSharedPref?.deleteUserEmail()
//                    homeViewModel?.appSharedPref?.deleteUserImage()
//                    homeViewModel?.appSharedPref?.deleteUserType()
//                    homeViewModel?.appSharedPref?.deleteStudentLogInPassword()
//                    startActivity(ActivityLogInOption.newIntent(this@HomeActivity))
//                    finishAffinity()
//
//                }
//
//            }, "Logout", "Are you sure you want to logout?")
//
//        }

//        activityLabTechnicianHomeBinding!!.drawerLayout.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


    }

    /*private void checkFragmentInBackstackAndOpen(FragmentStudentAttendanceRecord fragment) {
        String name_fragment_in_backstack = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.layout_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(name_fragment_in_backstack);
        ft.commit();
        //  showTextInToolbarRelativeToFragment(fragment);
    }*/

    private fun drawerNavigationMenu() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle.text = resources.getString(R.string.roots_care)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(false)
        }

        val constraintLayout = findViewById<ConstraintLayout>(R.id.parent_layout)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, activityLabTechnicianHomeBinding!!.drawerLayout,
            toolbar, R.string.app_name, R.string.app_name
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)

                hideKeyboard()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
//                if (homeViewModel?.appSharedPref?.studentName != null && !homeViewModel?.appSharedPref?.studentName.equals("")) {
//                    studentName = homeViewModel?.appSharedPref?.studentName!!
//                } else {
//                    studentName = ""
//                }
//                if (homeViewModel?.appSharedPref?.studentEmail != null && !homeViewModel?.appSharedPref?.studentEmail.equals("")) {
//                    studentEmail = homeViewModel?.appSharedPref?.studentEmail!!
//                } else {
//                    studentEmail = ""
//                }
//
//                if (homeViewModel?.appSharedPref?.studentProfileImage != null && !homeViewModel?.appSharedPref?.studentProfileImage.equals("")) {
//                    studentPrifileImage = homeViewModel?.appSharedPref?.studentProfileImage!!
//                } else {
//                    studentPrifileImage = ""
//                }
//
//
//                if (studentName != null && !studentName.equals("")) {
//                    activityLabTechnicianHomeBinding?.txtSidemenuName?.setText(studentName)
//                }
//
//                if (studentEmail != null && !studentEmail.equals("")) {
//                    activityLabTechnicianHomeBinding?.txtSidemenueEmail?.setText(studentEmail)
//                }
//                if (studentPrifileImage != null && !studentPrifileImage.equals("")) {
//                    Glide.with(this@HomeActivity)
//                        .load(getString(R.string.api_base) + "admin/uploads/" + (studentPrifileImage))
//                        .into(activityLabTechnicianHomeBinding?.profileImage!!)
//                }
                hideKeyboard()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val moveFactor = activityLabTechnicianHomeBinding!!.navView.width * slideOffset
                constraintLayout.translationX = moveFactor
            }

        }



        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        //        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.header_menu, HomeActivity.this.getTheme());
        //        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        activityLabTechnicianHomeBinding!!.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack.setOnClickListener {
            if (activityLabTechnicianHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityLabTechnicianHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityLabTechnicianHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            if (activityLabTechnicianHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityLabTechnicianHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityLabTechnicianHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }


    //Set up drawer side menu item

    private fun setDataAndSelectOptionInDrawerNavigation() {
        // updateNavigationDrawerprofile();
        val linearLayoutManager = LinearLayoutManager(this@LabTechnicianHomeActivity)
        activityLabTechnicianHomeBinding!!.navigationDrawerRecyclerview.layoutManager = linearLayoutManager
        activityLabTechnicianHomeBinding!!.navigationDrawerRecyclerview.setHasFixedSize(true)
        //     SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(HomeActivity.this);
        //  NotificationDatatype notificationDatatype = new Gson().fromJson(sharedPrefManager.getNotification(), NotificationDatatype.class);
        val strings = LinkedList<DrawerDatatype>()

        strings.add(DrawerDatatype("My Appointment", 0, R.drawable.my_appointment_side))
        strings.add(DrawerDatatype("My Speciality", 1, R.drawable.appointment_history))
        strings.add(DrawerDatatype("Manage Profile", 2, R.drawable.cancel_appointment))
//        strings.add(DrawerDatatype("Payment History", 3, R.drawable.checked))
//        strings.add(DrawerDatatype("Student LIVE Status", 6, 0))
        strings.add(DrawerDatatype("Review and Rating", 3, R.drawable.review_and_rating))


        strings.add(DrawerDatatype("Logout", 4, R.drawable.logout_icon))
//        strings.add(DrawerDatatype("Setting", 5, R.drawable.checked))
        drawerAdapter = DrawerAdapter(this@LabTechnicianHomeActivity, strings)
        activityLabTechnicianHomeBinding!!.navigationDrawerRecyclerview.adapter = drawerAdapter
        drawerAdapter!!.setonClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val menu = activityLabTechnicianHomeBinding!!.navigation.menu
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
                if (activityLabTechnicianHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    activityLabTechnicianHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    activityLabTechnicianHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
                }
                when (position) {
//
                    0 -> checkFragmentInBackstackAndOpen(FragmentLabtechnicianMyAppointment.newInstance())
                    1 -> checkFragmentInBackstackAndOpen(FragmentLabTechnicianMySpeciality.newInstance())
                    2 -> checkFragmentInBackstackAndOpen(FragmentLabtechnicianManageProfile.newInstance())
                    3 -> checkFragmentInBackstackAndOpen(FragmentReviewAndRating.newInstance())
                    4 -> logout()


                }
            }

        })
    }


    override fun onBackPressed() {
        if (activityLabTechnicianHomeBinding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityLabTechnicianHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1 || supportFragmentManager.findFragmentById(R.id.layout_container) is FragmentLabTechnicianHome) {
                if (check_for_close) {
                    finish()
                }
                check_for_close = true
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ check_for_close = false }, 2000)
            } else {
                super.onBackPressed()
                showSelectionOfBottomNavigationItem()
            }
        }
    }


//    private fun showSelectionOfBottomNavigationItem() {
//        val fragment = supportFragmentManager.findFragmentById(R.id.layout_container)
//        // Uncheck all menu item
//        val menu = activityLabTechnicianHomeBinding!!.navigation.menu
//        for (i in 0 until menu.size()) {
//            val menuItem = menu.getItem(i)
//            /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
//                        menuItem.setCheckable(false);
//            menuItem.isChecked = false
//        }
//        // Check only desired item and select the item and unselect other items
//        if (fragment is HomeFragment) {
//            menu.findItem(R.id.navigation_home).isChecked = true
//            /*menu.findItem(R.id.navigation_home).setIcon(R.drawable.specials_icon_selected);
//            menu.findItem(R.id.navigation_collection).setIcon(R.drawable.collections_icon);
//            menu.findItem(R.id.navigation_search).setIcon(R.drawable.search_icon);
//            menu.findItem(R.id.navigation_events).setIcon(R.drawable.event_icon);
//            menu.findItem(R.id.navigation_favourites).setIcon(R.drawable.favourites_icon);*/
//        }
//
////        else if (fragment is FragmentMedia) {
////            menu.findItem(R.id.navigation_media).isChecked = true
////
////        }
//        else if (fragment is FragmentProfile) {
//            menu.findItem(R.id.navigation_profile).isChecked = true
//
//        }
//
////        else if (fragment is FragmentAppointment) {
////
//////            menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_deselect)
//////            menu.findItem(R.id.navigation_booking).setIcon(R.drawable.booking_deselect)
//////            menu.findItem(R.id.navigation_cart).setIcon(R.drawable.cart_deselect)
//////            menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_deselect)
////        }
////        else if (fragment is FragmentChatContact) {
////            menu.findItem(R.id.navigation_message).isChecked = true
////        } else if (fragment is FragmentContact) {
////            menu.findItem(R.id.navigation_contact).isChecked = true
////        }
//        // For select or unselect the item in drawer navigation menu
//        /*if (fragment instanceof MyAccountFragment) {
//            drawerAdapter.setDraweritemPositionTobeActivated(0);
//        } else if (fragment instanceof NotificationFragment) {
//            drawerAdapter.setDraweritemPositionTobeActivated(3);
//        } else {
//            drawerAdapter.setDraweritemPositionTobeActivated(-1);
//        }*/
//        showTextInToolbarRelativeToFragment(fragment!!)
//    }


    private fun showSelectionOfBottomNavigationItem() {
        val fragment = supportFragmentManager.findFragmentById(R.id.layout_container)
        // Uncheck all menu item
        val menu = activityLabTechnicianHomeBinding!!.navigation.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
            //            menuItem.setCheckable(false);
            menuItem.isChecked = false
        }
        // Check only desired item and select the item and unselect other items
//        if (fragment is HomeFragment) {
//            menu.findItem(R.id.navigation_home).isChecked = true
//            /*menu.findItem(R.id.navigation_home).setIcon(R.drawable.specials_icon_selected);
//            menu.findItem(R.id.navigation_collection).setIcon(R.drawable.collections_icon);
//            menu.findItem(R.id.navigation_search).setIcon(R.drawable.search_icon);
//            menu.findItem(R.id.navigation_events).setIcon(R.drawable.event_icon);
//            menu.findItem(R.id.navigation_favourites).setIcon(R.drawable.favourites_icon);*/
//        } else if (fragment is FragmentProfile) {
//            menu.findItem(R.id.navigation_profile).isChecked = true
////            menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_deselect);
////            menu.findItem(R.id.navigation_booking).setIcon(R.drawable.booking_deselect);
////            menu.findItem(R.id.navigation_cart).setIcon(R.drawable.cart_deselect);
////            menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_select);
//        }else if (fragment is FragmentBookingAppointment) {
//            menu.findItem(R.id.navigation_booking).isChecked = true
//        }
//        else if (fragment is FragmentPatientbookPayNow) {
//            menu.findItem(R.id.navigation_booking).isChecked = true
//        }


//        else if (fragment is ProfileFragment) {
//            menu.findItem(R.id.navigation_profile).isChecked = true
//        } else if (fragment is FragmentChatContact) {
//            menu.findItem(R.id.navigation_message).isChecked = true
//        } else if (fragment is FragmentContact) {
//            menu.findItem(R.id.navigation_contact).isChecked = true
//        }
        // For select or unselect the item in drawer navigation menu
        /*if (fragment instanceof MyAccountFragment) {
            drawerAdapter.setDraweritemPositionTobeActivated(0);
        } else if (fragment instanceof NotificationFragment) {
            drawerAdapter.setDraweritemPositionTobeActivated(3);
        } else {
            drawerAdapter.setDraweritemPositionTobeActivated(-1);
        }*/
        showTextInToolbarRelativeToFragment(fragment!!)
    }


    private fun showTextInToolbarRelativeToFragment(fragment: Fragment) {

        val tootbar_text = activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle
        val tootbar_profile = activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarProfile
        val tootbar_notification = activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarNotification
        val tootbar_logout = activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarLogout

        val toolbar_back = activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarBack
        val toolbar_menu = activityLabTechnicianHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack

        if (fragment is FragmentLabTechnicianHome) {
            tootbar_text.text = resources.getString(R.string.home)
            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
//            tootbar_text.setTextColor(resources.getColor(android.R.color.white))
            //   drawerAdapter!!.selectItem(-1)
            tootbar_profile.visibility = View.VISIBLE
            tootbar_notification.visibility = View.VISIBLE
            tootbar_logout.visibility = View.GONE

            toolbar_back.visibility = View.GONE
            toolbar_menu.visibility = View.VISIBLE



            tootbar_profile.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentLabtechnicianManageProfile.newInstance())
            })
            tootbar_notification.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentHospitalManageNotification.newInstance())
            })
            tootbar_logout.setOnClickListener(View.OnClickListener {
                logout()
            })
            drawerAdapter!!.setSelection(-1)
        } else if (fragment is FragmentLabtechnicianManageProfile) {
            //   drawerAdapter!!.selectItem(0)
            tootbar_text.text = resources.getString(R.string.roots_care)
            tootbar_profile.visibility = View.GONE
            tootbar_notification.visibility = View.VISIBLE
            tootbar_logout.visibility = View.GONE

            toolbar_back.visibility = View.VISIBLE
            toolbar_menu.visibility = View.GONE
            toolbar_back.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

//            tootbar_profile?.setOnClickListener(View.OnClickListener {
//                checkFragmentInBackstackAndOpen(FragmentLabtechnicianManageProfile.newInstance())
//            })
            tootbar_notification.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentHospitalManageNotification.newInstance())
            })
            tootbar_logout.setOnClickListener(View.OnClickListener {
                logout()
            })
            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
        } else if (fragment is FragmentLabtechnicianMyAppointment) {
            //   drawerAdapter!!.selectItem(0)
            tootbar_text.text = "My Appointment"
            tootbar_profile.visibility = View.VISIBLE
            tootbar_notification.visibility = View.VISIBLE
            tootbar_logout.visibility = View.GONE
            toolbar_back.visibility = View.VISIBLE
            toolbar_menu.visibility = View.GONE
            toolbar_back.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })
            tootbar_profile.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentLabtechnicianManageProfile.newInstance())
            })
            tootbar_notification.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentHospitalManageNotification.newInstance())
            })
            tootbar_logout.setOnClickListener(View.OnClickListener {
                logout()
            })
            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
        }
//
        else if (fragment is FragmentLabTechnicianMyAppointmentDetails) {
            //   drawerAdapter!!.selectItem(0)
            tootbar_text.text = "Appointment Details"
            tootbar_profile.visibility = View.VISIBLE
            tootbar_notification.visibility = View.VISIBLE
            tootbar_logout.visibility = View.GONE
            toolbar_back.visibility = View.VISIBLE
            toolbar_menu.visibility = View.GONE
            toolbar_back.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })
            tootbar_profile.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentLabtechnicianManageProfile.newInstance())
            })
            tootbar_notification.setOnClickListener {
                checkFragmentInBackstackAndOpen(FragmentHospitalManageNotification.newInstance())
            }
            tootbar_logout.setOnClickListener {
                logout()
            }
            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
        } else if (fragment is FragmentReviewAndRating) {
            //   drawerAdapter!!.selectItem(0)
            tootbar_text.text = "Review and Rating"
            tootbar_profile.visibility = View.VISIBLE
            tootbar_notification.visibility = View.VISIBLE
            tootbar_logout.visibility = View.GONE
            toolbar_back.visibility = View.VISIBLE
            toolbar_menu.visibility = View.GONE
            toolbar_back.setOnClickListener {
                onBackPressed()
            }

            tootbar_profile.setOnClickListener {
                checkFragmentInBackstackAndOpen(FragmentLabtechnicianManageProfile.newInstance())
            }
            tootbar_notification.setOnClickListener {
                checkFragmentInBackstackAndOpen(FragmentHospitalManageNotification.newInstance())
            }
            tootbar_logout.setOnClickListener {
                logout()
            }
            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
        }
//
        else if (fragment is FragmentLabTechnicianMySpeciality) {
            //   drawerAdapter!!.selectItem(0)
            tootbar_text.text = "My Speciality"
            tootbar_profile.visibility = View.VISIBLE
            tootbar_notification.visibility = View.VISIBLE
            tootbar_logout.visibility = View.GONE
            toolbar_back.visibility = View.VISIBLE
            toolbar_menu.visibility = View.GONE
            toolbar_back.setOnClickListener {
                onBackPressed()
            }
            tootbar_profile.setOnClickListener {
                checkFragmentInBackstackAndOpen(FragmentLabtechnicianManageProfile.newInstance())
            }
            tootbar_notification.setOnClickListener {
                checkFragmentInBackstackAndOpen(FragmentHospitalManageNotification.newInstance())
            }
            tootbar_logout.setOnClickListener {
                logout()
            }
            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
        }
//
//        else if (fragment is FragmentDoctorMyschedule) {
//            //   drawerAdapter!!.selectItem(0)
//            tootbar_text.text = "My Schedule"
//            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
//        }
//
//        else if (fragment is FragmentdoctorManageSchedule) {
//            //   drawerAdapter!!.selectItem(0)
//            tootbar_text.text = "Manage Schedule"
//            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
//        }
//
//        else if (fragment is FragmentAddDoctorScheduleTime) {
//            //   drawerAdapter!!.selectItem(0)
//            tootbar_text.text = "Manage Schedule"
//            // tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
//        }
//
//        else if (fragment is FragmentDoctorPaymentHistory) {
//            //   drawerAdapter!!.selectItem(0)
//            tootbar_text.text = "Payment History"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@LabTechnicianHomeActivity, android.R.color.white))
//        }

//
//        else if (fragment is FragmentBookingAppointment) {
//            // drawerAdapter!!.selectItem(0)
//            tootbar_text.text = resources.getString(R.string.roots_care)
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//        }
//        else if (fragment is FragmentAppointment) {
//            //    drawerAdapter!!.selectItem(2)
//            tootbar_text.text ="Appointment History"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
//
//        else if (fragment is FragmentCancellMyUcomingAppointment) {
//            ///drawerAdapter!!.selectItem(1)
//            tootbar_text.text ="Cancel Appointment"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
//
//        else if (fragment is FragmentPatientbookPayNow) {
//            //drawerAdapter!!.selectItem(1)
//            tootbar_text.text ="Roots Care"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
//
//        else if (fragment is FragmentMyUpCommingAppointment) {
//            // drawerAdapter!!.selectItem(3)
//            tootbar_text.text ="Roots Care"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
//
//
//        else if (fragment is FragmnetViewPrespriction) {
//            //   drawerAdapter!!.selectItem(3)
//            tootbar_text.text ="View Prescription"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
//
//        else if (fragment is FragmentPaymentHistory) {
//            //   drawerAdapter!!.selectItem(3)
//            tootbar_text.text ="Payment History"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
//        else if (fragment is FragmentPatientProfileSetting) {
//            //   drawerAdapter!!.selectItem(3)
//            tootbar_text.text ="Profile Setting"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
//
//        else if (fragment is FragmentReviewAndRating) {
//            //   drawerAdapter!!.selectItem(3)
//            tootbar_text.text ="Review and Rating"
//            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
//
//        }
    }

    fun checkFragmentInBackstackAndOpen(fragment: Fragment) {

        val name_fragment_in_backstack = fragment.javaClass.name

        if (fragment_open_container == null && activityLabTechnicianHomeBinding!!.appBarHomepage.layoutContainer.id != null) {
            fragment_open_container = activityLabTechnicianHomeBinding!!.appBarHomepage.layoutContainer.id
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
        val fragment = supportFragmentManager.findFragmentById(activityLabTechnicianHomeBinding?.appBarHomepage?.layoutContainer?.id!!)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        CommonDialog.showDialog(this@LabTechnicianHomeActivity, object :
            DialogClickCallback {
            override fun onDismiss() {
            }

            override fun onConfirm() {
                if (isNetworkConnected) {
                    this@LabTechnicianHomeActivity.showLoading()
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("user_id", labTechnicianHomeActivityViewModel?.appSharedPref?.loginUserId!!)
                    val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
                    labTechnicianHomeActivityViewModel?.apiLogout(body)

                } else {
                    Toast.makeText(
                        this@LabTechnicianHomeActivity,
                        "Please check your network connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

        }, "Logout", "Are you sure you want to logout?")
    }

    override fun successLogoutResponse(commonResponse: CommonResponse?) {
        this@LabTechnicianHomeActivity.hideLoading()
        if (commonResponse?.code.equals("200")) {
//                homeViewModel?.appSharedPref?.deleteUserId()
            labTechnicianHomeActivityViewModel?.appSharedPref?.deleteLoginModelData()
            labTechnicianHomeActivityViewModel?.appSharedPref?.deleteIsLogINRemember()
            labTechnicianHomeActivityViewModel?.appSharedPref?.deleteLoginUserType()
            labTechnicianHomeActivityViewModel?.appSharedPref?.deleteLoginUserId()
            startActivity(LoginActivity.newIntent(this@LabTechnicianHomeActivity))
            finishAffinity()
        } else {
            Toast.makeText(this@LabTechnicianHomeActivity, commonResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorLogoutResponse(throwable: Throwable?) {
        this@LabTechnicianHomeActivity.hideLoading()
        if (throwable?.message != null) {
            Log.d(TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(this@LabTechnicianHomeActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}
