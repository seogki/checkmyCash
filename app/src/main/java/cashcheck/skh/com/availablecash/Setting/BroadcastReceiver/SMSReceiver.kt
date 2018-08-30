package cashcheck.skh.com.availablecash.Setting.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import cashcheck.skh.com.availablecash.Util.DLog


/**
 * Created by Seogki on 2018. 8. 30..
 */
class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                DLog.e("message $message")
                // Do whatever you want to do with SMS.
            }
        }
    }

}