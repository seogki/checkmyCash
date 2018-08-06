package cashcheck.skh.com.availablecash.Util

import android.util.Log
import cashcheck.skh.com.availablecash.BuildConfig

/**
 * Created by choibongjae on 2017. 7. 10..
 */

object DLog {
    private const val TAG = "CheeseCounter"

    /**
     * Log Level Error
     */
    fun e(message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, buildLogMsg(message))
        }
    }

    /**
     * Log Level Warning
     */
    fun w(message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, buildLogMsg(message))
        }
    }

    /**
     * Log Level Information
     */
    fun i(message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, buildLogMsg(message))
        }
    }

    /**
     * Log Level Debug
     */
    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, buildLogMsg(message))
        }
    }

    /**
     * Log Level Verbose
     */
    fun v(message: String) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, buildLogMsg(message))
        }
    }

    private fun buildLogMsg(message: String): String {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append("[")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append("::")
        sb.append(ste.methodName)
        sb.append("::")
        sb.append(ste.lineNumber)
        sb.append("]")
        sb.append(message)
        return sb.toString()
    }

}
