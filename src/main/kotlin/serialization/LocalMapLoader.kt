package serialization

import entities.Grass
import entities.Monster
import entities.Player
import entities.Wall
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
                                x = x,
                                y = y
                            )
                        }
                    }.plus(
                        listOf(
                            Wall(
                                x = 9,
                                y = 9
                            ),
                            Wall(
                                x = 9,
                                y = 10
                            ),
                            Wall(
                                x = 9,
                                y = 11
                            ),
                            Wall(
                                x = 9,
                                y = 12
                            ),
                            Wall(
                                x = 10,
                                y = 9
                            ),
                            Wall(
                                x = 11,
                                y = 9
                            ),
                            Wall(
                                x = 11,
                                y = 10
                            ),
                            Wall(
                                x = 11,
                                y = 11
                            ),
                            Wall(
                                x = 11,
                                y = 12
                            )
                        )
                    ).plus(
                        Player(
                            x = 10,
                            y = 3
                        )
                    ).plus(
                        listOf(
                            Monster(
                                x = 10,
                                y = 10
                            ),
                            Monster(
                                x = 10,
                                y = 11
                            ),
                            Monster(
                                x = 10,
                                y = 12
                            )
                        )
                    )
                )
            }
        }
    }
}