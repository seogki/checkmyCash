package cashcheck.skh.com.availablecash.Compare.tab


import android.annotation.SuppressLint
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
import cashcheck.skh.com.availablecash.Compare.Listener.CompareWeekListener
import cashcheck.skh.com.availablecash.Compare.Thread.CompareWeekThread
import cashcheck.skh.com.availablecash.Compare.model.CompareWeekModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentCompareWeekBinding
import java.text.DecimalFormat


/**
 * A simple [Fragment] subclass.
 */
class CompareWeekFragment : BaseFragment(), CompareWeekListener {


    lateinit var binding: FragmentCompareWeekBinding
    private lateinit var compareWeekAdapter: CompareWeekAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mItems: MutableList<CompareWeekModel>
    private lateinit var tempItem: MutableList<CompareWeekModel>
    private lateinit var dbHelper: DBHelper
    private lateinit var thread: CompareWeekThread
    private var isCreated: Boolean = false
    private var weekTotalUsage = 0
    private var mostUsage = 0
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
        if (isCreated)
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
        binding.fragCompareWeekRv.isNestedScrollingEnabled = false
        binding.fragCompareWeekRv.itemAnimator = null

        Handler().postDelayed({
            binding.fragCompareWeekRv.adapter = compareWeekAdapter
        }, 100)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeekFromDB() {
        try {
            thread = CompareWeekThread(dbHelper, weekUsage, fragDate, mutableListOf(), this)
            thread.run()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItems(weekUsage: Int, mItems: MutableList<CompareWeekModel>) {
        this.weekUsage = weekUsage
        this.mItems = mItems
        DLog.e("now getItemCalled?")
        if (tempItem != mItems) {
            weekTotalUsage = 0
            mostUsage = 0
            tempItem = mItems.asReversed()
            setItem(tempItem)
            getPercent(tempItem)
        }

    }


    private fun getTotalAndMostUsage(tempItem: MutableList<CompareWeekModel>) {
        val arr = ArrayList<Int>()
        var mostItem: CompareWeekModel? = null
        for (item in tempItem) {
            weekTotalUsage += item.total.toInt()
            arr.add(item.total.toInt())
        }
        mostUsage = arr.max()!!
        for (item in tempItem) {
            if (mostUsage == item.total.toInt()) {
                mostItem = item
            }
        }
        DLog.e("mostUsage : $mostUsage")
        setTotalAndMostUsage(tempItem.size, mostUsage, weekTotalUsage, mostItem)

    }

    private fun setTotalAndMostUsage(size: Int, mostUsage: Int, weekTotalUsage: Int, mostItem: CompareWeekModel?) {
        if (weekTotalUsage != 0) {
            setTextOfTotal("${size}주 합계: ${UtilMethod.currencyFormat(weekTotalUsage.toString())}원"
                    , "가장 많은 소비를 한 주"
                    , "${replaceDate(mostItem?.first)} ~ ${replaceDate(mostItem?.last)}\n${UtilMethod.currencyFormat(mostUsage.toString())}원"
                    )

            binding.fragCompareWeekTxtNodata.visibility = View.INVISIBLE
            binding.fragCompareWeekConstResult.visibility = View.VISIBLE
            binding.fragCompareWeekTxtAlltotal.visibility = View.VISIBLE

        } else {
            binding.fragCompareWeekTxtAlltotal.visibility = View.INVISIBLE
            binding.fragCompareWeekConstResult.visibility = View.INVISIBLE
            binding.fragCompareWeekTxtNodata.visibility = View.VISIBLE
        }
        if (!isCreated)
            isCreated = true
    }

    private fun replaceDate(date: String?): String {
        val month: String
        val days: String
        val done: String
        val result = date?.replace("-", "")?.replace(" ", "")
        if (result != "") {
            month = result!!.substring(2, 4)
            days = result.substring(4, 6)
            done = month + "월 " + days + "일"
        } else {
            done = ""
        }

        return done
    }


    private fun setTextOfTotal(allTotal: String, mostTotal: String, mostMonth: String) {

        binding.fragCompareWeekTxtAlltotal.text = allTotal
//        binding.fragCompareWeekTxtAlltotalMoney.text = allMoney
        binding.fragCompareWeekTxtMosttotal.text = mostTotal
        binding.fragCompareWeekTxtMosttotalMonth.text = mostMonth
//        binding.fragCompareWeekTxtMosttotalMoney.text = mostMoney
    }

    private fun setItem(mItems: MutableList<CompareWeekModel>) {
        compareWeekAdapter.clearItems()
        compareWeekAdapter.addItems(mItems)
        getTotalAndMostUsage(mItems)

    }

    private fun getPercent(mItems: MutableList<CompareWeekModel>) {
        for (i in 0 until mItems.size) {
            if (mItems[i].now == "now") {
                if (i != 0) {
                    val before = mItems[i + 1].total
                    val now = mItems[i].total
                    DLog.e("mitem ${mItems[i]} : mitem2 ${mItems[i + 1]}")

                    if (before == "0") {
                        binding.fragCompareWeekTxtPercent.text = "저번주의 데이터가 없습니다"
                        break
                    } else if (now == "0") {
                        binding.fragCompareWeekTxtPercent.text = "이번주의 데이터가 없습니다"
                        break
                    } else {

                        val result = now.toFloat().div(before.toFloat()).times(100)
                        DLog.e("result $result")
                        binding.fragCompareWeekTxtPercent.text = "저번주의 ${DecimalFormat("0.0").format(result)}%를 사용하셨습니다"
                        break
                    }
                } else {
                    binding.fragCompareWeekTxtPercent.text = "아직 데이터가 존재하지 않습니다"
                    break
                }
            }
        }
    }

}// Required empty public constructor
