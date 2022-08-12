package com.rootscare.serviceprovider.ui.manageDocLab

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityPriceListScreenBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.manageDocLab.fragments.FragmentManageHospitalDocsLab
import com.rootscare.serviceprovider.utilitycommon.HospitalUnder
import com.rootscare.utils.commonadapters.ViewPagerAdapterForTab
import kotlinx.coroutines.delay

class ManageDocAndLab : BaseActivity<ActivityPriceListScreenBinding, ManageDocLabViewModel>() {
    private var binding: ActivityPriceListScreenBinding? = null
    private var mViewModel: ManageDocLabViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_price_list_screen
    override val viewModel: ManageDocLabViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(ManageDocLabViewModel::class.java)
            return mViewModel as ManageDocLabViewModel
        }
    lateinit var adapter: ViewPagerAdapterForTab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        binding?.topToolbar?.run {
            tvHeader.text = getString(R.string.manage_doctor_amp_lab)
            btnBack.setOnClickListener { finish() }
        }

        lifecycleScope.launchWhenCreated {
            try {
                delay(5)
                setupViewPager(binding?.viewPager)
            } catch (e: Exception) {
                println("$e")
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
                    tabLayout.setTabTextColors(
                        ContextCompat.getColor(this@ManageDocAndLab, R.color.color_tab_text_normal),
                        ContextCompat.getColor(this@ManageDocAndLab, R.color.color_tab_text_selected)
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
                        } else {
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
        adapter.addFragment(FragmentManageHospitalDocsLab.newInstance(HospitalUnder.DOCTOR.getType()), HospitalUnder.DOCTOR.get())
      //  adapter.addFragment(FragmentManageHospitalDocsLab.newInstance(HospitalUnder.LAB.getType()), HospitalUnder.LAB.get())
      //  binding?.tabLayout?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
    }

}