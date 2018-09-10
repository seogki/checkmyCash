package cashcheck.skh.com.availablecash.Util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

/**
 * Created by Seogki on 2018. 8. 7..
 */
class CustomMainPercentFormatter : IValueFormatter, IAxisValueFormatter {

    private var mFormat: DecimalFormat = DecimalFormat("###,###,##0.0")


    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    // IValueFormatter
    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
        if (value < 3) {
            return ""
        }
        return ""
    }

    // IAxisValueFormatter
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        if (value < 3) {
            return ""
        }
        return ""
    }
}