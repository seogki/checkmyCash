package cashcheck.skh.com.availablecash.Setting

import android.Manifest
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Setting.Dialog.ExceltoDBDialogV2
import cashcheck.skh.com.availablecash.Setting.Model.SettingMsgModel
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentSettingMainBinding
import com.ajts.androidmads.library.ExcelToSQLite
import com.ajts.androidmads.library.SQLiteToExcel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.*


class SettingMainFragment : BaseFragment(), View.OnClickListener, ExceltoDBListener {


    lateinit var binding: FragmentSettingMainBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var mItem: MutableList<SettingMsgModel>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_main, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        binding.onClickListener = this
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_frag_btn_clear_data -> {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setTitle("데이터 삭제")
                        .setMessage("정말로 지우시겠습니까?")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()
                            try {
                                if ((context!!.getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()) {
                                    Toast.makeText(context!!, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context!!, "오류로 인해 삭제 불가 설정에가서 직접 지워주세요! \n설정 -> 어플리케이션 -> 앱 -> 저장공간 -> 데이터 삭제", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context!!, "오류가 발생했습니다. 설정에가서 직접 지워주세요! \n설정 -> 어플리케이션 -> 앱 -> 저장공간 -> 데이터 삭제", Toast.LENGTH_LONG).show()
                            }

                        }).setNegativeButton("취소", { dialog, _ ->
                            dialog.dismiss()

                        })
                        .show()
            }
            R.id.setting_frag_btn_ask_developer -> {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setTitle("문의하기")
                        .setMessage("앱의 문제 또는 개선해하야할 부분을 남겨주세요!")
                        .setPositiveButton("확인", { dialog, _ ->
                            startMyAppPlayStore()
                            dialog.dismiss()

                        }).setNegativeButton("취소", { dialog, _ ->
                            dialog.dismiss()
                        })
                        .show()

            }
            R.id.setting_frag_btn_excel_data -> {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setTitle("파일 저장")
                        .setMessage("데이터를 엑셀파일로 저장하시겠습니까?")
                        .setPositiveButton("확인", { dialog, _ ->
                            setTedPermission(true)
                            dialog.dismiss()

                        }).setNegativeButton("취소", { dialog, _ ->
                            dialog.dismiss()
                        })
                        .show()


            }
            R.id.setting_frag_btn_data_excel -> {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setTitle("데이터 저장")
                        .setMessage("엑셀파일을 데이터로 저장하시겠습니까?")
                        .setPositiveButton("확인", { dialog, _ ->
                            setTedPermission(false)
                            dialog.dismiss()

                        }).setNegativeButton("취소", { dialog, _ ->
                            dialog.dismiss()
                        })
                        .show()
            }
            R.id.setting_frag_btn_get_msg -> {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setTitle("메시지 내역")
                        .setMessage("메시지 내역을 저장하시겠습니까?\n현재 가능한 은행\n * 신한은행\n * 시티BC, 해외승인 USD")
                        .setPositiveButton("확인", { dialog, _ ->
                            setMessagePermission()
                            dialog.dismiss()

                        }).setNegativeButton("취소", { dialog, _ ->
                            dialog.dismiss()
                        })
                        .show()

            }
        }
    }

    private fun setMessagePermission() {
        TedPermission.with(context)
                .setPermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        Handler().postDelayed({
                            getMessageFromAndroid()

                        }, 10)
                    }

                    override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>) {

                    }
                }).check()
    }

    private fun getMessageFromAndroid() {
        val task = QueryTask()
        task.execute()
        mItem = task.get()
        checkcardIdFromDB(mItem)
    }

    private fun checkcardIdFromDB(mItem: MutableList<SettingMsgModel>) {
        val db = dbHelper.readableDatabase
        var howMany = 0
        var isThere = false
        val arr = ArrayList<String>()
        if (db != null) {
            var cursor: Cursor? = null
            try {


                cursor = db.rawQuery("SELECT card_id, card_name FROM ${Const.DbName} WHERE card_id IS NOT NULL ORDER BY _id DESC", null)
                while (cursor.moveToNext()) {
                    val a = cursor.getString(0)
                    val b = cursor.getString(1)
                    arr.add("$a $b")
                }

                for (item in mItem) {
                    for (a in arr) {
                        val split = a.split(" ")
                        if (split[0] == item.cardid && split[1] == item.cardName) {
                            isThere = true
                            break
                        } else {
                            isThere = false
                        }
                    }
                    DLog.e("isThere = $isThere")
                    if (!isThere) {
                        howMany += 1
                        dbHelper.insertData(item.date!!, item.cate!!, item.money!!, UtilMethod.getWeekofDay(item.date), item.cardid, item.cardName)
                    }
                }

                if (howMany <= 0) {
                    AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                            .setTitle("메시지 내역")
                            .setMessage("등록할 항목이 존재하지 않습니다")
                            .setPositiveButton("확인", { dialog, _ ->
                                dialog.dismiss()
                            }).setNegativeButton(null, null)
                            .show()
                } else {
                    AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                            .setTitle("메시지 내역")
                            .setMessage("$howMany 항목이 등록되었습니다.")
                            .setPositiveButton("확인", { dialog, _ ->
                                dialog.dismiss()
                            }).setNegativeButton(null, null)
                            .show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
    }


    private fun startMyAppPlayStore() {
        val appPackageName = activity?.packageName // getPackageName() from Context or Activity object
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    private fun onExceltoDBDialog() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val d = ExceltoDBDialogV2(context!!, this)
        d.show()
        d.window.setLayout((6 * width) / 7, (4 * height) / 12)

    }

    override fun ExcelData(date: String?) {
        if (date != null) {
            setExceltoDB(date)
        }
    }

    private fun setTedPermission(isExcel: Boolean) {
        TedPermission.with(context)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        Handler().postDelayed({
                            if (isExcel)
                                setDBtoExcel()
                            else
                                onExceltoDBDialog()

                        }, 10)
                    }

                    override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>) {

                    }
                }).check()
    }

    private fun setExceltoDB(date: String?) {
        DLog.e("date $date")
        val sql = ExcelToSQLite(context, "${Const.DbName}.db")
        sql.importFromFile(date, object : ExcelToSQLite.ImportListener {
            override fun onError(e: java.lang.Exception?) {
                Toast.makeText(context!!, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onStart() {

            }

            override fun onCompleted(dbName: String?) {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setMessage("데이터가 저장되었습니다.")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()

                        }).setNegativeButton(null, null)
                        .show()
            }

        })
    }

    private fun setDBtoExcel() {

        val date = "오늘가계부_" + UtilMethod.getExcelDate() + ".xls"
        val sql = SQLiteToExcel(context, "${Const.DbName}.db")
        DLog.e("excel start")
        sql.exportSingleTable(Const.DbName, date, object : SQLiteToExcel.ExportListener {
            override fun onError(e: java.lang.Exception?) {
                Toast.makeText(context!!, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onStart() {

            }

            override fun onCompleted(filePath: String?) {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setMessage("내파일 -> 내장메모리안에 파일이 저장되었습니다.")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()

                        }).setNegativeButton(null, null)
                        .show()
            }

        })
    }
    inner class QueryTask : AsyncTask<Void, Void, MutableList<SettingMsgModel>>() {
        override fun doInBackground(vararg params: Void?): MutableList<SettingMsgModel> {

            mItem = mutableListOf()
            val uriSMSURI = Uri.parse("content://sms/inbox")
            val cur = activity!!.contentResolver.query(uriSMSURI, null, null, null, null)

            while (cur != null && cur.moveToNext()) {
                val body = cur.getString(cur.getColumnIndexOrThrow("body"))
                val date = cur.getString(cur.getColumnIndex("date"))
                val id = cur.getString(cur.getColumnIndex("_id"))
                val msgArr = body.split(" ")
                if (msgArr[0].contains("씨티BC(7478)")) {
                    val dateFormat = UtilMethod.millisToDate(date.toLong())
                    if (msgArr[0].contains("해외승인")) {
//                        val first = msgArr[0].replace("\n", "")
                        val second = msgArr[1].replace("\n", "")
                        val third = msgArr[2].replace("\n", "")
                        val won = second.substring(0, 5).toDouble().times(1108.80).toInt().toString()
                        val cate = third.substring(5)
                        DLog.e("cate $cate : $won : $dateFormat size : USD")
                        mItem.add(SettingMsgModel(id, won, cate, dateFormat, "씨티BC(7478)해외승인"))
                    } else {

                        if (msgArr.size == 2) {
                            val first = msgArr[0].replace("\n", "").split("원")
                            val second = msgArr[1].replace("\n", "")
                            val won = first[0].substringAfter("님").replace(",", "")
                            val cate = second.substring(5)
                            DLog.e("cate $cate : $won : $dateFormat size : 2")
                            mItem.add(SettingMsgModel(id, won, cate, dateFormat, "씨티BC(7478)"))

                        } else if (msgArr.size == 3) {
                            val first = msgArr[0].replace("\n", "").split("원")
                            val second = msgArr[1].replace("\n", "")
                            val third = msgArr[2].replace("\n", "")
                            val won = first[0].substringAfter("님").replace(",", "")
                            val cate = second.substring(5) + third
                            DLog.e("cate $cate : $won : $dateFormat size : 3")
                            mItem.add(SettingMsgModel(id, won, cate, dateFormat, "씨티BC(7478)"))
                        }


                    }
                }
                if (msgArr[0].contains("신한카드(044)")) {
                    val money = msgArr[3].split("원")[0].replace(",".toRegex(), "")
                    val first = msgArr[0]
                    var five = msgArr[5]
                    var six = msgArr[6]
                    val dateFormat = UtilMethod.millisToDate(date.toLong())
                    if (msgArr.size == 7) {
                        DLog.e("set $id , $money, $five, $dateFormat, $first")
                        if (five.substring(five.length - 1).contains("-") || five.substring(five.length - 1).contains("(")) {
                            five = five.substring(0, five.length - 1)
                            mItem.add(SettingMsgModel(id, money, five, dateFormat, first))
                        } else {
                            mItem.add(SettingMsgModel(id, money, five, dateFormat, first))
                        }

                    } else if (msgArr.size == 8) {

                        if (five.substring(five.length - 1).contains("-") || five.substring(five.length - 1).contains("(")) {
                            five = five.substring(0, five.length - 1)
                        }

                        DLog.e("set $id , $money, $five + $six, $dateFormat, $first")
                        if (six.length <= 1) {
                            if (six.contains("-") || six.contains("(")) {
                                mItem.add(SettingMsgModel(id, money, five, dateFormat, first))
                            } else {
                                mItem.add(SettingMsgModel(id, money, five + six, dateFormat, first))
                            }
                        } else {
                            if (six.substring(six.length - 1).contains("-") || six.substring(six.length - 1).contains("(")) {
                                six = six.substring(0, six.length - 1)
                                mItem.add(SettingMsgModel(id, money, five + six, dateFormat, first))
                            } else {
                                mItem.add(SettingMsgModel(id, money, five + six, dateFormat, first))
                            }

                        }
                    } else if (msgArr.size == 9) {
                        var seven = msgArr[7]
                        DLog.e("set $id , $money, $five + $six + $seven, $dateFormat, $first")
                        if (six.length <= 1) {
                            if (six.contains("-") || six.contains("(")) {
                                six = ""
                            }
                        } else {
                            if (six.substring(six.length - 1).contains("-") || six.substring(six.length - 1).contains("(")) {
                                six = six.substring(0, six.length - 1)
                            }
                        }
                        if (seven.length <= 1) {
                            if (seven.contains("-") || seven.contains("(")) {
                                seven = ""
                                mItem.add(SettingMsgModel(id, money, five + six + seven, dateFormat, first))
                            } else {
                                mItem.add(SettingMsgModel(id, money, five + six + seven, dateFormat, first))
                            }
                        } else {
                            if (seven.substring(seven.length - 1).contains("-") || seven.substring(seven.length - 1).contains("(")) {
                                seven = seven.substring(0, seven.length - 1)
                                mItem.add(SettingMsgModel(id, money, five + six + seven, dateFormat, first))
                            } else {
                                mItem.add(SettingMsgModel(id, money, five + six + seven, dateFormat, first))
                            }

                        }
                    }

                }
            }
            cur?.close()
            return mItem
        }
    }
}// Required empty public constructor
