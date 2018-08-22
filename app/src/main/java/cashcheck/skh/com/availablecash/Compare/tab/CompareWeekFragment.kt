package cashcheck.skh.com.availablecash.Compare.tab


import android.annotation.SuppressLint
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
import cashcheck.skh.com.availablecash.Compare.Adapter.CompareWeekAdapter
import cashcheck.skh.com.availablecash.Compare.model.CompareWeekModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentCompareWeekBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class CompareWeekFragment : BaseFragment() {

    lateinit var binding: FragmentCompareWeekBinding
    private lateinit var compareWeekAdapter: CompareWeekAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mItems: MutableList<CompareWeekModel>
    private lateinit var tempItem: MutableList<CompareWeekModel>
    private lateinit var dbHelper: DBHelper
    private var weekUsage = 0
    private var fragDate: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_week, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        tempItem = mutableListOf()
        setRv()
        getWeekFromDB()
        return binding.root
    }

    fun getTimeFromFragment(date: String) {
        fragDate = date
        getWeekFromDB()
    }

    override fun onResume() {
        super.onResume()
        getWeekFromDB()
    }

    private fun setRv() {
        compareWeekAdapter = CompareWeekAdapter(context!!, ArrayList())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.fragCompareWeekRv.setHasFixedSize(true)
        compareWeekAdapter.setHasStableIds(true)
        binding.fragCompareWeekRv.layoutManager = layoutManager
        binding.fragCompareWeekRv.isDrawingCacheEnabled = true
        binding.fragCompareWeekRv.setItemViewCacheSize(20)
        binding.fragCompareWeekRv.isNestedScrollingEnabled = false
        binding.fragCompareWeekRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.fragCompareWeekRv.itemAnimator = null

        Handler().postDelayed({
            binding.fragCompareWeekRv.adapter = compareWeekAdapter
        }, 100)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeekFromDB() {
        var cursor: Cursor? = null
        try {
            val db = dbHelper.readableDatabase
            val item = getDaysFromMonth()
            for (i in 0 until item.size) {
                weekUsage = 0
                cursor = db.rawQuery("SELECT money FROM ${Const.DbName} WHERE date BETWEEN '" + item[i].first + " 00:00:00" + "' AND '" + item[i].last + " 23:59:59" + "' ORDER BY date DESC", null)
                while (cursor.moveToNext()) {
                    weekUsage += cursor.getString(0).toInt()

                }
                val model = item[i]
                model.total = weekUsage.toString()
            }
            if (tempItem != item) {
                tempItem = item.asReversed()
                setItem(tempItem)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    private fun setItem(mItems: MutableList<CompareWeekModel>) {
        compareWeekAdapter.clearItems()
        compareWeekAdapter.addItems(mItems)
    }


    @SuppressLint("SimpleDateFormat")
    private fun getDaysFromMonth(): MutableList<CompareWeekModel> {
        mItems = mutableListOf()
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

}// Required empty public constructor
