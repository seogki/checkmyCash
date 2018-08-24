package cashcheck.skh.com.availablecash.Setting

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.FragmentSettingMainBinding




class SettingMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentSettingMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_main, container, false)

        binding.onClickListener = this
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.setting_frag_btn_clear_data -> {
                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setTitle("데이터 삭제")
                        .setMessage("정말로 지우시겠습니까?")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()
                            try{
                                if((context!!.getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()){
                                    Toast.makeText(context!!,"삭제되었습니다",Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context!!,"오류로 인해 삭제 불가 설정에가서 직접 지워주세요! \n설정 -> 어플리케이션 -> 앱 -> 저장공간 -> 데이터 삭제",Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception){
                                Toast.makeText(context!!,"오류가 발생했습니다. 설정에가서 직접 지워주세요! \n설정 -> 어플리케이션 -> 앱 -> 저장공간 -> 데이터 삭제",Toast.LENGTH_LONG).show()
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
        }
    }

}// Required empty public constructor
