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
        holder.bind(getItem(holder.adapterPosition))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemRvCalendarBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rv_calendar, parent, false)
        return CalendarViewHolder(binding)

    }

    override fun getItemId(position: Int): Long {
        val id = arr[position]
        return id.date!!.hashCode().toLong()
    }


    inner class CalendarViewHolder(val binding: ItemRvCalendarBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(model: CalendarModel?) {
            binding.model = model
            if (model!!.isBelow == true) {
                if (model.isNow == true)
                    binding.itemRvCalendarConstAll.setBackgroundResource(R.drawable.view_calendar_now_stroke_status_notthismonth)
                else
                    binding.itemRvCalendarConstAll.setBackgroundResource(R.drawable.view_calendar_now_grey_ripple)

            } else {
                if (model.isNow == true)
                    binding.itemRvCalendarConstAll.setBackgroundResource(R.drawable.view_calendar_now_stroke_status)
                else
                    binding.itemRvCalendarConstAll.setBackgroundResource(R.drawable.rv_end_ripple_corner_btm)

            }
            when {
                model.weekday == "일요일" -> binding.itemRvCalendarTxtDate.setTextColor(ContextCompat.getColor(context!!, R.color.Red))
                model.weekday == "토요일" -> binding.itemRvCalendarTxtDate.setTextColor(ContextCompat.getColor(context!!, R.color.blue))
                else -> binding.itemRvCalendarTxtDate.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            }
            binding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            when (v?.id) {

            }
        }
    }
}