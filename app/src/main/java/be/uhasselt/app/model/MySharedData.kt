package be.uhasselt.app.model

import kotlinx.serialization.Serializable

@Serializable
data class MySharedData(var name: String = "", var age: Int = 0) : java.io.Serializable
