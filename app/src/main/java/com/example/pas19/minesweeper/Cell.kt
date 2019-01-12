package com.example.pas19.minesweeper

data class Cell(val location : Point){
    enum class Type {
        Empty, Bomb, Label
    }
    var type = Type.Empty
    var label = 0

    fun isBomb() : Boolean {
        return type == Type.Bomb
    }

    fun copy() : Cell {
        val res = Cell(this.location)
        res.label = this.label
        res.type = this.type
        return res
    }
}