package cashcheck.skh.com.availablecash.Register.adapter.Normal

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvEndBinding

/**
 * Created by Seogki on 2018. 8. 13..
 */
open class NormalEndRegisterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var binding: ItemNormalRegisterRvEndBinding

    init {
        super.itemView
        binding = DataBindingUtil.bind(itemView)
    }
}