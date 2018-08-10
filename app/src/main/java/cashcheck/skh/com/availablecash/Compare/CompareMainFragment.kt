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
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.FragmentCompareMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 */
class CompareMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentCompareMainBinding
    private lateinit var dbHelper: DBHelper
    private var cat1: String = ""
    private var cat2: String = ""
    private var currentName: String = ""
    private lateinit var spinnerArray: MutableList<String>
    private lateinit var lineMap: HashMap<String, Float>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_main, container, false)
        binding.compareFragImgLeft.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.compareFragImgRight.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.onClickListener = this
        currentName = binding.chartFragTxtTitlebar.text.toString()
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
            R.id.compare_frag_img_left -> {
                checkGraph(false)
            }
            R.id.compare_frag_img_right -> {
                checkGraph(true)
            }
        }
    }

    private fun checkGraph(isRight: Boolean) {
        when (currentName) {
            "라인 그래프" -> {
                if (isRight) {
                    binding.chartFragTxtTitlebar.text = "파이 그래프"
                    currentName = binding.chartFragTxtTitlebar.text.toString()
                    currentGraph()
                    setSpinner()
                } else {
                    binding.chartFragTxtTitlebar.text = "바 그래프"
                    currentName = binding.chartFragTxtTitlebar.text.toString()
                    currentGraph()
                    setSpinner()
                }
            }
            "파이 그래프" -> {
                if (!isRight) {
                    binding.chartFragTxtTitlebar.text = "라인 그래프"
                    currentName = binding.chartFragTxtTitlebar.text.toString()
                    currentGraph()

                }
            }
            "바 그래프" -> {
                if (isRight) {
                    binding.chartFragTxtTitlebar.text = "라인 그래프"
                    currentName = binding.chartFragTxtTitlebar.text.toString()
                    currentGraph()

                }
            }
        }
    }


    private fun setSpinner() {
        val db = dbHelper.readableDatabase
        spinnerArray = ArrayList()
        val cursor = db.rawQuery("SELECT date FROM ${Const.DbName} ORDER BY date DESC", null)
        while (cursor.moveToNext()) {
            val date = cursor.getString(0).replace("-".toRegex(), "").replace(" ", "").substring(2, 6)
            if (spinnerArray.contains(date)) {
                spinnerArray.remove(date)
                spinnerArray.add(date)
            } else {
                spinnerArray.add(date)
            }
        }
        DLog.e("setSpinner : $spinnerArray")
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
                cat1 = binding.compareFragTxtSet1.text.toString()
            } else if (v == "2") {
                binding.compareFragTxtSet2.text = sdf.format(myCalendar.time)
                cat2 = binding.compareFragTxtSet2.text.toString()
            }
            if (cat1 != "" && cat2 != "") {
                getDataFromDB()
            }

            mDay = selectedday
            mMonth = selectedmonth
            mYear = selectedyear

        }, mYear, mMonth, mDay)
        mDatePicker.show()
    }

    private fun getDataFromDB() {
        val db = dbHelper.readableDatabase
        lineMap = HashMap()
        val cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date BETWEEN '" + cat1 + " 00:00:00" + "' AND '" + cat2 + " 23:59:59" + "' ORDER BY date DESC", null)
        while (cursor.moveToNext()) {
            val date = cursor.getString(1).replace("-".toRegex(), "").replace(" ", "").substring(2, 6)
            val money = cursor.getString(3).toFloat()
            if (lineMap.containsKey(date)) {
                val data = lineMap.getValue(date).plus(money)
                lineMap.remove(date)
                lineMap[date] = data
            } else {
                lineMap[date] = money
            }
        }
        if (lineMap.size == 0) {
        } else {
            setLineChart()
        }
    }

    private fun setLineChart() {
        val chart = binding.compareFragLinechart
        val yValues = mutableListOf<Entry>()
        val xValues = mutableListOf<String>()

        for (i in 0 until lineMap.size) {
            yValues.add(Entry(i.toFloat(), lineMap.toList()[i].second))
            xValues.add(lineMap.toList()[i].first)
        }

        Collections.sort(yValues, EntryXComparator())
        xValues.sort()

        DLog.e("yvalues : $yValues")

        val dateSet = LineDataSet(yValues, "")

        dateSet.color = ContextCompat.getColor(context!!, R.color.statusbar)
        dateSet.valueTextColor = ContextCompat.getColor(context!!, R.color.black)
        dateSet.valueTextSize = 8F
        dateSet.lineWidth = 1.5F
        dateSet.valueFormatter = CustomWonFormatter()
        dateSet.setDrawFilled(false)
        dateSet.axisDependency = YAxis.AxisDependency.LEFT
        dateSet.fillColor = ContextCompat.getColor(context!!, R.color.rippleColor)
        dateSet.mode = LineDataSet.Mode.CUBIC_BEZIER


        val xAxis = chart.xAxis
        xAxis.setLabelCount(8, true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 7F
        xAxis.granularity = 1F
        xAxis.setDrawGridLines(true)
        xAxis.valueFormatter = CustomDateFormatter(xValues)


        val yRightAxis = chart.axisRight
        yRightAxis.isEnabled = false
        val yLeftAxis = chart.axisLeft

        yLeftAxis.setDrawGridLines(true)

        val data = LineData(dateSet)
        chart.data = data
        chart.legend.isWordWrapEnabled = true
        chart.description.isEnabled = false
        chart.animateX(1000)

        chart.invalidate()
    }

    private fun currentGraph() {
        when (currentName) {
            "라인 그래프" -> {
                binding.compareFragLinechart.visibility = View.VISIBLE
                binding.compareFragConstLinechart.visibility = View.VISIBLE
                binding.compareFragTxtSet1.visibility = View.VISIBLE
                binding.compareFragTxtSet2.visibility = View.VISIBLE
                binding.compareFragSpinner1.visibility = View.INVISIBLE
                binding.compareFragSpinner2.visibility = View.INVISIBLE
                binding.compareFragConstPiechart2.visibility = View.INVISIBLE
                binding.compareFragBarchart.visibility = View.INVISIBLE
                binding.compareFragPiechart1.visibility = View.INVISIBLE
                binding.compareFragPiechart2.visibility = View.INVISIBLE
            }
            "파이 그래프" -> {
                binding.compareFragLinechart.visibility = View.INVISIBLE

                binding.compareFragTxtSet1.visibility = View.INVISIBLE
                binding.compareFragTxtSet2.visibility = View.INVISIBLE

                binding.compareFragBarchart.visibility = View.INVISIBLE

                binding.compareFragSpinner1.visibility = View.VISIBLE
                binding.compareFragSpinner2.visibility = View.VISIBLE

                binding.compareFragPiechart1.visibility = View.VISIBLE
                binding.compareFragPiechart2.visibility = View.VISIBLE
                binding.compareFragConstLinechart.visibility = View.VISIBLE
                binding.compareFragConstPiechart2.visibility = View.VISIBLE
            }
            "바 그래프" -> {
                binding.compareFragLinechart.visibility = View.INVISIBLE

                binding.compareFragTxtSet1.visibility = View.INVISIBLE
                binding.compareFragTxtSet2.visibility = View.INVISIBLE

                binding.compareFragBarchart.visibility = View.VISIBLE

                binding.compareFragSpinner1.visibility = View.VISIBLE
                binding.compareFragSpinner2.visibility = View.VISIBLE
                binding.compareFragConstLinechart.visibility = View.VISIBLE
                binding.compareFragPiechart1.visibility = View.INVISIBLE
                binding.compareFragPiechart2.visibility = View.INVISIBLE

                binding.compareFragConstPiechart2.visibility = View.INVISIBLE
            }
        }
    }


}// Required empty public constructor
