package be.uhasselt.app.net

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.uhasselt.app.MainActivity
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import kotlinx.coroutines.launch

object Appwrite : ViewModel() {

    private var client: Client = Client(MainActivity.applicationContext())
        .setEndpoint("http://appwrite.home/v1") // Your API Endpoint
        .setProject("633351acaf343fd1732d") // Your project ID
    private val account: Account = Account(client)
    var accountName = "anonymous"

    fun createSession(
        email: String,
        password: String,
        onResponseCreateSession: (isSuccess: Boolean, errorMessage: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                account.createEmailSession(
                    email = email,
                    password = password
                )
                accountName = account.get().name
                onResponseCreateSession(true, "No error")
            } catch (e: AppwriteException) {
                onResponseCreateSession(false, e.message.toString())
            }
        }
    }

    fun createAccount(
        email: String,
        password: String,
        name: String,
        onResponseCreateAccount: (isSuccess: Boolean, errorMessage: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                account.create(
                    userId = ID.unique(),
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
