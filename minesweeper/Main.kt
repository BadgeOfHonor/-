package minesweeper
import kotlin.random.Random

class BattleField (_vertical_Field_Size: Int = 9, _horizontal_Field_Size: Int = 9) {

    val verticalFieldSize: Int
        init {
            verticalFieldSize = _vertical_Field_Size
        }

    val horizontalFieldSize: Int
        init {
            horizontalFieldSize = _horizontal_Field_Size
        }

    var `mine-position`: String = "X"

    var `free-position`: String = "."

    var `select-position`: String = "*"

    var `free-post-position`: String = "/"

    var numMines: Int = 0
        set(Value){
            val maxmines = verticalFieldSize * horizontalFieldSize - 1
            field = if ( Value > maxmines) maxmines else Value
        }
        init {
            print("How many mines do you want on the field? > ")
            numMines = readln().toInt()
        }

    var numFreePosition: Int = verticalFieldSize * horizontalFieldSize - numMines

    var numMinePosition: Int = 0

    var isSteppedOnMine: Boolean = false

    var mineFieldList: MutableList<Pair<Int, Int>>
        init { mineFieldList = emptyList<Pair<Int, Int>>().toMutableList() }

    var battleField: MutableList<MutableList<String>>
        init { battleField = emptyList<MutableList<String>>().toMutableList() }

    var firstCoordinates: Pair<Int, Int> = Pair(0, 0)
        set(value) {
            field = value
            val _mineField = setMinesField()
            battleField = setHintMine(_mineField)
            mineFieldList = setMineList(battleField)
        }

    val gameBattleField: MutableList<MutableList<String>>
        init { gameBattleField = setField() }

    fun setField(
            vertical: Int = verticalFieldSize,
            horizontal: Int = horizontalFieldSize,
            freePosition: String = `free-position`
    ): MutableList<MutableList<String>> {
        val freeFieldListNaked = MutableList(vertical + 2) { MutableList(horizontal + 2) { freePosition } }
        for (i in freeFieldListNaked.indices) {
            if (i == 0 || i == freeFieldListNaked.size - 1) {
                for (j in freeFieldListNaked[i].indices) {
                    if (j == 0 || j == freeFieldListNaked[i].size - 1) freeFieldListNaked[i][j] = "|"
                    else freeFieldListNaked[i][j] = "-"
                }
            } else {
                freeFieldListNaked[i][0] = "|"
                freeFieldListNaked[i][freeFieldListNaked[i].size - 1] = "|"
            }
        }
        return freeFieldListNaked
    }

    fun fieldToList (
            _freeFieldNaked: MutableList<MutableList<String>>,
    ): MutableList<Pair<Int, Int>> {
        val list: MutableList<Pair<Int, Int>> = emptyList<Pair<Int, Int>>().toMutableList()
        for (i in 1 until _freeFieldNaked.size - 1) {
            for (j in 1 until _freeFieldNaked[i].size - 1) {
                list.add(Pair(i, j))
            }
        }
        return list
    }

    fun listToField (
            _mineField: MutableList<MutableList<String>>,
            _listMineField: MutableList<Pair<Int, Int>>,
            _minePosition: String = `mine-position`,
            ) {
        for (i in _listMineField) {
            _mineField[i.first][i.second] = _minePosition
        }
    }

    fun setMinesField(
            _coordinates: Pair<Int, Int> = firstCoordinates,
            _numMines: Int = numMines,
    ): MutableList<MutableList<String>> {
        var mineField = setField()
        val listCoordinates = fieldToList(mineField)
        listCoordinates.remove(_coordinates)
        val listMineField:MutableList<Pair<Int, Int>> = emptyList<Pair<Int, Int>>().toMutableList()
        for (i in 1.. _numMines) {
            val selectIndex = Random.Default.nextInt(0, listCoordinates.size)
            val coordinate = listCoordinates.removeAt(selectIndex)
            listMineField.add(coordinate)
        }
        listToField(mineField, listMineField)
        return mineField
    }

    fun setHintMine(
            _minefieldList: MutableList<MutableList<String>>,
            minePosition: String = `mine-position`
    ): MutableList<MutableList<String>> {
        for (i in 1 until _minefieldList.size - 1) {
            for (j in 1 until _minefieldList[i].size - 1) {
                if (_minefieldList[i][j] != minePosition) {
                    val mineAroundList = mutableListOf<String>()
                    val ikmin = if (i - 1 in 1 until _minefieldList.size) i - 1 else i
                    val ikmax = if (i + 1 in 1 until _minefieldList.size) i + 1 else i
                    val jlmin = if (j - 1 in 1 until _minefieldList[i].size) j - 1 else j
                    val jlmax = if (j + 1 in 1 until _minefieldList[i].size) j + 1 else j
                    for (k in ikmin..ikmax) {
                        for (l in jlmin..jlmax) {
                            mineAroundList += _minefieldList[k][l]
                        }
                    }
                    val sumMine = mineAroundList.count { it == minePosition }
                    if (sumMine > 0) _minefieldList[i][j] = sumMine.toString()
                }
            }
        }
        return _minefieldList
    }

    fun setMineList(
            _BattleField: MutableList<MutableList<String>> = battleField,
            _mineFieldList: MutableList<Pair<Int, Int>> = mineFieldList,
            _minePosition: String = `mine-position`,
    ): MutableList<Pair<Int, Int>> {
        for (i in 1 until _BattleField.size - 1) {
            for (j in 1 until _BattleField[i].size - 1) {
                if (_BattleField[i][j] == _minePosition) {
                    _mineFieldList.add(Pair(i, j))
                }
            }
        }
        return _mineFieldList
    }

    fun setMineGameBattleField(
            _gameBattleField: MutableList<MutableList<String>> = gameBattleField,
            _mineFieldList: MutableList<Pair<Int, Int>> = mineFieldList,
            _minePosition: String = `mine-position`,
    ): MutableList<MutableList<String>> {
        for (i in _mineFieldList) {
            _gameBattleField[i.first][i.second] = _minePosition
                }
        return _gameBattleField
    }

    fun printField (minefieldList: MutableList<MutableList<String>> = gameBattleField) {
        val minefieldListHead = MutableList<String>(minefieldList[0].size + 1) {" "}
        for (i in minefieldListHead.indices) {
            minefieldListHead[i] = if (i == 0) " " else if (i == 1 || i == minefieldListHead.size - 1) "|" else {
                (i - 1).toString()
            }
        }
        println(minefieldListHead.joinToString(""))
        for (i in minefieldList.indices) {
            println(if (i == 0 || i == minefieldList.size - 1) "-" else {
                i.toString()
            } + minefieldList[i].joinToString(""))
        }
    }

    fun checkAround (
            _coordinatesPosition: Pair<Int, Int>,
            _battleField: MutableList<MutableList<String>> = battleField,
            _gameBattleField: MutableList<MutableList<String>> = gameBattleField,
            _freePostPosition: String = `free-post-position`,
    ) {
        _gameBattleField[_coordinatesPosition.first][_coordinatesPosition.second] = _freePostPosition
        numFreePosition--
        val aroundPosition: MutableList<Pair<Int, Int>> = mutableListOf(_coordinatesPosition)

        do {
            val nextPosition = aroundPosition.removeAt(0)
            val h = nextPosition.second
            val v = nextPosition.first
            val around: MutableList<Pair<Int, Int>> = mutableListOf(
                    Pair(v - 1, h), Pair(v - 1, h + 1), Pair(v - 1, h - 1),
                    Pair(v, h + 1), Pair(v, h - 1),
                    Pair(v + 1, h + 1), Pair(v + 1, h), Pair(v + 1, h - 1))
            for (i in around) {
                when (_battleField[i.first][i.second]) {
                    "1", "2", "3", "4", "5", "6", "7", "8" -> {
                        val position = _gameBattleField[i.first][i.second]
                        if (position == `free-position` || position == `select-position`) {
                        _gameBattleField[i.first][i.second] = _battleField[i.first][i.second]
                        numFreePosition--
                        }
                    }
                    "-", "|" -> {
                        continue
                    }
                    else -> {
                        if (_gameBattleField[i.first][i.second] != _freePostPosition) {
                            _gameBattleField[i.first][i.second] = _freePostPosition
                            aroundPosition.add(Pair(i.first, i.second))
                            numFreePosition--
                        }
                    }
                }
            }
        } while (aroundPosition.any())
    }

    fun statusPositionCheckFree(
            _coordinatesPosition: Pair<Int, Int>,
            _battleField: MutableList<MutableList<String>> = battleField,
            _gameBattleField: MutableList<MutableList<String>> = gameBattleField,
            _mineFieldList: MutableList<Pair<Int, Int>> = mineFieldList,
            _minePosition: String = `mine-position`,
            _freePostPosition: String = `free-post-position`
    ) {
        val h = _coordinatesPosition.second
        val v = _coordinatesPosition.first
        when (_battleField[v][h]) {
            "1", "2", "3", "4", "5", "6", "7", "8" -> {
                _gameBattleField[v][h] = _battleField[v][h]
                numFreePosition--
            }
            "-", "|" -> {
                println("Outside the boundaries of the field!")
            }
            _minePosition -> {
                setMineGameBattleField(_gameBattleField, _mineFieldList, _minePosition)
                isSteppedOnMine = true
            }
            else -> {
                if (_gameBattleField[v][h] == _freePostPosition) println("This position already post free!")
                else {
                    checkAround(_coordinatesPosition)
                }
            }
        }
    }

    fun statusPositionCheckMine(
            _coordinatesPosition: Pair<Int, Int>,
            _gameBattleField: MutableList<MutableList<String>> = gameBattleField,
            _mineFieldList: MutableList<Pair<Int, Int>> = mineFieldList,
            _freePosition: String = `free-position`,
            _selectPosition: String = `select-position`,
    ) {
        val h = _coordinatesPosition.second
        val v = _coordinatesPosition.first
        when (_gameBattleField[v][h]) {
                "1", "2", "3", "4", "5", "6", "7", "8" -> {
                    println("There is a number here!")
                }
                "-", "|" -> {
                    println("Outside the boundaries of the field!")
                }
                _selectPosition -> {
                    if (_coordinatesPosition in _mineFieldList) numMinePosition--
                    gameBattleField[v][h] = _freePosition
                }
                else -> {
                    if (_coordinatesPosition in _mineFieldList) numMinePosition++
                    gameBattleField[v][h] = _selectPosition
                }
        }
    }

    fun play (
            gameBattleField: MutableList<MutableList<String>> = this. gameBattleField,
    ) {
        while (numMinePosition != numMines && numFreePosition != 0 && !isSteppedOnMine) {
            println()
            printField(gameBattleField)
            print("Set/unset mine marks or claim a cell as free: ")
            val (x, y, z) = readln().split(" ")
            val v = y.toInt()
            val h = x.toInt()
            if (firstCoordinates == Pair(0, 0)) firstCoordinates = Pair(v, h)
            val coordinatesPosition: Pair<Int, Int> = Pair(v, h)
            when (z) {
                "free" -> {
                    statusPositionCheckFree(coordinatesPosition)
                }
                "mine" -> {
                    statusPositionCheckMine(coordinatesPosition)
                }
            }
        }
        println()
        printField(gameBattleField)
        if (isSteppedOnMine) println("You stepped on a mine and failed!")
        else println("Congratulations! You found all the mines!")
    }
}

fun main() {
    val minebattel = BattleField()
    minebattel.play()
}
