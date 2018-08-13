package cashcheck.skh.com.availablecash.Register.model

/**
 * Created by Seogki on 2018. 8. 13..
 */
class HeaderItem(val date: String) : ListItem() {

    // here getters and setters
    // for title and so on, built
    // using date

    override val type: Int
        get() = ListItem.TYPE_HEADER

}

