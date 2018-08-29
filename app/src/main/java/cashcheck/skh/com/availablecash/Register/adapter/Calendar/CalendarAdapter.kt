package cashcheck.skh.com.availablecash.Register.adapter.Calendar

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.model.CalendarModel
import cashcheck.skh.com.availablecash.databinding.ItemRvCalendarBinding

/**
 * Created by Seogki on 2018. 8. 29..
 */
class CalendarAdapter(context: Context, arraylist: MutableList<CalendarModel>) : BaseRecyclerViewAdapter<CalendarModel, CalendarAdapter.CalendarViewHolder>(context, arraylist) {


    private var arr = arraylist
    override fun onBindView(holder: CalendarViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        val model = getItem(holder.adapterPosition)
        holder.binding.model = model
        if (model?.isBelow == true) {
            if (model.isNow == true) {
                holder.binding.itemRvCalendarConstAll.setBackgroundResource(R.drawable.view_calendar_now_stroke_status_notthismonth)
            } else {
                holder.binding.itemRvCalendarConstAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.darkGrey))
            }
        } else {
            if(model?.isNow == true){
                holder.binding.itemRvCalendarConstAll.setBackgroundResource(R.drawable.view_calendar_now_stroke_status)
            } else {
                holder.binding.itemRvCalendarConstAll.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
            }
        }
        when {
            model?.weekday == "일요일" -> {
                holder.binding.itemRvCalendarTxtDate.setTextColor(ContextCompat.getColor(context!!, R.color.Red))
            }
            model?.weekday == "토요일" -> {
                holder.binding.itemRvCalendarTxtDate.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
            }
            else -> {
                holder.binding.itemRvCalendarTxtDate.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        val id = arr[position]
        return id.date!!.hashCode().toLong()
    }


    inner class CalendarViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var binding: ItemRvCalendarBinding

        init {
            super.itemView
            binding = DataBindingUtil.bind(itemView)
            binding.onClickListener = this
        }

        override fun onClick(v: View?) {
            when (v?.id) {

            }
        }
    }
}