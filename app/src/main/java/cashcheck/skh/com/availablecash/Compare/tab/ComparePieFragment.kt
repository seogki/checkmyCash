package cashcheck.skh.com.availablecash.Compare.tab


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.adapter.NormalRegisterAdapter
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.CustomComparePercentFormatter
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.databinding.FragmentComparePieBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


/**
 * A simple [Fragment] subclass.
 */
class ComparePieFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private lateinit var binding: FragmentComparePieBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var spinnerArray: MutableList<String>
    private lateinit var pieMap1: HashMap<String, String>
    private lateinit var pieMap2: HashMap<String, String>
    private lateinit var firstData: String
    private lateinit var secondData: String
    private lateinit var normalRegisterAdapter: NormalRegisterAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_pie, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        setSpinner()
        setRv()
        return binding.root
    }

    private fun setRv() {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.compare_frag_spinner_spinner_1 -> {
                firstData = binding.compareFragSpinnerSpinner1.selectedItem.toString()
                if (firstData != "") {
                    getPieDataFromDB(true)
                }
            }
            R.id.compare_frag_spinner_spinner_2 -> {
                secondData = binding.compareFragSpinnerSpinner2.selectedItem.toString()
                if (secondData != "") {
                    getPieDataFromDB(false)
                }
            }
        }
    }

    private fun addItemOnSpinner() {
        val spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner, spinnerArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.compareFragSpinnerSpinner1.adapter = spinnerAdapter
        binding.compareFragSpinnerSpinner2.adapter = spinnerAdapter
        binding.compareFragSpinnerSpinner1.onItemSelectedListener = this
        binding.compareFragSpinnerSpinner2.onItemSelectedListener = this
    }

    private fun setPieChart(chart: PieChart, map: HashMap<String, String>) {

        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()

        for ((key, value) in map) {
            yvalues.add(PieEntry(value.toFloat(), key))

        }

        val dataSet = PieDataSet(yvalues, "")


        dataSet.sliceSpace = 1F
        dataSet.selectionShift = 5F
        dataSet.valueTextSize = 9f
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomComparePercentFormatter()

        chart.isRotationEnabled = false
        chart.setDrawEntryLabels(false)
        chart.setTouchEnabled(false)
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)


        val colors = mutableListOf<Int>()
        colors.add(ContextCompat.getColor(context!!, R.color.lightBlue))
        colors.add(ContextCompat.getColor(context!!, R.color.lightOrange))
        colors.add(ContextCompat.getColor(context!!, R.color.lightGreen))
        colors.add(ContextCompat.getColor(context!!, R.color.lightPink))
//        colors.add(ContextCompat.getColor(context!!, R.color.lightPurple))
//        colors.add(ContextCompat.getColor(context!!, R.color.lightRed))
//        colors.add(ContextCompat.getColor(context!!, R.color.lightDarkBlue))
//        colors.add(ContextCompat.getColor(context!!, R.color.lightSkyGreen))

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
        chart.legend.isWordWrapEnabled = true
        chart.data = data
        chart.description.isEnabled = false
        chart.animateXY(1000, 1000)
        chart.invalidate()
    }

    private fun getPieDataFromDB(isFirst: Boolean) {
        val db = dbHelper.readableDatabase
        val data: String
        if (db != null) {

            if (isFirst) {
                pieMap1 = HashMap()
                data = firstData
            } else {
                pieMap2 = HashMap()
                data = secondData
            }
            val result = data.substring(0, 6)
            DLog.e("cur $result")
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
            val cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + result + "%' ORDER BY date DESC", null)
            while (cursor.moveToNext()) {
                val cate = cursor.getString(2)
                val money = cursor.getString(3)

                when (isFirst) {
                    true -> {
                        if (pieMap1.containsKey(cate)) {
                            val d = pieMap1.getValue(cate).toFloat().plus(money.toFloat())
                            pieMap1.remove(cate)
                            pieMap1[cate] = d.toString()
                        } else {
                            pieMap1[cate] = money
                        }
                        if (pieMap1.size == 0) {

                        } else {
                            setPieChart(binding.compareFragPiechart1, pieMap1)
                        }
                    }
                    false -> {
                        if (pieMap2.containsKey(cate)) {
                            val d = pieMap2.getValue(cate).toFloat().plus(money.toFloat())
                            pieMap2.remove(cate)
                            pieMap2[cate] = d.toString()
                        } else {
                            pieMap2[cate] = money
                        }
                        if (pieMap2.size == 0) {

                        } else {
                            setPieChart(binding.compareFragPiechart2, pieMap2)
                        }
                    }
                }


            }
        }
    }

    private fun setSpinner() {
        val db = dbHelper.readableDatabase
        spinnerArray = ArrayList()
        spinnerArray.add("")
        val cursor = db.rawQuery("SELECT date FROM ${Const.DbName} ORDER BY date DESC", null)
        while (cursor.moveToNext()) {
            val date = cursor.getString(0).substring(0, 6)
            if (spinnerArray.contains(date)) {
                spinnerArray.remove(date)
                spinnerArray.add(date)
            } else {
                spinnerArray.add(date)
            }
        }
        DLog.e("setSpinner : $spinnerArray")

        spinnerArray.sort()
        addItemOnSpinner()
    }

}// Required empty public constructor
