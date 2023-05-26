package entities

import kotlinx.coroutines.flow.StateFlow

interface Trackable {
    val position: StateFlow<Position>
}