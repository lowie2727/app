package be.uhasselt.app.fragments

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

        binding.buttonApiRequest.setOnClickListener(this::forceUpdate)
        binding.buttonToRocketListFragment.setOnClickListener(this::next)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        request = LL2Request(requireContext(), binding.root) { rockets ->
            rocketLaunches = rockets
            saveRocketsToFile(rocketLaunches)
            msg("update successful", view)
        }
        request()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun request() {
        loadRocketsFromFile()
        if (rocketLaunches.isEmpty()) {
            request.load()
        }
    }

    private fun forceUpdate(view: View) {
        request.load()
        msg("updating launches", view)
    }

    private fun saveRocketsToFile(rocketLaunches: ArrayList<RocketLaunch>) {
        val jsonDataLaunches: String = Gson().toJson(rocketLaunches)
        val saveFile = SaveFile(requireContext())
        saveFile.save(jsonDataLaunches, "rockets.txt")
    }

    private fun loadRocketsFromFile() {
        if (rocketLaunches.isEmpty()) {
            val saveFile = SaveFile(requireContext())
            val jsonDataLaunches = saveFile.load("rockets.txt")
            val type = object : TypeToken<ArrayList<RocketLaunch>>() {}.type
            val launchesLoadedFromFile =
                Gson().fromJson<ArrayList<RocketLaunch>>(jsonDataLaunches, type)
            if (launchesLoadedFromFile != null) {
                rocketLaunches = launchesLoadedFromFile
            }
        }
    }

    private fun next(view: View) {
        val jsonData = Gson().toJson(rocketLaunches)
        val bundle = bundleOf("data" to jsonData)
        findNavController().navigate(R.id.action_main_fragment_to_rocket_list_fragment, bundle)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
