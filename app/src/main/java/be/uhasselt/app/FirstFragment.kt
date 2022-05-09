package be.uhasselt.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.databinding.FragmentFirstBinding
import be.uhasselt.app.model.MySharedData
import be.uhasselt.app.net.LL2Request
import com.google.android.material.snackbar.Snackbar

class FirstFragment : Fragment(R.layout.fragment_first) {
    private lateinit var binding: FragmentFirstBinding
    private lateinit var request: LL2Request
    private var data: MySharedData = MySharedData()

    // here the view should be set
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("Fragment: onCreateView")
        // this looks a lot like activities! Fragments have their own lifecycle.
        // return inflater.inflate(R.layout.fragment_first, container, false)
        // -- above should NOT be needed since we used a constructor argument!
        binding = FragmentFirstBinding.inflate(layoutInflater)

        request = LL2Request(context, binding.root)

        binding.buttonAPI.setOnClickListener(this::request)
        binding.buttonWelcome.setOnClickListener(this::welcome)
        binding.buttonMap.setOnClickListener(this::maps)
        binding.buttonSnackbar.setOnClickListener(this::snackbarMessage)
        binding.btnGoToNext.setOnClickListener(this::next)

        updateTextFromModel()
        binding.txtFragmentFirst.setOnClickListener(this::click)

        // remember to do this instead of super.onCreateView()
        // otherwise nothing will happen.
        return binding.root
    }

    private fun updateTextFromModel() {
        binding.txtFragmentFirst.text = "Fragment 1, model: ${data.age}"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("Fragment: onViewCreated")
    }

    // here the view should NOT be set: onCreateView() is called afterwards.
    // remember that accessing UI components here will crash!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Fragment: onCreate")
    }

    private fun click(view: View) {
        data.age++
        updateTextFromModel()
    }

    private fun next(view: View) {
        // adding an object to a bundle only works with serialization plugins!
        // see the intents parts of the course.
        val bundle = bundleOf("mydata" to data)
        findNavController().navigate(R.id.action_firstFragment_to_secondFragment, bundle)
    }

    private fun snackbarMessage(view: View) {
        msg("Goed op de knop gedrukt!", view)
    }

    private fun welcome(view: View) {
        val intent = Intent(this.context, WelcomeActivity::class.java)
        startActivity(intent)
    }

    fun maps(view: View) {
        if (request.rocketLaunches.isNotEmpty()) {
            val latitude = request.rocketLaunches.get(0).latitude
            val longitude = request.rocketLaunches.get(0).longitude

            val callIntent = Intent(
                Intent.ACTION_VIEW,
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

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}