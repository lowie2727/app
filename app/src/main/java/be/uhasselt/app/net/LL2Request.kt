package be.uhasselt.app.net

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LL2Request(
    private val context: Context?,
    val onResponseFetched: (isSuccess: Boolean, response: JSONObject?, error: VolleyError?) -> Unit
) {

    fun load() {
        val queue = Volley.newRequestQueue(context)
        val url = "https://ll.thespacedevs.com/2.2.0/launch/upcoming"
        val jsonObj = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                onResponseFetched(true, response, null)
            },
            { error ->
                onResponseFetched(false, null, error)
            })
        queue.add(jsonObj)
    }
}
