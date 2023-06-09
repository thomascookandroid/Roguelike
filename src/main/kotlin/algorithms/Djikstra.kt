package algorithms

import java.util.LinkedList

const val IMPASSABLE = Int.MAX_VALUE

class Djikstra {
    private data class Position(
        val x: Int,
        val y: Int
    )

    fun floodFill(
        width: Int,
        height: Int,
        goalMatrix: Array<Array<Int>>,
        obstacleMatrix: Array<Array<Int>>
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

        val djikstraGrid = Array(width) {
            Array(height) {
                IMPASSABLE
            }
        }

        val startingPositions = mutableListOf<Position>()
        val frontier = LinkedList<Position>()
        val closedGrid = obstacleMatrix.copyOf()

        goalMatrix.forEachIndexed { x, row ->
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
}