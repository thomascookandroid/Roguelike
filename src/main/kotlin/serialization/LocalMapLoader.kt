package serialization

import kotlinx.serialization.json.Json
import state.LocalMapState
import java.io.File

class LocalMapLoader {
    fun load() : LocalMapState {
        return Json.decodeFromString(
            File("./src/main/resources/mapstate.json").readText(Charsets.UTF_8)
        )
    }
}