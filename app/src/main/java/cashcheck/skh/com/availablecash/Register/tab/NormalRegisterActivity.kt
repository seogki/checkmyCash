package cashcheck.skh.com.availablecash.Register.tab

import android.app.DatePickerDialog
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.CustomTextWatcher
import cashcheck.skh.com.availablecash.databinding.ActivityNormalRegisterBinding
import java.text.SimpleDateFormat
import java.util.*

class NormalRegisterActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityNormalRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_normal_register)
        binding.chartActvImgBack.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        val et = binding.normalAtvEditMoney
        et.addTextChangedListener(CustomTextWatcher(et))

        binding.onClickListener = this
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.normal_atv_edit_date -> {
                selectDate()
            }
            R.id.chart_actv_img_back -> {
                finishWithTransaction()
            }
        }
    }

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
            val myFormat = "yy-MM-dd hh:mm" //Change as you need
            val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
            binding.normalAtvEditDate.setText(sdf.format(myCalendar.time))
            mDay = selectedday
            mMonth = selectedmonth
            mYear = selectedyear
        }, mYear, mMonth, mDay)
        mDatePicker.show()
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition()
        else
            finish()
    }

    private fun finishWithTransaction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition()
        else
            finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);
    }
}
