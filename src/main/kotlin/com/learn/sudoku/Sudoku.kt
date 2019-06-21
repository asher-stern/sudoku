package com.learn.sudoku

import java.io.File

typealias Board = Array<Array<Int?>> // Row first. For example, b[1][3] means second row forth column.


fun main(args: Array<String>)
{
    val board = Sudoku.read(args[0])
    println(Sudoku.print(board))
    println(Sudoku.solve(board))

}


object Sudoku
{
    fun read(filename: String): Board
    {
        val board = Array<Array<Int?>>(9) { Array(9) { null } }
        var rowIndex = 0
        File(filename).forEachLine { line ->
            for ( (columnIndex, digitCH) in line.toCharArray().withIndex())
            {
                if (digitCH.isDigit())
                {
                    board[rowIndex][columnIndex] = digitCH.toString().toInt()
                }
            }
            ++rowIndex
        }
        return board
    }

    fun print(board: Board): String
    {
        return board.map { row -> row.map { it?.toString() ?: " " }.joinToString("") }.joinToString("\n")
    }

    fun solve(board: Board): Boolean = solve(board, 0, 0)


    private fun solve(board: Board, row: Int, column: Int): Boolean
    {
        if (board[row][column] == null)
        {
            for (value in 1..9)
            {
                if (!violates(value, row, column, board))
                {
                    board[row][column] = value
                    if ( (row == 8) && (column == 8) )
                    {
                        println(print(board))
                        return true
                    }
                    else
                    {
                        val (nextRow, nextColumn) = next(row, column)
                        if (solve(board, nextRow, nextColumn))
                        {
                            return true
                        }
                    }
                    board[row][column] = null
                }
            }
            return false
        }
        else
        {
            if ( (row == 8) && (column == 8) )
            {
                println(print(board))
                return true
            }
            else
            {
                val (nextRow, nextColumn) = next(row, column)
                return solve(board, nextRow, nextColumn)
            }
        }
    }


    private fun next(row: Int, column: Int): Pair<Int, Int>
    {
        if (column < 8) return Pair(row, column + 1)
        else return Pair(row + 1, 0)
    }



    private fun violates(value: Int, row: Int, column: Int, board: Board): Boolean
    {
        return violatesRow(value, row, column, board) ||
                violatesColumn(value, row, column, board) ||
                violatesSquare(value, row, column, board)
    }

    private fun violatesRow(value: Int, row: Int, column: Int, board: Board): Boolean
    {
        val boardRow = board[row]
        for (i in 0 until 9)
        {
            if (i != column)
            {
                if (boardRow[i] == value)
                {
                    return true
                }
            }
        }
        return false
    }

    private fun violatesColumn(value: Int, row: Int, column: Int, board: Board): Boolean
    {
        for (i in 0 until 9)
        {
            if (i != row)
            {
                if (board[i][column] == value)
                {
                    return true
                }
            }
        }
        return false
    }

    private fun violatesSquare(value: Int, row: Int, column: Int, board: Board): Boolean
    {
        val rowBegin = (row/3)*3
        val columnBegin = (column/3)*3
        for (r in rowBegin until (rowBegin+3))
        {
            for (c in columnBegin until (columnBegin+3))
            {
                if ( (r != row) && (c != column) )
                {
                    if (board[r][c] == value)
                    {
                        return true
                    }
                }
            }
        }
        return false
    }
}