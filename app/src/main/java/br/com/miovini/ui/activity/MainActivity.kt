package br.com.miovini.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import br.com.miovini.R
import br.com.miovini.databinding.ActivityMainBinding
import com.blankj.utilcode.util.KeyboardUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        initNavigation()

        KeyboardUtils.registerSoftInputChangedListener(this) { height ->
            binding.bottomNavigationView.visibility = if (height > 0) View.GONE else View.VISIBLE
        }
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.registrationFragment)
        )

        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment, R.id.purshasedFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
//            if(destination.id == R.id.full_screen_destination) {
//                toolbar.visibility = View.GONE
//                bottomNavigationView.visibility = View.GONE
//            } else {
//                toolbar.visibility = View.VISIBLE
//                bottomNavigationView.visibility = View.VISIBLE
//            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}