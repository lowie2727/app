package be.uhasselt.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.uhasselt.app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonSnackbar.setOnClickListener { view ->
            msg("Goed op de knop gedrukt!", view)
        }

        binding.buttonIntent.setOnClickListener(this::intent)
    }

    private fun intent(view: View) {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
