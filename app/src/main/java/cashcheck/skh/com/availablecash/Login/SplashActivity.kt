package cashcheck.skh.com.availablecash.Login

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import cashcheck.skh.com.availablecash.Chart.ChartMainActivity
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, ChartMainActivity::class.java))
        }, 500)
    }
}
