package data

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val x: Int,
    val y: Int
)