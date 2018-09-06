package cashcheck.skh.com.availablecash.Compare.Listener

import cashcheck.skh.com.availablecash.Compare.model.CompareLineModel

/**
 * Created by Seogki on 2018. 9. 6..
 */
interface CompareLineListener{
    fun getItem(arr: ArrayList<CompareLineModel>)
}