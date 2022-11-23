package id.co.mka.narasource.presentation.findNarasumber

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.databinding.ActivityFindNarasumberBinding

@AndroidEntryPoint
class FindNarasumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindNarasumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindNarasumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
