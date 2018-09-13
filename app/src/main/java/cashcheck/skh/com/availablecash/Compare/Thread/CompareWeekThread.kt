package cashcheck.skh.com.availablecash.Compare.Thread

import android.annotation.SuppressLint
import android.database.Cursor
import cashcheck.skh.com.availablecash.Compare.Listener.CompareWeekListener
import cashcheck.skh.com.availablecash.Compare.model.CompareWeekModel
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Seogki on 2018. 9. 5..
 */
class CompareWeekThread(private val dbHelper: DBHelper, private var weekUsage: Int, private var fragDate: String?, private var mItems: MutableList<CompareWeekModel>, private val onCompareWeekListener: CompareWeekListener) : Thread() {
    override fun run() {
        var cursor: Cursor? = null
        val item = getDaysFromMonth()
        try {
            val db = dbHelper.readableDatabase

            for (i in 0 until item.size) {
                weekUsage = 0
//                "SELECT * from TABLE_NAME Where COLUMN_NAME >='"+from_date+ "' AND <= '"+to_date+ "'";
                cursor = db.rawQuery("SELECT money FROM ${Const.DbName} WHERE date >= '" + item[i].first + "' AND date <='" + item[i].last + "' ORDER BY date DESC", null)
                while (cursor.moveToNext()) {
                    weekUsage += cursor.getString(0).toInt()

                }
                val model = item[i]
                model.total = weekUsage.toString()

            }
            onCompareWeekListener.getItems(weekUsage, mItems)
        } catch (e: Exception) {

        } finally {
            cursor?.close()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDaysFromMonth(): MutableList<CompareWeekModel> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yy-MM-dd")
        val sdf = SimpleDateFormat("yy-MM", Locale.KOREA)
        val currentDay = df.parse(UtilMethod.getCurrentMonth())
        val current = sdf.parse(UtilMethod.getCurrentMonth())
        if (fragDate != null) {
            val d = sdf.parse(fragDate)
            cal.time = d
        } else {
            cal.time = current
        }
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DATE, -1)
        val end = cal.get(Calendar.WEEK_OF_MONTH)

        for (i in 0 until end) {
            if (i == 0) {

                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
                val start = cal.time
                DLog.e("start $start")
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                val firstDate = cal.time
                val first = df.format(firstDate)

                cal.add(Calendar.DATE, 6)
                val sevenDate = cal.time
                val seven = df.format(sevenDate)

                if (currentDay in firstDate..sevenDate) {
                    mItems.add(CompareWeekModel(first, seven, "0", "now"))
                } else {
                    mItems.add(CompareWeekModel(first, seven, "0", ""))
                }


            } else {

                cal.add(Calendar.DATE, 1)
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                val firstDate = cal.time
                val first = df.format(firstDate)


                cal.add(Calendar.DATE, 6)
                val sevenDate = cal.time
                val seven = df.format(sevenDate)

                if (currentDay in firstDate..sevenDate) {
                    mItems.add(CompareWeekModel(first, seven, "0", "now"))
                } else {
                    mItems.add(CompareWeekModel(first, seven, "0", ""))
                }
            }
        }

        return mItems
    }
}