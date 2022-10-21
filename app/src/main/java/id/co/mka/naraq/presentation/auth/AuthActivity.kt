package id.co.mka.naraq.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.naraq.R
import id.co.mka.naraq.databinding.ActivityAuthBinding
import id.co.mka.naraq.presentation.main.MainActivity

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NaraQ_NoActionBar)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (authViewModel.isChecked) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        authViewModel.session.observe(this@AuthActivity) {
                            if (it != null) {
                                if (it.isNotEmpty()) {
                                    Intent(this@AuthActivity, MainActivity::class.java).also { intent ->
                                        startActivity(intent)
                                        finish()
                                    }
                                    authViewModel.isChecked = true
                                } else if (it.isEmpty()) {
                                    authViewModel.isChecked = true
                                }
                                authViewModel.session.removeObservers(this@AuthActivity)
                            }
                        }
                        false
                    }
                }
            }
        )
    }
}
