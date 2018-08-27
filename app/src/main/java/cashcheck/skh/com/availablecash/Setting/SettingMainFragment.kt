package cashcheck.skh.com.availablecash.Setting

import android.Manifest
import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentSettingMainBinding
import com.ajts.androidmads.library.SQLiteToExcel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission


class SettingMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentSettingMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_main, container, false)

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
                        .setMessage("아직 개발중입니다.")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()

                        }).setNegativeButton(null, null)
                        .show()
            }
            R.id.setting_frag_btn_excel_data -> {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setTitle("파일 저장")
                        .setMessage("데이터를 엑셀파일로 저장하시겠습니까?")
                        .setPositiveButton("확인", { dialog, _ ->
                            setTedPermission()
                            dialog.dismiss()

                        }).setNegativeButton("취소", { dialog, _ ->
                            dialog.dismiss()
                        })
                        .show()


            }
        }
    }

    private fun setTedPermission() {
        TedPermission.with(context)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        Handler().postDelayed({
                            setDBtoExcel()
                        }, 10)
                    }

                    override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>) {

                    }
                }).check()
    }

    private fun setDBtoExcel() {
        val date = "가계부_" + UtilMethod.getExcelDate() + ".xls"
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
                        .setMessage("$filePath\n에 파일이 저장되었습니다.")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()

                        }).setNegativeButton(null, null)
                        .show()
            }

        })
    }

}// Required empty public constructor
