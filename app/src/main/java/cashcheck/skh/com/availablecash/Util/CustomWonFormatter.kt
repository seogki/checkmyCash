package cashcheck.skh.com.availablecash.Util

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

/**
 * Created by Seogki on 2018. 8. 7..
 */
class CustomWonFormatter() : IValueFormatter {

    private var mFormat: DecimalFormat = DecimalFormat("###,###,###")

    val decimalDigits: Int
        get() = 1

    // IValueFormatter
    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
        return mFormat.format(value.toInt()) + "원"
    }

}