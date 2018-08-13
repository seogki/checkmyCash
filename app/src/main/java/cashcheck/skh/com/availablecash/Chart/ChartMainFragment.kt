package cashcheck.skh.com.availablecash.Chart


import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.FragmentChartMainBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


/**
 * A simple [Fragment] subclass.
 */
class ChartMainFragment : BaseFragment() {

    lateinit var binding: FragmentChartMainBinding
    private lateinit var dbHelper: DBHelper
    private var totalUsage: Float = 0F
    private var estimateUsage: Int = 0
    private var resultUsage: Int = 0
    private lateinit var map: HashMap<String, String>
    private lateinit var tempMap: HashMap<String, String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart_main, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        val date = UtilMethod.getCurrentDate().replace("-", "")
        binding.chartFragTxtMonth.text = "20" + date.substring(0, 2) + "년 " + date.substring(2, 4) + "월 "
        tempMap = HashMap()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setPieChart() {
        val chart = binding.chartFragPiechart
        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()

        for ((key, value) in map) {
            yvalues.add(PieEntry(value.toFloat(), key))
            totalUsage = totalUsage.plus(value.toFloat())
            DLog.e("total $totalUsage")
        }
        val moneyResult = UtilMethod.currencyFormat(totalUsage.toInt().toString())
        binding.chartFragTxtTotalMoney.text = moneyResult + "원"

        val dataSet = PieDataSet(yvalues, "")

        dataSet.sliceSpace = 1F
        dataSet.selectionShift = 5F
        dataSet.valueTextSize = 9f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 90f
        dataSet.valueLinePart1Length = 0.5f
        dataSet.valueLinePart2Length = 0.2f
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomPercentFormatter()
        chart.isRotationEnabled = false
        chart.setTouchEnabled(false)
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)
        chart.setExtraOffsets(25F, 15F, 25F, 15F)


        val colors = mutableListOf<Int>()
        colors.add(ContextCompat.getColor(context!!, R.color.lightBlue))
        colors.add(ContextCompat.getColor(context!!, R.color.lightOrange))
        colors.add(ContextCompat.getColor(context!!, R.color.lightGreen))
        colors.add(ContextCompat.getColor(context!!, R.color.lightPink))

        dataSet.colors = colors
        dataSet.label = ""
        val data = PieData(dataSet)

        chart.legend.isEnabled = false
//        val l = chart.legend
//        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
//        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//        l.orientation = Legend.LegendOrientation.HORIZONTAL
//        l.setDrawInside(false)
//        l.xEntrySpace = 7f
//        l.yEntrySpace = 5f
//        chart.legend.isWordWrapEnabled = true
        chart.data = data
        chart.description.isEnabled = false
        chart.animateXY(1000, 1000)
        chart.invalidate()

        getResultUsage()
    }

    private fun getResultUsage() {
        resultUsage = estimateUsage.minus(totalUsage.toInt())
        val moneys = UtilMethod.currencyFormat(resultUsage.toString())
        DLog.e("usage : $resultUsage")
        when {
            resultUsage < 0 -> {
                binding.chartFragTxtTotalresultMoney.text = moneys + "원... 분발하세요!"
                binding.chartFragTxtTotalresultMoney.setTextColor(ContextCompat.getColor(context!!, R.color.Red))
            }
            resultUsage > 0 -> {
                binding.chartFragTxtTotalresultMoney.text = moneys + "원 당신은 저축왕이군요!"
                binding.chartFragTxtTotalresultMoney.setTextColor(ContextCompat.getColor(context!!, R.color.Neon))
            }
            else -> {

            }
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
            estimateUsage = (trans * transDay) + (food * foodDay) + electric + phone + house + adjust + etc
            val data = UtilMethod.currencyFormat(estimateUsage.toString())
            binding.chartFragTxtEstimateMoney.text = "" + data + "원"

            if (totalUsage != 0F) {
                getResultUsage()
            }
        }

        val db = dbHelper.readableDatabase
        if (db != null) {

            map = HashMap()
            val currentDate = UtilMethod.getCurrentDate()
            DLog.e("cur $currentDate")
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
            val cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + currentDate + "%' ORDER BY date DESC", null)
            while (cursor.moveToNext()) {
                val cate = cursor.getString(2)
                val money = cursor.getString(3)
                if (map.containsKey(cate)) {
                    val data = map.getValue(cate).toFloat().plus(money.toFloat())
                    map.remove(cate)
                    map[cate] = data.toString()
                } else {
                    map[cate] = money
                }
            }
            if (tempMap != map) {
                tempMap = map
                totalUsage = 0F
                setPieChart()
            } else {

            }
        }

    }


}// Required empty public constructor
