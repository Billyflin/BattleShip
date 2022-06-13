package com.spm.battleship.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.spm.battleship.data.models.Board
import com.spm.battleship.data.models.Coordinate
import com.spm.battleship.data.models.Player
import com.spm.battleship.data.models.Role
import com.spm.battleship.ui.game.GameActivity.Companion.role
import com.spm.battleship.ui.main.MainActivity.Companion.prefs

class GameViewModel : ViewModel() {

    lateinit var myBoard: Board
    lateinit var opponentBoard: Board

    lateinit var myPlayer: Player
    lateinit var vsPlayer: Player

    var isOpponentBoardEnabled = true

    lateinit var roomName: String
    lateinit var roleName: String



    lateinit var shot: Coordinate
    lateinit var shotRef: DatabaseReference

    lateinit var shotBack: Coordinate
    lateinit var shotBackRef: DatabaseReference

    var score: Int = 0
    lateinit var scoreRef: DatabaseReference

    val refreshMyBoardLiveData = MutableLiveData<Board>()
    val refreshOpponentBoardLiveData = MutableLiveData<Board>()

    fun initReferences() {

//        shotRef =
//
//        shotBackRef =
//
//        scoreRef =
//
//        scoreRef =


        shotRef.setValue(null)
        scoreRef.setValue(null)
        shotBackRef.setValue(null)
    }

    fun sendCoordinate(x: Int, y: Int, roleName: String) {

        val hashMap: HashMap<String, Int> = hashMapOf()

        if (roleName == Role.GUEST.roleName)
            hashMap[role] = Role.GUEST.id
        else
            hashMap[role] = Role.HOST.id

        hashMap[GameActivity.x] = x
        hashMap[GameActivity.y] = y

        shotRef.setValue(hashMap);

    }

    private fun sendCoordinatesBack(coordinate: Coordinate, result: Int) {

        shotBack = coordinate
//        shotBackRef =

        val hashMap: HashMap<String, Int> = hashMapOf()

        if (roleName == Role.GUEST.roleName)
            hashMap[GameActivity.role] = Role.GUEST.id
        else
            hashMap[GameActivity.role] = Role.HOST.id

        hashMap[GameActivity.result] = result
        hashMap[GameActivity.x] = shotBack.x
        hashMap[GameActivity.y] = shotBack.y

        shotBackRef.setValue(hashMap);
    }

    fun sendScore(score: Int, roleName: String, win: Int) {

        val hashMap: HashMap<String, Int> = hashMapOf()

        if (roleName == Role.GUEST.roleName)
            hashMap[GameActivity.role] = Role.HOST.id
        else
            hashMap[GameActivity.role] = Role.GUEST.id

        hashMap[GameActivity.score] = score
        hashMap[GameActivity.win] = win

        scoreRef.setValue(hashMap);
    }

    fun isHost(): Boolean{
//        return roleName ==
        return false
    }

    fun shotCoordinatesReceived(dataSnapshot: DataSnapshot): String {

        val coordinate: Coordinate = getCoordinatesFromSnapshot(dataSnapshot)
        var result: Int = myBoard.fieldStatus[coordinate.x][coordinate.y]

        if (myBoard.canPlaceShot(coordinate)) {
            result = myBoard.placeShot(coordinate)
        }

        refreshMyBoardLiveData.value = myBoard

        sendCoordinatesBack(coordinate, result)

        return myBoard.getShipNameIfKill(coordinate)
    }

    fun getShipPointsIfKill(dataSnapshot: DataSnapshot) : Int {
        return myBoard.getShipPointsIfKill(getCoordinatesFromSnapshot(dataSnapshot))
    }

    private fun getCoordinatesFromSnapshot(dataSnapshot: DataSnapshot) : Coordinate{
        val x: Int = dataSnapshot.child(GameActivity.x).value.toString().toInt()
        val y: Int = dataSnapshot.child(GameActivity.y).value.toString().toInt()
        return Coordinate(x, y)
    }

    fun shotBackCoordinatesReceived(dataSnapshot: DataSnapshot) {

        val coordinate: Coordinate = getCoordinatesFromSnapshot(dataSnapshot)
        val result: Int = dataSnapshot.child("result").value.toString().toInt()

        opponentBoard.fieldStatus[coordinate.x][coordinate.y] = result

        refreshOpponentBoardLiveData.value = opponentBoard
    }

}