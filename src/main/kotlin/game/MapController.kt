package game

import entities.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

class MapController {

    private val mapGrid = MapGrid(20, 20)
    private val entitiesTurnQueue = TurnQueue()
    private val _entities = mutableListOf<Entity>()

    private val scope = CoroutineScope(Dispatchers.IO)

    val entities: List<Entity>
        get() = _entities.toList()

    init {
        loadMap()
    }

    private fun loadMap() {
        (0 until 20).forEach { x ->
            (0 until 20).forEach { y ->
                val grassEntity = Grass(
                    position = MutableStateFlow(
                        Position(
                            x = x,
                            y = y
                        )
                    )
                )
                mapGrid.trackEntity(grassEntity)
                _entities.add(grassEntity)
            }
        }

        val playerEntity = Player(
            position = MutableStateFlow(
                Position(
                    x = 10,
                    y = 10
                ),
            ),
            mapGrid = mapGrid
        )

        mapGrid.trackEntity(playerEntity)
        entitiesTurnQueue.add(playerEntity)
        _entities.add(playerEntity)

        val monsterEntity = Monster(
            position = MutableStateFlow(
                Position(
                    x = 9,
                    y = 9
                )
            ),
            mapGrid = mapGrid
        )


        mapGrid.trackEntity(monsterEntity)
        entitiesTurnQueue.add(monsterEntity)
        _entities.add(monsterEntity)
    }

    fun start(
        render: () -> Unit
    ) {
        scope.launch {
            while (entitiesTurnQueue.isNotEmpty() && isActive) {
                val dequeued = entitiesTurnQueue.poll()
                dequeued.getAction().run()
                entitiesTurnQueue.add(dequeued)
                render()
                delay(200)
            }
        }
    }
}