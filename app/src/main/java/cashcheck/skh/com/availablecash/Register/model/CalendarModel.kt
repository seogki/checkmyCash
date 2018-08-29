package cashcheck.skh.com.availablecash.Register.model

/**
 * Created by Seogki on 2018. 8. 29..
 */
data class CalendarModel(var id: String? = null
                         , var date: String? = null
                         , var weekday: String? = null
                         , var money: String? = null
                         , var isBelow: Boolean? = null
                         , var isNow: Boolean? = null)