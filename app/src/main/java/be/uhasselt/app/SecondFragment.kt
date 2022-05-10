package be.uhasselt.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import be.uhasselt.app.databinding.FragmentSecondBinding
import be.uhasselt.app.model.MySharedData
import be.uhasselt.app.model.RocketLaunch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SecondFragment : Fragment(R.layout.fragment_second) {

    private lateinit var binding: FragmentSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(layoutInflater)

        // use the "elvis operator": if left-hand side is null, provide right-hand side.
        // since arguments is nullable ("?"), always make sure to provide an alternative.
        val age = (arguments?.getSerializable("age") as MySharedData?) ?: MySharedData()
        val data = (arguments?.getSerializable("data") as String?) ?: String()

        val type = object : TypeToken<ArrayList<RocketLaunch>>() {}.type
        val temp = Gson().fromJson<ArrayList<RocketLaunch>>(data, type)

        binding.txtFragmentSecond.text = "Fragment 2, model: ${age.age}"

        if (temp != null) {
            val adapter = RocketLaunchAdapter(temp)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }
}
