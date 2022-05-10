package be.uhasselt.app.model

import android.content.Context
import java.io.*

class SaveFile(private val context: Context) {

    fun load(): List<*> {
        try {
            val openFileInput =
                context.openFileInput("rocketLauches.txt") ?: return arrayListOf<RocketLaunch>()
            ObjectInputStream(openFileInput).use {
                return it.readObject() as ArrayList<*>
            }
        } catch (fileNotFound: FileNotFoundException) {
            // no file yet, revert to defaults.
        } catch (prematureEndOfFile: EOFException) {
            // also ignore this: file incomplete/corrupt, revert to defaults.
        }
        return arrayListOf<RocketLaunch>()
    }

    fun save(rocketLaunches: List<RocketLaunch>) {
        val openFileOutput =
            context.openFileOutput("rocketLauches.txt", Context.MODE_PRIVATE) ?: return
        ObjectOutputStream(openFileOutput).use {
            it.writeObject(rocketLaunches)
        }
    }
}
