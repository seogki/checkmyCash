package cashcheck.skh.com.availablecash.Util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter


/**
 * Created by Seogki on 2018. 8. 10..
 */
class LabelFormatter(xValues: MutableList<String>) : IAxisValueFormatter {

    private val result = xValues

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return result[value.toInt()]
    }
}