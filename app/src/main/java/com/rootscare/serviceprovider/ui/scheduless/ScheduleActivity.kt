package com.rootscare.serviceprovider.ui.scheduless


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.ScheduleTypes
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
import com.rootscare.utils.commonadapters.ViewPagerAdapterForTab
import kotlinx.android.synthetic.main.activity_schedule.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mumayank.com.airlocationlibrary.AirLocation
import java.util.*

class ScheduleActivity : BaseActivity<ActivityScheduleBinding, ScheduleActivityViewModel>() {
    private var binding: ActivityScheduleBinding? = null
    private var scheduleActivityViewModel: ScheduleActivityViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_schedule
    override val viewModel: ScheduleActivityViewModel
        get() {
            scheduleActivityViewModel = ViewModelProviders.of(this).get(ScheduleActivityViewModel::class.java)
            return scheduleActivityViewModel as ScheduleActivityViewModel
        }
    lateinit var adapter: ViewPagerAdapterForTab

    companion object {
        var isCurrLocChoosed = MutableLiveData<Boolean?>(false)
        var isDiffLocChoosed = MutableLiveData<Boolean?>(false)

        var currLat :Double  = 0.0
        var currLong :Double = 0.0
        var diffAddressPicked :String = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        binding?.topToolbar?.run {
            tvHeader.text = getString(R.string.schedule_availability)
            btnBack.setOnClickListener { onBackPressed()}
        }

       lifecycleScope.launchWhenResumed {
            try {
                delay(5)
                setupViewPager(binding?.viewPager)
            } catch (e: Exception) {
                println("$e")
            }
        }
        observers()
    }

    private fun observers() {
        isCurrLocChoosed.observe(this) {
            try {
                it?.let {
                    if(it) { startGettingCurrenttLocation()  }
                }
            } catch (e: java.lang.Exception){
                println(e)
            }
        }
        isDiffLocChoosed.observe(this) {
            try {
                it?.let {
                    if(it) {
                        (currentFragment as? ScheduleFragmentCombine)?.updateAddress(diffAddressPicked, currLat, currLong)
                        isDiffLocChoosed.value = false
                    }
                }
            }catch (e: java.lang.Exception){
                println(e)
            }
        }
    }


    private var currentFragment: Fragment? = null
    private fun setupViewPager(viewPager: ViewPager?) {
        try {
            adapter = ViewPagerAdapterForTab(supportFragmentManager)
            setFragmentAccordingToUser()
            viewPager?.adapter = adapter
            try {
                binding?.run {
                    tabLayout.setupWithViewPager(binding?.viewPager)
//                    viewPager?.offscreenPageLimit = 2
                    tabLayout.setTabTextColors(
                        ContextCompat.getColor(this@ScheduleActivity, R.color.color_tab_text_normal),
                        ContextCompat.getColor(this@ScheduleActivity, R.color.color_tab_text_selected)
                    )

                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0888D1"))
                }
            } catch (e: Exception) {
                println("$e")
            }

            viewPager?.isSaveFromParentEnabled = false
        } catch (e: Exception) {
            println("$e")
        }
        try {
            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    try {
                        currentFragment = if (position == 0) {
                            adapter.getItem(0)
                        }
                       else {
                          adapter.getItem(1)
                      }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }

                override fun onPageSelected(position: Int) {

                }

            })
        } catch (e: Exception) {
            println(e.message)
        }

    }

    private fun setFragmentAccordingToUser() {
        when {
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.NURSE.type) -> {
                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.TASK_BASED.nm), ScheduleTypes.TASK_BASED.nm)
                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.HOURLY_BASED.nm), ScheduleTypes.HOURLY_BASED.nm)
                binding?.tabLayout?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.BABYSITTER.type) ||
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.CAREGIVER.type) -> {
                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.HOURLY_BASED.nm), ScheduleTypes.HOURLY_BASED.nm)
            }
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.PHYSIOTHERAPY.type) -> {
                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.TASK_BASED.nm), ScheduleTypes.TASK_BASED.nm)
            }
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.DOCTOR.type) -> {
                val mRes = viewModel.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result
                if(mRes?.hospital_id.isNullOrBlank()){
                    adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.ONLINE_HOME_BASED.nm), getString(R.string.online_and_home_consultation))
                } else {
                    adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.ONLINE_BASED.nm), getString(R.string.online_cons))
                }

            }

//            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.LAB_TECHNICIAN.type) -> {
//                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.TASK_BASED.nm), "LAB Schedule")
//            }
//            scheduleActivityViewModel?.appSharedPref?.loginUserType.equals(LoginTypes.PATHOLOGY.type) -> {
//                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.TASK_BASED.nm), ScheduleTypes.TASK_BASED.nm)
//            }
//            scheduleActivityViewModel?.appSharedPref?.loginUserType.equals(LoginTypes.HOSPITAL.type) -> {
//                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.TASK_BASED.nm), "LAB Schedule")
//            }

        }

    }

    fun startGettingCurrenttLocation() {
        showLoading()
        airLocation.start()
    }

    private val airLocation = AirLocation(this, object : AirLocation.Callback {
        override fun onSuccess(locations: ArrayList<Location>) {
            for (location in locations) {
                println("${location.longitude},${location.latitude}  ")
                getAddressFromLatLong(location.latitude, location.longitude)
            }
            hideLoading()
        }

        override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
            hideLoading()
            Toast.makeText(this@ScheduleActivity, locationFailedEnum.name, Toast.LENGTH_SHORT)
                .show()
        }
    }, true)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun getAddressFromLatLong(latitude: Double, longitude: Double) {
        GlobalScope.launch(Dispatchers.IO) {
            val addresses: List<Address>
            val geocoder = Geocoder(this@ScheduleActivity, Locale.ENGLISH).apply {
                addresses = getFromLocation(latitude, longitude,1)
            } // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            val address: String = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            launch(Dispatchers.Main) {
                (currentFragment as? ScheduleFragmentCombine)?.updateAddress(address, latitude, longitude)
                isDiffLocChoosed.value = false
                isCurrLocChoosed.value = false
            }

        }

//        val city: String = addresses[0].getLocality()
//        val state: String = addresses[0].getAdminArea()
//        val country: String = addresses[0].getCountryName()
//        val postalCode: String = addresses[0].getPostalCode()
//        val knownName: String = addresses[0].getFeatureName() // Only if available else return NULL

    }

}