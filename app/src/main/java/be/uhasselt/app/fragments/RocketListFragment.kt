package be.uhasselt.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import be.uhasselt.app.R
import be.uhasselt.app.RocketLaunchAdapter
import be.uhasselt.app.databinding.RocketListFragmentBinding
import be.uhasselt.app.model.RocketLaunch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RocketListFragment : Fragment(R.layout.rocket_list_fragment) {

    private lateinit var binding: RocketListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RocketListFragmentBinding.inflate(layoutInflater)

        val data = (arguments?.getSerializable("data") as String?) ?: String()

        val type = object : TypeToken<ArrayList<RocketLaunch>>() {}.type
        val temp = Gson().fromJson<ArrayList<RocketLaunch>>(data, type)


        if (temp != null) {
            val adapter = RocketLaunchAdapter(temp)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }
}
