package com.appdev.kisanmitra.ui.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.appdev.kisanmitra.R
import com.appdev.kisanmitra.databinding.ActivityMainBinding
import com.appdev.kisanmitra.ui.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fix toolbar under status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.topAppBar) { view, insets ->
            val statusBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.updatePadding(top = statusBarHeight)
            insets
        }

        // Setup Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        // Profile icon click
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menu_profile) {
                showProfileMenu(binding.topAppBar)
                true
            } else {
                false
            }
        }
    }

    private fun showProfileMenu(anchorView: View) {

        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.layout_profile_dropdown, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.elevation = 20f

        val currentUser = FirebaseAuth.getInstance().currentUser
        val username = currentUser?.displayName ?: "User"

        val tvUsername = popupView.findViewById<TextView>(R.id.tvUsername)
        val btnLogout = popupView.findViewById<Button>(R.id.btnLogout)

        tvUsername.text = "Hi, $username 👋"

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            popupWindow.dismiss()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        //popup width before showing
        popupView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )

        val popupWidth = popupView.measuredWidth
        val xOffset = anchorView.width - popupWidth - 16

        popupWindow.showAsDropDown(anchorView, xOffset, 20)
    }
}