package id.co.ardilobrt.jankengame.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.co.ardilobrt.jankengame.R
import id.co.ardilobrt.jankengame.databinding.FragmentScreenOnBoardingBinding

class FirstOnBoardingFragment : Fragment() {
    // https://developer.android.com/topic/libraries/view-binding/migration#kts
    private var _binding: FragmentScreenOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentScreenOnBoardingBinding.inflate(inflater, container, false)

        binding.ivOnBoarding.setImageResource(R.drawable.ic_onboarding_1)
        binding.tvTitle.text = resources.getString(R.string.title_janken_game)
        binding.ivDescription.text = resources.getString(R.string.desc_janken_game)

        return binding.root
    }

    // https://developer.android.com/topic/libraries/view-binding/migration#kts
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}