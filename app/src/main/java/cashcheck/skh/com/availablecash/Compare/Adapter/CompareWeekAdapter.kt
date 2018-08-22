package cashcheck.skh.com.availablecash.Compare.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
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
        holder.binding.model = getItem(holder.adapterPosition)
        if (holder.binding.model.now == "now") {
            holder.binding.itemRvWeekFirst.setTextColor(ContextCompat.getColor(context!!, R.color.statusbar))
            holder.binding.itemRvWeekLast.setTextColor(ContextCompat.getColor(context!!, R.color.statusbar))
            holder.binding.itemRvWeekBetween.setTextColor(ContextCompat.getColor(context!!, R.color.statusbar))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_week, parent, false)
        return CompareWeekViewHolder(view)
    }


    override fun getItemId(position: Int): Long {
        val id = arr[position]
        return id.first.hashCode().toLong()
    }


    inner class CompareWeekViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemRvWeekBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)

        }
    }
}