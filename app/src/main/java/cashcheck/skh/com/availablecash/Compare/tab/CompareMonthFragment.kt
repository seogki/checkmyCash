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
import cashcheck.skh.com.availablecash.Util.UtilMethod
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
    private lateinit var tempItem: ArrayList<CompareMonthModel>
    private lateinit var mItems: ArrayList<CompareMonthModel>
    private lateinit var dbHelper: DBHelper
    private var year: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_month, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        tempItem = ArrayList()
        year = UtilMethod.getCurrentYear()
        setRv()

        return binding.root
    }

    fun getTimeFromFragment(date: String) {
        year = date
        getDateFromDB()
    }

    override fun onResume() {
        super.onResume()
        getDateFromDB()
    }

    private fun setRv() {
        compareMonthAdapter = CompareMonthAdapter(context!!, ArrayList())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 2
        binding.fragCompareMonthRv.setHasFixedSize(true)
        compareMonthAdapter.setHasStableIds(true)
        binding.fragCompareMonthRv.layoutManager = layoutManager
        binding.fragCompareMonthRv.isDrawingCacheEnabled = true
        binding.fragCompareMonthRv.setItemViewCacheSize(10)
        binding.fragCompareMonthRv.isNestedScrollingEnabled = false
        binding.fragCompareMonthRv.itemAnimator = null

        Handler().postDelayed({
            binding.fragCompareMonthRv.adapter = compareMonthAdapter
            getDateFromDB()

        }, 10)
    }

    private fun getDateFromDB() {
        var cursor: Cursor? = null
        try {
            val db = dbHelper.readableDatabase
            monthArray = ArrayList()
            cursor = db.rawQuery("SELECT date FROM ${Const.DbName} ORDER BY date DESC", null)
            while (cursor.moveToNext()) {
                DLog.e("sub ${year.substring(2, 4)} + ${cursor.getString(0).substring(0, 2)}")
                if (year.substring(2, 4) == cursor.getString(0).substring(0, 2)) {
                    val date = cursor.getString(0).substring(0, 5)
                    if (monthArray.contains(date)) {
                        monthArray.remove(date)
                        monthArray.add(date)
                    } else {
                        monthArray.add(date)
                    }

                }
            }

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
            if (mItems != tempItem) {
                tempItem = mItems
                onAddItem()
            }
            if (mItems.size > 0) {
                binding.fragCompareMonthTxtEmpty.visibility = View.GONE
            } else {
                binding.fragCompareMonthTxtEmpty.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {

            cursor?.close()

        }


    }

    private fun onAddItem() {
        compareMonthAdapter.clearItems()
        compareMonthAdapter.addItems(mItems.asReversed())
    }

}// Required empty public constructor
