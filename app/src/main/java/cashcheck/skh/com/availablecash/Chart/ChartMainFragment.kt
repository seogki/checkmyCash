package cashcheck.skh.com.availablecash.Chart


import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Chart.Adapter.RecentChartAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.model.EstimateRegisterModel
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
class ChartMainFragment : BaseFragment(), OnNormalRegisterDeleteListener {


    lateinit var binding: FragmentChartMainBinding
    private lateinit var dbHelper: DBHelper
    private var totalUsage: Float = 0F
    private var estimateUsage: Int = 0
    private var resultUsage: Int = 0
    private lateinit var map: HashMap<String, Float>
    private lateinit var tempMap: HashMap<String, Float>
    private lateinit var recentChartAdapter: RecentChartAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mItems: MutableList<EstimateRegisterModel>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart_main, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        binding.chartFragPiechart.setNoDataText("데이터가 존재하지 않습니다.")
        checkTable()
        tempMap = HashMap()
        setRecentAddRv()
        return binding.root
    }

    private fun checkTable() {
        if(!dbHelper.isColumnExists(Const.DbName, "card_id")){
            dbHelper.checkTable(Const.DbName, "card_id")
        }
        if(!dbHelper.isColumnExists(Const.DbName, "card_name")){
            dbHelper.checkTable(Const.DbName, "card_name")
        }
    }

    override fun onCompleteDelete(done: String?) {
        if (done == "done") {
            getRecentChart()
        }
    }

    private fun setRecentAddRv() {
        recentChartAdapter = RecentChartAdapter(context!!, ArrayList())
        recentChartAdapter.onNormalRegisterDeleteListener = this
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.chartFragRvRecent.layoutManager = layoutManager
        binding.chartFragRvRecent.isDrawingCacheEnabled = true
        binding.chartFragRvRecent.setItemViewCacheSize(20)
        binding.chartFragRvRecent.isNestedScrollingEnabled = false
        binding.chartFragRvRecent.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.chartFragRvRecent.itemAnimator = null

        Thread(Runnable {
            binding.chartFragRvRecent.adapter = recentChartAdapter
            getRecentChart()
        }).start()
    }

    private fun getRecentChart() {
        val db = dbHelper.readableDatabase
        mItems = mutableListOf()
        if (db != null) {
            var cursor: Cursor? = null
            try {
                cursor = db.rawQuery("SELECT * FROM ${Const.DbName} ORDER BY _id DESC LIMIT 7", null)
                while (cursor.moveToNext()) {
                    val num = cursor.getString(0)
                    val date = cursor.getString(1)
                    val cate = cursor.getString(2)
                    val money = cursor.getString(3)
                    val days = cursor.getString(4)

                    mItems.add(EstimateRegisterModel(num, date, cate, money, days))
                }
                addRecentData()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
    }

    private fun addRecentData() {
        recentChartAdapter.clearItems()
        recentChartAdapter.addItems(mItems)
    }

    @SuppressLint("SetTextI18n")
    private fun setPieChart() {
        val chart = binding.chartFragPiechart
        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()
        val result = map.toList().sortedByDescending { (_, value) -> value }.toMap()
        for ((key, value) in result) {

            totalUsage = totalUsage.plus(value.toFloat())

        }
        if (result.size <= 4) {
            for ((key, value) in result) {
                val percent = value.toFloat().div(totalUsage).times(100F)
                DLog.e("percent : $percent")
                yvalues.add(PieEntry(value.toFloat(), "$key ${DecimalFormat("0.0").format(percent)}%"))

                DLog.e("total $totalUsage")
            }
        } else {
            var data = 0F
            for (i in 0 until result.size) {
                if (i >= 3) {
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
        dataSet.valueTextColor = ContextCompat.getColor(context!!, R.color.black)
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomMainPercentFormatter()
        chart.isRotationEnabled = false
        chart.setDrawEntryLabels(false)
        chart.setTouchEnabled(false)
//        chart.isDrawHoleEnabled = false
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)
        chart.holeRadius = 70f
        chart.setExtraOffsets(5F, 5F, 5F, 0F)
        chart.setHoleColor(Color.argb(0, 0, 0, 0))
        val colors = mutableListOf<Int>()

        colors.add(ContextCompat.getColor(context!!, R.color.lightRed))
        colors.add(ContextCompat.getColor(context!!, R.color.lightOrange))
        colors.add(ContextCompat.getColor(context!!, R.color.lightYellow))
        colors.add(ContextCompat.getColor(context!!, R.color.lightestYellow))

        dataSet.colors = colors
        dataSet.label = ""
        val data = PieData(dataSet)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        l.textColor = ContextCompat.getColor(context!!, R.color.black)
        l.xEntrySpace = 4f
        l.yEntrySpace = 4f
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
        getRecentChart()
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
