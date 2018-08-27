package cashcheck.skh.com.availablecash.Register.tab.Estimate


import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cashcheck.skh.com.availablecash.Base.BaseFragment
import cashcheck.skh.com.availablecash.R
import cashcheck.skh.com.availablecash.Register.Interface.OnNormalRegisterDeleteListener
import cashcheck.skh.com.availablecash.Register.adapter.Estimate.EstimateRegisterAdapter
import cashcheck.skh.com.availablecash.Register.model.EstimateRegisterModel
import cashcheck.skh.com.availablecash.Util.*
import cashcheck.skh.com.availablecash.databinding.FragmentEstimateRegisterBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


/**
 * A simple [Fragment] subclass.
 */
class EstimateRegisterFragment : BaseFragment(), View.OnClickListener, OnNormalRegisterDeleteListener, TextView.OnEditorActionListener {


    lateinit var binding: FragmentEstimateRegisterBinding
    private lateinit var map: HashMap<String, String>
    private lateinit var tempMap: HashMap<String, String>
    private lateinit var estimateRegisterAdapter: EstimateRegisterAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mItems: ArrayList<EstimateRegisterModel>
    private var totalUsage: Int = 0
    private lateinit var db: EstimateDBHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_estimate_register, container, false)
        binding.onClickListener = this
        binding.estimateEditCate.setOnEditorActionListener(this)
        binding.estimateEditMoney.setOnEditorActionListener(this)
        binding.estimateEditDays.setOnEditorActionListener(this)
        binding.estimateFragPiechart.setNoDataText("데이터가 존재하지 않습니다.")
        tempMap = HashMap()
        db = EstimateDBHelper(context!!.applicationContext, "${Const.DbEstimateName}.db", null, 1)
        setRv()
        getDbData()
        getEditText()
        return binding.root
    }

    override fun onCompleteDelete(done: String?) {
        if (done == "done")
            getDbData()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            when(v?.id){
                R.id.estimate_edit_cate -> {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        binding.estimateEditMoney.requestFocus()
                    }
                }
                R.id.estimate_edit_money -> {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        binding.estimateEditDays.requestFocus()
                    }
                }
                R.id.estimate_edit_days -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        savePreference()
                    }
                }
            }
        return false
    }


    private fun getDbData() {
        val database = db.readableDatabase
        map = HashMap()
        totalUsage = 0
        mItems = ArrayList()
        var cursor: Cursor? = null
        try {
            cursor = database.rawQuery("SELECT * FROM ${Const.DbEstimateName} ORDER BY date DESC", null)
            while (cursor.moveToNext()) {
                val num = cursor.getString(0)
                val date = cursor.getString(1)
                val cate = cursor.getString(2)
                val money = cursor.getString(3)
                val days = cursor.getString(4)

                totalUsage += money.toInt().times(days.toInt())

                mItems.add(EstimateRegisterModel(num, date, cate, money.toInt().times(days.toInt()).toString(),days))

                if (map.containsKey(cate)) {
                    val data = map.getValue(cate).toInt().plus(money.toInt().times(days.toInt()))
                    map.remove(cate)
                    map[cate] = data.toString()
                } else {
                    map[cate] = money.toInt().times(days.toInt()).toString()
                }
            }

            if (map.size > 0) {
                binding.estimateTxtEmpty.visibility = View.GONE
                checkTempMap()
            } else {
                setPieChart()
                totalUsage = 0
                binding.estimateFragPiechart.centerText =""
                saveTotalInSharedPreference()
                estimateRegisterAdapter.clearItems()
                binding.estimateTxtEmpty.visibility = View.VISIBLE
            }


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

    }

    private fun checkTempMap() {
        if (tempMap != map) {
            tempMap = map
//            mItems.add(EstimateRegisterModel("last", UtilMethod.getCurrentDateToSec(), "합계", totalUsage.toString()))
            saveTotalInSharedPreference()
            setPieChart()
        } else {

        }
    }

    private fun saveTotalInSharedPreference() {
        val pref = activity?.getSharedPreferences(Const.PreferenceTitle, MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putInt(Const.EstimateUsage, totalUsage)
        editor?.apply()

    }

    private fun setRvData() {
        if (mItems.size > 0) {
            estimateRegisterAdapter.clearItems()
            estimateRegisterAdapter.notifyDataSetChanged()
            estimateRegisterAdapter.addItems(mItems)
        }
    }

    private fun setRv() {
        estimateRegisterAdapter = EstimateRegisterAdapter(context!!, ArrayList())
        estimateRegisterAdapter.onNormalRegisterDeleteListener = this
        layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.isItemPrefetchEnabled = true
        layoutManager.initialPrefetchItemCount = 4
        binding.estimateFragRv.setHasFixedSize(true)
        estimateRegisterAdapter.setHasStableIds(true)
        binding.estimateFragRv.layoutManager = layoutManager
        binding.estimateFragRv.isDrawingCacheEnabled = true
        binding.estimateFragRv.setItemViewCacheSize(20)
        binding.estimateFragRv.setHasFixedSize(true)
        binding.estimateFragRv.isNestedScrollingEnabled = false
        binding.estimateFragRv.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.estimateFragRv.itemAnimator = null

        Thread(Runnable {
            binding.estimateFragRv.adapter = estimateRegisterAdapter
        }).start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.estimate_btn_regi -> {
                binding.estimateBtnRegi.isEnabled = false
                savePreference()
            }
        }
    }

    private fun setPieChart() {
        val chart = binding.estimateFragPiechart
        chart.setUsePercentValues(true)

        val yvalues = mutableListOf<PieEntry>()

        for ((key, value) in map) {
            yvalues.add(PieEntry(value.toFloat(), key))
        }
        val dataSet = PieDataSet(yvalues, "")

        dataSet.sliceSpace = 1F
        dataSet.selectionShift = 5F
        dataSet.valueTextSize = 9f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 100f
        dataSet.valueLinePart1Length = 0.3f
        dataSet.valueLinePart2Length = 0.1f
        dataSet.isValueLineVariableLength = true
        dataSet.valueFormatter = CustomPercentFormatter()
        chart.isRotationEnabled = false
        chart.setTouchEnabled(false)
        chart.setUsePercentValues(true)
        chart.setEntryLabelColor(ContextCompat.getColor(context!!, R.color.black))
        chart.setEntryLabelTextSize(9F)
        chart.holeRadius = 70f
        chart.setHoleColor( Color.argb(0,0,0,0))
        chart.setExtraOffsets(25F, 15F, 25F, 15F)
        chart.legend.isWordWrapEnabled = true
        chart.setCenterTextColor(ContextCompat.getColor(context!!,R.color.statusbar))
        chart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        if(totalUsage != 0) {
            chart.centerText = UtilMethod.currencyFormat(totalUsage.toString()) + "원"
        } else {
            chart.centerText = ""
        }
        chart.setCenterTextSize(18F)
        val colors = mutableListOf<Int>()
        colors.add(ContextCompat.getColor(context!!, R.color.lightYellow))
        colors.add(ContextCompat.getColor(context!!, R.color.lightBlue))
        colors.add(ContextCompat.getColor(context!!, R.color.lightOrange))
        colors.add(ContextCompat.getColor(context!!, R.color.lightRed))

        dataSet.colors = colors
        dataSet.label = ""
        val data = PieData(dataSet)

        chart.legend.isEnabled = false

        chart.data = data
        chart.description.isEnabled = false
        chart.animateXY(1000, 1000)
        chart.invalidate()
        setRvData()
    }


    private fun getEditText() {

        setTextWatcher(binding.estimateEditMoney)
        setTextWatcherDay(binding.estimateEditDays)
    }


    private fun setTextWatcher(et: EditText) {
        et.addTextChangedListener(CustomTextWatcher(et))
    }

    private fun setTextWatcherDay(et: EditText) {
        et.addTextChangedListener(CustomTextWatcherDay(et))
    }


    private fun savePreference() {
        if (binding.estimateEditCate.text.toString() != ""
                && binding.estimateEditMoney.text.toString().length > 1
                && binding.estimateEditDays.text.toString() != ""
                && binding.estimateEditDays.text.toString() != "일") {
            try {
                val cate = binding.estimateEditCate.text.toString()
                val money = binding.estimateEditMoney.text.toString().replace(",".toRegex(), "").replace("원", "")
                val days = binding.estimateEditDays.text.toString().replace("일", "")
                db.insertData(UtilMethod.getCurrentDateToSec(), cate, money, days)

                AlertDialog.Builder(context!!, R.style.MyDialogTheme)
                        .setMessage("등록 되었습니다.")
                        .setPositiveButton("확인", { dialog, _ ->
                            dialog.dismiss()
                            binding.estimateEditCate.text.clear()
                            binding.estimateEditMoney.text.clear()
                            binding.estimateEditDays.text.clear()
                            getDbData()
                            binding.estimateBtnRegi.isEnabled = true
                        }).setNegativeButton(null, null)
                        .show()
            } catch (e: Exception) {
                alertDialog(context!!, "다시 시도해주시기 바랍니다.")
                binding.estimateBtnRegi.isEnabled = true
            }
        } else {
            Toast.makeText(context, "모두 채워주세요", Toast.LENGTH_SHORT).show()
            binding.estimateBtnRegi.isEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
        getDbData()
    }

}// Required empty public constructor
