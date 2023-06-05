package components

import data.Position
import kotlinx.coroutines.flow.MutableStateFlow

interface Positionable {
    val position: MutableStateFlow<Position>
}