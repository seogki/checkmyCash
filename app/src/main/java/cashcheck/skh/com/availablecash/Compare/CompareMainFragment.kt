package cashcheck.skh.com.availablecash.Compare


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_main, container, false)
        return binding.root
    }

}// Required empty public constructor
