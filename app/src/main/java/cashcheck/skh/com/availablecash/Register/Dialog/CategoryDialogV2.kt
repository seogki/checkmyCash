package cashcheck.skh.com.availablecash.Register.Dialog

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.CategoryDialogInterface
import cashcheck.skh.com.availablecash.Register.Interface.DialogInterface
import cashcheck.skh.com.availablecash.Util.Const
import cashcheck.skh.com.availablecash.Util.DLog
import cashcheck.skh.com.availablecash.Util.DialogDBHelper
import cashcheck.skh.com.availablecash.databinding.ItemCategoryDialogv2Binding


/**
 * Created by Seogki on 2018. 8. 7..
 */
class CategoryDialogV2(context: Context, private var listener: CategoryDialogInterface) : Dialog(context), View.OnClickListener, DialogInterface, TextView.OnEditorActionListener {


    lateinit var binding: ItemCategoryDialogv2Binding
    private var mContext: Context = context
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var mItem: ArrayList<String>
    private lateinit var dialogDBHelper: DialogDBHelper
    private lateinit var categoryDialogAdapter: CategoryDialogAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_category_dialogv2, null, false)
        binding.catdlogEditAdd.setOnEditorActionListener(this)
        dialogDBHelper = DialogDBHelper(mContext.applicationContext, "${Const.DbDialog}.db", null, 1)
        mItem = ArrayList()
        setItem()
        setContentView(binding.root)
        setRv()
        binding.onClickListener = this
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            return if (binding.catdlogEditAdd.text.toString() != "") {
                onSaveDbAndEnd(binding.catdlogEditAdd.text.toString())
                true
            } else {
                Toast.makeText(mContext, "모두 채워주세요", Toast.LENGTH_SHORT).show()
                true
            }
        }
        return false
    }

    private fun setItem() {


        val pref = mContext.getSharedPreferences(Const.prefDB, MODE_PRIVATE)

        val isSet = pref.getBoolean(Const.DbDialog, false)
        if (isSet) {
            return
        } else {
            firstSetDB("식비")
            firstSetDB("교통비")
            firstSetDB("관리")
            firstSetDB("통신")
            firstSetDB("마트")
            firstSetDB("편의점")
            firstSetDB("카페")
            firstSetDB("주거")
            firstSetDB("주유")
            val editor = pref.edit()
            editor.putBoolean(Const.DbDialog, true)
            editor.apply()
        }
    }

    private fun firstSetDB(text: String) {
        dialogDBHelper.insertData(text)
    }

    override fun getPosFromAdapter(pos: Int) {
        try {
            val data = categoryDialogAdapter.getItem(pos)
            AlertDialog.Builder(context, R.style.MyDialogTheme)
                    .setMessage("정말로 지우시겠습니까?")
                    .setPositiveButton("확인", { dialog, _ ->
                        dialogDBHelper.deleteData(data!!)
                        getDb()
                        dialog.dismiss()

                    }).setNegativeButton("취소", { dialog, _ ->
                        dialog.dismiss()
                    })
                    .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getTextFromAdapter(text: String?) {
        if (text != null) {
            onSaveDbAndEnd(text)
        }
    }


    private fun setRv() {
        layoutManager = GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        binding.catdlogRv.layoutManager = layoutManager
        categoryDialogAdapter = CategoryDialogAdapter(mContext, mItem)
        categoryDialogAdapter.categoryDialogInterface = this
        binding.catdlogRv.setHasFixedSize(true)
        Handler().postDelayed({
            binding.catdlogRv.adapter = categoryDialogAdapter
            getDb()
        }, 10)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.catdlog_btn_no -> {
                dismiss()
            }
            R.id.catdlog_btn_ok -> {
                val data = binding.catdlogEditAdd.text.toString()
                if (data.isNotEmpty()) {
                    onSaveDbAndEnd(data)
                } else {
                    Toast.makeText(context, "선택하거나 추가 해주세요!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun onSaveDbAndEnd(data: String) {
        insertDb(data)
        listener.getTextFromDialog(data)
        dismiss()
    }


    private fun insertDb(text: String) {
        val db = dialogDBHelper.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT categories FROM ${Const.DbDialog} ORDER BY _id DESC", null)
            while (cursor.moveToNext()) {
                if (text == cursor.getString(0)) {
                    cursor?.close()
                    return
                }
            }
            dialogDBHelper.insertData(text)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    private fun getDb() {
        mItem = ArrayList()
        val db = dialogDBHelper.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT categories FROM ${Const.DbDialog} ORDER BY _id DESC", null)
            while (cursor.moveToNext()) {
                mItem.add(cursor.getString(0))
            }
            DLog.e("mitem ${mItem.toList()}")
            if (mItem.size > 0) {
                categoryDialogAdapter.clearItems()
                categoryDialogAdapter.addItems(mItem)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {

            cursor?.close()
        }
    }

}