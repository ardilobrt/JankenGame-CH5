package id.co.ardilobrt.jankengame.game.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.co.ardilobrt.jankengame.databinding.FragmentDialogBinding

class CustomDialogFragment : DialogFragment() {
    // Step 2 Send data from fragment to activity -> make variable
    private var listener: OnDialogButtonListener? = null
    // View binding
    private var _binding: FragmentDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make background dialog with radius
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Get result from activity
        val bundle = arguments
        val message = bundle!!.getString("result")
        Log.i(CustomDialogFragment::class.java.simpleName, message.toString())
        binding.tvGameResult.text = message

        binding.btnPlay.setOnClickListener {
            // Step 6 Send data from fragment to activity -> call method and send
            listener?.refreshGame()
            dialog?.dismiss()
        }

        binding.btnMenu.setOnClickListener {
            // Step 6 Send data from fragment to activity -> call method and send
            listener?.backToMenu()
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(CustomDialogFragment::class.java.simpleName, "onAttach")
        // Step 5 Send data from fragment to activity -> init variable listener
        if (context is OnDialogButtonListener) {
            listener = context
        }
    }

    // Step 1 Send data from fragment to activity -> make interface
    interface OnDialogButtonListener {
        fun refreshGame()
        fun backToMenu()
    }
}