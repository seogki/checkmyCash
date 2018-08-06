package cashcheck.skh.com.availablecash.Base

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import cashcheck.skh.com.availablecash.Util.DLog

/**
 * Created by TedPark on 15. 9. 10..
 */
abstract class BaseRecyclerViewAdapter<T, H : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var arrayList: MutableList<T>? = null
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null
    private var onItemTouchLister: OnItemTouchLister? = null
    var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    constructor(context: Context, arrayList: MutableList<T>) {
        this.context = context
        this.arrayList = arrayList
    }

    override fun getItemCount(): Int {
        return if (arrayList == null) 0 else arrayList!!.size


    }

    fun getItem(position: Int): T? {

        return if (arrayList == null) null else arrayList!![position]


    }

    fun updateItems(items: List<T>) {
        if (this.arrayList == null) {
            arrayList = ArrayList()
        }
        this.arrayList!!.clear()
        this.arrayList!!.addAll(items)

        Handler().post { notifyDataSetChanged() }
    }

    fun updateItemAt(position: Int, item: T) {


        if (this.arrayList!!.size - 1 < position) {
            arrayList!!.add(item)

        } else {
            this.arrayList!!.removeAt(position)
            this.arrayList!!.add(position, item)

        }
        notifyItemChanged(position)
    }

    fun removeItemAt(position: Int) {
        if (this.arrayList == null)
            return
        if (position >= 0) {
            this.arrayList!!.removeAt(position)
        }
        notifyItemRemoved(position)

    }

    fun removeItemObject(items: T?) {
        if (this.arrayList == null)
            return
        if (items != null) {
            this.arrayList!!.remove(items)
        }
        notifyDataSetChanged()
    }

    fun addItems(items: MutableList<T>) {

        if (this.arrayList == null) {
            this.arrayList = items
        } else {
            this.arrayList!!.addAll(items)
        }
        Handler(Looper.getMainLooper()).post { notifyItemRangeInserted(this@BaseRecyclerViewAdapter.itemCount + 1, items.size) }
    }

    fun addItem(item: T) {
        this.arrayList!!.add(item)
        notifyItemInserted(this.itemCount)
    }

    fun addItemNotifyRanged(item: T, firstPosition: Int, lastPosition: Int) {
        this.arrayList!!.add(item)
        Handler().post { notifyItemRangeInserted(firstPosition, lastPosition) }

    }

    fun addItemsPosition(position: Int, items: List<T>) {
        this.arrayList!!.addAll(position, items)
        Handler().post { notifyItemChanged(position) }

    }

    fun addItemAtPosition(position: Int, item: T) {
        if (this.arrayList == null)
            return
        if (position >= 0) {
            this.arrayList!!.add(position, item)
        }
        notifyItemInserted(position)
    }

    fun clearItems() {
        if (arrayList != null) {

            arrayList!!.clear()
            notifyDataSetChanged()
        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnTouchListener { v, event ->
            if (onItemTouchLister != null) {
                onItemTouchLister!!.onItemTouch(v, holder.adapterPosition, event)
            }
            false
        }

        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                DLog.e("onItemClickedafter")
                onItemClickListener!!.onItemClick(holder.itemView, holder.adapterPosition)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (onItemLongClickListener != null) {
                onItemLongClickListener!!.onItemLongClick(holder.itemView, holder.adapterPosition)
            }
            false
        }


        @Suppress("UNCHECKED_CAST")
        onBindView(holder as H, position)


    }

    abstract fun onBindView(holder: H, position: Int)

    /**
     * recyclerview item click listener 리사이클러뷰를 구현한 Activity 나  Fragment 에서 implement 받는다.
     *
     * @param onItemClickListener recyclerview 의 아이템클릭리스터
     */
    fun setOnItemClickListener(
            onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemTouchLister(itemTouchLister: OnItemTouchLister) {
        this.onItemTouchLister = itemTouchLister
    }

    fun setOnItemLongClickListener(
            onItemLongClickListener: OnItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    interface OnItemClickListener {

        fun onItemClick(view: View, position: Int)
    }

    interface OnItemTouchLister {
        fun onItemTouch(view: View, position: Int, event: MotionEvent)
    }


    interface OnItemLongClickListener {

        fun onItemLongClick(view: View, position: Int)
    }


}
