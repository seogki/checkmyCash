package cashcheck.skh.com.availablecash.Compare


import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Compare.tab.CompareLineFragment
import cashcheck.skh.com.availablecash.Compare.tab.CompareMonthFragment
import cashcheck.skh.com.availablecash.Compare.tab.ComparePieFragment
import cashcheck.skh.com.availablecash.Compare.tab.CompareWeekFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.TabPagerAdapter
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentCompareMainBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CompareMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentCompareMainBinding
    private lateinit var adapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    var date: String = ""
    var year: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_main, container, false)
        binding.onClickListener = this
        binding.compareFragImgLeft.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.compareFragImgRight.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        date = UtilMethod.getCurrentDate()
        year = UtilMethod.getCurrentYear()
        val result = UtilMethod.getCurrentDate().replace("-", "")
        binding.chartFragTxtTitlebar.text = "20" + result.substring(0, 2) + "년 " + result.substring(2, 4) + "월 "
        setTabLayout()
        onPageSelected()
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.compare_frag_img_right -> {
                if (binding.compareFragViewpager.currentItem == 0) {
                    setTimeToRight()
                } else if (binding.compareFragViewpager.currentItem == 1) {
                    setYearToRight()
                }
            }
            R.id.compare_frag_img_left -> {
                if (binding.compareFragViewpager.currentItem == 0) {
                    setTimeToLeft()
                } else if (binding.compareFragViewpager.currentItem == 1) {
                    setYearToLeft()
                }
            }

        }
    }


    @SuppressLint("SetTextI18n")
    private fun setTitleBar(a: String, pos: Int) {
        if (pos == 0) {
            val result = a.replace("-", "")
            binding.chartFragTxtTitlebar.text = "20" + result.substring(0, 2) + "년 " + result.substring(2, 4) + "월 "
        } else if (pos == 1) {
            binding.chartFragTxtTitlebar.text = a + "년 "
        }
    }

    private fun setYearToRight() {
        val newYear = getDesignatedYear(1, year).substring(1, 5)
        arrowRight(newYear)
        year = newYear
        setTitleBar(year, 1)
    }

    private fun setYearToLeft() {
        val newYear = getDesignatedYear(-1, year).substring(1, 5)
        arrowRight(newYear)
        year = newYear
        setTitleBar(year, 1)
    }

    private fun setTimeToRight() {
        val newDate = getDesignatedDate(1, date)
        arrowRight(newDate)
        date = newDate
        setTitleBar(date, 0)
    }


    private fun setTimeToLeft() {
        val newDate = getDesignatedDate(-1, date)
        arrowLeft(newDate)
        date = newDate
        setTitleBar(date, 0)
    }

    private fun getDesignatedDate(month: Int, d: String): String {
        val sdf = SimpleDateFormat("yy-MM", Locale.KOREA)
        val dt = sdf.parse(d)
        val c = Calendar.getInstance()
        c.time = dt
        c.add(Calendar.MONTH, month)
        return sdf.format(c.time)
    }

    private fun getDesignatedYear(year: Int, d: String): String {
        val sdf = SimpleDateFormat("yyyyy", Locale.KOREA)
        val dt = sdf.parse(d)
        val c = Calendar.getInstance()
        c.time = dt
        c.add(Calendar.YEAR, year)
        return sdf.format(c.time)
    }

    private fun setTabLayout() {
        viewPager = binding.compareFragViewpager
        adapter = TabPagerAdapter(childFragmentManager)
        tabLayout = binding.compareFragTablayout
        viewPager.offscreenPageLimit = 4
        adapter.addFragment(CompareWeekFragment(), "주별")
        adapter.addFragment(CompareMonthFragment(), "월별")
        adapter.addFragment(CompareLineFragment(), "라인")
        adapter.addFragment(ComparePieFragment(), "파이")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun onPageSelected() {
        binding.compareFragViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (adapter.getItem(position) == null) {
                    return
                }
                when (position) {
                    0 -> {
                        setTitleBar(date, 0)
                        binding.compareFragImgLeft.visibility = View.VISIBLE
                        binding.compareFragImgRight.visibility = View.VISIBLE
                    }
                    1 -> {
                        setTitleBar(year, 1)
                        binding.compareFragImgLeft.visibility = View.VISIBLE
                        binding.compareFragImgRight.visibility = View.VISIBLE
                    }
                    2 -> {
                        if (binding.chartFragTxtTitlebar.text != "그래프") {
                            binding.chartFragTxtTitlebar.text = "그래프"
                            binding.compareFragImgLeft.visibility = View.INVISIBLE
                            binding.compareFragImgRight.visibility = View.INVISIBLE
                        }
                    }
                    3 -> {
                        if (binding.chartFragTxtTitlebar.text != "그래프") {
                            binding.chartFragTxtTitlebar.text = "그래프"
                            binding.compareFragImgLeft.visibility = View.INVISIBLE
                            binding.compareFragImgRight.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun arrowLeft(date: String) {
        if (adapter.getItem(viewPager.currentItem) == null) {
            return
        }

        if (viewPager.currentItem == 0) {
            val f = adapter.getItem(0) as CompareWeekFragment
            f.getTimeFromFragment(date)
        } else if (viewPager.currentItem == 1) {
            val f = adapter.getItem(1) as CompareMonthFragment
            f.getTimeFromFragment(date)
        }
    }

    private fun arrowRight(date: String) {
        if (adapter.getItem(viewPager.currentItem) == null) {
            return
        }
        if (viewPager.currentItem == 0) {
            val f = adapter.getItem(0) as CompareWeekFragment
            f.getTimeFromFragment(date)
        } else if (viewPager.currentItem == 1) {
            val f = adapter.getItem(1) as CompareMonthFragment
            f.getTimeFromFragment(date)
        }
    }

}// Required empty public constructor
