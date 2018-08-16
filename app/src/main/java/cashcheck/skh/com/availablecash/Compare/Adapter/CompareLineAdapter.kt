package cashcheck.skh.com.availablecash.Register.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.Compare.model.CompareLineModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.ItemCompareLineBinding


/**
 * Created by Seogki on 2018. 6. 5..
 */
open class CompareLineAdapter(context: Context, arrayList: MutableList<CompareLineModel>) : BaseRecyclerViewAdapter<CompareLineModel, CompareLineAdapter.CompareLineViewHolder>(context, arrayList) {

    override fun onBindView(holder: CompareLineViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        val model = getItem(holder.adapterPosition)
        holder.binding.model = model

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_compare_line, parent, false).let { CompareLineViewHolder(it) }


    inner class CompareLineViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemCompareLineBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}