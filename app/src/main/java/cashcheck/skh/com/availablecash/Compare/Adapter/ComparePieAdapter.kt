package cashcheck.skh.com.availablecash.Register.adapter

import android.content.Context
import android.databinding.DataBindingUtil
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

    private var mitem = arrayList

    override fun onBindView(holder: ComparePieViewHolder, position: Int) {
        holder.setIsRecyclable(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_compare_pie, parent, false)
        return ComparePieViewHolder(view)
    }

    inner class ComparePieViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemComparePieBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}