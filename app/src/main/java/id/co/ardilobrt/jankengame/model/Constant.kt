package id.co.ardilobrt.jankengame.model


import android.content.Intent

object Constant {
   // To avoid human error (typo)
   const val EXTRA_PLAYER = "extra_player"

   // Avoid double code
   fun getParcelable(intent: Intent): Player {
      return intent.getParcelableExtra<Player>(EXTRA_PLAYER) as Player
   }
}