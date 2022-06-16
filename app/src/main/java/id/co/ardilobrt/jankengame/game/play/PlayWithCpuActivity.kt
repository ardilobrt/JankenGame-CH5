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
class PlayWithCpuActivity : AppCompatActivity(), CustomDialogFragment.OnDialogButtonListener {

    private lateinit var binding: ActivityPlayBinding
    private lateinit var player1: Player
    private lateinit var player2: Player
    private val handler = Handler(Looper.getMainLooper())
    private var backClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Glide - load image from URL with internet permission
        Glide.with(this)
            .load("https://i.ibb.co/wYXbtX1/image-head-layout.png")
            .into(binding.ivHead)

        binding.ivPlayer2.setImageResource(R.drawable.image_cpu)

        player1 = intent.getParcelableExtra<Player>("EXTRA_PLAYER") as Player
        logD(getString(R.string.menu_vs_cpu, player1.name))

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
        binding.tvMessage.text = resources.getString(R.string.pick_hand, player1.name.uppercase())
        setOnClickPlayer(binding.ivRockP1, 1)
        setOnClickPlayer(binding.ivPaperP1, 2)
        setOnClickPlayer(binding.ivScissorP1, 3)
    }

    private fun setOnClickPlayer(view: ImageView, idView: Int) {
        view.setOnClickListener {
            setHandBackground(view, true)
            setHandEnabled(false)

            player1.handId = idView
            logD(player1.showMessage())
            setToast(player1.showMessage())

            binding.tvMessage.text = getString(R.string.wait)
            showAnimation()

            // Hold 2 seconds until animation finish
            binding.ivRefresh.visibility = View.INVISIBLE
            handler.postDelayed({
                if (backClick == 0) {
                    setPlayer2()
                    startGame()
                }
                binding.ivRefresh.visibility = View.VISIBLE
            }, 2000)
        }
    }

    private fun setHandBackground(view: ImageView, selected: Boolean) {
        if (selected) {
            view.setBackgroundResource(R.drawable.ic_hand_background)
        } else view.setBackgroundResource(0)
    }

    private fun setHandEnabled(enable: Boolean) {
        binding.ivRockP1.isEnabled = enable
        binding.ivPaperP1.isEnabled = enable
        binding.ivScissorP1.isEnabled = enable
    }

    // +1 untuk animasi cpu
    private fun showAnimation() {
        setHandBackground(binding.ivRockP2, true)
        handler.postDelayed({
            backgroundAnimation(binding.ivRockP2, binding.ivPaperP2)
            handler.postDelayed({
                backgroundAnimation(binding.ivPaperP2, binding.ivScissorP2)
                handler.postDelayed({
                    setHandBackground(binding.ivScissorP2, false)
                }, 500)
            }, 500)
        }, 500)
    }

    private fun backgroundAnimation(view1: ImageView, view2: ImageView) {
        setHandBackground(view1, false)
        setHandBackground(view2, true)
    }

    private fun setPlayer2() {
        player2 = Player("Player 2")
        player2.handId = (1..3).random()
        when (player2.handId) {
            1 -> setHandBackground(binding.ivRockP2, true)
            2 -> setHandBackground(binding.ivPaperP2, true)
            3 -> setHandBackground(binding.ivScissorP2, true)
        }
        logD(player2.showMessage())
        setToast(player2.showMessage())
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
        showPlayer1()
        clearViewPlayer1()
        clearViewPlayer2()
    }

    private fun clearViewPlayer1() {
        setHandEnabled(true)
        setHandBackground(binding.ivRockP1, false)
        setHandBackground(binding.ivPaperP1, false)
        setHandBackground(binding.ivScissorP1, false)
    }

    private fun clearViewPlayer2() {
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
        Log.i(PlayWithCpuActivity::class.java.simpleName, message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backClick = 1
    }
}