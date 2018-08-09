package cashcheck.skh.com.availablecash.Compare


import android.app.DatePickerDialog
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.databinding.FragmentCompareMainBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CompareMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentCompareMainBinding
    private lateinit var dbHelper: DBHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_main, container, false)
        binding.compareFragImgLeft.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.compareFragImgRight.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.onClickListener = this
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.compare_frag_txt_set1 -> {
                selectDate("1")
            }

            R.id.compare_frag_txt_set2 -> {
                selectDate("2")
            }
        }
    }

    private fun selectDate(v: String) {

        val mcurrentDate = Calendar.getInstance()
        var mYear = mcurrentDate.get(Calendar.YEAR)
        var mMonth = mcurrentDate.get(Calendar.MONTH)
        var mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

        val mDatePicker = DatePickerDialog(context!!, R.style.MyDatePickerDialogTheme, DatePickerDialog.OnDateSetListener { _, selectedyear, selectedmonth, selectedday ->
            val myCalendar = Calendar.getInstance()
            myCalendar.set(Calendar.YEAR, selectedyear)
            myCalendar.set(Calendar.MONTH, selectedmonth)
            myCalendar.set(Calendar.DAY_OF_MONTH, selectedday)
            val myFormat = "yy-MM dd" //Change as you need
            //kk or HH
            val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
            if (v == "1") {
                binding.compareFragTxtSet1.text = sdf.format(myCalendar.time)
            } else if (v == "2") {
                binding.compareFragTxtSet2.text = sdf.format(myCalendar.time)
            }

            mDay = selectedday
            mMonth = selectedmonth
            mYear = selectedyear

        }, mYear, mMonth, mDay)
        mDatePicker.show()
    }

//    private fun getDataFromDB() {
//        dbHelper.readableDatabase
//        val cursor = dbHelper.rawQuery("SELECT * FROM ${Const.DbName} WHERE date BETWEEN '%" + dates + "%' ORDER BY date DESC", null)
//        while (cursor.moveToNext()) {
//
//            val cate = cursor.getString(2)
//            val money = cursor.getString(3)
//            if (map.containsKey(cate)) {
//                val data = map.getValue(cate).toFloat().plus(money.toFloat())
//                map.remove(cate)
//                map[cate] = data.toString()
//            } else {
//                map[cate] = money
//            }
//        }
//    }

}// Required empty public constructor
