package cashcheck.skh.com.availablecash.Util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Seogki on 2018. 8. 7..
 */
class CustomDateFormatter(xValues: MutableList<String>) : IValueFormatter, IAxisValueFormatter {

    private var xValues = xValues

    //     IValueFormatter
    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
        val stringValue = entry.x.toString()
        val month = stringValue.substring(0, 2)
        val days = stringValue.substring(2, 4)
        return "${month}-${days}"
    }

    // IAxisValueFormatter
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        val index = value.toInt()
        if (index < xValues.size) {
            if(index == -1)
                return ""
            val stringValue = xValues[index]
            var month = ""
            var days = ""
            if (stringValue.length == 4) {
                month = stringValue.substring(0, 2)
                days = stringValue.substring(2, 4)
            } else {
                month = stringValue.substring(0, 1)
                days = stringValue.substring(1, 3)
            }
            return "${month}-${days}"
        }
        return ""
    }
}