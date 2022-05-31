package be.uhasselt.app.net

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.uhasselt.app.MainActivity
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.launch

object Appwrite : ViewModel() {

    private var client: Client = Client(MainActivity.applicationContext())
        .setEndpoint("https://appwrite.lowie.xyz/v1") // Your API Endpoint
        .setProject("627bac3c2508bec32bc2") // Your project ID
        .setSelfSigned(true)
    private val account: Account = Account(client)
    var currentLoginName = "anonymous"

    fun createSession(
        email: String,
        password: String,
        onResponseCreateSession: (isSuccess: Boolean, errorMessage: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = account.createSession(
                    email = email,
                    password = password
                )
                currentLoginName = response.userId
                onResponseCreateSession(true, "No error")
            } catch (e: AppwriteException) {
                onResponseCreateSession(false, e.message.toString())
            }
        }
    }

    fun createAccount(
        userId: String,
        email: String,
        password: String,
        name: String,
        onResponseCreateAccount: (isSuccess: Boolean, errorMessage: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                account.create(
                    userId = userId,
                    email = email,
                    password = password,
                    name = name
                )
                onResponseCreateAccount(true, "No error")
            } catch (e: AppwriteException) {
                onResponseCreateAccount(false, e.message.toString())
            }
        }
    }
}
