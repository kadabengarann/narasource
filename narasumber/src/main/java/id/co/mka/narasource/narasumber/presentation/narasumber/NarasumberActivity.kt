package id.co.mka.narasource.narasumber.presentation.narasumber

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mka.narasource.narasumber.databinding.ActivityNarasumberBinding

class NarasumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNarasumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNarasumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
