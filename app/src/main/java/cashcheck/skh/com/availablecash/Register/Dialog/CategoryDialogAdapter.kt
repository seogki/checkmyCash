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
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryDialogv2ItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_category_dialogv2_item, parent, false)
        return CategoryDialogViewHolder(binding, categoryDialogInterface)

    }


    inner class CategoryDialogViewHolder(val binding: ItemCategoryDialogv2ItemBinding, private var categoryDialogInterface: DialogInterface) : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        fun bind(model: String?) {
            binding.cate = model
            binding.onClickListener = this
            binding.onLongClickListener = this
            binding.executePendingBindings()
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