package be.uhasselt.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.databinding.FragmentFirstBinding
import be.uhasselt.app.model.MySharedData
import be.uhasselt.app.model.RocketLaunch
import be.uhasselt.app.net.LL2Request
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FirstFragment : Fragment(R.layout.fragment_first) {
    private lateinit var binding: FragmentFirstBinding
    private lateinit var request: LL2Request
    private var data: MySharedData = MySharedData()
    private var jsonData: String = ""
    private lateinit var sharedPref: SharedPreferences
    private var rocketLaunches = arrayListOf<RocketLaunch>()

    // here the view should be set
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        println("Fragment: onCreateView")
        // this looks a lot like activities! Fragments have their own lifecycle.
        // return inflater.inflate(R.layout.fragment_first, container, false)
        // -- above should NOT be needed since we used a constructor argument!
        binding = FragmentFirstBinding.inflate(layoutInflater)

        request = LL2Request(requireContext(), binding.root)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!

        binding.buttonAPI.setOnClickListener(this::request)
        binding.buttonSave.setOnClickListener(this::saveRocketsToSharedPreferences)
        binding.buttonLoad.setOnClickListener(this::loadRocketsFromSharedPreferences)
        binding.buttonSnackbar.setOnClickListener(this::snackbarMessage)
        binding.btnGoToNext.setOnClickListener(this::next)

        updateTextFromModel()
        binding.txtFragmentFirst.setOnClickListener(this::click)

        // remember to do this instead of super.onCreateView()
        // otherwise nothing will happen.
        return binding.root
    }

    private fun request(view: View) {
        request.load()
    }

    private fun saveRocketsToSharedPreferences(view: View) {
        jsonData = Gson().toJson(request.rocketLaunches)
        sharedPref.edit().putString("key", jsonData).apply()
        msg("saved all rockets", view)
    }

    private fun loadRocketsFromSharedPreferences(view: View) {
        val json = sharedPref.getString("key", null)
        val type = object : TypeToken<ArrayList<RocketLaunch>>() {}.type
        val temp = Gson().fromJson<ArrayList<RocketLaunch>>(json, type)
        if (temp != null) {
            rocketLaunches = temp
            if (rocketLaunches.isEmpty()) {
                println("lijst is empty")
            } else {
                for (element in rocketLaunches) {
                    println(element.toString())
                }
            }
        }
        msg("loaded all rockets", view)
    }

    private fun snackbarMessage(view: View) {
        msg("Goed op de knop gedrukt!", view)
    }

    private fun next(view: View) {
        // adding an object to a bundle only works with serialization plugins!
        // see the intents parts of the course.
        val bundle = bundleOf("age" to data, "data" to jsonData)
        findNavController().navigate(R.id.action_firstFragment_to_secondFragment, bundle)
    }

    private fun updateTextFromModel() {
        binding.txtFragmentFirst.text = "Fragment 1, model: ${data.age}"
    }

    private fun click(view: View) {
        data.age++
        updateTextFromModel()
    }

    private fun welcome(view: View) {
        val intent = Intent(this.context, WelcomeActivity::class.java)
        startActivity(intent)
    }

    /*fun maps(view: View) {
        if (request.rocketLaunches.isNotEmpty()) {
            val latitude = request.rocketLaunches[0].latitude
            val longitude = request.rocketLaunches[0].longitude

            val callIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:launch location?q=$latitude,$longitude")
            )
            startActivity(callIntent)
        } else {
            msg("no rockets found", view)
        }
    }

    private fun loadRocketsFromFile(view: View) {
        val saveFile = SaveFile(requireContext())
        val rockets = saveFile.load()
        for (element in rockets) {
            println(element.toString())
        }
    }

    private fun saveRocketsToFile(view: View) {
        val saveFile = SaveFile(requireContext())
        if (request.rocketLaunches.isNotEmpty()) {
            saveFile.save(request.rocketLaunches)
        }
    }*/

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
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
}
