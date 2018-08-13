//package cashcheck.skh.com.availablecash.Register.adapter
//
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
//import cashcheck.skh.com.availablecash.Compare.model.ComparePieModel
//import cashcheck.skh.com.availablecash.R
//import cashcheck.skh.com.availablecash.Register.model.EventItem
//import cashcheck.skh.com.availablecash.Register.model.HeaderItem
//import cashcheck.skh.com.availablecash.Register.model.ListItem
//
//
///**
// * Created by Seogki on 2018. 6. 5..
// */
//open class ComparePieAdapter(context: Context, arrayList: MutableList<ComparePieModel>) : BaseRecyclerViewAdapter<ListItem, RecyclerView.ViewHolder>(context, arrayList) {
//
//    private var mitem = arrayList
//
//    override fun onBindView(holder: RecyclerView.ViewHolder, position: Int) {
//        val type = getItemViewType(position)
//        holder.setIsRecyclable(true)
//        if (type == ListItem.TYPE_EVENT) {
//            val data = mitem[holder.adapterPosition] as EventItem
//            if (holder is NormalRegisterViewHolder)
//                holder.binding.model = data.event
//
//        } else {
//            val data = mitem[holder.adapterPosition] as HeaderItem
//            if (holder is NormalDateRegisterViewHolder)
//                holder.binding.model = data.date
//        }
//
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//
//        val view: View
//
//        return if (viewType == ListItem.TYPE_EVENT) {
//            view = LayoutInflater.from(parent.context).inflate(R.layout.item_normal_register_rv, parent, false)
//            NormalRegisterViewHolder(view)
//        } else {
//            view = LayoutInflater.from(parent.context).inflate(R.layout.item_normal_register_rv_header, parent, false)
//            NormalDateRegisterViewHolder(view)
//        }
//
//
//    }
//}