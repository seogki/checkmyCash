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
import android.widget.EditText
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Chart.ChartMainActivity
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.*
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
        getEditText()
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.estimate_frag_btn_register -> {
                savePreference()
            }
        }
    }


    private fun getEditText() {
        setTextWatcher(binding.estimateFragEditAdjust)
        setTextWatcher(binding.estimateFragEditElectric)
        setTextWatcher(binding.estimateFragEditEtc)
        setTextWatcher(binding.estimateFragEditFood)
        setTextWatcher(binding.estimateFragEditHouse)
        setTextWatcher(binding.estimateFragEditPhonebill)
        setTextWatcher(binding.estimateFragEditTransport)

        setTextWatcherDay(binding.estimateFragEditFoodDay)
        setTextWatcherDay(binding.estimateFragEditTransportDay)
    }


    private fun setTextWatcher(et: EditText) {
        et.addTextChangedListener(CustomTextWatcher(et))
    }

    private fun setTextWatcherDay(et: EditText) {
        et.addTextChangedListener(CustomTextWatcherDay(et))
    }


    private fun savePreference() {
        try {
            val pref = activity?.getSharedPreferences(Const.PreferenceTitle, MODE_PRIVATE)
            val editor = pref?.edit()
            editor?.putInt(Const.EstimateTransport
                    , binding.estimateFragEditTransport.text.toString().replace("원", "").replace(",".toRegex(), "").toInt())
            editor?.putInt(Const.EstimateTransportDay
                    , binding.estimateFragEditTransportDay.text.toString().replace("일", "").toInt())
            editor?.putInt(Const.EstimateFood
                    , binding.estimateFragEditFood.text.toString().replace("원", "").replace(",".toRegex(), "").toInt())
            editor?.putInt(Const.EstimateFoodDay
                    , binding.estimateFragEditFoodDay.text.toString().replace("일", "").toInt())
            editor?.putInt(Const.EstimateElectric
                    , binding.estimateFragEditElectric.text.toString().replace("원", "").replace(",".toRegex(), "").toInt())
            editor?.putInt(Const.EstimatePhone
                    , binding.estimateFragEditPhonebill.text.toString().replace("원", "").replace(",".toRegex(), "").toInt())
            editor?.putInt(Const.EstimateHouse
                    , binding.estimateFragEditHouse.text.toString().replace("원", "").replace(",".toRegex(), "").toInt())
            editor?.putInt(Const.EstimateAdjust
                    , binding.estimateFragEditAdjust.text.toString().replace("원", "").replace(",".toRegex(), "").toInt())
            editor?.putInt(Const.EstimateEtc
                    , binding.estimateFragEditEtc.text.toString().replace("원", "").replace(",".toRegex(), "").toInt())
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
            if (trans != 0)
                binding.estimateFragEditTransport.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(trans.toString()) + "원")
            if (transDay != 0)
                binding.estimateFragEditTransportDay.text = Editable.Factory.getInstance().newEditable(transDay.toString() + "일")
            if (food != 0)
                binding.estimateFragEditFood.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(food.toString()) + "원")
            if (foodDay != 0)
                binding.estimateFragEditFoodDay.text = Editable.Factory.getInstance().newEditable(foodDay.toString() + "일")
            if (electric != 0)
                binding.estimateFragEditElectric.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(electric.toString()) + "원")
            if (phone != 0)
                binding.estimateFragEditPhonebill.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(phone.toString()) + "원")
            if (house != 0)
                binding.estimateFragEditHouse.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(house.toString()) + "원")
            if (adjust != 0)
                binding.estimateFragEditAdjust.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(adjust.toString()) + "원")
            if (etc != 0)
                binding.estimateFragEditEtc.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(etc.toString()) + "원")
        }

    }

}// Required empty public constructor
