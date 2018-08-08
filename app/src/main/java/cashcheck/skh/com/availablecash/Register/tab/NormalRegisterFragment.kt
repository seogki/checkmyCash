package cashcheck.skh.com.availablecash.Register.tab


import android.content.Intent
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
import cashcheck.skh.com.availablecash.databinding.FragmentNormalRegisterBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


/**
 * A simple [Fragment] subclass.
 */
class NormalRegisterFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentNormalRegisterBinding
    private lateinit var db: DBHelper
    private lateinit var map: HashMap<String, String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normal_register, container, false)
        binding.normalFragFab.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.onClickListener = this
        db = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.normal_frag_fab -> {
                val i = Intent(context!!, NormalRegisterActivity::class.java)
                startActivity(i)
                activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val db = db.readableDatabase
        map = HashMap()
        val currentDate = UtilMethod.getCurrentDate()
        DLog.e("cur $currentDate")
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        val cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + currentDate + "%'", null)
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

        DLog.e("picMap : $map")
        setPieChart()

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
        dataSet.valueTextSize = 10f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 100f
        dataSet.valueLinePart1Length = 0.5f
        dataSet.valueLinePart2Length = 0.3f
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomPercentFormatter()
        chart.isRotationEnabled = false
        chart.setTouchEnabled(false)
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)
        chart.setExtraOffsets(23F, 10F, 23F, 10F)


        val colors = mutableListOf<Int>()
        colors.add(ContextCompat.getColor(context!!, R.color.lightBlue))
        colors.add(ContextCompat.getColor(context!!, R.color.lightGreen))
        colors.add(ContextCompat.getColor(context!!, R.color.lightOrange))
        colors.add(ContextCompat.getColor(context!!, R.color.lightPink))
        colors.add(ContextCompat.getColor(context!!, R.color.lightPurple))
        colors.add(ContextCompat.getColor(context!!, R.color.lightRed))
        colors.add(ContextCompat.getColor(context!!, R.color.lightDarkBlue))
        colors.add(ContextCompat.getColor(context!!, R.color.lightSkyGreen))

        dataSet.colors = colors
        dataSet.label = ""
        val data = PieData(dataSet)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 5f

        chart.data = data
        chart.description.isEnabled = false
        chart.animateXY(1000, 1000)
        chart.invalidate()
    }


}// Required empty public constructor
