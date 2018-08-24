package cashcheck.skh.com.availablecash

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric

/**
 * Created by Seogki on 2018. 8. 24..
 */
class ApplicationClass : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this, getString(R.string.admob_id))
        AdRequest.Builder().addTestDevice("A86E700BF47A5B43A7D1B1882060F2AA")
        AdRequest.Builder().addTestDevice("945C9CAA6FF2EC9D7AE09BE4244D1081")
        Fabric.with(this,Crashlytics())
    }
}