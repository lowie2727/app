package be.uhasselt.app.model

class RocketLaunch(
    var rocketName: String,
    var lauchDate: String,
    var latitude: String,
    var longitude: String
) {
    override fun toString(): String {
        return "rocket name: $rocketName\nlaunch date: $lauchDate\ncoordinates: $latitude°N, $longitude°E"
    }
}
