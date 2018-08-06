package cashcheck.skh.com.availablecash.Register.tab


import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Chart.ChartMainActivity
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.FragmentEstimateRegisterBinding
import cashcheck.skh.com.availablecash.Util.Const


/**
 * A simple [Fragment] subclass.
 */
class EstimateRegisterFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentEstimateRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_estimate_register, container, false)
        binding.onClickListener = this

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.estimate_frag_btn_register -> {
                savePreference()
            }
        }
    }


    private fun savePreference() {
        try {
            val pref = activity?.getSharedPreferences(Const.PreferenceTitle, MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putInt(Const.EstimateTransport, binding.estimateFragEditTransport.text.toString().toInt())
            editor?.putInt(Const.EstimateTransportDay, binding.estimateFragEditTransportDay.text.toString().toInt())
            editor?.putInt(Const.EstimateFood, binding.estimateFragEditFood.text.toString().toInt())
            editor?.putInt(Const.EstimateFoodDay, binding.estimateFragEditFoodDay.text.toString().toInt())
            editor?.apply()
            AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                    .setMessage("등록 되었습니다.")
                    .setPositiveButton("확인", { dialog, _ ->
                        dialog.dismiss()
                        beginActivity(Intent(context!!, ChartMainActivity::class.java))
                    }).setNegativeButton(null, null)
                    .show()
        } catch (e: Exception) {
            alertDialog(context!!, "다시 시도해주시기 바랍니다.")
        }
    }

}// Required empty public constructor
