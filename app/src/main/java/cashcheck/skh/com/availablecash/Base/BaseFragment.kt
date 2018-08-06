package cashcheck.skh.com.availablecash.Base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import cashcheck.skh.com.availablecash.R

/**
 * Created by Seogki on 2018. 6. 7..
 */
open class BaseFragment : Fragment() {

    private lateinit var dialog: android.app.AlertDialog

    fun Fragment.beginNewActivity(intent: Intent) {
        startActivity(intent)
    }

    fun getURLForResource(resourceId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.`package`.name + "/" + resourceId).toString()
    }


    fun addFragment(activity: FragmentActivity?, @IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean, backstack: Boolean, tag: String) {

        val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(frameId, fragment, tag)

        if (backstack) {
            transaction?.addToBackStack(fragment.tag)
        }

        if (AllowStateloss)
            transaction?.commitAllowingStateLoss()
        else
            transaction?.commit()
    }
    fun Fragment.beginActivity(intent: Intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    fun closeKeyboard() {
        val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

    }
    fun clearAndClose(edit: EditText) {
        edit.text.clear()
        val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun openKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }



    fun showAlertDialog(c: Context, msg: String) {
        AlertDialog.Builder(c)
                .setMessage(msg)
                .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }.setNegativeButton(null, null)
                .show()

    }

    fun alertDialog(c: Context, msg: String) {
        AlertDialog.Builder(c, R.style.MyDialogTheme)
                .setMessage(msg)
                .setPositiveButton("확인", { dialog, _ ->
                    dialog.dismiss()
                }).setNegativeButton(null, null)
                .show()
    }

}