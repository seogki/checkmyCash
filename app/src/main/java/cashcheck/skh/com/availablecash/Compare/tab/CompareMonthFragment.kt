package cashcheck.skh.com.availablecash.Compare.tab


import android.database.Cursor
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Compare.Adapter.CompareMonthAdapter
import cashcheck.skh.com.availablecash.Compare.model.CompareMonthModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.databinding.FragmentCompareMonthBinding


/**
 * A simple [Fragment] subclass.
 */
class CompareMonthFragment : BaseFragment() {

    lateinit var binding: FragmentCompareMonthBinding
    private lateinit var compareMonthAdapter: CompareMonthAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var monthArray: MutableList<String>
    private var monthUsage = 0
    private lateinit var mItems: ArrayList<CompareMonthModel>
    private lateinit var dbHelper: DBHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_month, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        setRv()

        return binding.root
    }

    private fun setRv() {
        compareMonthAdapter = CompareMonthAdapter(context!!, ArrayList())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.fragCompareMonthRv.layoutManager = layoutManager
        binding.fragCompareMonthRv.isDrawingCacheEnabled = true
        binding.fragCompareMonthRv.setItemViewCacheSize(20)
        binding.fragCompareMonthRv.isNestedScrollingEnabled = false
        binding.fragCompareMonthRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.fragCompareMonthRv.itemAnimator = null

        Handler().postDelayed({
            binding.fragCompareMonthRv.adapter = compareMonthAdapter
            getDateFromDB()

        }, 100)
    }

    private fun getDateFromDB() {
        var cursor: Cursor? = null
        try {
            val db = dbHelper.readableDatabase
            monthArray = ArrayList()
            cursor = db.rawQuery("SELECT date FROM ${Const.DbName} ORDER BY date DESC", null)
            while (cursor.moveToNext()) {
                val date = cursor.getString(0).substring(0, 5)
                if (monthArray.contains(date)) {
                    monthArray.remove(date)
                    monthArray.add(date)
                } else {
                    monthArray.add(date)
                }
            }
            DLog.e("setSpinner : $monthArray")

            monthArray.sort()
            getTotalFromMonth()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()

        }
    }

    private fun getTotalFromMonth() {
        mItems = ArrayList()
        var cursor: Cursor? = null
        try {
            for (i in 0 until monthArray.size) {
                monthUsage = 0

                val db = dbHelper.readableDatabase

                cursor = db.rawQuery("SELECT money FROM ${Const.DbName} WHERE date LIKE '%" + monthArray[i] + "%' ORDER BY date DESC", null)
                while (cursor.moveToNext()) {
                    val money = cursor.getString(0)
                    monthUsage += money.toInt()
                }
                mItems.add(CompareMonthModel("", monthArray[i], monthUsage.toString()))
            }
            DLog.e("items : ${mItems.toList()}")
            onAddItem()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {

            cursor?.close()

        }


    }

    private fun onAddItem() {

        compareMonthAdapter.clearItems()
        compareMonthAdapter.addItems(mItems)
    }

}// Required empty public constructor
