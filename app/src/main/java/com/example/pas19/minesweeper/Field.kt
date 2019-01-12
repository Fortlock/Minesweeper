package com.example.pas19.minesweeper

data class Generator(private val width: Int) {
    fun generate(index: Int): Cell {
        val point = pointForIndex(index)
        return Cell(point)
    }

    private fun pointForIndex(index : Int) : Point{
        return Point(index % width, index / width)
    }

    fun indexForPoint(point : Point) : Int{
        return point.y * width + point.x
    }
}

data class Field(private val size : Size) {
    private val generator : Generator = Generator(size.width)
    var cells: ArrayList<Cell> = arrayListOf()

    init {
        (0 until size.area).map { i -> generator.generate(i) }.toCollection(this.cells)
    }

    private fun isAvailable(point : Point) : Boolean {
        val index = generator.indexForPoint(point)
        return (0 until size.area).contains(index)
    }

    operator fun get(point: Point) : Cell? {
        if(!isAvailable(point))
            return null
        return cells[generator.indexForPoint(point)]
    }

    operator fun set(point : Point, newValue : Cell?) {
        if(!isAvailable(point))
            return
        val index = generator.indexForPoint(point)
        if (newValue != null )
            cells[index] = newValue
        else
            cells.removeAt(index)
    }

    fun getNeighbourCells(cell: Cell): ArrayList<Cell> {
        val neighbours = ArrayList<Cell>()
        for(i in -1..1) {
            for(j in -1..1) {
                val supposedPoint = Point(cell.location.x + i, cell.location.y + j)

                if(!(0 until size.width).contains(supposedPoint.x) ||
                    !(0 until size.height).contains(supposedPoint.y))
                    continue

                val supposedNeighbour = this[supposedPoint]

                if( supposedNeighbour == null || supposedNeighbour.location == cell.location)
                    continue

                neighbours.add(supposedNeighbour)
            }
        }
        return  neighbours
    }
}

