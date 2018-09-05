package cashcheck.skh.com.availablecash.Register.adapter.Normal

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.Modify.ModifyRegisterActivity
import cashcheck.skh.com.availablecash.Register.model.NormalRegisterModel
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DBHelper
import cashcheck.skh.com.availablecash.databinding.ItemNormalRegisterRvBinding

/**
 * Created by Seogki on 2018. 8. 13..
 */
class NormalRegisterViewHolder(val binding: ItemNormalRegisterRvBinding, private var onNormalRegisterDeleteListener: OnNormalRegisterDeleteListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private var isExpend: Boolean = false
    private var db: DBHelper? = null
    private var context: Context = itemView!!.context

    init {
        super.itemView

    }

    fun bind(model: NormalRegisterModel?){

        binding.model = model
        db = DBHelper(context.applicationContext, "${Const.DbName}.db", null, 1)
        binding.onClickListener = this
        binding.executePendingBindings()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.item_cate_main_const -> {
                if (!isExpend) {
                    binding.itemCateSubConst.visibility = View.VISIBLE
                    isExpend = true
                } else {
                    binding.itemCateSubConst.visibility = View.GONE
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

        AlertDialog.Builder(context, R.style.MyDialogTheme)
                .setTitle("데이터 삭제")
                .setMessage("정말로 지우시겠습니까?")
                .setPositiveButton("확인", { dialog, _ ->
                    dialog.dismiss()
                    binding.model?.id?.let { db?.deleteData(it) }
                    onNormalRegisterDeleteListener.onCompleteDelete("done")
                    binding.itemCateSubConst.visibility = View.GONE
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
        i.putExtra(Const.ItemId, model?.id)
        i.putExtra(Const.ItemCategory, model?.category)
        i.putExtra(Const.ItemMoney, model?.money)
        i.putExtra(Const.ItemDate, model?.date)
        i.putExtra(Const.ItemDBNAME, Const.DbName)
        context.startActivity(i)
        binding.itemCateSubConst.visibility = View.GONE
    }
}