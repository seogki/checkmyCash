package cashcheck.skh.com.availablecash.Compare.tab


import android.database.Cursor
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Compare.Listener.ComparePieListener
import cashcheck.skh.com.availablecash.Compare.Thread.ComparePieThread
import cashcheck.skh.com.availablecash.Compare.model.ComparePieModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.adapter.ComparePieAdapter
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.CustomComparePercentFormatter
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.databinding.FragmentComparePieBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ComparePieFragment : BaseFragment(), AdapterView.OnItemSelectedListener, ComparePieListener {


    private lateinit var binding: FragmentComparePieBinding
    private lateinit var dbHelper: DBHelper
    private var total = 0F
    private lateinit var spinnerArray: MutableList<String>
    private var pieMap1: HashMap<String, String>? = null
    private var pieMap2: HashMap<String, String>? = null
    private lateinit var firstData: String
    private lateinit var secondData: String
    private lateinit var comparePieAdapter: ComparePieAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_pie, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        binding.compareFragPiechart1.setNoDataText("데이터가 존재하지 않습니다.")
        binding.compareFragPiechart2.setNoDataText("데이터가 존재하지 않습니다.")
        binding.compareFragPieHeader.visibility = View.INVISIBLE
        setSpinner()
        setRv()

        return binding.root
    }

    private fun setRv() {
        comparePieAdapter = ComparePieAdapter(context!!, ArrayList())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 2
        binding.compareFragPiechartRv.layoutManager = layoutManager
        binding.compareFragPiechartRv.isNestedScrollingEnabled = false
        binding.compareFragPiechartRv.itemAnimator = null

        Handler().postDelayed({
            binding.compareFragPiechartRv.adapter = comparePieAdapter
        }, 10)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.compare_frag_spinner_spinner_1 -> {
                firstData = binding.compareFragSpinnerSpinner1.selectedItem.toString()
                if (firstData != "") {
                    setYearMonth(firstData, binding.compareFragPieFirstTitle)
                    getPieDataFromDB(true)
                }
            }
            R.id.compare_frag_spinner_spinner_2 -> {
                secondData = binding.compareFragSpinnerSpinner2.selectedItem.toString()
                if (secondData != "") {
                    setYearMonth(secondData, binding.compareFragPieSecondTitle)
                    getPieDataFromDB(false)

                }
            }
        }
    }

    private fun setYearMonth(date: String, textView: TextView) {
        val result = date.replace("-", "")
        val year = result.substring(0, 2)
        val month = result.substring(2, 4)

        textView.text = "20${year}년 ${month}월"
    }

    private fun setRvData() {
        val thread = ComparePieThread(ArrayList(), pieMap1, pieMap2, this)
        thread.run()
    }

    override fun getItem(arr: ArrayList<ComparePieModel>) {
        comparePieAdapter.clearItems()
        activity?.runOnUiThread { binding.compareFragPieHeader.visibility = View.VISIBLE }
        comparePieAdapter.addItems(arr)
    }

    private fun addItemOnSpinner() {
        val spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner, spinnerArray)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
        binding.compareFragSpinnerSpinner1.adapter = spinnerAdapter
        binding.compareFragSpinnerSpinner2.adapter = spinnerAdapter
        binding.compareFragSpinnerSpinner1.onItemSelectedListener = this
        binding.compareFragSpinnerSpinner2.onItemSelectedListener = this
    }

    private fun setPieChart(chart: PieChart, map: HashMap<String, String>) {
        total = 0F
        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()

        val result = map.toList().sortedByDescending { (_, value) -> value.toFloat() }.toMap()
        for ((_, value) in result) {

            total = total.plus(value.toFloat())

        }
        if (result.size <= 4) {
            for ((key, value) in result) {
                yvalues.add(PieEntry(value.toFloat(), key))
            }
        } else {
            var data = 0F
            for (i in 0 until result.size) {
                if (i >= 3) {
                    data += result.toList()[i].second.toFloat()
                } else {
                    yvalues.add(PieEntry(result.toList()[i].second.toFloat(), result.toList()[i].first))
                }
            }
            yvalues.add(PieEntry(data, "그외"))

        }

        val dataSet = PieDataSet(yvalues, "")


        dataSet.sliceSpace = 0F
        dataSet.selectionShift = 5F
        dataSet.valueTextSize = 9f
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomComparePercentFormatter()
        dataSet.valueTextColor = ContextCompat.getColor(context!!, R.color.black)
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD
        chart.isRotationEnabled = false
        chart.setDrawEntryLabels(false)
        chart.setTouchEnabled(false)
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)


        val colors = mutableListOf<Int>()
        colors.add(ContextCompat.getColor(context!!, R.color.ripplePie1))
        colors.add(ContextCompat.getColor(context!!, R.color.ripplePie2))
        colors.add(ContextCompat.getColor(context!!, R.color.ripplePie3))
        colors.add(ContextCompat.getColor(context!!, R.color.ripplePie4))

        dataSet.colors = colors
        dataSet.label = ""
        val data = PieData(dataSet)


        chart.legend.isWordWrapEnabled = true
        chart.data = data
        chart.holeRadius = 0F
        chart.isDrawHoleEnabled = false
        chart.description.isEnabled = false
        activity?.runOnUiThread {
            chart.legend.isEnabled = false
            chart.animateXY(Const.ChartAnimation, Const.ChartAnimation)
            chart.invalidate()
        }
    }

    private fun getPieDataFromDB(isFirst: Boolean) {
//        val task = QueryTask(this, isFirst)
//        task.execute()

        val db = dbHelper.readableDatabase
        val data: String
        var cursor: Cursor? = null
        try {
            if (db != null) {

                if (isFirst) {
                    pieMap1 = HashMap()
                    data = firstData
                } else {
                    pieMap2 = HashMap()
                    data = secondData
                }
                cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + data + "%' ORDER BY date DESC", null)
                while (cursor.moveToNext()) {
                    val cate = cursor.getString(2)
                    val money = cursor.getString(3)
                    addDateToFromCursor(cate, money, isFirst)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        if (pieMap1 != null && pieMap2 != null) {
            setRvData()
        }
    }

    private fun addDateToFromCursor(cate: String, money: String, isFirst: Boolean) {
        when (isFirst) {
            true -> {
                if (pieMap1!!.containsKey(cate)) {
                    val d = pieMap1!!.getValue(cate).toFloat().plus(money.toFloat())
                    pieMap1!!.remove(cate)
                    pieMap1!![cate] = d.toString()
                } else {
                    pieMap1!![cate] = money
                }
                if (pieMap1!!.size == 0) {

                } else {
                    setPieChart(binding.compareFragPiechart1, pieMap1!!)
                }
            }
            false -> {
                if (pieMap2!!.containsKey(cate)) {
                    val d = pieMap2!!.getValue(cate).toFloat().plus(money.toFloat())
                    pieMap2!!.remove(cate)
                    pieMap2!![cate] = d.toString()
                } else {
                    pieMap2!![cate] = money
                }
                if (pieMap2!!.size == 0) {

                } else {
                    setPieChart(binding.compareFragPiechart2, pieMap2!!)
                }
            }
        }
    }

    override fun onResume() {
        setSpinner()
        super.onResume()
    }

    private fun setSpinner() {
        var cursor: Cursor? = null
        try {
            val db = dbHelper.readableDatabase
            spinnerArray = ArrayList()
            spinnerArray.add("")
            cursor = db.rawQuery("SELECT date FROM ${Const.DbName} ORDER BY date DESC", null)
            while (cursor.moveToNext()) {
                val date = cursor.getString(0).substring(0, 5)
                if (spinnerArray.contains(date)) {
                    spinnerArray.remove(date)
                    spinnerArray.add(date)
                } else {
                    spinnerArray.add(date)
                }
            }

            spinnerArray.sort()
            addItemOnSpinner()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

//    companion object {
//        class QueryTask(private val comparePieFragment: ComparePieFragment, val isFirst: Boolean) : AsyncTask<Void, Void, Void>() {
//            override fun doInBackground(vararg params: Void?): Void? {
//                val db = comparePieFragment.dbHelper.readableDatabase
//                val data: String
//                var cursor: Cursor? = null
//                try {
//                    if (db != null) {
//                        if (isFirst) {
//                            comparePieFragment.pieMap1 = HashMap()
//                            data = comparePieFragment.firstData
//                        } else {
//                            comparePieFragment.pieMap2 = HashMap()
//                            data = comparePieFragment.secondData
//                        }
//                        cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + data + "%' ORDER BY date DESC", null)
//                        while (cursor.moveToNext()) {
//                            val cate = cursor.getString(2)
//                            val money = cursor.getString(3)
//                            comparePieFragment.addDateToFromCursor(cate, money, isFirst)
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                } finally {
//                    cursor?.close()
//                }
//                return null
//            }
//        }
//    }

}// Required empty public constructor
