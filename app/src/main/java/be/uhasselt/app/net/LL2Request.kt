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
            { respone ->
                try {
                    rocketLaunches = LL2ResultParser.parse(respone)
                    for (i in 0 until rocketLaunches.size) {
                        println(rocketLaunches.get(i).toString())
                    }
                    msg("rockets launches up to date", view)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                if (error.networkResponse.statusCode == 429) {
                    msg("timeout probeer nog eens in ${error.networkResponse.headers?.get("retry-after")} seconden", view)
                    println("timeout probeer nog eens in ${error.networkResponse.headers?.get("retry-after")} seconden")
                } else {
                    msg("fout opgetreden met status code ${error.networkResponse.statusCode}", view)
                    println("fout opgetreden met status code ${error.networkResponse.statusCode}")
                }
            })
        queue.add(jsonObj)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
