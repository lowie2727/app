package be.uhasselt.app;

import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray

class VolleyAPI(temp: MainActivity, view: View) {
    private val queue = Volley.newRequestQueue(temp)

    init {
        val url = "https://ll.thespacedevs.com/2.2.0/launch/?mode=detailed&search=SpaceX";
        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val jsonArray: JSONArray = it.getJSONArray("results")
                for (i in jsonArray.length() - 1 until jsonArray.length()) {
                    val waarde = jsonArray.getJSONObject(i)
                    val naam = waarde.getString("name")
                    val lanceerDag = waarde.getString("window_start")
                    msg("Check de console", view)
                    println("racket naam: $naam, lanceer datum: $lanceerDag")
                }
            }, {
                if (it.networkResponse.statusCode == 429) {
                    println("timeout probeer nog eens in ${it.networkResponse.headers?.get("retry-after")} seconden")
                    msg(
                        "timeout probeer nog eens in ${it.networkResponse.headers?.get("retry-after")} seconden",
                        view
                    )
                } else {
                    println("fout opgetreden met status code ${it.networkResponse.statusCode}")
                    msg("fout opgetreden met status code ${it.networkResponse.statusCode}", view)

                }
            })
        queue.add(request)
    }

    private fun msg(text: String, view: View) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}
