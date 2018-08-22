package cashcheck.skh.com.availablecash.Register.tab.Normal

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Dialog.CategoryDialogV2
import cashcheck.skh.com.availablecash.Register.Interface.CategoryDialogInterface
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.CustomTextWatcher
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.databinding.ActivityNormalRegisterBinding
import java.text.SimpleDateFormat
import java.util.*


class NormalRegisterActivity : AppCompatActivity(), View.OnClickListener, CategoryDialogInterface, TextView.OnEditorActionListener {


    lateinit var binding: ActivityNormalRegisterBinding
    private lateinit var db: DBHelper
    private var days: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_normal_register)
        binding.chartActvImgBack.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.normalAtvImgClear.drawable.setColorFilter(ContextCompat.getColor(this, R.color.midGrey), PorterDuff.Mode.SRC_ATOP)
        binding.normalAtvBtnDone.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        db = DBHelper(applicationContext, "${Const.DbName}.db", null, 1)
        val et = binding.normalAtvEditMoney
        binding.normalAtvEditMoney.setOnEditorActionListener(this)
        et.addTextChangedListener(CustomTextWatcher(et))
        binding.onClickListener = this
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            return if (binding.normalAtvEditCat.text.toString() != "" && binding.normalAtvEditDate.text.toString() != "" && binding.normalAtvEditMoney.text.toString() != "") {
                saveInSqlLite()
                true
            } else {
                Toast.makeText(this, "모두 채워주세요", Toast.LENGTH_SHORT).show()
                true
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.normal_atv_edit_date -> {
                selectDate()
            }
            R.id.chart_actv_img_back -> {
                finish()
            }
            R.id.normal_atv_edit_cat -> {
                measureDialogSize()
            }
            R.id.normal_atv_img_clear -> {
                binding.normalAtvEditMoney.text.clear()
            }
            R.id.normal_atv_btn_done -> {
                binding.normalAtvBtnDone.isEnabled = false
                saveInSqlLite()
            }
        }
    }


    private fun saveInSqlLite() {
        if (binding.normalAtvEditCat.text.toString() != "" && binding.normalAtvEditDate.text.toString() != "" && binding.normalAtvEditMoney.text.toString() != "") {
            val date = binding.normalAtvEditDate.text.toString().trim()
            val category = binding.normalAtvEditCat.text.toString().trim()
            val money = binding.normalAtvEditMoney.text.toString().trim().replace(",".toRegex(), "").replace("원", "")
            db.insertData(date, category, money, days)
            DLog.e("insert result : ${db.getResult()}")
            binding.normalAtvBtnDone.isEnabled = true
            finish()
        } else {
            Toast.makeText(this, "모두 채워주세요", Toast.LENGTH_SHORT).show()
            binding.normalAtvBtnDone.isEnabled = true
        }
    }

    private fun measureDialogSize() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val d = CategoryDialogV2(this, this)
        d.show()
        d.window.setLayout((6 * width) / 7, (3 * height) / 4)
    }

    @SuppressLint("SimpleDateFormat")
    private fun selectDate() {

        val mcurrentDate = Calendar.getInstance()
        var mYear = mcurrentDate.get(Calendar.YEAR)
        var mMonth = mcurrentDate.get(Calendar.MONTH)
        var mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

        val mDatePicker = DatePickerDialog(this, R.style.MyDatePickerDialogTheme, DatePickerDialog.OnDateSetListener { _, selectedyear, selectedmonth, selectedday ->

            val myCalendar = Calendar.getInstance()
            myCalendar.set(Calendar.YEAR, selectedyear)
            myCalendar.set(Calendar.MONTH, selectedmonth)
            myCalendar.set(Calendar.DAY_OF_MONTH, selectedday)
            val myFormat = "yy-MM-dd" //Change as you need
            //kk or HH
            val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
            binding.normalAtvEditDate.setText(sdf.format(myCalendar.time))

            mDay = selectedday
            mMonth = selectedmonth
            mYear = selectedyear
            dayIntToString(myCalendar.get(Calendar.DAY_OF_WEEK) - 1)

        }, mYear, mMonth, mDay)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDatePicker.show()
        } else {
            mDatePicker.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDatePicker.show()
        }

    }

    private fun dayIntToString(i: Int) {
        when (i) {
            0 -> days = "일요일"
            1 -> days = "월요일"
            2 -> days = "화요일"
            3 -> days = "수요일"
            4 -> days = "목요일"
            5 -> days = "금요일"
            6 -> days = "토요일"
        }
        DLog.e("current day : $days")
    }


    override fun getTextFromDialog(text: String) {
        binding.normalAtvEditCat.text = Editable.Factory.getInstance().newEditable(text)

        Handler().postDelayed({
            binding.normalAtvEditMoney.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.normalAtvEditMoney, InputMethodManager.SHOW_IMPLICIT)
        }, 100)

    }
}
