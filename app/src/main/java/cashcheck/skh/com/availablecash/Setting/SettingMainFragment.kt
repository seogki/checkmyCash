package cashcheck.skh.com.availablecash.Setting

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Util.TabPagerAdapter
import cashcheck.skh.com.availablecash.databinding.FragmentSettingMainBinding


class SettingMainFragment : BaseFragment() {

    lateinit var binding: FragmentSettingMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_main, container, false)



        return binding.root
    }



}// Required empty public constructor
