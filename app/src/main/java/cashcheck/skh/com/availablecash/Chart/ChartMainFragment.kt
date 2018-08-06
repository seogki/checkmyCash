package cashcheck.skh.com.availablecash.Chart


import android.content.Context.MODE_PRIVATE
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentChartMainBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


/**
 * A simple [Fragment] subclass.
 */
class ChartMainFragment : BaseFragment() {

    lateinit var binding: FragmentChartMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart_main, container, false)
        setPieChart()

        return binding.root
    }

    private fun setPieChart() {
        val chart = binding.chartFragPiechart
        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()

        yvalues.add(PieEntry(8f, "1"))
        yvalues.add(PieEntry(15f, "2"))
        yvalues.add(PieEntry(12f, "3"))
        yvalues.add(PieEntry(25f, "4"))
        yvalues.add(PieEntry(23f, "5"))
        yvalues.add(PieEntry(17f, "6"))

        val dataSet = PieDataSet(yvalues, "")

        dataSet.sliceSpace = 3F
        dataSet.selectionShift = 5F
        dataSet.valueTextSize = 11f

        val colors = mutableListOf<Int>()
        colors.add(Color.GRAY)
        colors.add(Color.BLUE)
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        colors.add(Color.CYAN)
        colors.add(Color.YELLOW)
        colors.add(Color.MAGENTA)

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
        chart.animateXY(1100, 1100)
        chart.invalidate()
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

            val result = (trans * transDay) + (food * foodDay) + electric + phone + house + adjust + etc
            val data = UtilMethod.currencyFormat(result.toString())
            DLog.e("trans $trans + $transDay + $food + $foodDay + $result + $data")
            binding.chartFragTxtEstimateMoney.text = "" + data + "원"
        }

    }


}// Required empty public constructor
