package cashcheck.skh.com.availablecash.Register.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
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
        holder.bind(getItem(holder.adapterPosition))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCompareLineBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_compare_line, parent, false)
        return CompareLineViewHolder(binding)
    }


    inner class CompareLineViewHolder(val binding: ItemCompareLineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CompareLineModel?) {
            binding.model = model
            when {
                model!!.result.contains("-") -> binding.itemCompareLineTxtSdate.setTextColor(ContextCompat.getColor(context!!, R.color.Red))
                model.result.isEmpty() -> binding.itemCompareLineTxtSdate.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                else -> binding.itemCompareLineTxtSdate.setTextColor(ContextCompat.getColor(context!!, R.color.green))
            }
            binding.executePendingBindings()
        }
    }
}