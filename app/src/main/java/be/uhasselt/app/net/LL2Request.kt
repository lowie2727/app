package be.uhasselt.app.net

import android.content.Context
import be.uhasselt.app.model.RocketLaunch
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class LL2Request(private val context: Context) {

    private var rocketLaunches: ArrayList<RocketLaunch> = arrayListOf()

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
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                if (error.networkResponse.statusCode == 429) {
                    println("timeout probeer nog eens in ${error.networkResponse.headers?.get("retry-after")} seconden")
                } else {
                    println("fout opgetreden met status code ${error.networkResponse.statusCode}")
                }
            })
        queue.add(jsonObj)
    }
}
