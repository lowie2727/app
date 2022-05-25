package be.uhasselt.app.net

import be.uhasselt.app.model.RocketLaunch
import org.json.JSONObject

object LL2ResultParser {
    fun parse(jsonObj: JSONObject): ArrayList<RocketLaunch> {

        var mission: String
        var imageUrl: String
        val rocketLaunches = arrayListOf<RocketLaunch>()
        val launches = jsonObj.getJSONArray("results")

        for (i in 0 until launches.length()) {
            val launch = launches.getJSONObject(i)
            val rocket = launch.getJSONObject("rocket")
            val launchPad = launch.getJSONObject("pad")

            if (!launch.isNull("mission")) {
                val launchMission = launch.getJSONObject("mission")
                mission = launchMission.getString("name")
            } else {
                mission = "Not part of a mission"
            }

            if (!launch.isNull("image")) {
                imageUrl = launch.getString("image")
            } else {
                imageUrl = ""
            }

            val rocketLaunch = RocketLaunch(
                rocket.getJSONObject("configuration").getString("name"),
                launch.getString("window_start"),
                launchPad.getString("latitude"),
                launchPad.getString("longitude"),
                mission,
                imageUrl,
                false
            )
            rocketLaunches.add(rocketLaunch)
        }
        return rocketLaunches
    }
}
