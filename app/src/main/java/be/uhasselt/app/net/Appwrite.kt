package be.uhasselt.app.net

import android.content.Context
import androidx.lifecycle.ViewModel
import io.appwrite.Client
import io.appwrite.services.Account

class Appwrite : ViewModel() {

    private lateinit var client: Client
    lateinit var account: Account

    fun createClient(context: Context) {
        client = Client(context)
            .setEndpoint("https://appwrite.lowie.xyz/v1") // Your API Endpoint
            .setProject("627bac3c2508bec32bc2") // Your project ID
            .setSelfSigned(true)

        account = Account(client)
    }
}
