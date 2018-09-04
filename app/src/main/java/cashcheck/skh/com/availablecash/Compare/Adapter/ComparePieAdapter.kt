package cashcheck.skh.com.availablecash.Register.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
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
        holder.bind(getItem(holder.adapterPosition))


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemComparePieBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_compare_pie, parent, false)
        return ComparePieViewHolder(binding)
    }


    inner class ComparePieViewHolder(val binding: ItemComparePieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ComparePieModel?) {
            binding.model = model
            if (model!!.result!!.contains("-")) {
                binding.itemComparePieImgArrow.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.baseline_arrow_downward_black_24))
                binding.itemComparePieImgArrow.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.Red), PorterDuff.Mode.SRC_ATOP)
            } else if (model.result == "0" || model.result == "0.0") {
                binding.itemComparePieImgArrow.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.baseline_remove_black_24))
                binding.itemComparePieImgArrow.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.blue), PorterDuff.Mode.SRC_ATOP)
            } else {
                binding.itemComparePieImgArrow.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.baseline_arrow_upward_black_24))
                binding.itemComparePieImgArrow.drawable.setColorFilter(ContextCompat.getColor(context!!, R.color.green), PorterDuff.Mode.SRC_ATOP)
            }
            binding.executePendingBindings()

        }
    }
}