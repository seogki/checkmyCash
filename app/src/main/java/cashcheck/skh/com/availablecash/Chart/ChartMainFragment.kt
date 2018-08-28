package cashcheck.skh.com.availablecash.Chart


import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.graphics.Color
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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 */
class ChartMainFragment : BaseFragment() {

    lateinit var binding: FragmentChartMainBinding
    private lateinit var dbHelper: DBHelper
    private var totalUsage: Float = 0F
    private var estimateUsage: Int = 0
    private var resultUsage: Int = 0
    private lateinit var map: HashMap<String, Float>
    private lateinit var tempMap: HashMap<String, Float>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart_main, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        binding.chartFragPiechart.setNoDataText("데이터가 존재하지 않습니다.")
        tempMap = HashMap()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setPieChart() {
        val chart = binding.chartFragPiechart
        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()
        val result = map.toList().sortedByDescending { (_, value) -> value }.toMap()
        for((key,value) in result){

            totalUsage = totalUsage.plus(value.toFloat())

        }
        if(result.size <= 4){
            for ((key, value) in result) {
                val percent = value.toFloat().div(totalUsage).times(100F)
                DLog.e("percent : $percent")
                yvalues.add(PieEntry(value.toFloat(), "$key ${DecimalFormat("0.0").format(percent)}%"))

                DLog.e("total $totalUsage")
            }
        } else {
            var data = 0F
            for(i in 0 until result.size){
                if(i >= 3){
                    data += result.toList()[i].second
                } else {
                    val percent = result.toList()[i].second.toFloat().div(totalUsage).times(100F)
                    yvalues.add(PieEntry(result.toList()[i].second.toFloat(), "${result.toList()[i].first} ${DecimalFormat("0.0").format(percent)}%"))
                }
            }
            val percent = data.div(totalUsage).times(100F)
            yvalues.add(PieEntry(data, "그외 ${DecimalFormat("0.0").format(percent)}%"))

        }


        val moneyResult = UtilMethod.currencyFormat(totalUsage.toInt().toString())
        binding.chartFragTxtTotalMoney.text = moneyResult + "원"
        val dataSet = PieDataSet(yvalues, "")



        dataSet.sliceSpace = 1F
        dataSet.selectionShift = 5F
        dataSet.valueTextSize = 9f
        dataSet.valueTextColor = ContextCompat.getColor(context!!,R.color.black)
//        dataSet.valueTypeface = Typeface.DEFAULT_BOLD
//        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
//        dataSet.valueLinePart1OffsetPercentage = 100f
//        dataSet.valueLinePart1Length = 0.3f
//        dataSet.valueLinePart2Length = 0.1f
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomMainPercentFormatter()
        chart.isRotationEnabled = false
        chart.setDrawEntryLabels(false)
        chart.setTouchEnabled(false)
        chart.isDrawHoleEnabled = false
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)
//        chart.setExtraOffsets(25F, 15F, 25F, 15F)
        chart.holeRadius = 70f
        chart.setHoleColor( Color.argb(0,0,0,0))
        val colors = mutableListOf<Int>()

        colors.add(ContextCompat.getColor(context!!, R.color.lightRed))
        colors.add(ContextCompat.getColor(context!!, R.color.lightOrange))
        colors.add(ContextCompat.getColor(context!!, R.color.lightYellow))
        colors.add(ContextCompat.getColor(context!!, R.color.lightestYellow))




        dataSet.colors = colors
        dataSet.label = ""
        val data = PieData(dataSet)

//        chart.legend.isEnabled = false
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        l.textColor = ContextCompat.getColor(context!!,R.color.black)
        l.xEntrySpace = 3f
        l.yEntrySpace = 7f
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
                binding.chartFragTxtTotalresultMoney.text = moneys + "원! 잘하셨습니다!"
                binding.chartFragTxtTotalresultMoney.setTextColor(ContextCompat.getColor(context!!, R.color.Neon))
            }
            else -> {
                binding.chartFragTxtTotalresultMoney.text = moneys + "원"
                binding.chartFragTxtTotalresultMoney.setTextColor(ContextCompat.getColor(context!!, R.color.darkGrey))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity != null) {
            val pref = activity!!.getSharedPreferences(Const.PreferenceTitle, MODE_PRIVATE)
            estimateUsage = pref.getInt(Const.EstimateUsage, 0)
            val data = UtilMethod.currencyFormat(estimateUsage.toString())
            binding.chartFragTxtEstimateMoney.text = "" + data + "원"
            getResultUsage()
        }

        val db = dbHelper.readableDatabase
        if (db != null) {
            var cursor: Cursor? = null
            try {
                map = HashMap()
                val currentDate = UtilMethod.getCurrentDate()
                DLog.e("cur $currentDate")
                cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + currentDate + "%' ORDER BY date DESC", null)
                while (cursor.moveToNext()) {
                    val cate = cursor.getString(2)
                    val money = cursor.getString(3).toFloat()
                    if (map.containsKey(cate)) {
                        val data = map.getValue(cate).toFloat().plus(money.toFloat())
                        map.remove(cate)
                        map[cate] = data
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
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
    }


}// Required empty public constructor
