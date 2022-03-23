package com.pg.justbalance

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment? ?: return
        val navController = host.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (isNightTheme) {
            Configuration.UI_MODE_NIGHT_YES -> {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.DarkTheme)
                supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.light_blue
                        )
                    )
                )
                supportActionBar?.setTitle("")
                bottomNav.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue))

            }

            Configuration.UI_MODE_NIGHT_NO -> {
            setTheme(R.style.LightTheme)
                supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.off_white
                        )
                    )
                )
                supportActionBar?.setTitle("")
                bottomNav.setBackgroundColor(ContextCompat.getColor(this,R.color.off_white))
        }
        }







        fun setupBottomNavMenu(navController: NavController) {
            bottomNav?.setupWithNavController(navController)
        }
        setupBottomNavMenu(navController)
    }
}