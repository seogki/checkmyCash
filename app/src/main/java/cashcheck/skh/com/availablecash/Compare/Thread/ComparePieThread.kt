package cashcheck.skh.com.availablecash.Compare.Thread

import cashcheck.skh.com.availablecash.Compare.Listener.ComparePieListener
import cashcheck.skh.com.availablecash.Compare.model.ComparePieModel

/**
 * Created by Seogki on 2018. 9. 6..
 */
class ComparePieThread(private val rvArray: ArrayList<ComparePieModel>,private val pieMap1: HashMap<String, String>?,private val pieMap2: HashMap<String, String>?,private val onComparePieListener: ComparePieListener) : Thread() {
    override fun run() {
        pieMap1?.let {
            for ((key, value) in it) {
                if (value.contains(".")) {
                    val data = value.replace(".", "").substring(0, value.length - 2)
                    rvArray.add(ComparePieModel(key, "" + data, "0", "" + data))
                } else {
                    rvArray.add(ComparePieModel(key, "" + value, "0", "" + value))
                }
            }
        }

        var isDiff = false
        var pieMapSize = pieMap2!!.size
        pieMap2.let {
            for (i in 0 until pieMapSize) {

                val key = pieMap2.toList()[i].first
                val value = pieMap2.toList()[i].second
                val data: String
                data = if (value.contains(".")) {
                    value.replace(".", "").substring(0, value.length - 2)
                } else {
                    value
                }
                for (x in 0 until rvArray.size) {
                    val cate = rvArray[x].cate
                    if (key == cate) {
                        val model = rvArray[x]
                        model.second = data
                        model.result = "" + model.first?.toInt()?.minus(data.toInt())
                        pieMapSize -= 1
                        rvArray.removeAt(x)
                        rvArray.add(model)
                        isDiff = false
                        break
                    } else {
                        isDiff = true
                    }
                }
                if (isDiff) {
                    rvArray.add(ComparePieModel(key, "0", value, "-$value"))
                    isDiff = false
                }
            }
        }


        var firstMoney = 0
        var secondMoney = 0

        for (models in rvArray) {
            val x: String = if (models.first!!.contains(".")) {
                models.first!!.replace(".", "").substring(0, models.first!!.length - 2)
            } else {
                models.first!!
            }
            val y: String = if (models.second!!.contains(".")) {
                models.second!!.replace(".", "").substring(0, models.second!!.length - 2)
            } else {
                models.second!!
            }
            firstMoney += x.toInt()
            secondMoney += y.toInt()
        }

        rvArray.add(ComparePieModel("합계", "" + firstMoney, "" + secondMoney, "" + firstMoney.minus(secondMoney)))

        onComparePieListener.getItem(rvArray)

    }
}