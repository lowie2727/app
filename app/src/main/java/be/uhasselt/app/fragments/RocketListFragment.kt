package be.uhasselt.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import be.uhasselt.app.R
import be.uhasselt.app.adapter.RocketLaunchAdapter
import be.uhasselt.app.databinding.RocketListFragmentBinding
import be.uhasselt.app.file.SaveFile
import be.uhasselt.app.model.RocketLaunch
import be.uhasselt.app.net.LL2Request
import be.uhasselt.app.net.LL2ResultParser
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RocketListFragment : Fragment(R.layout.rocket_list_fragment) {

    private lateinit var binding: RocketListFragmentBinding
    private lateinit var request: LL2Request
    private var rocketLaunches = arrayListOf<RocketLaunch>()
    private var isClear = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RocketListFragmentBinding.inflate(layoutInflater)

        loadRocketsFromFile()
        setupAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAPIResponse(view)

        if (rocketLaunches.isEmpty()) {
            msg("loading in rockets...", view)
            isClear = false
            request.load()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupAPIResponse(view: View) {
        request = LL2Request(requireContext()) { isSuccess, jsonObject, error ->
            if (isSuccess) {
                rocketLaunches = LL2ResultParser.parse(jsonObject!!)
                saveRocketsToFile(rocketLaunches)
                msg("update successful", view)
            } else {
                if (error!!.networkResponse == null) {
                    msg("No internet connection", view)
                } else if (error.networkResponse.statusCode == 429) {
                    val errorStatus = error.networkResponse.headers?.get("retry-after")
                    val errorMessage = "timeout probeer nog eens in $errorStatus seconden"
                    msg(errorMessage, view)
                } else {
                    val errorStatus = error.networkResponse.statusCode
                    val errorMessage = "fout opgetreden met status code $errorStatus"
                    msg(errorMessage, view)
                }
            }
            setupAdapter()
            isClear = true
        }
    }

    private fun setupAdapter() {
        val adapter = RocketLaunchAdapter(rocketLaunches)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
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

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
