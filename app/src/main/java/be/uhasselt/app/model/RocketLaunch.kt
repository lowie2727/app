package be.uhasselt.app.model

import kotlinx.serialization.Serializable

@Serializable
data class RocketLaunch(
    val rocketName: String,
    val launchDate: String,
    val latitude: String,
    val longitude: String,
    val missionName: String,
    val rocketImageUrl: String

) : java.io.Serializable {

    override fun toString(): String {
        return "rocket name: $rocketName\nmission name: $missionName\nlaunch date: $launchDate\ncoordinates: $latitude°N, $longitude°E"
    }
}
