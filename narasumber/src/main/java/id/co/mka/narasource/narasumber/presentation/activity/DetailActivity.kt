package id.co.mka.narasource.narasumber.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.co.mka.narasource.narasumber.databinding.ActivityNarasumberDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNarasumberDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNarasumberDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
