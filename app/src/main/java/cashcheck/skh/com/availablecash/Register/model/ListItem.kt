package cashcheck.skh.com.availablecash.Register.model

/**
 * Created by Seogki on 2018. 8. 13..
 */
abstract class ListItem {

    abstract val type: Int

    companion object {

        val TYPE_HEADER = 0
        val TYPE_EVENT = 1
    }
}
