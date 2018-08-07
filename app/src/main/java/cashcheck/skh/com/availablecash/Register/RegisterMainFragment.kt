package cashcheck.skh.com.availablecash.Register


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.tab.EstimateRegisterFragment
import cashcheck.skh.com.availablecash.Register.tab.NormalRegisterFragment
import cashcheck.skh.com.availablecash.Util.TabPagerAdapter
import cashcheck.skh.com.availablecash.databinding.FragmentRegisterMainBinding


/**
 * A simple [Fragment] subclass.
 */
class RegisterMainFragment : BaseFragment() {

    lateinit var binding: FragmentRegisterMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_main, container, false)
        setTabLayout()
        return binding.root
    }
    private fun setTabLayout() {
        val viewPager = binding.registerFragViewpager
        val adapter = TabPagerAdapter(childFragmentManager)
        val tabLayoutList = binding.registerFragTablayout

        adapter.addFragment(NormalRegisterFragment(), "기본")
        adapter.addFragment(EstimateRegisterFragment(), "대략")
        viewPager.adapter = adapter
        tabLayoutList.setupWithViewPager(viewPager)


    }

}// Required empty public constructor
