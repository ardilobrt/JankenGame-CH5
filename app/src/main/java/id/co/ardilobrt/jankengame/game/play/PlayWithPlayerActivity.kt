package id.co.ardilobrt.jankengame.game.play

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.co.ardilobrt.jankengame.R
import id.co.ardilobrt.jankengame.databinding.ActivityPlayBinding
import id.co.ardilobrt.jankengame.game.controller.Controller
import id.co.ardilobrt.jankengame.game.dialog.CustomDialogFragment
import id.co.ardilobrt.jankengame.model.Player

// Step 3 Send data from fragment to activity -> implement interface
class PlayWithPlayerActivity : AppCompatActivity(), CustomDialogFragment.OnDialogButtonListener {

    private lateinit var binding: ActivityPlayBinding
    private lateinit var player1: Player
    private lateinit var player2: Player
    private val handler = Handler(Looper.getMainLooper())
    private var playerClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Glide - load image from URL with internet permission
        Glide.with(this)
            .load("https://i.ibb.co/wYXbtX1/image-head-layout.png")
            .into(binding.ivHead)

        player1 = intent.getParcelableExtra<Player>("EXTRA_PLAYER") as Player
        logD(getString(R.string.menu_vs_player, player1.name))

        showPlayer1()

        binding.ivRefresh.setOnClickListener {
            logD("Button Refresh Clicked")
            refreshGame()
        }
        binding.ivClose.setOnClickListener {
            backToMenu()
        }
    }

    private fun showPlayer1() {
        playerClick = 1
        binding.tvMessage.text = resources.getString(R.string.pick_hand, player1.name.uppercase())
        setOnClickPlayer(binding.ivRockP1, 1)
        setOnClickPlayer(binding.ivPaperP1, 2)
        setOnClickPlayer(binding.ivScissorP1, 3)
    }

    private fun setOnClickPlayer(view: ImageView, idView: Int) {
        view.setOnClickListener {
            when (playerClick) {
                1 -> setPlayer1(view, idView)
                2 -> setPlayer2(view, idView)
            }
        }
    }

    private fun setPlayer1(view: ImageView, idView: Int) {
        setHandBackground(view, true)
        setHandPlayer1Enabled(false)

        player1.handId = idView
        logD(player1.showMessage())
        setToast(player1.showMessage())

        // Make a waiting time until player 1 hide & player 2 can click hand after 1 seconds
        binding.tvMessage.text = getString(R.string.wait)
        binding.ivRefresh.visibility = View.INVISIBLE
        handler.postDelayed({
            setPlayer1Hidden(true)
            showPlayer2()
            binding.ivRefresh.visibility = View.VISIBLE
        }, 1000)
    }

    private fun setHandBackground(view: ImageView, selected: Boolean) {
        if (selected) {
            view.setBackgroundResource(R.drawable.ic_hand_background)
        } else view.setBackgroundResource(0)
    }

    private fun setHandPlayer1Enabled(enable: Boolean) {
        binding.ivRockP1.isEnabled = enable
        binding.ivPaperP1.isEnabled = enable
        binding.ivScissorP1.isEnabled = enable
    }

    private fun setPlayer1Hidden(hidden: Boolean) {
        if (hidden) {
            binding.layoutP1.setBackgroundResource(R.drawable.ic_hand_background)
        } else binding.layoutP1.setBackgroundResource(0)
    }

    private fun showPlayer2() {
        playerClick = 2
        player2 = Player("Player 2")
        binding.tvMessage.text = getString(R.string.pick_hand, player2.name.uppercase())
        setHandPlayer2Enabled(true)
        setOnClickPlayer(binding.ivRockP2, 1)
        setOnClickPlayer(binding.ivPaperP2, 2)
        setOnClickPlayer(binding.ivScissorP2, 3)
    }

    private fun setHandPlayer2Enabled(enable: Boolean) {
        binding.ivRockP2.isEnabled = enable
        binding.ivPaperP2.isEnabled = enable
        binding.ivScissorP2.isEnabled = enable
    }

    private fun setPlayer2(view: ImageView, idView: Int) {
        setHandBackground(view, true)
        setHandPlayer2Enabled(false)

        player2.handId = idView
        logD(player2.showMessage())
        setToast(player2.showMessage())

        setPlayer1Hidden(false)
        startGame()
    }

    private fun startGame() {
        // Make object from class Controller and call function ruleGame
        val controller = Controller()
        val resultGame = controller.ruleGame(player1.handId, player2.handId)
        logD("Start Game = ${player1.handName} VS ${player2.handName}")

        val player1Name = player1.name.uppercase()
        val player2Name = player2.name.uppercase()
        when (resultGame) {
            player1.handId -> showDialogResult(getString(R.string.result_win, player1Name))
            player2.handId -> showDialogResult(getString(R.string.result_win, player2Name))
            else -> showDialogResult(getString(R.string.result_draw))
        }
    }

    private fun showDialogResult(message: String) {
        logD("Result = $message")
        binding.tvMessage.text = getString(R.string.refresh_game)
        // Show result game in Dialog
        val dialogView = CustomDialogFragment()
        val bundle = Bundle()
        bundle.putString("result", message)
        dialogView.arguments = bundle
        dialogView.show(supportFragmentManager, CustomDialogFragment::class.java.simpleName)
    }

    // Step 4 Send data from fragment to activity -> override function from fragment
    override fun refreshGame() {
        setPlayer1Hidden(false)
        showPlayer1()
        clearViewPlayer1()
        clearViewPlayer2()
    }

    private fun clearViewPlayer1() {
        setHandPlayer1Enabled(true)
        setHandBackground(binding.ivRockP1, false)
        setHandBackground(binding.ivPaperP1, false)
        setHandBackground(binding.ivScissorP1, false)
    }

    private fun clearViewPlayer2() {
        setHandPlayer2Enabled(false)
        setHandBackground(binding.ivRockP2, false)
        setHandBackground(binding.ivPaperP2, false)
        setHandBackground(binding.ivScissorP2, false)
    }

    // Step 4 Send data from fragment to activity -> override function from fragment
    override fun backToMenu() {
        this.onBackPressed()
    }

    private fun setToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logD(message: String) {
        Log.i(PlayWithPlayerActivity::class.java.simpleName, message)
    }
}