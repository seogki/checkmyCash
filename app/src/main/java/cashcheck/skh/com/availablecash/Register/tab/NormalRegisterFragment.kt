package cashcheck.skh.com.availablecash.Register.tab


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.FragmentNormalRegisterBinding


/**
 * A simple [Fragment] subclass.
 */
class NormalRegisterFragment : Fragment() {


    lateinit var binding: FragmentNormalRegisterBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normal_register, container, false)

        return binding.root
    }

}// Required empty public constructor
