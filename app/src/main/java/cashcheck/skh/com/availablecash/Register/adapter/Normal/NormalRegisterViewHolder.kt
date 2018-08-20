package cashcheck.skh.com.availablecash.Register.adapter.Normal

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvBinding

/**
 * Created by Seogki on 2018. 8. 13..
 */
open class NormalRegisterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var binding: ItemNormalRegisterRvBinding

    init {
        super.itemView
        binding = DataBindingUtil.bind(itemView)
    }
}