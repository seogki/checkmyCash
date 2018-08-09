package cashcheck.skh.com.availablecash.Register.tab

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.model.NormalRegisterModel
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvBinding


/**
 * Created by Seogki on 2018. 6. 5..
 */
open class NormalRegisterAdapter(context: Context, arrayList: MutableList<NormalRegisterModel>) : BaseRecyclerViewAdapter<NormalRegisterModel, NormalRegisterAdapter.NormalRegisterViewHolder>(context, arrayList) {


    override fun onBindView(holder: NormalRegisterViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        holder.binding.model = getItem(holder.adapterPosition)
    }

    override fun getItemId(position: Int): Long {
        val id = arrayList?.get(position)
        return id!!.id!!.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_normal_register_rv, parent, false).let { NormalRegisterViewHolder(it) }

    inner class NormalRegisterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemNormalRegisterRvBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}