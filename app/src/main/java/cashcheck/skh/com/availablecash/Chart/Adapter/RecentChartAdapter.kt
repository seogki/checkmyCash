package cashcheck.skh.com.availablecash.Chart.Adapter

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cashcheck.skh.com.availablecash.BR
import cashcheck.skh.com.availablecash.Base.BaseRecyclerViewAdapter
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.Modify.ModifyRegisterActivity
import cashcheck.skh.com.availablecash.Register.model.EstimateRegisterModel
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.databinding.ItemChartmainBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class RecentChartAdapter(context: Context, arraylist: MutableList<EstimateRegisterModel>) : BaseRecyclerViewAdapter<EstimateRegisterModel, RecentChartAdapter.RecentChartViewHolder>(context, arraylist) {

    lateinit var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener
    private var arr = arraylist
    private var mContext = context
    override fun onBindView(holder: RecentChartViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val model = getItem(holder.adapterPosition)
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemChartmainBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_chartmain, parent, false)

        return RecentChartViewHolder(binding, onNormalRegisterDeleteListener)
    }


    inner class RecentChartViewHolder(val binding: ItemChartmainBinding, private var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


        private var isExpend: Boolean = false
        private var db: DBHelper? = null


        fun bind(data: EstimateRegisterModel?) {
            binding.setVariable(BR.model, data)
            binding.executePendingBindings()
            db = DBHelper(mContext.applicationContext, "${Const.DbName}.db", null, 1)
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
                    .setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                        binding.model?.num?.let { db?.deleteData(it) }
                        onNormalRegisterDeleteListener.onCompleteDelete("done",adapterPosition)
                        binding.estimateCateConst2.visibility = View.GONE
                        isExpend = false
                    }.setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                        isExpend = true
                    }
                    .show()

        }


        private fun beginActivity() {
            val model = binding.model
            val i = Intent(context, ModifyRegisterActivity::class.java)
            i.putExtra(Const.ItemId, model?.num)
            i.putExtra(Const.ItemCategory, model?.cate)
            i.putExtra(Const.ItemMoney, model?.money)
            i.putExtra(Const.ItemDate, model?.date)
            i.putExtra(Const.ItemDays, model?.days)
            i.putExtra(Const.ItemDBNAME, Const.DbName)
            context!!.startActivity(i)
            binding.estimateCateConst2.visibility = View.GONE

        }
    }
}