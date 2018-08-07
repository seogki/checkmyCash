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
class CustomPercentFormatter : IValueFormatter, IAxisValueFormatter {

    protected var mFormat: DecimalFormat

    val decimalDigits: Int
        get() = 1

    constructor() {
        mFormat = DecimalFormat("###,###,##0.0")
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    constructor(format: DecimalFormat) {
        this.mFormat = format
    }

    // IValueFormatter
    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
        return mFormat.format(value.toDouble()) + "%"
    }

    // IAxisValueFormatter
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return mFormat.format(value.toDouble()) + "%"
    }
}