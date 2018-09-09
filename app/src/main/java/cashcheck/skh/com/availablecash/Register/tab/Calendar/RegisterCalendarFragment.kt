package cashcheck.skh.com.availablecash.Register.tab.Calendar


import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.adapter.Calendar.CalendarAdapter
import cashcheck.skh.com.availablecash.Register.adapter.Calendar.CalendarTodayAdapter
import cashcheck.skh.com.availablecash.Register.model.CalendarModel
import cashcheck.skh.com.availablecash.Register.model.EstimateRegisterModel
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.FragmentRegisterCalendarBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class RegisterCalendarFragment : BaseFragment(), BaseRecyclerViewAdapter.OnItemClickListener, OnNormalRegisterDeleteListener {


    private lateinit var binding: FragmentRegisterCalendarBinding
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var layoutManager2: LinearLayoutManager
    private lateinit var calendarTodayAdapter: CalendarTodayAdapter
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var todayItem: MutableList<EstimateRegisterModel>
    private lateinit var mItem: MutableList<CalendarModel>
    private lateinit var db: DBHelper
    private var isStart = false
    private var result = ""
    private var date: String? = null
    fun getDate(date: String) {
        this.date = "$date-01"
        setCalendar()
    }

    override fun onResume() {
        super.onResume()
        if (isStart) {
            setCalendar()
            getTodayDB()
        }
    }

    override fun onCompleteDelete(done: String?) {
        if (done == "done") {
            setCalendar()
            getTodayDB()
        }

    }


    override fun onItemClick(view: View, position: Int) {
        val end = calendarAdapter.getItem(position)?.date!!
        val replaced = end.replace("-", "").replace(" ", "")
        var month = replaced.substring(2, 4)
        var day = replaced.substring(4, 6)
        if (month.substring(0, 1) == "0") {
            month = month.substring(1, 2)
        }
        if (day.substring(0, 1) == "0") {
            day = day.substring(1, 2)
        }
        AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                .setTitle(month + "월 " + day + "일")
                .setMessage("항목을 추가 또는 조회 하시겠습니까?")
                .setPositiveButton("추가") { dialog, _ ->
                    val i = Intent(context!!, RegisterCalendarActivity::class.java)
                            .putExtra(Const.ItemClickCalendar, calendarAdapter.getItem(position)?.date)
                            .putExtra(Const.ItemClickCalendarDays, calendarAdapter.getItem(position)?.weekday)
                    startActivity(i)
                    dialog.dismiss()

                }.setNeutralButton("조회") { dialog, _ ->
                    result = calendarAdapter.getItem(position)?.date!!
                    getTodayDB()
                    dialog.dismiss()

                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun getTodayDB() {

        todayItem = mutableListOf()
        val db = db.readableDatabase
        var cursor: Cursor? = null
        try {
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
            cursor = db.rawQuery("SELECT * FROM ${Const.DbName} WHERE date LIKE '%" + result + "%' ORDER BY date DESC", null)
            var totalMoney = 0
            var dayofWeek = ""
            while (cursor.moveToNext()) {
                val num = cursor.getString(0)
                val date = cursor.getString(1)
                val cate = cursor.getString(2)
                val money = cursor.getString(3)
                val days = cursor.getString(4)
                totalMoney += money.toInt()
                dayofWeek = days
                todayItem.add(EstimateRegisterModel(num, date, cate, money, days))
            }
            if (todayItem.size < 1) {
                binding.fragCalendarConstHeader.visibility = View.INVISIBLE
                calendarTodayAdapter.clearItems()
            } else {
                setHeaderText(totalMoney, dayofWeek, result)
                setTodayData()
                binding.fragCalendarConstHeader.visibility = View.VISIBLE
            }


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setHeaderText(totalMoney: Int, dayofWeek: String, result: String) {
        val replaced = result.replace("-", "").replace(" ", "")
        val day = replaced.substring(4, 6)
        binding.calendarCateTxtDays.text = dayofWeek
        binding.calendarCateTxtMoney.text = "${UtilMethod.currencyFormat(totalMoney.toString())}원"
        binding.calendarCateTxtTime.text = day
    }

    private fun setTodayData() {
        calendarTodayAdapter.clearItems()
        calendarTodayAdapter.addItems(todayItem)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_calendar, container, false)
        db = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        binding.fragCalendarConstHeader.visibility = View.INVISIBLE
        result = UtilMethod.getCurrentMonth()
        setRv()
        setCalendar()
        setRvToday()
        return binding.root
    }

    private fun setRvToday() {
        calendarTodayAdapter = CalendarTodayAdapter(context!!, ArrayList())
        layoutManager2 = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager2.isItemPrefetchEnabled = true
        layoutManager2.initialPrefetchItemCount = 4
        calendarTodayAdapter.onNormalRegisterDeleteListener = this
        binding.fragCalendarRvToday.layoutManager = layoutManager2
        binding.fragCalendarRvToday.isNestedScrollingEnabled = false
        binding.fragCalendarRvToday.itemAnimator = null

        Thread(Runnable {
            binding.fragCalendarRvToday.adapter = calendarTodayAdapter
        }).run()
        getTodayDB()
    }

    private fun setRv() {
        layoutManager = GridLayoutManager(context, 7, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.fragCalendarRv.layoutManager = layoutManager
        calendarAdapter = CalendarAdapter(context!!, mutableListOf())
        binding.fragCalendarRv.addItemDecoration(GridSpacingItemDecoration(7, 5, false, 0))
        binding.fragCalendarRv.setHasFixedSize(false)
        calendarAdapter.setOnItemClickListener(this)
        Handler().postDelayed({
            binding.fragCalendarRv.adapter = calendarAdapter
        }, 10)
    }

    private fun checkMoneyFromDate() {

        val db = db.readableDatabase
        for (dates in mItem) {
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
            val cursor = db.rawQuery("SELECT money FROM ${Const.DbName} WHERE date LIKE '%" + dates.date + "%' ORDER BY date DESC", null)
            var money = 0
            while (cursor.moveToNext()) {

                money += cursor.getString(0).toInt()
                dates.money = money.toString()

            }
        }

        setRvData()
    }


    private fun setCalendar() {


        val task = QueryTask(this, mutableListOf())
        task.execute()
        mItem = task.get()
        checkMoneyFromDate()
    }

    private fun setRvData() {
        calendarAdapter.clearItems()
        calendarAdapter.addItems(mItem)
        if (!isStart) {
            isStart = true
        }
    }





    companion object {
        class QueryTask(private val registerCalendarFragment: RegisterCalendarFragment,private val mItem: MutableList<CalendarModel>) : AsyncTask<Void, Void, MutableList<CalendarModel>>() {
            override fun doInBackground(vararg params: Void?): MutableList<CalendarModel> {
                try {

                    val cal = Calendar.getInstance()
                    val df = SimpleDateFormat("yy-MM-dd", Locale.KOREA)
                    val current = df.parse(UtilMethod.getCurrentMonth())
                    if (registerCalendarFragment.date != null) {
                        val d = df.parse(registerCalendarFragment.date)
                        cal.time = d
                    } else {
                        cal.time = current
                    }
                    val end = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

                    // 처음 시작을 달의 시작으로 세팅
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
                    val start = cal.time
                    DLog.e("start $start")
                    for (i in 0 until end) {
                        if (i == 0) {
                            // 만약 처음시작이 일요일일시 아무것도 안하고 데이터를 넣어도된다
                            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

                                addDate(cal, df)
                            } else {

                                val date = cal.time
                                val now = df.format(date)
                                // 만약 처음시작이 일요일이 아닐시 현재요일의 전요일을 다가져온다
                                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                                val sundayDate = cal.time
                                val sunday = df.format(sundayDate)
                                val dates = getBetweenDates(sunday, now)
                                for (between in dates) {
                                    addDateByDate(between, df, now)
                                }
                                cal.add(Calendar.MONTH, 1)
                                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
                            }

                        } else if (i == end - 1) {
                            //만약 마지막에 날이 토요일이면 아무것도 안하고 데이터 넣으면된다
                            cal.add(Calendar.DATE, 1)
                            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                                addDate(cal, df)

                            } else {

                                //만약 마지막이 토요일이 아니면 현재 요일 부터 토요일까지 모두 가져온다
                                val date = cal.time
                                val now = df.format(date)
                                cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                                val saturdayDate = cal.time
                                val saturday = df.format(saturdayDate)

                                val dates = getBetweenDates(now, saturday)
                                for (between in dates) {
                                    addDateByDate(between, df, now)
                                }
                            }


                        } else {
                            //중간 부분일땐 그냥 데이터 집어넣으면된다.
                            cal.add(Calendar.DATE, 1)
                            addDate(cal, df)
                        }
                    }

                } catch (e: Exception) {

                }

                return mItem
            }


            private fun addDate(cal: Calendar, df: SimpleDateFormat) {
                val date = cal.time
                val now = df.format(date)
                if (UtilMethod.getCurrentMonth() == now) {
                    mItem.add(CalendarModel("", now, dayIntToString(cal.get(Calendar.DAY_OF_WEEK) - 1), "", null, true))
                } else {
                    mItem.add(CalendarModel("", now, dayIntToString(cal.get(Calendar.DAY_OF_WEEK) - 1), ""))
                }
            }

            private fun addDateByDate(date: Date, df: SimpleDateFormat, startorEnd: String) {

                val now = df.format(date)
                val cal = Calendar.getInstance()
                cal.time = date
                if (startorEnd == now) {
                    if (UtilMethod.getCurrentMonth() == now)
                        mItem.add(CalendarModel("", now, dayIntToString(cal.get(Calendar.DAY_OF_WEEK) - 1), "", null, true))
                    else
                        mItem.add(CalendarModel("", now, dayIntToString(cal.get(Calendar.DAY_OF_WEEK) - 1), "", null))

                } else {
                    if (UtilMethod.getCurrentMonth() == now)
                        mItem.add(CalendarModel("", now, dayIntToString(cal.get(Calendar.DAY_OF_WEEK) - 1), "", true, true))
                    else
                        mItem.add(CalendarModel("", now, dayIntToString(cal.get(Calendar.DAY_OF_WEEK) - 1), "", true))

                }

            }

            private fun dayIntToString(i: Int): String {
                return when (i) {
                    0 -> "일요일"
                    1 -> "월요일"
                    2 -> "화요일"
                    3 -> "수요일"
                    4 -> "목요일"
                    5 -> "금요일"
                    6 -> "토요일"
                    else -> ""
                }
            }

            @SuppressLint("SimpleDateFormat")
            private fun getBetweenDates(dateString1: String, dateString2: String): List<Date> {
                val dates = ArrayList<Date>()
                val df1 = SimpleDateFormat("yy-MM-dd", Locale.KOREA)

                var date1: Date? = null
                var date2: Date? = null

                try {
                    date1 = df1.parse(dateString1)
                    date2 = df1.parse(dateString2)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val cal1 = Calendar.getInstance()
                cal1.time = date1


                val cal2 = Calendar.getInstance()
                cal2.time = date2

                while (!cal1.after(cal2)) {
                    dates.add(cal1.time)
                    cal1.add(Calendar.DATE, 1)
                }
                return dates
            }
        }
    }
}
