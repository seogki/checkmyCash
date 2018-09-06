package cashcheck.skh.com.availablecash.Compare.tab


import android.app.DatePickerDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Compare.Listener.CompareLineListener
import cashcheck.skh.com.availablecash.Compare.Thread.CompareLineThread
import cashcheck.skh.com.availablecash.Compare.model.CompareLineModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.adapter.CompareLineAdapter
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.FragmentCompareLineBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class CompareLineFragment : BaseFragment(), View.OnClickListener, CompareLineListener {



    private lateinit var binding: FragmentCompareLineBinding

    private lateinit var dbHelper: DBHelper
    private var cat1: String = ""
    private var cat2: String = ""
    private lateinit var lineMap: HashMap<String, Float>
    private lateinit var compareLineAdapter: CompareLineAdapter
    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_line, container, false)
        binding.compareFragLinechart.setNoDataText("데이터가 존재하지 않습니다.")
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        binding.compareFragLineHeader.visibility = View.INVISIBLE
        binding.onClickListener = this
        setRv()
        return binding.root
    }

    private fun setRv() {
        compareLineAdapter = CompareLineAdapter(context!!, ArrayList())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 2
        binding.compareFragLineRv.layoutManager = layoutManager
        binding.compareFragLineRv.isNestedScrollingEnabled = false
        binding.compareFragLineRv.itemAnimator = null

        Handler().postDelayed({
            binding.compareFragLineRv.adapter = compareLineAdapter
        }, 10)
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

    private fun setLineChart() {


        val chart = binding.compareFragLinechart
        val yValues = mutableListOf<Entry>()
        val xValues = mutableListOf<String>()

        val sortedMap = lineMap.toSortedMap()

        for (i in 0 until sortedMap.size) {
            yValues.add(Entry(i.toFloat(), sortedMap.toList()[i].second))
            xValues.add(sortedMap.toList()[i].first)
        }

        DLog.e("yvalues : $yValues")

        val dateSet = LineDataSet(yValues, "금액")

        dateSet.color = ContextCompat.getColor(context!!, R.color.statusbar)
        dateSet.valueTextColor = ContextCompat.getColor(context!!, R.color.black)
        dateSet.valueTextSize = 8F
        dateSet.lineWidth = 1.5F
        dateSet.valueFormatter = CustomNoWonFormatter()
        dateSet.setDrawFilled(true)
        dateSet.axisDependency = YAxis.AxisDependency.LEFT
        dateSet.fillColor = ContextCompat.getColor(context!!, R.color.rippleColor)
        dateSet.mode = LineDataSet.Mode.CUBIC_BEZIER


        val xAxis = chart.xAxis

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


    private fun getDataFromDB() {
        val db = dbHelper.readableDatabase
        lineMap = HashMap()
//        val cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date BETWEEN '" + cat1 + " 00:00:00" + "' AND '" + cat2 + " 23:59:59" + "' ORDER BY date DESC", null)
        val cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date BETWEEN '" + cat1 + "' AND '" + cat2 + "' ORDER BY date DESC", null)
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
        if (lineMap.size < 1) {
        } else {
            setLineChart()
            setRvData()
        }
    }

    private fun setRvData() {
        val thread = CompareLineThread(lineMap,ArrayList(), this)
        thread.start()



    }

    override fun getItem(arr: ArrayList<CompareLineModel>) {
        activity?.runOnUiThread {  binding.compareFragLineHeader.visibility = View.VISIBLE }
        compareLineAdapter.clearItems()
        compareLineAdapter.addItems(arr)
    }

    private fun setTitleText(date: String, textView: TextView) {
        val result = date.replace("-", "").replace(" ", "")
        val year = result.substring(0, 2)
        val month = result.substring(2, 4)
        val days = result.substring(4, 6)

        textView.text = "20${year}년 ${month}월 ${days}일"
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
            val myFormat = "yy-MM-dd" //Change as you need
            //kk or HH
            val sdf = SimpleDateFormat(myFormat, Locale.KOREA)
            if (v == "1") {
                binding.compareFragTxtSet1.text = sdf.format(myCalendar.time)
                cat1 = binding.compareFragTxtSet1.text.toString()
                setTitleText(cat1, binding.compareFragLineFirstTitle)
            } else if (v == "2") {
                binding.compareFragTxtSet2.text = sdf.format(myCalendar.time)
                cat2 = binding.compareFragTxtSet2.text.toString()
                setTitleText(cat2, binding.compareFragLineSecondTitle)
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
}// Required empty public constructor
