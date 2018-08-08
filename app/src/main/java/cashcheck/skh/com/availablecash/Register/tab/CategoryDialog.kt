package cashcheck.skh.com.availablecash.Register.tab

import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.CategoryDialogInterface
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.databinding.ItemCategoryDialogBinding

/**
 * Created by Seogki on 2018. 8. 7..
 */
class CategoryDialog(context: Context, private var listener: CategoryDialogInterface) : Dialog(context), View.OnClickListener {

    lateinit var binding: ItemCategoryDialogBinding
    private var mcontext: Context = context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        binding = DataBindingUtil.inflate(LayoutInflater.from(mcontext), R.layout.item_category_dialog, null, false)
        setContentView(binding.root)
        binding.onClickListener = this
        DLog.e("owner : $ownerActivity")
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.catdlog_btn_no -> {
                dismiss()
            }
            R.id.catdlog_btn_ok -> {
                if (binding.catdlogEditAdd.text.toString().isNotEmpty()) {
                    listener.getTextFromDialog(binding.catdlogEditAdd.text.toString())
                    dismiss()
                } else {
                    Toast.makeText(context, "선택하거나 추가 해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.catdlog_txt_adjust -> {
                listener.getTextFromDialog(binding.catdlogTxtAdjust.text.toString())
                dismiss()
            }
            R.id.catdlog_txt_cafe -> {
                listener.getTextFromDialog(binding.catdlogTxtCafe.text.toString())
                dismiss()
            }
            R.id.catdlog_txt_convi -> {
                listener.getTextFromDialog(binding.catdlogTxtConvi.text.toString())
                dismiss()
            }
            R.id.catdlog_txt_gas -> {
                listener.getTextFromDialog(binding.catdlogTxtGas.text.toString())
                dismiss()
            }
            R.id.catdlog_txt_house -> {
                listener.getTextFromDialog(binding.catdlogTxtHouse.text.toString())
                dismiss()
            }
            R.id.catdlog_txt_mart -> {
                listener.getTextFromDialog(binding.catdlogTxtMart.text.toString())
                dismiss()
            }
            R.id.catdlog_txt_phonebill -> {
                listener.getTextFromDialog(binding.catdlogTxtPhonebill.text.toString())
                dismiss()
            }
            R.id.catdlog_txt_trans -> {
                listener.getTextFromDialog(binding.catdlogTxtTrans.text.toString())
                dismiss()
            }
        }
    }

}