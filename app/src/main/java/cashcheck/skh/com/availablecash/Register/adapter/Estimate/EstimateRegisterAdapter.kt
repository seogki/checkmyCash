package cashcheck.skh.com.availablecash.Register.adapter.Estimate

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
import cashcheck.skh.com.availablecash.Util.EstimateDBHelper
import cashcheck.skh.com.availablecash.databinding.ItemEstimateBinding

/**
 * Created by Seogki on 2018. 8. 20..
 */
class EstimateRegisterAdapter(context: Context, arraylist: MutableList<EstimateRegisterModel>) : BaseRecyclerViewAdapter<EstimateRegisterModel, EstimateRegisterAdapter.EstimateRegisterViewHolder>(context, arraylist) {

    lateinit var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener
    override fun onBindView(holder: EstimateRegisterViewHolder, position: Int) {
        holder.setIsRecyclable(true)
        val model = getItem(holder.adapterPosition)
        holder.binding.model = model
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estimate, parent, false)
        return EstimateRegisterViewHolder(view, onNormalRegisterDeleteListener)
    }


    inner class EstimateRegisterViewHolder(itemView: View?, private var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        var binding: ItemEstimateBinding
        private var isExpend: Boolean = false
        private var db: EstimateDBHelper

        init {
            super.itemView
            db = EstimateDBHelper(context!!.applicationContext, "${Const.DbEstimateName}.db", null, 1)
            binding = DataBindingUtil.bind(itemView)
            binding.onClickListener = this
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.estimate_cate_const1 -> {
                    if (binding.model.cate != "합계") {
                        if (!isExpend) {
                            binding.estimateCateConst2.visibility = View.VISIBLE
                            isExpend = true
                        } else {
                            binding.estimateCateConst2.visibility = View.GONE
                            isExpend = false
                        }
                    }
                }
                R.id.item_cate_sub_img_clear -> {
                    deleteData()
                    isExpend = false
                }
                R.id.item_cate_sub_img_edit -> {
                    beginActivity()
                    isExpend = false
                }
            }
        }

        private fun deleteData() {

            AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                    .setMessage("정말로 지우시겠습니까?")
                    .setPositiveButton("확인", { dialog, _ ->
                        dialog.dismiss()
                        binding.model?.num?.let { db.deleteData(it) }
                        onNormalRegisterDeleteListener.onCompleteDelete("done")
                        binding.estimateCateConst2.visibility = View.GONE
                    }).setNegativeButton("취소", { dialog, _ ->
                        dialog.dismiss()
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
            i.putExtra(Const.ItemDBNAME, Const.DbEstimateName)
            context!!.startActivity(i)
            binding.estimateCateConst2.visibility = View.GONE

        }
    }
}