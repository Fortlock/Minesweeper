package com.example.pas19.minesweeper

class GamePresenter(private var gameView: GameView,private val gameModel : GameModel) : StateListener {
    enum class State {
        Win, Lose, Continues
    }
    var state : State = State.Continues
    fun openCellAt(point: Point){
        gameModel.revealCellAt(point, this)
    }

    override fun onChangeGameField() {
        gameModel.getOpenedCells().forEach { i -> gameView.openCellAt(i.location,
            when (i.type) {
                Cell.Type.Empty -> GameView.CellType.Empty
                Cell.Type.Bomb -> when (state) {
                        State.Continues-> GameView.CellType.Bomb
                        State.Win -> GameView.CellType.Flagged
                        State.Lose -> GameView.CellType.Exploded
                    }
                Cell.Type.Label -> GameView.CellType.Label
            },
            i.label)
        }
    }

    override fun onLose() {
        state = State.Lose
        gameModel.lose()
        gameView.isBlocked=true
        gameView.showLoseMessage()
    }

    override fun onWin() {
        state = State.Win
        gameModel.win()
        gameView.isBlocked = true
        gameView.showWinMessage()
    }

    fun newGame() {
        state = State.Continues
        gameModel.restart()
        buildGameField()
        gameView.isBlocked = false
    }

    fun buildGameField() {
        gameView.buildGameField(gameModel.configuration.size)
    }
}