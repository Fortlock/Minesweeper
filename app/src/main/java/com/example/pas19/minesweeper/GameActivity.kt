package com.example.pas19.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_game.*
import pl.polidea.view.ZoomView

class GameActivity : AppCompatActivity(), GameView {
    private val presenter = GamePresenter(this, GameModel())
    override var isBlocked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        presenter.buildGameField()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_new_game -> presenter.newGame()
        }
        return true
    }

    override fun buildGameField(size : Size) {

        /*val fieldTable = layoutInflater.inflate(R.layout.table_field, null)*/
        if (size.height == fieldTable.childCount && size.width == (fieldTable.getChildAt(0) as TableRow).childCount)
            clearField()
        else {
            for (i in 0 until size.height) {
                val row = TableRow(this) // создание строки таблицы
                for (j in 0 until size.width) {
                    val button = CellButton(this)
                    var point = Point(j,i)

                    button.setOnClickListener{onClickCell(point)} // установка слушателя, реагирующего на клик по кнопке
                    row.addView(
                        button, TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 1f
                        )
                    ) // добавление кнопки в строку таблицы
                }
                fieldTable.addView(
                    row, TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                ) // добавление строки в таблицу
            }
        }
        /*val zoomView = ZoomView(this)
        zoomView.addView(fieldTable)*/

    }

    private fun clearField(){
        for (i in 0 until fieldTable.childCount){
            val row = fieldTable.getChildAt(i) as TableRow
            for (j in 0 until row.childCount)
                (row.getChildAt(j) as CellButton).open(GameView.CellType.Closed)
        }
    }

    override fun openCellAt(point : Point, type : GameView.CellType, label : Int){
        ((fieldTable.getChildAt(point.y) as TableRow).getChildAt(point.x) as CellButton).open(type, label)
    }

    private fun onClickCell(point: Point) {
        if (!isBlocked)
            presenter.openCellAt(point)
    }

    override fun showLoseMessage() {

        Toast.makeText(this, R.string.you_lose, Toast.LENGTH_LONG).show()
    }

    override fun showWinMessage() {
        Toast.makeText(this, R.string.you_win, Toast.LENGTH_LONG).show()
    }
}
