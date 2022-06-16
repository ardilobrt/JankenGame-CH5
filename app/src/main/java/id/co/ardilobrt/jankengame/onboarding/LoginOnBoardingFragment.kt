package id.co.ardilobrt.jankengame.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.co.ardilobrt.jankengame.MainActivity
import id.co.ardilobrt.jankengame.R
import id.co.ardilobrt.jankengame.databinding.FragmentLoginOnBoardingBinding
import id.co.ardilobrt.jankengame.model.Constant
import id.co.ardilobrt.jankengame.model.Player

// Step 3 Send data from activity to fragment -> implement interface
class LoginOnBoardingFragment : Fragment(), MainActivity.OnSendDataToFragment {
    private var _binding: FragmentLoginOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginOnBoardingBinding.inflate(inflater, container, false)

        // Step 5 Send data from activity to fragment -> fill variable listener from activity
        (activity as MainActivity).listener = this

        return binding.root
    }

    // Step 4 Send data from activity to fragment -> override function from activity
    override fun goToMenuActivity(intent: Intent) {
        val userInput = binding.edtUser.text.toString()
        if (userInput.isNotEmpty()) {
            logD("User = $userInput")
            val player = Player(userInput)
            // Make variable extra constant to avoid human error (typo)
            intent.putExtra(Constant.EXTRA_PLAYER, player)
            startActivity(intent)
            (activity as MainActivity).finish()
        } else {
            logD("Input is Empty")
            binding.edtUser.error = resources.getString(R.string.input_error)
            binding.edtUser.requestFocus()
        }
    }

    private fun logD(message: String) {
        Log.i(LoginOnBoardingFragment::class.java.simpleName, message)
    }

    override fun onResume() {
        super.onResume()
        logD("onResume")
    }

    override fun onPause() {
        super.onPause()
        logD("onPause")
        binding.edtUser.text?.clear()
        binding.edtUser.error = null
    }

    // https://developer.android.com/topic/libraries/view-binding/migration#kts
    override fun onDestroyView() {
        super.onDestroyView()
        logD("onDestroy")
        _binding = null
    }
}
