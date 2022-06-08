package com.pg.justbalance

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (isNightTheme) {
            Configuration.UI_MODE_NIGHT_YES -> {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.DarkTheme)
                supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.main_blue
                        )
                    )
                )
                supportActionBar?.setTitle("")
              bottomNav.setBackgroundColor(ContextCompat.getColor(this,R.color.off_white))

            }

            Configuration.UI_MODE_NIGHT_NO -> {
            setTheme(R.style.LightTheme)
                supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.main_blue
                        )
                    )
                )
                supportActionBar?.setTitle("")
                supportActionBar?.elevation = 0f
                bottomNav.setBackgroundColor(ContextCompat.getColor(this,R.color.off_white))
        }
        }

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment? ?: return
        val navController = host.navController

        fun setupBottomNavMenu(navController: NavController) {
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav?.setupWithNavController(navController)
        }
        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.balanceFragment -> showBottomNav()
                R.id.settingsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

    }

    fun showBottomNav(){
        bottomNav.visibility = View.VISIBLE
    }

    fun hideBottomNav(){
        bottomNav.visibility = View.GONE
    }
}