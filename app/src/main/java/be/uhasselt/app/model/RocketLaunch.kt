package be.uhasselt.app.model

import kotlinx.serialization.Serializable

@Serializable
data class RocketLaunch(
    var rocketName: String,
    var launchDate: String,
    var latitude: String,
    var longitude: String,
    var missionName: String
) : java.io.Serializable {

    override fun toString(): String {
        return "rocket name: $rocketName\nmission name: $missionName\nlaunch date: $launchDate\ncoordinates: $latitude°N, $longitude°E"
    }
}
