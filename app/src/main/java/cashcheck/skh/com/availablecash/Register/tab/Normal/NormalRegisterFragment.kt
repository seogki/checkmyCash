package cashcheck.skh.com.availablecash.Register.tab.Normal


import android.database.Cursor
import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.adapter.Normal.NormalRegisterAdapter
import cashcheck.skh.com.availablecash.Register.model.ListItem
import cashcheck.skh.com.availablecash.Register.model.NormalRegisterModel
import cashcheck.skh.com.availablecash.Register.tab.Normal.Listener.NormalThreadListener
import cashcheck.skh.com.availablecash.Register.tab.Normal.Thread.NormalRegisterThread
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentNormalRegisterBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 */
class NormalRegisterFragment : BaseFragment(), OnNormalRegisterDeleteListener, NormalThreadListener {


    lateinit var binding: FragmentNormalRegisterBinding
    private lateinit var db: DBHelper
    private lateinit var normalRegisterAdapter: NormalRegisterAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var monthTotal = 0F
    private lateinit var itemTreeMap: TreeMap<String, MutableList<NormalRegisterModel>>
    private lateinit var mItems: ArrayList<ListItem>
    private lateinit var map: HashMap<String, Float>
    private lateinit var tempMap: HashMap<String, Float>
    private var isRvOn: Boolean = false
    private var dates: String = ""
    fun getDate(v: String) {
        dates = v
        checkDiffAndRefresh()
    }

    override fun onCompleteDelete(done: String?, position: Int) {
        if (done == "done") {
            checkDiffAndRefresh()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normal_register, container, false)
//        binding.normalFragPiechart.setNoDataText("데이터가 존재하지 않습니다.")
        db = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        tempMap = HashMap()
        itemTreeMap = TreeMap()
        dates = UtilMethod.getCurrentDate()
        return binding.root
    }

    private fun setRv() {
        normalRegisterAdapter = NormalRegisterAdapter(context!!, ArrayList())
        normalRegisterAdapter.onNormalRegisterDeleteListener = this
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.normalFragRv.layoutManager = layoutManager
        binding.normalFragRv.setHasFixedSize(true)
        normalRegisterAdapter.setHasStableIds(true)
        binding.normalFragRv.isNestedScrollingEnabled = false
        binding.normalFragRv.itemAnimator = null

        Handler().postDelayed({
            binding.normalFragRv.adapter = normalRegisterAdapter
        }, 10)
    }


    override fun onResume() {
        super.onResume()
        checkDiffAndRefresh()
    }

    private fun checkDiffAndRefresh() {
        map = HashMap()
        mItems = ArrayList()
        itemTreeMap = TreeMap()
        DLog.e("cur $dates")

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        val task = QueryTask(this)
        task.execute()
        map = task.get()

        if (map.size > 0) {
            binding.normalFragTxtEmpty.visibility = View.GONE

        } else {
            binding.normalFragTxtEmpty.visibility = View.VISIBLE
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
            setTotal()
//            setPieChart()

        } else {

        }
    }

    private fun setTotal() {
        monthTotal = 0F
        for ((_, value) in map) {
            monthTotal = monthTotal.plus(value)
        }
        if(monthTotal == 0F)
            binding.fragTxtPercent.text = "아직 데이터가 존재하지 않습니다"
         else
            binding.fragTxtPercent.text = "이번달 합계 ${UtilMethod.currencyFormat(monthTotal.toInt().toString())}원"
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
        val normalThread = NormalRegisterThread(context!!, mItems, itemTreeMap, this)
        normalThread.start()
    }

    private fun refresh() {
        setViewTypeData()
    }

    override fun getmItems(mItems: ArrayList<ListItem>) {
        activity?.runOnUiThread {
            normalRegisterAdapter.clearItems()
            normalRegisterAdapter.addItems(mItems)
        }
    }

//    private fun setPieChart() {
//
//        val chart = binding.normalFragPiechart
//        chart.setUsePercentValues(true)
//        monthTotal = 0F
//        val yvalues = mutableListOf<PieEntry>()
//        val result = map.toList().sortedByDescending { (_, value) -> value }.toMap()
//        for ((_, value) in result) {
//
//            monthTotal = monthTotal.plus(value)
//
//        }
//        if (result.size <= 4) {
//            for ((key, value) in result) {
//                yvalues.add(PieEntry(value, key))
//            }
//        } else {
//            var data = 0F
//            for (i in 0 until result.size) {
//                if (i >= 3) {
//                    data += result.toList()[i].second
//                } else {
//                    yvalues.add(PieEntry(result.toList()[i].second, result.toList()[i].first))
//                }
//            }
//            yvalues.add(PieEntry(data, "그외"))
//
//        }
//
//
//        val dataSet = PieDataSet(yvalues, "")
//
//        dataSet.sliceSpace = 0F
//        dataSet.selectionShift = 5F
//        dataSet.valueTextSize = 9f
//        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
//        dataSet.valueLinePart1OffsetPercentage = 100f
//        dataSet.valueLinePart1Length = 0.3f
//        dataSet.valueLinePart2Length = 0.1f
//        dataSet.valueTextColor = ContextCompat.getColor(context!!, R.color.black)
//        dataSet.valueTypeface = Typeface.DEFAULT_BOLD
//        dataSet.isValueLineVariableLength = true
//        dataSet.valueFormatter = CustomPercentFormatter()
//        chart.isRotationEnabled = false
//        chart.setTouchEnabled(false)
//        chart.setHoleColor(Color.argb(0, 0, 0, 0))
//        chart.setUsePercentValues(true)
//        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
//        chart.setEntryLabelTextSize(9F)
//        chart.holeRadius = 70f
//        chart.setCenterTextColor(ContextCompat.getColor(context!!, R.color.black))
//        chart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
//        if (monthTotal != 0F) {
//            chart.centerText = UtilMethod.currencyFormat(monthTotal.toInt().toString()) + "원"
//        } else {
//            chart.centerText = ""
//        }
//        chart.setCenterTextSize(18F)
//        chart.setExtraOffsets(25F, 15F, 25F, 15F)
//        chart.legend.isWordWrapEnabled = true
//
//        val colors = mutableListOf<Int>()
//
//        colors.add(ContextCompat.getColor(context!!, R.color.pie1))
//        colors.add(ContextCompat.getColor(context!!, R.color.pie2))
//        colors.add(ContextCompat.getColor(context!!, R.color.pie3))
//        colors.add(ContextCompat.getColor(context!!, R.color.pie4))
//
//        dataSet.colors = colors
//        dataSet.label = ""
//        val data = PieData(dataSet)
//
//        chart.legend.isEnabled = false
//        chart.data = data
//        chart.description.isEnabled = false
//        chart.invalidate()
//    }

    companion object {
        class QueryTask(private val normalRegisterFragment: NormalRegisterFragment) : AsyncTask<Void, Void, HashMap<String, Float>>() {
            override fun doInBackground(vararg params: Void?): HashMap<String, Float> {
                var cursor: Cursor? = null
                try {
                    val db = normalRegisterFragment.db.readableDatabase
                    normalRegisterFragment.map = HashMap()
                    normalRegisterFragment.mItems = ArrayList()
                    normalRegisterFragment.itemTreeMap = TreeMap()
                    cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + normalRegisterFragment.dates + "%' ORDER BY date DESC", null)
                    while (cursor.moveToNext()) {
                        normalRegisterFragment.setHeaderAndData(NormalRegisterModel(cursor.getString(0), cursor.getString(1).substring(0, 8), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
                        val cate = cursor.getString(2)
                        val money = cursor.getString(3).toFloat()
                        if (normalRegisterFragment.map.containsKey(cate)) {
                            val data = normalRegisterFragment.map.getValue(cate).toFloat().plus(money.toFloat())
                            normalRegisterFragment.map.remove(cate)
                            normalRegisterFragment.map[cate] = data
                        } else {
                            normalRegisterFragment.map[cate] = money
                        }
                    }
                } catch (e: Exception) {

                } finally {
                    cursor?.close()
                }

                return normalRegisterFragment.map
            }
        }
    }
}// Required empty public constructor
