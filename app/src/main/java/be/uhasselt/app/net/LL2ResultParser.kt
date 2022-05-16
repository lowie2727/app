package be.uhasselt.app.net

import be.uhasselt.app.model.RocketLaunch
import org.json.JSONObject

object LL2ResultParser {
    fun parse(jsonObj: JSONObject): ArrayList<RocketLaunch> {

        var launchMission: JSONObject
        var mission: String
        val rocketLaunches = arrayListOf<RocketLaunch>()
        val launches = jsonObj.getJSONArray("results")

        for (i in 0 until launches.length()) {
            val launch = launches.getJSONObject(i)
            val rocket = launch.getJSONObject("rocket")
            val launchPad = launch.getJSONObject("pad")

            if (!launch.isNull("mission")) {
                launchMission = launch.getJSONObject("mission")
                mission = launchMission.getString("name")
            } else {
                mission = ""
            }

            val rocketLaunch = RocketLaunch(
                rocket.getJSONObject("configuration").getString("name"),
                launch.getString("window_start"),
                launchPad.getString("latitude"),
                launchPad.getString("longitude"),
                mission
            )
            rocketLaunches.add(rocketLaunch)
        }
        return rocketLaunches
    }
}
