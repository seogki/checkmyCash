package cashcheck.skh.com.availablecash.Compare.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
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
        holder.binding.model = getItem(holder.adapterPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_month, parent, false)
        return CompareMonthViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        val id = arr[position]
        return id.months.hashCode().toLong()
    }

    inner class CompareMonthViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var binding: ItemRvMonthBinding


        init {
            super.itemView

            binding = DataBindingUtil.bind(itemView)

        }
    }
}