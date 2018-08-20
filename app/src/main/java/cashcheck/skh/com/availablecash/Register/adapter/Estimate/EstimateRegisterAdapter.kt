package cashcheck.skh.com.availablecash.Register.adapter.Estimate

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.model.EstimateRegisterModel
import cashcheck.skh.com.availablecash.databinding.ItemEstimateBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class EstimateRegisterAdapter(context: Context, arraylist: MutableList<EstimateRegisterModel>) : BaseRecyclerViewAdapter<EstimateRegisterModel, EstimateRegisterAdapter.EstimateRegisterViewHolder>(context, arraylist) {

    override fun onBindView(holder: EstimateRegisterViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        val model = getItem(holder.adapterPosition)
        holder.binding.model = model
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estimate, parent, false)
        return EstimateRegisterViewHolder(view)
    }


    inner class EstimateRegisterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemEstimateBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
        }
    }
}