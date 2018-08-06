package cashcheck.skh.com.availablecash.Register

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import cashcheck.skh.com.availablecash.Base.BaseActivity
import cashcheck.skh.com.availablecash.Chart.ChartMainActivity
import cashcheck.skh.com.availablecash.Compare.CompareMainActivity
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Setting.SettingMainActivity
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.databinding.ActivityRegisterMainBinding

class RegisterMainActivity : BaseActivity(), View.OnClickListener {
    private var backKeyPressedTime: Long = 0
    lateinit var binding: ActivityRegisterMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@RegisterMainActivity, R.layout.activity_register_main)
        binding.layoutBottomTab.onClickListener = this
        addFragment(R.id.frame_layout, RegisterMainFragment(), false, false, "RegisterMainFragment")
        setCurrentTab()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this, ChartMainActivity::class.java))
            }
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this, CompareMainActivity::class.java))
            }
            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this, SettingMainActivity::class.java))
            }
        }
    }

    private fun setCurrentTab() {
//        binding.layoutBottomTab.bottomLayoutBtn1Txt.setImageDrawable(ContextCompat.getDrawable(this@SettingMainActivity, R.drawable.icons8_home_24_fill))
//        binding.layoutBottomTab.bottomLayoutBtn1Txt.drawable.setColorFilter(ContextCompat.getColor(this, R.color.rippleColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab.bottomLayoutText1.setTextColor(ContextCompat.getColor(this, R.color.rippleColor))
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
        return curFragment.tag == "RegisterMainFragment"
    }
}
