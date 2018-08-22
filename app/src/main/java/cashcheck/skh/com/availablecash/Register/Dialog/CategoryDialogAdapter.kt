package cashcheck.skh.com.availablecash.Register.Dialog

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.DialogInterface
import cashcheck.skh.com.availablecash.databinding.ItemCategoryDialogv2ItemBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class CategoryDialogAdapter(context: Context, arraylist: MutableList<String>) : BaseRecyclerViewAdapter<String, CategoryDialogAdapter.CategoryDialogViewHolder>(context, arraylist) {

    lateinit var categoryDialogInterface: DialogInterface
    override fun onBindView(holder: CategoryDialogViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val model = getItem(holder.adapterPosition)
        holder.binding.cate = model
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_dialogv2_item, parent, false)
        return CategoryDialogViewHolder(view, categoryDialogInterface)
    }


    inner class CategoryDialogViewHolder(itemView: View?, private var categoryDialogInterface: DialogInterface) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {


        var binding: ItemCategoryDialogv2ItemBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
            binding.onClickListener = this
            binding.onLongClickListener = this
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.item_v2_category -> {
                    categoryDialogInterface.getTextFromAdapter(binding.cate)
                }
            }
        }

        override fun onLongClick(v: View?): Boolean {
            when (v?.id) {
                R.id.item_v2_category -> {
                    categoryDialogInterface.getPosFromAdapter(adapterPosition)
                }
            }

            return true
        }

    }
}