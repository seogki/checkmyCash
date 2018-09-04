package cashcheck.skh.com.availablecash.Register.adapter.Normal

import android.support.v7.widget.RecyclerView
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvHeaderBinding

/**
 * Created by Seogki on 2018. 8. 13..
 */
open class NormalDateRegisterViewHolder(val binding: ItemNormalRegisterRvHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: String?) {
        binding.model = model
    }
}