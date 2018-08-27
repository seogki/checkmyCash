package cashcheck.skh.com.availablecash.Register.Modify

import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.ActivityModifyRegisterBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class ModifyRegisterActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {


    lateinit var binding: ActivityModifyRegisterBinding
    lateinit var dbname: String
    lateinit var id: String
    private var days: String = ""
    private lateinit var estimateDBHelper: EstimateDBHelper
    private lateinit var dbHelper: DBHelper
    private var adView: AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_register)
        binding.chartActvImgBack.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.normalAtvImgClear.drawable.setColorFilter(ContextCompat.getColor(this, R.color.midGrey), PorterDuff.Mode.SRC_ATOP)
        binding.normalAtvImgClearIll.drawable.setColorFilter(ContextCompat.getColor(this, R.color.midGrey), PorterDuff.Mode.SRC_ATOP)
        estimateDBHelper = EstimateDBHelper(applicationContext, "${Const.DbEstimateName}.db", null, 1)
        dbHelper = DBHelper(applicationContext, "${Const.DbName}.db", null, 1)
        val et = binding.normalAtvEditMoney
        adView = binding.adView
        abADs()
        binding.normalAtvEditMoney.setOnEditorActionListener(this)
        binding.normalAtvEditIll.setOnEditorActionListener(this)
        et.addTextChangedListener(CustomTextWatcher(et))
        binding.normalAtvEditIll.addTextChangedListener(CustomTextWatcherDay(binding.normalAtvEditIll))
        getDataFromIntent()
        binding.onClickListener = this
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

    override fun onPause() {
        super.onPause()
        adView?.pause()
    }

    private fun abADs() {

//        val mInterstitialAd = InterstitialAd(this)
//        mInterstitialAd.adUnitId = getString(R.string.admob_banner_compare)
//        mInterstitialAd.loadAd(AdRequest.Builder().build())

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
        adView?.adListener = object : AdListener() {
            override fun onAdClosed() {
//                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }
    }

    private fun getDataFromIntent() {
        val i = intent
        id = i.getStringExtra(Const.ItemId)
        val cate = i.getStringExtra(Const.ItemCategory)
        val money = i.getStringExtra(Const.ItemMoney)
        val date = i.getStringExtra(Const.ItemDate)
        dbname = i.getStringExtra(Const.ItemDBNAME)
        val days = i.getStringExtra(Const.ItemDays)


        if(dbname == Const.DbName){
            binding.normalAtvEditIll.visibility = View.GONE
            binding.normalAtvImgClearIll.visibility = View.GONE
            binding.normalAtvTxtIll.visibility = View.GONE
            binding.normalAtvEditMoney.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(money) + "원")
        } else {
            val moneyResult = money.toInt().div(days.toInt())
            binding.normalAtvEditIll.text = Editable.Factory.getInstance().newEditable(days + "일")
            binding.normalAtvEditMoney.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(moneyResult.toString()) + "원")
        }

        binding.normalAtvEditDate.text = Editable.Factory.getInstance().newEditable(date)
        binding.normalAtvEditCat.text = Editable.Factory.getInstance().newEditable(cate)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.normal_atv_btn_done -> {
                binding.normalAtvBtnDone.isEnabled = false
                checkEmptyAndSave()
            }
            R.id.chart_actv_img_back -> {
                finish()
            }
            R.id.normal_atv_img_clear -> {
                binding.normalAtvEditMoney.text.clear()
            }
            R.id.normal_atv_img_clear_ill -> {
                binding.normalAtvEditIll.text.clear()
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (v?.id) {
            R.id.normal_atv_edit_money -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(dbname == Const.DbName){
                        checkEmptyAndSave()
                        return true
                    } else if(dbname == Const.DbEstimateName){
                        binding.normalAtvEditIll.requestFocus()
                        return true
                    }

                }
            }
            R.id.normal_atv_edit_ill -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkEmptyAndSave()
                    return true
                }

            }
        }
        return false
    }


    private fun checkEmptyAndSave() {
        if (dbname == Const.DbName) {
            if (binding.normalAtvEditCat.text.toString() != ""
                    && binding.normalAtvEditDate.text.toString() != ""
                    && binding.normalAtvEditMoney.text.toString() != "") {

                dbHelper.updateData(id
                        , binding.normalAtvEditCat.text.toString()
                        , binding.normalAtvEditMoney.text.toString().trim().replace(",".toRegex(), "").replace("원", ""))
                finish()
                binding.normalAtvBtnDone.isEnabled = true
            } else {
                Toast.makeText(this, "모두 채워주세요", Toast.LENGTH_SHORT).show()
                binding.normalAtvBtnDone.isEnabled = true
            }
        } else if (dbname == Const.DbEstimateName) {
            if (binding.normalAtvEditCat.text.toString() != ""
                    && binding.normalAtvEditDate.text.toString() != ""
                    && binding.normalAtvEditMoney.text.toString() != ""
                    && binding.normalAtvEditIll.text.toString() != ""
                    && binding.normalAtvEditIll.text.toString() != "일") {

                estimateDBHelper.updateData(id
                        , binding.normalAtvEditCat.text.toString()
                        , binding.normalAtvEditMoney.text.toString().trim().replace(",".toRegex(), "").replace("원", "")
                        , binding.normalAtvEditIll.text.toString().replace("일",""))
                finish()
                binding.normalAtvBtnDone.isEnabled = true
            } else {
                Toast.makeText(this, "모두 채워주세요", Toast.LENGTH_SHORT).show()
                binding.normalAtvBtnDone.isEnabled = true
            }


        }
    }
}
