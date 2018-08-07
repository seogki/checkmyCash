package cashcheck.skh.com.availablecash.Setting

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import cashcheck.skh.com.availablecash.Base.BaseActivity
import cashcheck.skh.com.availablecash.Chart.ChartMainActivity
import cashcheck.skh.com.availablecash.Compare.CompareMainActivity
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.RegisterMainActivity
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.databinding.ActivitySettingMainBinding

class SettingMainActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivitySettingMainBinding
    private var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SettingMainActivity, R.layout.activity_setting_main)
        binding.layoutBottomTab.onClickListener = this
        addFragment(R.id.frame_layout, SettingMainFragment(), false, false, "SettingMainFragment")
        setCurrentTab()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this, ChartMainActivity::class.java))
            }
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this, RegisterMainActivity::class.java))
            }
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this, CompareMainActivity::class.java))
            }
        }
    }

    private fun setCurrentTab() {
        binding.layoutBottomTab.bottomLayoutBtn3Txt.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_settings_black_24))
        binding.layoutBottomTab.bottomLayoutBtn3Txt.drawable.setColorFilter(ContextCompat.getColor(this, R.color.rippleColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab.bottomLayoutText3.setTextColor(ContextCompat.getColor(this, R.color.rippleColor))
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
        return curFragment.tag == "SettingMainFragment"
    }
}
