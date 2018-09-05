package cashcheck.skh.com.availablecash.Compare.Listener

import cashcheck.skh.com.availablecash.Compare.model.CompareWeekModel

/**
 * Created by Seogki on 2018. 9. 5..
 */
interface CompareWeekListener{
    fun getItems(weekUsage: Int, mItems: MutableList<CompareWeekModel>)
}