package be.uhasselt.app.model

import android.content.Context
import java.io.*

class SaveFile(private val context: Context) {

    fun load(fileName: String): String? {
        try {
            val openFileInput =
                context.openFileInput(fileName) ?: return null
            ObjectInputStream(openFileInput).use {
                return it.readObject() as String
            }
        } catch (fileNotFound: FileNotFoundException) {
            // no file yet, revert to defaults.
        } catch (prematureEndOfFile: EOFException) {
            // also ignore this: file incomplete/corrupt, revert to defaults.
        }
        return null
    }

    fun save(rocketLaunches: String, fileName: String) {
        val openFileOutput =
            context.openFileOutput(fileName, Context.MODE_PRIVATE) ?: return
        ObjectOutputStream(openFileOutput).use {
            it.writeObject(rocketLaunches)
        }
    }
}
