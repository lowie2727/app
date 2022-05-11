package be.uhasselt.app.net

import android.content.Context
import android.view.View
import be.uhasselt.app.model.RocketLaunch
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException

class LL2Request(private val context: Context?, private val view: View) {

    var rocketLaunches: ArrayList<RocketLaunch> = arrayListOf()

    fun load() {
        val queue = Volley.newRequestQueue(context)
        val url = "https://ll.thespacedevs.com/2.2.0/launch/upcoming"
        val jsonObj = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    rocketLaunches = LL2ResultParser.parse(response)
                    msg("succes", view)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                if(error.networkResponse == null) {
                    msg("No internet connection", view)
                }
                else if (error.networkResponse.statusCode == 429) {
                    val errorStatus = error.networkResponse.headers?.get("retry-after")
                    val errorMessage = "timeout probeer nog eens in $errorStatus seconden"
                    msg(errorMessage, view)
                    println(errorMessage)
                } else {
                    val errorStatus = error.networkResponse.statusCode
                    val errorMessage = "fout opgetreden met status code $errorStatus"
                    msg(errorMessage, view)
                    println(errorMessage)
                }
            })
        queue.add(jsonObj)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
