package be.uhasselt.app.fragments

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


class DetailedRocketFragment : Fragment(R.layout.detailed_rocket_fragment) {
    private lateinit var binding: DetailedRocketFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DetailedRocketFragmentBinding.inflate(layoutInflater)

        val data = (arguments?.getSerializable("data") as String?) ?: String()

        val type = object : TypeToken<RocketLaunch>() {}.type
        val temp = Gson().fromJson<RocketLaunch>(data, type)!!

        binding.textViewDetailedRocket.text =
            "${temp.rocketName}\n${temp.missionName}\n${temp.longitude}\n${temp.latitude}"

        return binding.root
    }

}
