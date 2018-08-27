package cashcheck.skh.com.availablecash.Compare

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import cashcheck.skh.com.availablecash.Base.BaseActivity
import cashcheck.skh.com.availablecash.Chart.ChartMainActivity
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.RegisterMainActivity
import cashcheck.skh.com.availablecash.Setting.SettingMainActivity
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.databinding.ActivityCompareMainBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class CompareMainActivity : BaseActivity(), View.OnClickListener {
    private var backKeyPressedTime: Long = 0
    lateinit var binding: ActivityCompareMainBinding
    private var adView: AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@CompareMainActivity, R.layout.activity_compare_main)
        binding.layoutBottomTab.onClickListener = this
        addFragment(R.id.frame_layout, CompareMainFragment(), false, false, "CompareMainFragment")
        adView = binding.adView
        setCurrentTab()
        abADs()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

    override fun onPause() {
        super.onPause()
        adView?.pause()
    }

    private fun abADs() {

//        val mInterstitialAd = InterstitialAd(this)
//        mInterstitialAd.adUnitId = getString(R.string.admob_banner_compare)
//        mInterstitialAd.loadAd(AdRequest.Builder().build())

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
        adView?.adListener = object : AdListener() {
            override fun onAdClosed() {
//                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this, ChartMainActivity::class.java))
            }
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this, RegisterMainActivity::class.java))
            }
            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this, SettingMainActivity::class.java))
            }
        }
    }

    private fun setCurrentTab() {
        binding.layoutBottomTab.bottomLayoutBtn2Txt.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_insert_chart_black_24))
        binding.layoutBottomTab.bottomLayoutBtn2Txt.drawable.setColorFilter(ContextCompat.getColor(this, R.color.rippleColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab.bottomLayoutText2.setTextColor(ContextCompat.getColor(this, R.color.rippleColor))
    }

    override fun onBackPressed() {
        DLog.e("onBack Pressed" + isFirstFragment())

        if (isFirstFragment()) {

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                showGuide()
                return
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                this.finishAffinity()
                finishToast()
            }

        } else {
            super.onBackPressed()
        }

    }

    private fun isFirstFragment(): Boolean {
        val curFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        DLog.e("current Fragment ${curFragment.tag}")
        return curFragment.tag == "CompareMainFragment"
    }
}
