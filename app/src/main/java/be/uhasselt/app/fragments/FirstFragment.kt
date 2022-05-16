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
import be.uhasselt.app.databinding.FirstFragmentBinding
import be.uhasselt.app.model.MySharedData
import be.uhasselt.app.model.RocketLaunch
import be.uhasselt.app.file.SaveFile
import be.uhasselt.app.net.LL2Request
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FirstFragment : Fragment(R.layout.first_fragment) {

    private lateinit var binding: FirstFragmentBinding
    private lateinit var request: LL2Request
    private var data: MySharedData = MySharedData()
    private var rocketLaunches = arrayListOf<RocketLaunch>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FirstFragmentBinding.inflate(layoutInflater)

        request = LL2Request(requireContext(), binding.root)

        binding.buttonApiRequest.setOnClickListener(this::request)
        binding.buttonSaveToFile.setOnClickListener(this::saveToFile)
        binding.buttonLoadFromFile.setOnClickListener(this::loadFromFile)
        binding.buttonMaps.setOnClickListener(this::maps)
        binding.buttonToFragmentSecond.setOnClickListener(this::next)

        updateTextFromModel()
        binding.textViewFragmentFirst.setOnClickListener(this::click)

        return binding.root
    }

    private fun request(view: View) {
        if (rocketLaunches.isEmpty()) {
            request.load()
            msg("updating launches", view)
        } else {
            msg("already up to date", view)
        }
    }

    private fun saveToFile(view: View) {
        saveAgeToFile()
        saveRocketsToFile()
        loadFromFile(view)
    }

    private fun saveRocketsToFile() {
        val jsonDataLaunches = Gson().toJson(request.rocketLaunches)
        val saveFile = SaveFile(requireContext())
        if (jsonDataLaunches != null) {
            saveFile.save(jsonDataLaunches, "rockets.txt")
        }
    }

    private fun saveAgeToFile() {
        val jsonDataObject = Gson().toJson(data)
        val saveFile = SaveFile(requireContext())
        if (jsonDataObject != null) {
            saveFile.save(jsonDataObject, "data.txt")
        }
    }

    private fun loadFromFile(view: View) {
        loadAgeFromFile()
        loadRocketsFromFile()
    }

    private fun loadRocketsFromFile() {
        if (rocketLaunches.isEmpty()) {
            val saveFile = SaveFile(requireContext())
            val jsonDataLaunches = saveFile.load("rockets.txt")
            val type = object : TypeToken<ArrayList<RocketLaunch>>() {}.type
            val temp = Gson().fromJson<ArrayList<RocketLaunch>>(jsonDataLaunches, type)
            if (temp != null) {
                rocketLaunches = temp
                for (element in rocketLaunches) {
                    println(element.toString())
                }
            }
        } else {
            msg("already up to date", requireView())
        }
    }

    private fun loadAgeFromFile() {
        val saveFile = SaveFile(requireContext())
        val jsonDataLaunches = saveFile.load("data.txt")
        val type = object : TypeToken<MySharedData>() {}.type
        val temp = Gson().fromJson<MySharedData>(jsonDataLaunches, type)
        if (temp != null) {
            data = temp
        }
        updateTextFromModel()
    }

    private fun next(view: View) {
        val jsonData = Gson().toJson(rocketLaunches)
        val bundle = bundleOf("age" to data, "data" to jsonData)
        findNavController().navigate(R.id.action_first_fragment_to_second_fragment, bundle)
    }

    private fun updateTextFromModel() {
        binding.textViewFragmentFirst.text = "Fragment 1, model: ${data.age}"
    }

    private fun click(view: View) {
        data.age++
        updateTextFromModel()
        saveAgeToFile()
    }

    private fun maps(view: View) {
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

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadFromFile(view)
        super.onViewCreated(view, savedInstanceState)
        println("Fragment: onViewCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Fragment: onCreate")
    }
}
