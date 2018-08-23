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
    private var isCreated: Boolean = false
    private var totalUsage = 0
    private var mostUsage = 0
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
        if (isCreated)
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
                getTotalAndMostUsage(mItems)
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

    private fun getTotalAndMostUsage(mItems: ArrayList<CompareMonthModel>) {
        totalUsage = 0
        mostUsage = 0
        val arr = ArrayList<Int>()
        var tempModel: CompareMonthModel? = null
        for (item in mItems) {
            totalUsage += item.total.toInt()
            arr.add(item.total.toInt())
        }

        if (arr.isNotEmpty()) {
            mostUsage = arr.max()!!
            for (i in mItems) {
                if (mostUsage == i.total.toInt())
                    tempModel = i
            }

            setTotalAndMostUsage(tempModel, mostUsage, totalUsage)
        } else {
            setTextView(""
                    ,""
                    ,"",
                    ""
                    ,""
                    ,16)
        }

    }

    private fun setTotalAndMostUsage(tempModel: CompareMonthModel?, mostUsage: Int, totalUsage: Int) {
        var month = tempModel?.months?.substring(3, 5)
        if (month?.substring(0, 1) == "0") {
            month = month.substring(1, 2)
        }
        setTextView("20${tempModel?.months?.substring(0, 2)}년 합계"
                ,"${UtilMethod.currencyFormat(totalUsage.toString())}원"
                ,"가장 많은 소비를 한 달",
                "${month}월"
                ,"${UtilMethod.currencyFormat(mostUsage.toString())}원"
                ,18)

        if (!isCreated)
            isCreated = true
    }
    private fun setTextView(allYear: String, allMoney: String, mostMonth: String, month: String, mostMoney: String, font: Int){
//        if(font == 18){
//            binding.fragCompareMonthTxtAlltotal.textSize = 18F
//            binding.fragCompareMonthTxtAlltotal.typeface = Typeface.DEFAULT_BOLD
//            binding.fragCompareMonthTxtAlltotal.setTextColor(ContextCompat.getColor(context!!,R.color.lightBlack))
//        } else {
//
//            binding.fragCompareMonthTxtAlltotal.textSize = 16F
//            binding.fragCompareMonthTxtAlltotal.typeface = Typeface.DEFAULT
//            binding.fragCompareMonthTxtAlltotal.setTextColor(ContextCompat.getColor(context!!,R.color.darkGrey))
//        }

        binding.fragCompareMonthTxtAlltotal.text = allYear
        binding.fragCompareMonthTxtAlltotalMoney.text = allMoney
        binding.fragCompareMonthTxtMosttotal.text = mostMonth
        binding.fragCompareMonthTxtMosttotalMonth.text = month
        binding.fragCompareMonthTxtMosttotalMoney.text = mostMoney

    }

    private fun onAddItem() {
        compareMonthAdapter.clearItems()
        compareMonthAdapter.addItems(mItems.asReversed())
    }

}// Required empty public constructor
