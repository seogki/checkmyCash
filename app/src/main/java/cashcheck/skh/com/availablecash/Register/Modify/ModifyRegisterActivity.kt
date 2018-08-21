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

class ModifyRegisterActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {


    lateinit var binding: ActivityModifyRegisterBinding
    lateinit var dbname: String
    lateinit var id: String
    private var days: String = ""
    private lateinit var estimateDBHelper: EstimateDBHelper
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_register)
        binding.chartActvImgBack.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.normalAtvImgClear.drawable.setColorFilter(ContextCompat.getColor(this, R.color.midGrey), PorterDuff.Mode.SRC_ATOP)
        estimateDBHelper = EstimateDBHelper(applicationContext, "${Const.DbEstimateName}.db", null, 1)
        dbHelper = DBHelper(applicationContext, "${Const.DbName}.db", null, 1)
        val et = binding.normalAtvEditMoney
        binding.normalAtvEditMoney.setOnEditorActionListener(this)
        et.addTextChangedListener(CustomTextWatcher(et))
        getDataFromIntent()
        binding.onClickListener = this
    }

    private fun getDataFromIntent() {
        val i = intent
        id = i.getStringExtra(Const.ItemId)
        val cate = i.getStringExtra(Const.ItemCategory)
        val money = i.getStringExtra(Const.ItemMoney)
        val date = i.getStringExtra(Const.ItemDate)
        dbname = i.getStringExtra(Const.ItemDBNAME)

        binding.normalAtvEditDate.text = Editable.Factory.getInstance().newEditable(date)
        binding.normalAtvEditCat.text = Editable.Factory.getInstance().newEditable(cate)
        binding.normalAtvEditMoney.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(money) + "원")
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
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            return if (binding.normalAtvEditCat.text.toString() != "" && binding.normalAtvEditDate.text.toString() != "" && binding.normalAtvEditMoney.text.toString() != "") {
                checkEmptyAndSave()
                true
            } else {
                Toast.makeText(this, "모두 채워주세요", Toast.LENGTH_SHORT).show()
                true
            }
        }
        return false
    }


    private fun checkEmptyAndSave() {
        if (binding.normalAtvEditCat.text.toString() != "" && binding.normalAtvEditDate.text.toString() != "" && binding.normalAtvEditMoney.text.toString() != "") {
            when (dbname) {
                Const.DbName -> {
                    dbHelper.updateData(id
                            , binding.normalAtvEditCat.text.toString()
                            , binding.normalAtvEditMoney.text.toString().trim().replace(",".toRegex(), "").replace("원", ""))
                    finish()
                    binding.normalAtvBtnDone.isEnabled = true
                }
                Const.DbEstimateName -> {
                    estimateDBHelper.updateData(id
                            , binding.normalAtvEditCat.text.toString()
                            , binding.normalAtvEditMoney.text.toString().trim().replace(",".toRegex(), "").replace("원", ""))
                    finish()
                    binding.normalAtvBtnDone.isEnabled = true
                }
            }

        } else {
            Toast.makeText(this, "모두 채워주세요", Toast.LENGTH_SHORT).show()
            binding.normalAtvBtnDone.isEnabled = true
        }
    }
}
