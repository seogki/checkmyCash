package cashcheck.skh.com.availablecash.Register.model

/**
 * Created by Seogki on 2018. 8. 20..
 */
class EndItem(val event: NormalRegisterModel) : ListItem() {
    override val type: Int
        get() = ListItem.TYPE_END
    override val header: String?
        get() = ""

}