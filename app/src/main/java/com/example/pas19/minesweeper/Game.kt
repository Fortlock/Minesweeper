package com.example.pas19.minesweeper

import kotlin.collections.ArrayList

data class Game(private var field : Field, private val initialLocation : Point, private val bombsCount : Int) {

    constructor(size : Size, initialLocation : Point , bombsCount : Int) : this(Field(size),initialLocation, bombsCount){
        putBombs()
        addLabels()
    }

    var openedCells = ArrayList<Cell>()
        private set

    private fun generateBombsLocations() : ArrayList<Point>{
        val field = this.field.copy()
        field[initialLocation] = null
        return (0 until bombsCount).mapNotNull { field.cells.pickRandomElement()?.location }.toCollection(ArrayList())
    }

    private fun putBombs() {
        for(location in generateBombsLocations()){
            field[location]?.type = Cell.Type.Bomb
        }
    }

    private fun addLabels() {
        for (cell in field.cells)
            if (cell.type == Cell.Type.Bomb) {
                val neighbours = field.getNeighbourCells(cell)
                for(neighbour in neighbours)
                    if (neighbour.type !=Cell.Type.Bomb) {
                    neighbour.type = Cell.Type.Label
                    neighbour.label++
                 }
            }
    }

    fun revealCellAt(point: Point) : Cell.Type {
        val crrCell = field[point] ?: return Cell.Type.Empty

        if (!openedCells.contains(crrCell))
            openedCells.add(crrCell)

        if(crrCell.type == Cell.Type.Empty) {
            val neighbours = field.getNeighbourCells(crrCell)
            for(neighbour in neighbours) {
                if(!openedCells.contains(neighbour) && neighbour.type!=Cell.Type.Bomb)
                    revealCellAt(neighbour.location)
            }
        }
        return crrCell.type
    }

    fun revealBombs() {
        for(cell in field.cells)
            if (cell.isBomb() && !openedCells.contains(cell))
                openedCells.add(cell)
    }

}