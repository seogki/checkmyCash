package cashcheck.skh.com.availablecash.Register.adapter.Calendar

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.Modify.ModifyRegisterActivity
import cashcheck.skh.com.availablecash.Register.model.EstimateRegisterModel
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.databinding.ItemEstimateBinding

/**
 * Created by Seogki on 2018. 8. 29..
 */
class CalendarTodayAdapter(context: Context, arraylist: MutableList<EstimateRegisterModel>) : BaseRecyclerViewAdapter<EstimateRegisterModel, CalendarTodayAdapter.CalendarTodayViewHolder>(context, arraylist) {

    lateinit var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener
    private var arr = arraylist
    private var mContext = context
    override fun onBindView(holder: CalendarTodayViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val model = getItem(holder.adapterPosition)
        holder.binding.model = model

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estimate, parent, false)
        return CalendarTodayViewHolder(view, onNormalRegisterDeleteListener)
    }




    inner class CalendarTodayViewHolder(itemView: View?, private var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var binding: ItemEstimateBinding
        private var isExpend: Boolean = false
        private var db: DBHelper
        init {
            super.itemView
            db = DBHelper(mContext.applicationContext, "${Const.DbName}.db", null, 1)
            binding = DataBindingUtil.bind(itemView)
            binding.onClickListener = this
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.estimate_cate_const1 -> {
                    if (!isExpend) {
                        binding.estimateCateConst2.visibility = View.VISIBLE
                        isExpend = true
                    } else {
                        binding.estimateCateConst2.visibility = View.GONE
                        isExpend = false
                    }
                }
                R.id.item_cate_sub_img_clear -> {
                    deleteData()

                }
                R.id.item_cate_sub_img_edit -> {
                    beginActivity()
                    isExpend = false
                }
            }
        }

        private fun deleteData() {

            AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                    .setTitle("데이터 삭제")
                    .setMessage("정말로 지우시겠습니까?")
                    .setPositiveButton("확인", { dialog, _ ->
                        dialog.dismiss()
                        binding.model?.num?.let { db.deleteData(it) }
                        onNormalRegisterDeleteListener.onCompleteDelete("done")
                        binding.estimateCateConst2.visibility = View.GONE
                        isExpend = false
                    }).setNegativeButton("취소", { dialog, _ ->
                        dialog.dismiss()
                        isExpend = true
                    })
                    .show()

        }


        private fun beginActivity() {
            val model = binding.model
            val i = Intent(context, ModifyRegisterActivity::class.java)
            i.putExtra(Const.ItemId, model.num)
            i.putExtra(Const.ItemCategory, model.cate)
            i.putExtra(Const.ItemMoney, model.money)
            i.putExtra(Const.ItemDate, model.date)
            i.putExtra(Const.ItemDays, model.days)
            i.putExtra(Const.ItemDBNAME, Const.DbName)
            context!!.startActivity(i)
            binding.estimateCateConst2.visibility = View.GONE

        }
    }
}