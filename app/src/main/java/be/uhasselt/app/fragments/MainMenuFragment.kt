package be.uhasselt.app.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.R
import be.uhasselt.app.databinding.MainMenuFragmentBinding
import be.uhasselt.app.model.RocketLaunch
import be.uhasselt.app.file.SaveFile
import be.uhasselt.app.net.LL2Request
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainMenuFragment : Fragment(R.layout.main_menu_fragment) {

    private lateinit var binding: MainMenuFragmentBinding
    private lateinit var request: LL2Request
    private var rocketLaunches = arrayListOf<RocketLaunch>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = MainMenuFragmentBinding.inflate(layoutInflater)

        request = LL2Request(requireContext(), binding.root) { rockets ->
            rocketLaunches = rockets
            saveToFile(requireView())
            msg("success", requireView())
        }

        binding.buttonApiRequest.setOnClickListener(this::request)
        binding.buttonToFragmentSecond.setOnClickListener(this::next)

        return binding.root
    }

    private fun request(view: View) {
        if (rocketLaunches.isEmpty()) {
            request.load()
            msg("updating launches", view)
        } else {
            msg("launches up to date", view)
        }
    }

    private fun saveToFile(view: View) {
        saveRocketsToFile()
        loadFromFile(view)
    }

    private fun saveRocketsToFile() {
        val jsonDataLaunches = Gson().toJson(rocketLaunches)
        val saveFile = SaveFile(requireContext())
        if (jsonDataLaunches != null) {
            saveFile.save(jsonDataLaunches, "rockets.txt")
        }
    }

    private fun loadFromFile(view: View) {
        loadRocketsFromFile()
    }

    private fun loadRocketsFromFile() {
        if (rocketLaunches.isEmpty()) {
            val saveFile = SaveFile(requireContext())
            val jsonDataLaunches = saveFile.load("rockets.txt")
            val type = object : TypeToken<ArrayList<RocketLaunch>>() {}.type
            val launchesLoadedFromFile =
                Gson().fromJson<ArrayList<RocketLaunch>>(jsonDataLaunches, type)
            if (launchesLoadedFromFile == null) {
                request.load()
            } else {
                rocketLaunches = launchesLoadedFromFile
            }
        }
    }

    private fun next(view: View) {
        val jsonData = Gson().toJson(rocketLaunches)
        val bundle = bundleOf("data" to jsonData)
        findNavController().navigate(R.id.action_first_fragment_to_second_fragment, bundle)
    }

    private fun maps(view: View) {
        if (rocketLaunches.isNotEmpty()) {
            val latitude = rocketLaunches[0].latitude
            val longitude = rocketLaunches[0].longitude

            val callIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:launch location?q=$latitude,$longitude")
            )
            startActivity(callIntent)
        } else {
            msg("no rockets found", view)
        }
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadFromFile(view)
        request(view)
        super.onViewCreated(view, savedInstanceState)
    }
}
