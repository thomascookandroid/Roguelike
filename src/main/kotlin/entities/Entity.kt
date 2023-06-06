package entities

import components.Positionable
import components.Renderable
import kotlinx.serialization.Serializable

@Serializable
sealed interface Entity : Renderable, Positionable