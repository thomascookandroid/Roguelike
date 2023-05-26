package algorithms

import entities.Position
import java.util.LinkedList

class Djikstra {

    fun floodFill(
        width: Int,
        height: Int,
        goals: List<Position>,
        obstacles: List<Position>
    ) : Array<Array<Int>>{
        fun getNeighbours(
            closedGrid: Array<Array<Int>>,
            x: Int,
            y: Int
        ) : List<Position> {
            val neighbours = mutableListOf<Position>()
            (x - 1..x + 1).forEach { fx ->
                (y - 1..y + 1).forEach { fy ->
                    if (fx in (0 until width) && fy in (0 until height) && closedGrid[fx][fy] == 0) {
                        closedGrid[fx][fy] = 1
                        neighbours.add(Position(fx, fy))
                    }
                }
            }
            return neighbours
        }

        val goalGrid = createGrid(width, height, goals, false)
        val obstacleGrid = createGrid(width, height, obstacles, true)

        val djikstraGrid = Array(width) { rows ->
            Array(height) { cell ->
                Int.MAX_VALUE
            }
        }

        val startingPositions = mutableListOf<Position>()
        val frontier = LinkedList<Position>()
        val closedGrid = obstacleGrid.copyOf()

        goalGrid.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                if (cell == 0) {
                    djikstraGrid[x][y] = 0
                    closedGrid[x][y] = 1
                    startingPositions.add(Position(x, y))
                }
            }
        }

        startingPositions.forEach { (x, y) ->
            frontier.addAll(
                getNeighbours(closedGrid, x, y)
            )
        }

        var cost = 1
        while (frontier.isNotEmpty()) {
            val frontierNeighbours = mutableListOf<Position>()
            while (frontier.isNotEmpty()) {
                val position = frontier.poll()
                djikstraGrid[position.x][position.y] = cost
                frontierNeighbours.addAll(getNeighbours(closedGrid, position.x, position.y))
            }
            frontier.addAll(frontierNeighbours)
            cost += 1
        }

        return djikstraGrid
    }

    private fun createGrid(
        width: Int,
        height: Int,
        positions: List<Position>,
        isObstacles: Boolean
    ) : Array<Array<Int>> {
        val maskPositionMap = positions.associateWith {
            if (isObstacles) 1 else 0
        }
        val inputGrid = Array(width) { x ->
            Array(height) { y ->
                maskPositionMap[Position(x, y)] ?: if (isObstacles) 0 else 1
            }
        }
        return inputGrid
    }
}