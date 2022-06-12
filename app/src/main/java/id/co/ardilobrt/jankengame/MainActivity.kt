package id.co.ardilobrt.jankengame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import id.co.ardilobrt.jankengame.databinding.ActivityMainBinding
import id.co.ardilobrt.jankengame.menu.MenuActivity
import id.co.ardilobrt.jankengame.onboarding.FirstOnBoardingFragment
import id.co.ardilobrt.jankengame.onboarding.LoginOnBoardingFragment
import id.co.ardilobrt.jankengame.onboarding.SecondOnBoardingFragment
import id.co.ardilobrt.jankengame.onboarding.ThirdOnBoardingFragment
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var contentHasLoaded = false

    // Step 2 Send data from activity to fragment -> make variable
    var listener: OnSendDataToFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make splash screen without layout & activity
        // https://developer.android.com/guide/topics/ui/splash-screen/migrate
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Make timer for splash screen
        // https://www.raywenderlich.com/32555180-splash-screen-tutorial-for-android
        Timer().schedule(1000) {
            contentHasLoaded = true
        }
        setupSplashScreen()

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        // Make dots indicator using library https://github.com/tommybuonomo/dotsindicator
        binding.dotsIndicator.attachTo(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.i(MainActivity::class.java.simpleName, "Current Fragment: $position")
                binding.tvNext.text = if (position == 3) "Play" else "Next"
            }
        })

        binding.tvNext.setOnClickListener {
            if (binding.viewPager.currentItem == 3) {
                val intentMenu = Intent(this, MenuActivity::class.java)
                // Step 6 Send data from activity to fragment -> call method and send
                listener?.goToMenuActivity(intentMenu)
            }
            binding.viewPager.currentItem += 1
        }
    }

    // https://www.raywenderlich.com/32555180-splash-screen-tutorial-for-android
    private fun setupSplashScreen() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (contentHasLoaded) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )
    }

    private inner class ViewPagerAdapter(fm: FragmentManager, lf: Lifecycle) :
        FragmentStateAdapter(fm, lf) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> FirstOnBoardingFragment()
            1 -> SecondOnBoardingFragment()
            2 -> ThirdOnBoardingFragment()
            else -> LoginOnBoardingFragment()
        }
    }

    // Step 1 Send data from activity to fragment -> make interface
    interface OnSendDataToFragment {
        fun goToMenuActivity(intent: Intent)
    }
}