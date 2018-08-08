package cashcheck.skh.com.availablecash.Compare


import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseFragment

import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.FragmentCompareMainBinding


/**
 * A simple [Fragment] subclass.
 */
class CompareMainFragment : BaseFragment() {

    lateinit var binding: FragmentCompareMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_main, container, false)
        binding.compareFragImgLeft.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.compareFragImgRight.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        return binding.root
    }

}// Required empty public constructor
