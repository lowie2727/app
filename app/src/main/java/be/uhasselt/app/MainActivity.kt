package be.uhasselt.app

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.uhasselt.app.databinding.ActivityMainBinding
import be.uhasselt.app.net.LL2Request
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var request: LL2Request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonSnackbar.setOnClickListener { view ->
            msg("Goed op de knop gedrukt!", view)
        }

        request = LL2Request(this, binding.root)
        binding.buttonAPI.setOnClickListener(this::request)
        binding.buttonMap.setOnClickListener(this::maps)
        binding.buttonWelcome.setOnClickListener(this::welcome)
    }

    fun maps(view: View) {
        if (request.rocketLaunches.isNotEmpty()) {
            val latitude = request.rocketLaunches.get(0).latitude
            val longitude = request.rocketLaunches.get(0).longitude

            val callIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:launch location?q=$latitude,$longitude")
            )
            startActivity(callIntent)
        } else {
            msg("no rockets found", view)
        }
    }

    fun request(view: View) {
        request.load()
    }

    private fun welcome(view: View) {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
