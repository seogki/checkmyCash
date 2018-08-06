package cashcheck.skh.com.availablecash.Register.tab


import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Chart.ChartMainActivity
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentEstimateRegisterBinding


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
            editor?.putInt(Const.EstimateElectric, binding.estimateFragEditElectric.text.toString().toInt())
            editor?.putInt(Const.EstimatePhone, binding.estimateFragEditPhonebill.text.toString().toInt())
            editor?.putInt(Const.EstimateHouse, binding.estimateFragEditHouse.text.toString().toInt())
            editor?.putInt(Const.EstimateAdjust, binding.estimateFragEditAdjust.text.toString().toInt())
            editor?.putInt(Const.EstimateEtc, binding.estimateFragEditEtc.text.toString().toInt())
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
    override fun onResume() {
        super.onResume()
        if (activity != null) {
            val pref = activity!!.getSharedPreferences(Const.PreferenceTitle, MODE_PRIVATE)
            val trans = pref.getInt(Const.EstimateTransport, 0)
            val transDay = pref.getInt(Const.EstimateTransportDay, 0)
            val food = pref.getInt(Const.EstimateFood, 0)
            val foodDay = pref.getInt(Const.EstimateFoodDay, 0)
            val electric = pref.getInt(Const.EstimateElectric, 0)
            val phone = pref.getInt(Const.EstimatePhone, 0)
            val house = pref.getInt(Const.EstimateHouse, 0)
            val adjust = pref.getInt(Const.EstimateAdjust, 0)
            val etc = pref.getInt(Const.EstimateEtc, 0)

            val result = (trans * transDay) + (food * foodDay) + electric + phone + house + adjust + etc
            val data = UtilMethod.currencyFormat(result.toString())
            DLog.e("trans $trans + $transDay + $food + $foodDay + $result + $data")
            binding.estimateFragEditTransport.text = Editable.Factory.getInstance().newEditable(trans.toString())
            binding.estimateFragEditTransportDay.text = Editable.Factory.getInstance().newEditable(transDay.toString())
            binding.estimateFragEditFood.text = Editable.Factory.getInstance().newEditable(food.toString())
            binding.estimateFragEditFoodDay.text = Editable.Factory.getInstance().newEditable(foodDay.toString())
            binding.estimateFragEditElectric.text = Editable.Factory.getInstance().newEditable(electric.toString())
            binding.estimateFragEditPhonebill.text = Editable.Factory.getInstance().newEditable(phone.toString())
            binding.estimateFragEditHouse.text = Editable.Factory.getInstance().newEditable(house.toString())
            binding.estimateFragEditAdjust.text = Editable.Factory.getInstance().newEditable(adjust.toString())
            binding.estimateFragEditEtc.text = Editable.Factory.getInstance().newEditable(etc.toString())
        }

    }

}// Required empty public constructor
