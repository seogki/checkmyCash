package cashcheck.skh.com.availablecash.Setting.Dialog

import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Setting.ExceltoDBListener
import cashcheck.skh.com.availablecash.databinding.ItemExcelDbDialogBinding


/**
 * Created by Seogki on 2018. 8. 7..
 */
class ExceltoDBDialogV2(context: Context, private var listener: ExceltoDBListener) : Dialog(context), View.OnClickListener, TextView.OnEditorActionListener {


    lateinit var binding: ItemExcelDbDialogBinding
    private var mContext: Context = context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_excel_db_dialog, null, false)
        setContentView(binding.root)
        binding.onClickListener = this
        binding.excelDbEditDate.setOnEditorActionListener(this)
        binding.excelDbTxtTitle.text = "경로: ${Environment.getExternalStorageDirectory()}/\n해당 경로에 파일을 넣어주시고\n우리가계부_************.xls\n *표 12자리 날짜만 적어주세요"
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.excel_db_btn_done -> {
                if (binding.excelDbEditDate.text.toString().length == 12) {
                    listener.ExcelData("${Environment.getExternalStorageDirectory()}/우리가계부_${binding.excelDbEditDate.text}.xls")
                    dismiss()
                } else {
                    Toast.makeText(context!!, "12자리를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            return if (binding.excelDbEditDate.text.toString().length == 12) {
                listener.ExcelData("${Environment.getExternalStorageDirectory()}/우리가계부_${binding.excelDbEditDate.text}.xls")
                dismiss()
                true
            } else {
                Toast.makeText(context!!, "12자리를 입력해주세요", Toast.LENGTH_SHORT).show()
                true
            }
        }
        return false
    }


}