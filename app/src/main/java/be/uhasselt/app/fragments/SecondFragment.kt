package be.uhasselt.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import be.uhasselt.app.R
import be.uhasselt.app.RocketLaunchAdapter
import be.uhasselt.app.databinding.SecondFragmentBinding
import be.uhasselt.app.model.MySharedData
import be.uhasselt.app.model.RocketLaunch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SecondFragment : Fragment(R.layout.second_fragment) {

    private lateinit var binding: SecondFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecondFragmentBinding.inflate(layoutInflater)

        // use the "elvis operator": if left-hand side is null, provide right-hand side.
        // since arguments is nullable ("?"), always make sure to provide an alternative.
        val age = (arguments?.getSerializable("age") as MySharedData?) ?: MySharedData()
        val data = (arguments?.getSerializable("data") as String?) ?: String()

        val type = object : TypeToken<ArrayList<RocketLaunch>>() {}.type
        val temp = Gson().fromJson<ArrayList<RocketLaunch>>(data, type)

        binding.textViewFragmentSecond.text = "Fragment 2, model: ${age.age}"

        if (temp != null) {
            val adapter = RocketLaunchAdapter(temp)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }
}
