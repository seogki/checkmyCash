package cashcheck.skh.com.availablecash.Compare


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.Compare.tab.CompareLineFragment
import cashcheck.skh.com.availablecash.Compare.tab.ComparePieFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.Util.TabPagerAdapter
import cashcheck.skh.com.availablecash.databinding.FragmentCompareMainBinding


/**
 * A simple [Fragment] subclass.
 */
class CompareMainFragment : BaseFragment(){

    lateinit var binding: FragmentCompareMainBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_main, container, false)
        dbHelper = DBHelper(context!!.applicationContext, "${Const.DbName}.db", null, 1)
        setTabLayout()
        return binding.root
    }

    private fun setTabLayout(){
        viewPager = binding.compareFragViewpager
        adapter = TabPagerAdapter(childFragmentManager)
        tabLayout = binding.compareFragTablayout

        adapter.addFragment(CompareLineFragment(), "라인 그래프")
        adapter.addFragment(ComparePieFragment(), "파이 그래프")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

}// Required empty public constructor
