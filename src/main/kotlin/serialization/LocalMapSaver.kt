package serialization

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import state.LocalMapState
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Exception

class LocalMapSaver {
    fun save(localMapState: LocalMapState) {
        try {
            PrintWriter(
                FileWriter(
                    "./src/main/resources/mapstate.json"
                )
            ).use { printWriter ->
                printWriter.write(
                    Json.encodeToString(localMapState)
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}