package id.co.mka.narasource.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
        setupBottomNavigation()
    }

    private fun setupAction() {
        binding.fabFind.setOnClickListener {
            Toast.makeText(this, "Find Narasumber", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_dash) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.navView

        navView.setupWithNavController(navController)
        navView.background = null
        navView.menu.getItem(1).isEnabled = false
    }
}
    