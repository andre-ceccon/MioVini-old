package br.com.miovini.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import br.com.miovini.R
import br.com.miovini.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var navigationMenu: BottomNavigationView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navigationMenu = binding.navView
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_activity_main
        ) as NavHostFragment

        binding.topAppBar.setupWithNavController(
            navHostFragment.navController,
            AppBarConfiguration(setOf(R.id.navigation_home))
        )


        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Log.d("Testando", "Home")
                    true
                }
                R.id.nav_search -> {
                    Log.d("Testando", "Search")
                    true
                }
                R.id.nav_add -> {
                    Log.d("Testando", "Add")
                    true
                }
                R.id.nav_settings -> {
                    Log.d("Testando", "Settings")
                    true
                }
                else -> false
            }
        }
    }
}