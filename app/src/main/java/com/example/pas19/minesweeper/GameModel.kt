package com.example.pas19.minesweeper

import kotlin.collections.ArrayList

class GameModel {
    data class Configuration(val size : Size, private var bombsCnt: Int) {
        val bombsCount
            get() = bombsCnt
        init {if(bombsCnt >= size.area) bombsCnt = size.area - 1}
        companion object {
            val Beginer = Configuration(Size(9,9), 10)
            val Intermediate = Configuration(Size(16,16), 40)
            val Expert = Configuration(Size(31,16), 99)
        }
    }
    private var game : Game? = null

    var configuration = Configuration.Beginer
    set(value) {
        configuration = value
        restart()
    }

    fun revealCellAt(point: Point, stateListener: StateListener) {
        if(game == null) {
            game = Game(configuration.size, point, configuration.bombsCount)
        }
        if(game?.revealCellAt(point)==Cell.Type.Bomb) {
            stateListener.onLose()
        }
        if(game?.openedCells?.count() == configuration.size.area - configuration.bombsCount)
            stateListener.onWin()

        stateListener.onChangeGameField()
    }

    fun restart() {
        game = null
    }

    fun lose() {
        game?.revealBombs()
    }

    fun win() {
        game?.revealBombs()
    }

    fun getOpenedCells() : ArrayList<Cell> {
        val res = ArrayList<Cell>()
        game?.openedCells?.forEach { i -> res.add(i.copy()) }
        return res
    }

}