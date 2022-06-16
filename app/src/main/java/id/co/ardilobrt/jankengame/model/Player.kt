package id.co.ardilobrt.jankengame.model


import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(val name: String, var opponent: Int = 0) : Parcelable {

    @IgnoredOnParcel
    var handId: Int = 0

    @IgnoredOnParcel
    lateinit var handName: String

    fun showMessage(): String {
        handName = when (handId) {
            1 -> "Rock"
            2 -> "Paper"
            3 -> "Scissor"
            else -> "None"
        }
        return "$name Choose $handName"
    }
}