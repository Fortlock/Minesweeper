package com.example.pas19.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.table_field.*
import pl.polidea.view.ZoomView

class GameActivity : AppCompatActivity(), GameView {
    private val presenter = GamePresenter(this, GameModel())
    private lateinit var zoomView : ZoomView
    override var isBlocked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.buildGameField()
    }

    private fun setupView() {
        setContentView(R.layout.activity_game)
        zoomView = ZoomView(this)
        val field = layoutInflater.inflate(R.layout.table_field, null)
        zoomView.addView(field)
        findViewById<ConstraintLayout>(R.id.activity_main).addView(zoomView)
        zoomView.id = ZoomView.generateViewId()
        val set = ConstraintSet()
        set.clone(activity_main)
        set.connect(zoomView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(zoomView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(zoomView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(zoomView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.constrainHeight(zoomView.id, ConstraintSet.MATCH_CONSTRAINT)
        //set.setVerticalBias(zoomView.id, 0.4f)
        set.applyTo(activity_main)
        zoomView.maxZoom = 1f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_new_game -> presenter.newGame()
            R.id.action_beginner -> {presenter.changeLevel(GamePresenter.Level.Beginner); item.isChecked = true; zoomView.maxZoom = 1f}
            R.id.action_intermediate ->{presenter.changeLevel(GamePresenter.Level.Intermediate); item.isChecked = true; zoomView.maxZoom = 1.7f}
            R.id.action_expert -> {presenter.changeLevel(GamePresenter.Level.Expert); item.isChecked = true; zoomView.maxZoom = 3.3f}
        }
        return true

    }

    override fun buildGameField(size : Size) {
        zoomView.zoomTo(1f,0f,0f)
        if (size.height == table_field.childCount && size.width == (table_field.getChildAt(0) as TableRow).childCount)
            clearField()
        else {
            table_field.removeAllViews()
            /*(0..table_field.childCount).map { i -> table_field.removeViewAt(i) }*/
            for (i in 0 until size.height) {
                val row = TableRow(this) // создание строки таблицы
                for (j in 0 until size.width) {
                    val button = CellButton(this)
                    var point = Point(j, i)

                    button.setOnClickListener{onClickCell(point)} // установка слушателя, реагирующего на клик по кнопке
                    row.addView(
                        button, TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 1f
                        )
                    )
                }
                table_field.addView(
                    row, TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        }
    }

    private fun clearField(){
        for (i in 0 until table_field.childCount){
            val row = table_field.getChildAt(i) as TableRow
            for (j in 0 until row.childCount)
                (row.getChildAt(j) as CellButton).open(GameView.CellType.Closed)
        }
    }

    override fun openCellAt(point : Point, type : GameView.CellType, label : Int) {
        ((table_field.getChildAt(point.y) as TableRow).getChildAt(point.x) as CellButton).open(type, label)
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
