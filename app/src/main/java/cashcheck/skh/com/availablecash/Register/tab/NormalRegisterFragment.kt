package cashcheck.skh.com.availablecash.Register.tab


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.adapter.NormalRegisterAdapter
import cashcheck.skh.com.availablecash.Register.model.EventItem
import cashcheck.skh.com.availablecash.Register.model.HeaderItem
import cashcheck.skh.com.availablecash.Register.model.ListItem
import cashcheck.skh.com.availablecash.Register.model.NormalRegisterModel
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.FragmentNormalRegisterBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class NormalRegisterFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentNormalRegisterBinding
    private lateinit var db: DBHelper
    private lateinit var normalRegisterAdapter: NormalRegisterAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var itemTreeMap: TreeMap<String, MutableList<NormalRegisterModel>>
    private lateinit var mItems: ArrayList<ListItem>
    private lateinit var tempMItem: ArrayList<ListItem>
    private lateinit var map: HashMap<String, String>
    private lateinit var tempMap: HashMap<String, String>
    private var isRvOn: Boolean = false
    private var dates: String = ""
    fun getDate(v: String) {
        dates = v
        checkDiffAndRefresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normal_register, container, false)
        binding.onClickListener = this
        binding.normalFragPiechart.setNoDataText("데이터가 존재하지 않습니다.")
        db = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        tempMap = HashMap()
        itemTreeMap = TreeMap()
        tempMItem = ArrayList()
        dates = UtilMethod.getCurrentDate()
        return binding.root
    }

    private fun setRv() {
        normalRegisterAdapter = NormalRegisterAdapter(context!!, ArrayList())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.normalFragRv.layoutManager = layoutManager
        binding.normalFragRv.isDrawingCacheEnabled = true
        binding.normalFragRv.setItemViewCacheSize(20)
        binding.normalFragRv.setHasFixedSize(true)
        normalRegisterAdapter.setHasStableIds(true)
        binding.normalFragRv.isNestedScrollingEnabled = false
        binding.normalFragRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.normalFragRv.itemAnimator = null

        Thread(Runnable {
            binding.normalFragRv.adapter = normalRegisterAdapter
        }).start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onResume() {
        super.onResume()
        checkDiffAndRefresh()
    }

    private fun checkDiffAndRefresh() {
        val db = db.readableDatabase
        map = HashMap()
        mItems = ArrayList()
        itemTreeMap = TreeMap()
        DLog.e("cur $dates")
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        val cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + dates + "%' ORDER BY date DESC", null)
        while (cursor.moveToNext()) {
            setHeaderAndData(NormalRegisterModel(cursor.getString(0), cursor.getString(1).substring(0, 8), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
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
            if (!isRvOn) {
                Handler().postDelayed({
                    setRv()
                    refresh()
                    isRvOn = true
                }, 1350)
            } else {
                refresh()
            }
            setPieChart()

        } else {

        }
    }

    private fun setHeaderAndData(resultModel: NormalRegisterModel) {

        val dateDays = resultModel.date + resultModel.days

        if (itemTreeMap.containsKey(dateDays)) {
            val models = itemTreeMap[dateDays]
            itemTreeMap.remove(dateDays)
            models!!.add(resultModel)
            itemTreeMap[dateDays] = models
        } else {
            val model = mutableListOf<NormalRegisterModel>()
            model.add(resultModel)
            itemTreeMap[dateDays] = model
        }

    }

    private fun setViewTypeData() {
        DLog.e("data : $itemTreeMap")

        val reversed = itemTreeMap.toSortedMap(Collections.reverseOrder())


        for (i in 0 until reversed.size) {
            val list = reversed.toList()[i]
            var totalMoney = 0F
            for (x in 0 until list.second.size) {
                totalMoney += list.second[x].money!!.toFloat()
            }
            val tempdata = reversed.toList()[i].first
            val tempModel = reversed.toList()[i].second
            reversed.remove(tempdata)
            reversed[tempdata+totalMoney.toInt()] = tempModel
        }

        mItems = ArrayList()

        for (date in reversed.keys) {
            val header = HeaderItem(date)
            mItems.add(header)
            for (event in reversed[date]!!) {
                val item = EventItem(event)
                mItems.add(item)
            }
        }

        val firstItem = mItems.first()
        val result = firstItem.header.toString()
        mItems.removeAt(0)
        mItems.add(0,HeaderItem(result+"첫번째"))

        DLog.e("mitem : ${mItems.toList()}")
    }

    private fun refresh() {
        setViewTypeData()
        normalRegisterAdapter.clearItems()
        normalRegisterAdapter.notifyDataSetChanged()
        normalRegisterAdapter.addItems(mItems)
    }


    private fun setPieChart() {
        val chart = binding.normalFragPiechart
        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()

        for ((key, value) in map) {
            yvalues.add(PieEntry(value.toFloat(), key))
        }
        val dataSet = PieDataSet(yvalues, "")

        dataSet.sliceSpace = 1F
        dataSet.selectionShift = 5F
        dataSet.valueTextSize = 9f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 100f
        dataSet.valueLinePart1Length = 0.5f
        dataSet.valueLinePart2Length = 0.1f
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomPercentFormatter()
        chart.isRotationEnabled = false
        chart.setTouchEnabled(false)
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)
        chart.setExtraOffsets(25F, 15F, 25F, 15F)
        chart.legend.isWordWrapEnabled = true

        val colors = mutableListOf<Int>()
        colors.add(ContextCompat.getColor(context!!, R.color.lightBlue))
        colors.add(ContextCompat.getColor(context!!, R.color.lightOrange))
        colors.add(ContextCompat.getColor(context!!, R.color.lightGreen))
        colors.add(ContextCompat.getColor(context!!, R.color.lightPink))

        dataSet.colors = colors
        dataSet.label = ""
        val data = PieData(dataSet)

        chart.legend.isEnabled = false
        chart.setNoDataText("데이터가 존재하지 않습니다.")
//        val l = chart.legend
//        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
//        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//        l.orientation = Legend.LegendOrientation.HORIZONTAL
//        l.setDrawInside(false)
//        l.xEntrySpace = 7f
//        l.yEntrySpace = 5f

        chart.data = data
        chart.description.isEnabled = false
        chart.animateXY(1000, 1000)
        chart.invalidate()
    }
}// Required empty public constructor
