package be.uhasselt.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.uhasselt.app.R
import be.uhasselt.app.file.SaveFile
import be.uhasselt.app.model.RocketLaunch
import com.google.gson.Gson

class RocketLaunchAdapter(private val rocketLaunches: ArrayList<RocketLaunch>) :
    RecyclerView.Adapter<RocketLaunchAdapter.RocketViewHolder>() {

    private lateinit var context: Context

    inner class RocketViewHolder(currentItemView: View) : RecyclerView.ViewHolder(currentItemView) {
        init {
            currentItemView.setOnClickListener {
                val position = absoluteAdapterPosition
                val rocketLaunch = rocketLaunches[position]
                val jsonData = Gson().toJson(rocketLaunch)
                val bundle = bundleOf("data" to jsonData)
                it.findNavController().navigate(R.id.detailed_rocket_fragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle, parent, false)
        return RocketViewHolder(view)
    }

    override fun onBindViewHolder(holder: RocketViewHolder, position: Int) {
        val rocketLaunch = rocketLaunches[position]
        holder.itemView.apply {
            val checkBox = findViewById<CheckBox>(R.id.favorite_selector)
            checkBox.isChecked = rocketLaunch.isFavorite

            checkBox.setOnClickListener {
                rocketLaunch.isFavorite = checkBox.isChecked
                saveRocketsToFile(rocketLaunches, "rockets.txt")
                showFavorites()
            }

            findViewById<TextView>(R.id.text_view_rocket_launch).text =
                "${rocketLaunch.rocketName} |\n${rocketLaunch.missionName}"
        }
    }

    private fun saveRocketsToFile(rocketLaunches: ArrayList<RocketLaunch>, fileName: String) {
        val jsonDataLaunches: String = Gson().toJson(rocketLaunches)
        val saveFile = SaveFile(context)
        saveFile.save(jsonDataLaunches, fileName)
    }


    private fun showFavorites() {
        val favoriteList = arrayListOf<RocketLaunch>()
        for (rocket in rocketLaunches) {
            if (rocket.isFavorite) {
                favoriteList.add(rocket)
            }
        }
        saveRocketsToFile(favoriteList, "favorites.txt")
    }

    override fun getItemCount(): Int = rocketLaunches.size
}
