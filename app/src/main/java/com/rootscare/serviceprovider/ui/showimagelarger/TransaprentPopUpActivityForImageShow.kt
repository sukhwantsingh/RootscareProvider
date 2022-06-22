package com.rootscare.serviceprovider.ui.showimagelarger

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityTransparentForShowBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.utils.asynctaskutil.GetInputStreamFromUrl
import java.io.InputStream
import java.util.*


class TransaprentPopUpActivityForImageShow : BaseActivity<ActivityTransparentForShowBinding, TransparentActivityForShowViewModel>(),
    TransparentActivityForShowNavigator {

    companion object {
        private val TAG = TransaprentPopUpActivityForImageShow::class.java.simpleName
        fun newIntent(activity: Activity, PassData: String): Intent {
            return Intent(activity, TransaprentPopUpActivityForImageShow::class.java).putExtra("PassData", PassData)
        }

    }

    private var fileUrl: String? = null
    private var fileType: String? = null
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    private var activityDoctorListingBinding: ActivityTransparentForShowBinding? = null
    private var doctorListingViewModel: TransparentActivityForShowViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_transparent_for_show
    override val viewModel: TransparentActivityForShowViewModel
        get() {
            doctorListingViewModel = ViewModelProviders.of(this).get(TransparentActivityForShowViewModel::class.java)
            return doctorListingViewModel as TransparentActivityForShowViewModel
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doctorListingViewModel?.navigator = this
        activityDoctorListingBinding = viewDataBinding

        with(activityDoctorListingBinding!!) {
            imageViewCross.setOnClickListener {
                onBackPressed()
            }

            fileUrl = intent?.extras?.getString("PassData")
            if (fileUrl?.lowercase(Locale.ROOT)?.contains("pdf")!!) {
                fileType = "pdf"
            } else {
                fileType = "image"
            }

            if (fileUrl != null) {
                if (fileType?.trim() == "pdf") {
                    imageviewShow.visibility = View.GONE
                    pdfView.visibility = View.VISIBLE
//                    viewInWebView()
                    openPdfToPDFViewer()
//                    pdfView.fromUri(Uri.parse(fileUrl!!))
//                    pdfView.fromStream(AppData.responseBodyForPDF?.byteStream())
                } else {
                    val circularProgressDrawable = CircularProgressDrawable(this@TransaprentPopUpActivityForImageShow)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.setColorSchemeColors(*intArrayOf(R.color.green_light_1, R.color.green))
                    circularProgressDrawable.start()
                    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    Glide.with(this@TransaprentPopUpActivityForImageShow).load(fileUrl)
                        .apply(RequestOptions().placeholder(circularProgressDrawable)).timeout(1000 * 60).apply(requestOptions)
                        .into(imageviewShow)
                    imageviewShow.visibility = View.VISIBLE
//                    webView.visibility = View.GONE
                    pdfView.visibility = View.GONE
                }
            }

            mScaleGestureDetector = ScaleGestureDetector(this@TransaprentPopUpActivityForImageShow, ScaleListener())
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mScaleGestureDetector?.onTouchEvent(event)
        return true
    }


    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            return true
        }
    }

    private fun openPdfToPDFViewer() {
        with(activityDoctorListingBinding!!) {
            loader.visibility = View.VISIBLE
            GetInputStreamFromUrl(object : GetInputStreamFromUrl.CallBackAfterFetchingInputStream {
                override fun onCallback(result: InputStream?) {
                    Log.d(TAG, "streaammmm ${result}")
                    pdfView.fromStream(result)
                        .enableSwipe(true)
                        .enableDoubletap(true)
                        .onLoad { loader.visibility = View.GONE }
                        .enableAntialiasing(true)
                        .invalidPageColor(Color.WHITE)
                        .load()
                }
            }).execute(fileUrl)
        }
    }
}
