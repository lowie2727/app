package be.uhasselt.app.net

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.uhasselt.app.MainActivity
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Database
import io.appwrite.services.Storage
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

object Appwrite : ViewModel() {

    private var client: Client = Client(MainActivity.applicationContext())
        .setEndpoint("https://appwrite.lowie.xyz/v1") // Your API Endpoint
        .setProject("627bac3c2508bec32bc2") // Your project ID
        .setSelfSigned(true)
    private var account: Account = Account(client)
    private var storage: Storage = Storage(client)
    private var database: Database = Database(client)

    fun createSession(
        email: String,
        password: String,
        onResponseCreateSession: (isSuccess: Boolean, errorMessage: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                account.createSession(
                    email = email,
                    password = password
                )
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

    fun getAccount(onResponseGetAccount: (isSuccess: Boolean, errorMessage: String, userData: User?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = account.get()
                onResponseGetAccount(true, "No error", user)
            } catch (e: AppwriteException) {
                onResponseGetAccount(false, e.message.toString(), null)
            }
        }
    }

    fun storeToDataBase(
        jsonData: JSONObject,
        onResponseCreateFile: (isSuccess: Boolean, errorMessage: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                database.createDocument(
                    collectionId = "favorites",
                    documentId = "favorites",
                    data = jsonData
                )
                onResponseCreateFile(true, "No error")
            } catch (e: AppwriteException) {
                onResponseCreateFile(false, e.message.toString())
            }
        }
    }

    fun storeToFile(
        file: File,
        onResponseFile: (isSuccess: Boolean, errorMessage: String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                storage.createFile(
                    bucketId = "favorites",
                    fileId = "unique()",
                    file = file
                )
                onResponseFile(true, "No error")
            } catch (e: AppwriteException) {
                onResponseFile(false, e.message.toString())
            }
        }
    }
}
