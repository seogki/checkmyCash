package cashcheck.skh.com.availablecash.Register.tab


import android.content.Intent
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
import cashcheck.skh.com.availablecash.databinding.FragmentNormalRegisterBinding


/**
 * A simple [Fragment] subclass.
 */
class NormalRegisterFragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentNormalRegisterBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normal_register, container, false)
        binding.normalFragFab.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.onClickListener = this
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.normal_frag_fab -> {
                val i = Intent(context!!, NormalRegisterActivity::class.java)
                startActivity(i)
                activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
        }
    }

    private fun createChart() {

    }


}// Required empty public constructor
