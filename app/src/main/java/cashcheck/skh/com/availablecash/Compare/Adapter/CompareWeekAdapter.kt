package cashcheck.skh.com.availablecash.Compare.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.Compare.model.CompareWeekModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.ItemRvWeekBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class CompareWeekAdapter(context: Context, arraylist: MutableList<CompareWeekModel>) : BaseRecyclerViewAdapter<CompareWeekModel, CompareWeekAdapter.CompareWeekViewHolder>(context, arraylist) {

    private val arr = arraylist
    override fun onBindView(holder: CompareWeekViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(getItem(holder.adapterPosition))


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemRvWeekBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rv_week, parent, false)
        return CompareWeekViewHolder(binding)
    }


    override fun getItemId(position: Int): Long {
        val id = arr[position]
        return id.first.hashCode().toLong()
    }


    inner class CompareWeekViewHolder(val binding: ItemRvWeekBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CompareWeekModel?) {
            binding.model = model
            if (binding.model!!.now == "now") {
                binding.itemRvWeekFirst.setTextColor(ContextCompat.getColor(context!!, R.color.statusbar))
                binding.itemRvWeekLast.setTextColor(ContextCompat.getColor(context!!, R.color.statusbar))
                binding.itemRvWeekBetween.setTextColor(ContextCompat.getColor(context!!, R.color.statusbar))
            }
            binding.executePendingBindings()
        }
    }
}