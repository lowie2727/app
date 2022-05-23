package be.uhasselt.app.net

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import kotlinx.coroutines.launch

class Appwrite(private val onResponse: (isSuccess: Boolean, errorMessage: String?, user: User?) -> Unit) :
    ViewModel() {

    private lateinit var client: Client
    lateinit var account: Account

    fun createClient(context: Context) {
        client = Client(context)
            .setEndpoint("https://appwrite.lowie.xyz/v1") // Your API Endpoint
            .setProject("627bac3c2508bec32bc2") // Your project ID
            .setSelfSigned(true)

        account = Account(client)
    }

    fun createSession(email: String, password: String) {
        viewModelScope.launch {
            try {
                account.createSession(
                    email = email,
                    password = password
                )
                onResponse(true, null, null)
            } catch (e: AppwriteException) {
                onResponse(false, e.message.toString(), null)
            }
        }
    }

    fun createAccount(userId: String, email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                account.create(
                    userId = userId,
                    email = email,
                    password = password,
                    name = name
                )
                onResponse(true, null, null)
            } catch (e: AppwriteException) {
                onResponse(false, e.message.toString(), null)
            }
        }
    }

    fun getAccount() {
        viewModelScope.launch {
            try {
                val user = account.get()
                onResponse(true, null, user)
            } catch (e: AppwriteException) {
                onResponse(true, e.message.toString(), null)
            }
        }
    }
}
