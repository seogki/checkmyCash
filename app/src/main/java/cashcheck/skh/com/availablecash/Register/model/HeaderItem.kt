package cashcheck.skh.com.availablecash.Register.model

/**
 * Created by Seogki on 2018. 8. 13..
 */
class HeaderItem(var date: String) : ListItem() {

    override val header: String
        get() = date

    // here getters and setters
    // for title and so on, built
    // using date

    override val type: Int
        get() = ListItem.TYPE_HEADER
}

