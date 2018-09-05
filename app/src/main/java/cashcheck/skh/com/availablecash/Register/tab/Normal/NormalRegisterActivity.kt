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
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.ActivityNormalRegisterBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.text.SimpleDateFormat
import java.util.*


class NormalRegisterActivity : AppCompatActivity(), View.OnClickListener, CategoryDialogInterface, TextView.OnEditorActionListener {


    lateinit var binding: ActivityNormalRegisterBinding
    private lateinit var db: DBHelper
    private var days: String = ""
    private var firstCalData = ""
    private var secondCalData = ""
    private var whichOne = ""
    private var adView: AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_normal_register)
        binding.chartActvImgBack.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.normalAtvImgClear.drawable.setColorFilter(ContextCompat.getColor(this, R.color.midGrey), PorterDuff.Mode.SRC_ATOP)
        binding.normalAtvBtnDone.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        db = DBHelper(applicationContext, "${Const.DbName}.db", null, 1)
        val et = binding.normalAtvEditMoney
        adView = binding.adView
        abADs()
        binding.onClickListener = this
        binding.normalAtvEditMoney.setOnEditorActionListener(this)
        et.addTextChangedListener(CustomTextWatcher(et))


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


        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
        adView?.adListener = object : AdListener() {
            override fun onAdClosed() {

            }
        }
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

            R.id.cal_txt_0 -> {
                if (binding.calTxtTitle.text.toString().isNotEmpty()) {
                    binding.calTxtTitle.append("0")
                    binding.calTxtResultBtm.append("0")
                }
            }
            R.id.cal_txt_1 -> {
                binding.calTxtTitle.append("1"); binding.calTxtResultBtm.append("1")
            }
            R.id.cal_txt_2 -> {
                binding.calTxtTitle.append("2");binding.calTxtResultBtm.append("2")
            }
            R.id.cal_txt_3 -> {
                binding.calTxtTitle.append("3");binding.calTxtResultBtm.append("3")
            }
            R.id.cal_txt_4 -> {
                binding.calTxtTitle.append("4");binding.calTxtResultBtm.append("4")
            }
            R.id.cal_txt_5 -> {
                binding.calTxtTitle.append("5"); binding.calTxtResultBtm.append("5")
            }
            R.id.cal_txt_6 -> {
                binding.calTxtTitle.append("6"); binding.calTxtResultBtm.append("6")
            }
            R.id.cal_txt_7 -> {
                binding.calTxtTitle.append("7"); binding.calTxtResultBtm.append("7")
            }
            R.id.cal_txt_8 -> {
                binding.calTxtTitle.append("8"); binding.calTxtResultBtm.append("8")
            }
            R.id.cal_txt_9 -> {
                binding.calTxtTitle.append("9"); binding.calTxtResultBtm.append("9")
            }
            R.id.cal_txt_dot -> {
                if (binding.calTxtTitle.text.toString().isNotEmpty()) {
                    binding.calTxtTitle.append(".")
                    binding.calTxtResultBtm.append("1")
                }
            }
            R.id.cal_txt_cancel -> {
                firstCalData = ""
                secondCalData = ""
                whichOne = ""
                binding.calTxtTitle.text = ""
                binding.calTxtResult.text = ""
                binding.normalAtvEditMoney.text = Editable.Factory.getInstance().newEditable("")
                binding.calTxtResultBtm.text = ""
            }
            R.id.cal_txt_divide -> {
                calculate("/")
                binding.calTxtResultBtm.append(" / ")
            }
            R.id.cal_txt_time -> {
                calculate("*")
                binding.calTxtResultBtm.append(" * ")
            }
            R.id.cal_txt_plus -> {
                calculate("+")
                binding.calTxtResultBtm.append(" + ")
            }
            R.id.cal_txt_minus -> {
                calculate("-")
                binding.calTxtResultBtm.append(" - ")
            }
            R.id.cal_txt_equal -> {
                calculate("=")

            }
        }
    }

    private fun calculate(what: String) {
        when (what) {
            "/" -> {
                if (firstCalData != "") {
                    secondCalData = binding.calTxtTitle.text.toString()
                    var result = ""
                    when (whichOne) {
                        "/" -> result = firstCalData.toFloat().div(secondCalData.toFloat()).toString()
                        "*" -> result = firstCalData.toFloat().times(secondCalData.toFloat()).toString()
                        "-" -> result = firstCalData.toFloat().minus(secondCalData.toFloat()).toString()
                        "+" -> result = firstCalData.toFloat().plus(secondCalData.toFloat()).toString()
                    }
                    binding.calTxtResult.text = result
                    binding.calTxtTitle.text = ""
                    whichOne = "/"
                    firstCalData = result
                } else {
                    firstCalData = binding.calTxtTitle.text.toString()
                    binding.calTxtTitle.text = ""
                    whichOne = "/"
                }
            }
            "*" -> {
                if (firstCalData != "") {
                    secondCalData = binding.calTxtTitle.text.toString()
                    var result = ""
                    when (whichOne) {
                        "/" -> result = firstCalData.toFloat().div(secondCalData.toFloat()).toString()
                        "*" -> result = firstCalData.toFloat().times(secondCalData.toFloat()).toString()
                        "-" -> result = firstCalData.toFloat().minus(secondCalData.toFloat()).toString()
                        "+" -> result = firstCalData.toFloat().plus(secondCalData.toFloat()).toString()
                    }
                    binding.calTxtResult.text = result
                    binding.calTxtTitle.text = ""
                    whichOne = "*"
                    firstCalData = result
                } else {
                    firstCalData = binding.calTxtTitle.text.toString()
                    binding.calTxtTitle.text = ""
                    whichOne = "*"
                }
            }
            "+" -> {
                if (firstCalData != "") {
                    secondCalData = binding.calTxtTitle.text.toString()
                    var result = ""
                    when (whichOne) {
                        "/" -> result = firstCalData.toFloat().div(secondCalData.toFloat()).toString()
                        "*" -> result = firstCalData.toFloat().times(secondCalData.toFloat()).toString()
                        "-" -> result = firstCalData.toFloat().minus(secondCalData.toFloat()).toString()
                        "+" -> result = firstCalData.toFloat().plus(secondCalData.toFloat()).toString()
                    }
                    binding.calTxtResult.text = result
                    binding.calTxtTitle.text = ""
                    whichOne = "+"
                    firstCalData = result
                } else {
                    firstCalData = binding.calTxtTitle.text.toString()
                    binding.calTxtTitle.text = ""
                    whichOne = "+"
                }
            }
            "-" -> {
                if (firstCalData != "") {
                    secondCalData = binding.calTxtTitle.text.toString()
                    var result = ""
                    when (whichOne) {
                        "/" -> result = firstCalData.toFloat().div(secondCalData.toFloat()).toString()
                        "*" -> result = firstCalData.toFloat().times(secondCalData.toFloat()).toString()
                        "-" -> result = firstCalData.toFloat().minus(secondCalData.toFloat()).toString()
                        "+" -> result = firstCalData.toFloat().plus(secondCalData.toFloat()).toString()
                    }
                    binding.calTxtResult.text = result
                    binding.calTxtTitle.text = ""
                    whichOne = "-"
                    firstCalData = result
                } else {
                    firstCalData = binding.calTxtTitle.text.toString()
                    binding.calTxtTitle.text = ""
                    whichOne = "-"
                }
            }
            "=" -> {
                if (firstCalData != "") {
                    secondCalData = binding.calTxtTitle.text.toString()
                    if (secondCalData != "") {
                        var result = ""
                        when (whichOne) {
                            "/" -> result = firstCalData.toFloat().div(secondCalData.toFloat()).toString()
                            "*" -> result = firstCalData.toFloat().times(secondCalData.toFloat()).toString()
                            "-" -> result = firstCalData.toFloat().minus(secondCalData.toFloat()).toString()
                            "+" -> result = firstCalData.toFloat().plus(secondCalData.toFloat()).toString()
                        }

                        binding.calTxtTitle.text = result
                        binding.calTxtResult.text = result
                        binding.normalAtvEditMoney.text = Editable.Factory.getInstance().newEditable(UtilMethod.currencyFormat(Math.ceil(result.toDouble()).toString()) + "원")
                        firstCalData = ""
                        secondCalData = ""
                    }
                }
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
            binding.normalAtvEditCat.performClick()
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
