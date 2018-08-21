package cashcheck.skh.com.availablecash.Compare.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
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

    
    override fun onBindView(holder: CompareWeekViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_week, parent, false)
        return CompareWeekViewHolder(view)
    }


    inner class CompareWeekViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {


        var binding: ItemRvWeekBinding
       

        init {
            super.itemView
            
            binding = DataBindingUtil.bind(itemView)
            
        }
    }
}