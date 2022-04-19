package be.uhasselt.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.uhasselt.app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.button.setText("test knop")

        binding.button.setOnClickListener { view ->
            Snackbar.make(view, "Nice, clicked a button", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}