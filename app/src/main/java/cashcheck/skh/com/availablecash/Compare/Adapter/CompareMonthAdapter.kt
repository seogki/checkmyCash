package cashcheck.skh.com.availablecash.Compare.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.Compare.model.CompareMonthModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.ItemRvMonthBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class CompareMonthAdapter(context: Context, arraylist: MutableList<CompareMonthModel>) : BaseRecyclerViewAdapter<CompareMonthModel, CompareMonthAdapter.CompareMonthViewHolder>(context, arraylist) {

    private var arr = arraylist
    override fun onBindView(holder: CompareMonthViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(getItem(holder.adapterPosition))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemRvMonthBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rv_month, parent, false)
        return CompareMonthViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        val id = arr[position]
        return id.months.hashCode().toLong()
    }

    inner class CompareMonthViewHolder(val binding: ItemRvMonthBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CompareMonthModel?) {
            binding.model = model
            binding.executePendingBindings()
        }
    }
}