package be.uhasselt.app.model

import kotlinx.serialization.Serializable

@Serializable
data class RocketLaunch(
    var rocketName: String,
    var lauchDate: String,
    var latitude: String,
    var longitude: String
) : java.io.Serializable {

    override fun toString(): String {
        return "rocket name: $rocketName\nlaunch date: $lauchDate\ncoordinates: $latitude°N, $longitude°E"
    }
}
