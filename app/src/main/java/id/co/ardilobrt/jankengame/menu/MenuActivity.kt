package id.co.ardilobrt.jankengame.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import id.co.ardilobrt.jankengame.R
import id.co.ardilobrt.jankengame.databinding.ActivityMenuBinding
import id.co.ardilobrt.jankengame.game.play.PlayWithCpuActivity
import id.co.ardilobrt.jankengame.model.Player
import id.co.ardilobrt.jankengame.game.play.PlayWithPlayerActivity

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var playerName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val player = intent.getParcelableExtra<Player>("EXTRA_PLAYER")
        playerName = player?.name.toString()

        showSnackBar()

        binding.tvVsPlayer.text = getString(R.string.menu_vs_player, playerName)
        binding.tvVsCpu.text = getString(R.string.menu_vs_cpu, playerName)

        binding.ivPlayer.setOnClickListener {
            logD("$playerName choose Menu VS Player")
            val intentVsPlayer = Intent(this, PlayWithPlayerActivity::class.java)
            putIntent(intentVsPlayer, player)
        }

        binding.ivCpu.setOnClickListener {
            logD("$playerName choose Menu VS CPU")
            val intentVsCPU = Intent(this, PlayWithCpuActivity::class.java)
            putIntent(intentVsCPU, player)
        }
    }

    private fun showSnackBar() {
        val snackBar =
            Snackbar.make(binding.root, "Welcome $playerName".uppercase(), Snackbar.LENGTH_SHORT)
        snackBar.setAction("CLOSE") {
            snackBar.dismiss()
        }
        snackBar.setActionTextColor(ContextCompat.getColor(applicationContext, R.color.brown_light))
        snackBar.show()
    }

    private fun putIntent(intent: Intent, player: Player?) {
        intent.putExtra("EXTRA_PLAYER", player)
        startActivity(intent)
    }

    private fun logD(message: String) {
        Log.i(MenuActivity::class.java.simpleName, message)
    }

    override fun onResume() {
        super.onResume()
        logD("OnResume")
    }

    override fun onPause() {
        super.onPause()
        logD("OnPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        logD("OnDestroy")
    }
}