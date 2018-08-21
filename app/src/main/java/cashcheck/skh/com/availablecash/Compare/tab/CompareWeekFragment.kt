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
import cashcheck.skh.com.availablecash.databinding.FragmentCompareWeekBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CompareWeekFragment : BaseFragment() {

    lateinit var binding: FragmentCompareWeekBinding
    private lateinit var compareWeekAdapter: CompareWeekAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mItems: ArrayList<CompareWeekModel>
    private lateinit var dbHelper: DBHelper
    private lateinit var weekArray: MutableList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_week, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        setRv()
        getDateFromDB()
        return binding.root
    }

    private fun setRv() {
        compareWeekAdapter = CompareWeekAdapter(context!!, ArrayList())
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
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

    private fun getDateFromDB() {
        var cursor: Cursor? = null
        try {
            val db = dbHelper.readableDatabase
            weekArray = ArrayList()
            cursor = db.rawQuery("SELECT date FROM ${Const.DbName} ORDER BY date DESC", null)
            while (cursor.moveToNext()) {
                val date = cursor.getString(0)
                if (weekArray.contains(date)) {
                    weekArray.remove(date)
                    weekArray.add(date)
                } else {
                    weekArray.add(date)
                }
            }
            DLog.e("setSpinner : $weekArray")

            weekArray.sort()
            getWeekFromDB()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()

        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeekFromDB() {
        var cursor: Cursor? = null
        try {
            for (i in 0 until weekArray.size) {
                val db = dbHelper.readableDatabase
                try {
                    val format = SimpleDateFormat("yy-MM-dd")
                    val c = Calendar.getInstance()
                    c.time = format.parse(weekArray[i])
                    c.set(Calendar.DAY_OF_WEEK, c.getActualMinimum(Calendar.DAY_OF_WEEK))
                    val firstday = c.time

                    DLog.e("days $firstday")
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                cursor = db.rawQuery("", null)
                while (cursor.moveToNext()) {
                    DLog.e("cursor + ${cursor.getString(0)}")

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

}// Required empty public constructor
