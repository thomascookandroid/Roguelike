package serialization

import data.Position
import entities.Grass
import entities.Monster
import entities.Player
import entities.Wall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import state.LocalMapState
import java.io.File
import java.lang.Exception

private const val TAG = "LocalMapLoader"

class LocalMapLoader {
    fun load() : LocalMapState {
        return try {
            Json.decodeFromString(
                File("./src/main/resources/mapstate_saved.json").readText(Charsets.UTF_8)
            )
        } catch (ex: Exception) {
            try {
                Json.decodeFromString(
                    File("./src/main/resources/mapstate_initial.json").readText(Charsets.UTF_8)
                )
            } catch (ex: Exception) {
                val columns = 20
                val rows = 20
                LocalMapState(
                    columns = columns,
                    rows = rows,
                    (0 until columns).flatMap { x ->
                        (0 until rows).map { y ->
                            Grass(
                                position = MutableStateFlow(
                                    Position(
                                        x = x,
                                        y = y
                                    )
                                )
                            )
                        }
                    }.plus(
                        listOf(
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 9,
                                        y = 9
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 9,
                                        y = 10
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 9,
                                        y = 11
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 9,
                                        y = 12
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 10,
                                        y = 9
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 11,
                                        y = 9
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 11,
                                        y = 10
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 11,
                                        y = 11
                                    )
                                )
                            ),
                            Wall(
                                position = MutableStateFlow(
                                    Position(
                                        x = 11,
                                        y = 12
                                    )
                                )
                            )
                        )
                    ).plus(
                        Player(
                            position = MutableStateFlow(
                                Position(
                                    x = 10,
                                    y = 3
                                )
                            )
                        )
                    ).plus(
                        listOf(
                            Monster(
                                position = MutableStateFlow(
                                    Position(
                                        x = 10,
                                        y = 10
                                    )
                                )
                            ),
                            Monster(
                                position = MutableStateFlow(
                                    Position(
                                        x = 10,
                                        y = 11
                                    )
                                )
                            ),
                            Monster(
                                position = MutableStateFlow(
                                    Position(
                                        x = 10,
                                        y = 12
                                    )
                                )
                            )
                        )
                    )
                )
            }
        }
    }
}