package be.uhasselt.app.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import be.uhasselt.app.R
import be.uhasselt.app.databinding.DetailedRocketFragmentBinding
import be.uhasselt.app.model.RocketLaunch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso


class DetailedRocketFragment : Fragment(R.layout.detailed_rocket_fragment) {
    private lateinit var binding: DetailedRocketFragmentBinding
    private lateinit var rocketLaunch: RocketLaunch

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailedRocketFragmentBinding.inflate(layoutInflater)

        receiveData()
        loadImage()
        binding.buttonMaps.setOnClickListener(this::maps)

        return binding.root
    }

    private fun receiveData() {
        val data = (arguments?.getSerializable("data") as String?) ?: String()

        val type = object : TypeToken<RocketLaunch>() {}.type
        rocketLaunch = Gson().fromJson<RocketLaunch>(data, type)!!

        binding.textViewDetailedRocket.text =
            "rocket: ${rocketLaunch.rocketName}\nmission: ${rocketLaunch.missionName}\n\n\n" +
                    "coordinates\nlongitude: ${rocketLaunch.longitude}, latitude: ${rocketLaunch.latitude}"
    }

    private fun maps(view: View) {
        val latitude = rocketLaunch.latitude
        val longitude = rocketLaunch.longitude

        val callIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:launch location?q=$latitude,$longitude")
        )
        startActivity(callIntent)
    }

    private fun loadImage() {
        if (rocketLaunch.rocketImageUrl.isNotEmpty()) {
            Picasso.get().load(rocketLaunch.rocketImageUrl).into(binding.imageViewRocket)
        }
    }
}
