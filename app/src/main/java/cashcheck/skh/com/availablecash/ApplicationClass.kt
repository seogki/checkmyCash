package cashcheck.skh.com.availablecash

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric

/**
 * Created by Seogki on 2018. 8. 24..
 */
class ApplicationClass : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        MobileAds.initialize(this, getString(R.string.admob_id))
        AdRequest.Builder().addTestDevice("A86E700BF47A5B43A7D1B1882060F2AA")
        AdRequest.Builder().addTestDevice("945C9CAA6FF2EC9D7AE09BE4244D1081")
        val crashlytics = Crashlytics.Builder().core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build()

        Fabric.with(this, crashlytics)
    }
}