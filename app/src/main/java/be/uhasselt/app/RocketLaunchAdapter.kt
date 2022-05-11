package be.uhasselt.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.uhasselt.app.model.RocketLaunch

class RocketLaunchAdapter(private val rocketLaunches: ArrayList<RocketLaunch>) :
    RecyclerView.Adapter<RocketLaunchAdapter.RocketViewHolder>() {

    inner class RocketViewHolder(currentItemView: View) : RecyclerView.ViewHolder(currentItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle, parent, false)
        return RocketViewHolder(view)
    }

    override fun onBindViewHolder(holder: RocketViewHolder, position: Int) {
        val rocketLaunch = rocketLaunches[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.text_view_rocket_launch).text =
                "${rocketLaunch.rocketName} |\n${rocketLaunch.missionName}"
        }
    }

    override fun getItemCount(): Int = rocketLaunches.size
}
