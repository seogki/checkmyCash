package cashcheck.skh.com.availablecash.Register.adapter.Normal

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.model.EndItem
import cashcheck.skh.com.availablecash.Register.model.EventItem
import cashcheck.skh.com.availablecash.Register.model.HeaderItem
import cashcheck.skh.com.availablecash.Register.model.ListItem
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvBinding
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvEndBinding
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvHeaderBinding


/**
 * Created by Seogki on 2018. 6. 5..
 */
open class NormalRegisterAdapter(context: Context, arrayList: MutableList<ListItem>) : BaseRecyclerViewAdapter<ListItem, RecyclerView.ViewHolder>(context, arrayList) {

    private var mitem = arrayList
    lateinit var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener
    override fun onBindView(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        holder.setIsRecyclable(true)
        if (type == ListItem.TYPE_EVENT) {
            val data = mitem[holder.adapterPosition] as EventItem
            if (holder is NormalRegisterViewHolder) {
                holder.bind(data.event)

            }

        } else if (type == ListItem.TYPE_HEADER) {
            val data = mitem[holder.adapterPosition] as HeaderItem
            if (holder is NormalDateRegisterViewHolder) {
                holder.bind(data.date)
            }
        } else {
            val data = mitem[holder.adapterPosition] as EndItem
            if (holder is NormalEndRegisterViewHolder) {
                holder.bind(data.event)

            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        return mitem[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ListItem.TYPE_HEADER -> {

                val binding: ItemNormalRegisterRvHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_normal_register_rv_header, parent, false)
                NormalDateRegisterViewHolder(binding)
            }
            ListItem.TYPE_EVENT -> {
                val binding: ItemNormalRegisterRvBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_normal_register_rv, parent, false)

                NormalRegisterViewHolder(binding, onNormalRegisterDeleteListener)
            }

            else -> {
                val binding: ItemNormalRegisterRvEndBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_normal_register_rv_end, parent, false)
                NormalEndRegisterViewHolder(binding, onNormalRegisterDeleteListener)
            }
        }


    }
}