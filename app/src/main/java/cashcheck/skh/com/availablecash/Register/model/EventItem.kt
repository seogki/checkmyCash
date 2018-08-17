package cashcheck.skh.com.availablecash.Register.model

/**
 * Created by Seogki on 2018. 8. 13..
 */
class EventItem(val event: NormalRegisterModel) : ListItem() {
    override val header: String?
        get() = ""

    // here getters and setters
    // for title and so on, built
    // using event

    override val type: Int
        get() = ListItem.TYPE_EVENT

}