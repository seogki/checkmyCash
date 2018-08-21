package cashcheck.skh.com.availablecash.Register.adapter.Normal

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.Modify.ModifyRegisterActivity
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvEndBinding

/**
 * Created by Seogki on 2018. 8. 13..
 */
class NormalEndRegisterViewHolder(itemView: View?, private var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


    var binding: ItemNormalRegisterRvEndBinding
    private var isExpend: Boolean = false
    private var db: DBHelper
    private var context: Context = itemView!!.context

    init {
        super.itemView
        db = DBHelper(context.applicationContext, "${Const.DbName}.db", null, 1)
        binding = DataBindingUtil.bind(itemView)
        binding.onClickListener = this
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.item_cate_const_1 -> {
                if (!isExpend) {
                    binding.itemCateConst2.visibility = View.VISIBLE
                    isExpend = true
                } else {
                    binding.itemCateConst2.visibility = View.GONE
                    isExpend = false
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

        AlertDialog.Builder(context, R.style.MyDialogTheme)
                .setMessage("정말로 지우시겠습니까?")
                .setPositiveButton("확인", { dialog, _ ->
                    dialog.dismiss()
                    binding.model?.id?.let { db.deleteData(it) }
                    onNormalRegisterDeleteListener.onCompleteDelete("done")
                    binding.itemCateConst2.visibility = View.GONE
                }).setNegativeButton("취소", { dialog, _ ->
                    dialog.dismiss()
                })
                .show()

    }


    private fun beginActivity() {
        val model = binding.model
        val i = Intent(context, ModifyRegisterActivity::class.java)
        i.putExtra(Const.ItemId, model.id)
        i.putExtra(Const.ItemCategory, model.category)
        i.putExtra(Const.ItemMoney, model.money)
        i.putExtra(Const.ItemDate, model.date)
        i.putExtra(Const.ItemDBNAME,Const.DbName)
        context.startActivity(i)
        binding.itemCateConst2.visibility = View.GONE

    }
}