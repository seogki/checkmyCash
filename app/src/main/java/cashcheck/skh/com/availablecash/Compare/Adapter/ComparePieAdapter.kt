package cashcheck.skh.com.availablecash.Register.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.Compare.model.ComparePieModel
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.databinding.ItemComparePieBinding


/**
 * Created by Seogki on 2018. 6. 5..
 */
open class ComparePieAdapter(context: Context, arrayList: MutableList<ComparePieModel>) : BaseRecyclerViewAdapter<ComparePieModel, ComparePieAdapter.ComparePieViewHolder>(context, arrayList) {

    override fun onBindView(holder: ComparePieViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        val model = getItem(holder.adapterPosition)
        holder.binding.model = model
        if (model!!.result!!.contains("-")) {
            holder.binding.itemComparePieImgArrow.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.baseline_arrow_downward_black_24))
            holder.binding.itemComparePieImgArrow.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.Red), PorterDuff.Mode.SRC_ATOP)
        } else if (model.result == "0" || model.result == "0.0") {
            holder.binding.itemComparePieImgArrow.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.baseline_remove_black_24))
            holder.binding.itemComparePieImgArrow.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.blue), PorterDuff.Mode.SRC_ATOP)
        } else {
            holder.binding.itemComparePieImgArrow.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.baseline_arrow_upward_black_24))
            holder.binding.itemComparePieImgArrow.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.green), PorterDuff.Mode.SRC_ATOP)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_compare_pie, parent, false).let { ComparePieViewHolder(it) }


    inner class ComparePieViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemComparePieBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}