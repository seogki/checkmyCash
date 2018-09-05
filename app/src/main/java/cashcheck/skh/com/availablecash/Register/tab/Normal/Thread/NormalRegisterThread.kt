package cashcheck.skh.com.availablecash.Register.tab.Normal.Thread

import android.content.Context
import cashcheck.skh.com.availablecash.Register.model.*
import cashcheck.skh.com.availablecash.Register.tab.Normal.Listener.NormalThreadListener
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Seogki on 2018. 9. 5..
 */
class NormalRegisterThread(val context: Context, var mItems: ArrayList<ListItem>, private val itemTreeMap: TreeMap<String, MutableList<NormalRegisterModel>>, val onNormalThreadListener: NormalThreadListener) : Thread() {

    override fun run() {
        val reversed = itemTreeMap.toSortedMap(Collections.reverseOrder())
        for (i in 0 until reversed.size) {
            val list = reversed.toList()[i]
            var totalMoney = 0F
            for (x in 0 until list.second.size) {
                totalMoney += list.second[x].money!!.toFloat()
            }
            val tempdata = reversed.toList()[i].first
            val tempModel = reversed.toList()[i].second
            reversed.remove(tempdata)
            reversed[tempdata + totalMoney.toInt()] = tempModel
        }

        mItems = ArrayList()

        for (date in reversed.keys) {
            val header = HeaderItem(date)
            mItems.add(header)
            for (event in reversed[date]!!) {
                if (event == reversed[date]!!.last()) {
                    val end = EndItem(event)
                    mItems.add(end)
                } else {
                    val item = EventItem(event)
                    mItems.add(item)
                }
            }
        }
        onNormalThreadListener.getmItems(mItems)
    }

}