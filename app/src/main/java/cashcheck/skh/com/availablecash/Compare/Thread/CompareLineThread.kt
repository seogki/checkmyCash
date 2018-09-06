package cashcheck.skh.com.availablecash.Compare.Thread

import cashcheck.skh.com.availablecash.Compare.Listener.CompareLineListener
import cashcheck.skh.com.availablecash.Compare.model.CompareLineModel
import cashcheck.skh.com.availablecash.Util.DLog
import java.util.*

/**
 * Created by Seogki on 2018. 9. 6..
 */
class CompareLineThread(private val lineMap: HashMap<String, Float>, private val rvArray: ArrayList<CompareLineModel>, private val onCompareLineListener: CompareLineListener) : Thread() {

    override fun run() {
        DLog.e("lineMap : ${lineMap.toList()}")
        val sortedMap = lineMap.toSortedMap()
        for (i in 0 until sortedMap.size) {

            if (sortedMap.size == 1) {
                val firstdata = sortedMap.toList()[i].second
                rvArray.add(CompareLineModel(sortedMap.toList()[i].first
                        , firstdata.toString()
                        , sortedMap.toList()[i].first
                        , firstdata.toString()
                        , ""))
                break
            }
            if (i + 1 < sortedMap.size) {
                val firstdata = sortedMap.toList()[i].second
                val seconddata = sortedMap.toList()[i + 1].second
                rvArray.add(CompareLineModel(sortedMap.toList()[i].first
                        , firstdata.toString()
                        , sortedMap.toList()[i + 1].first
                        , seconddata.toString()
                        , firstdata.minus(seconddata).toString()))
            } else if (i + 1 == sortedMap.size) {

            }
        }

        onCompareLineListener.getItem(rvArray)
    }
}