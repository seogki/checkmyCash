package cashcheck.skh.com.availablecash.Register


import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.tab.EstimateRegisterFragment
import cashcheck.skh.com.availablecash.Register.tab.NormalRegisterFragment
import cashcheck.skh.com.availablecash.Util.TabPagerAdapter
import cashcheck.skh.com.availablecash.Util.UtilMethod
import cashcheck.skh.com.availablecash.databinding.FragmentRegisterMainBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class RegisterMainFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentRegisterMainBinding
    private lateinit var adapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    var date: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_main, container, false)
        binding.registerFragImgLeft.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.registerFragImgRight.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        setTabLayout()
        binding.onClickListener = this
        date = UtilMethod.getCurrentDate()
        val result = UtilMethod.getCurrentDate().replace("-", "")
        binding.registerFragTxtTitlebar.text = "20" + result.substring(0, 2) + "년 " + result.substring(2, 4) + "월 "
        return binding.root
    }

    private fun setTabLayout() {
        viewPager = binding.registerFragViewpager
        adapter = TabPagerAdapter(childFragmentManager)
        tabLayout = binding.registerFragTablayout

        adapter.addFragment(NormalRegisterFragment(), "기본")
        adapter.addFragment(EstimateRegisterFragment(), "예상")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        onPageSelected()

    }

    @SuppressLint("SetTextI18n")
    private fun setTitleBar(a: String) {
        val result = a.replace("-", "")
        binding.registerFragTxtTitlebar.text = "20" + result.substring(0, 2) + "년 " + result.substring(2, 4) + "월 "
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_frag_img_right -> {
                setTimeToRight()
            }
            R.id.register_frag_img_left -> {
                setTimeToLeft()
            }
        }
    }

    private fun onPageSelected() {
        binding.registerFragViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (adapter.getItem(position) == null) {
                    return
                }
                if (position == 0) {
                    val fab = activity!!.findViewById(R.id.normal_atv_fab) as FloatingActionButton
                    fab.show()
                    binding.registerFragImgRight.visibility = View.VISIBLE
                    binding.registerFragImgLeft.visibility = View.VISIBLE

                } else {
                    val fab = activity!!.findViewById(R.id.normal_atv_fab) as FloatingActionButton
                    fab.hide()
                    binding.registerFragImgRight.visibility = View.INVISIBLE
                    binding.registerFragImgLeft.visibility = View.INVISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private fun setTimeToRight() {
        val newDate = getDesignatedDate(1, date)
        arrowRight(newDate)
        date = newDate
        setTitleBar(date)
    }


    private fun setTimeToLeft() {
        val newDate = getDesignatedDate(-1, date)
        arrowLeft(newDate)
        date = newDate
        setTitleBar(date)
    }


    private fun getDesignatedDate(month: Int, d: String): String {
        val sdf = SimpleDateFormat("yy-MM", Locale.KOREA)
        val dt = sdf.parse(d)
        val c = Calendar.getInstance()
        c.time = dt
        c.add(Calendar.MONTH, month)
        return sdf.format(c.time)
    }

    private fun arrowLeft(date: String) {
        if (adapter.getItem(viewPager.currentItem) == null) {
            return
        }
        if (viewPager.currentItem == 0) {
            /* 1번째 인덱스에 있는 탭을 의미*/
            val f = adapter.getItem(0) as NormalRegisterFragment
            f.getDate(date)
        }
    }

    private fun arrowRight(date: String) {
        if (adapter.getItem(viewPager.currentItem) == null) {
            return
        }
        if (viewPager.currentItem == 0) {
            /* 1번째 인덱스에 있는 탭을 의미*/
            val f = adapter.getItem(0) as NormalRegisterFragment
            f.getDate(date)
        }
    }

}// Required empty public constructor
