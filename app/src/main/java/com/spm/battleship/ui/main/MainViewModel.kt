package com.spm.battleship.ui.main

import androidx.lifecycle.ViewModel
import com.spm.battleship.data.models.Player
import com.spm.battleship.ui.main.MainActivity.Companion.prefs

class MainViewModel : ViewModel() {

    fun getMyPlayer(): Player {
        val currentUser = prefs.getUsername()
        return Player(currentUser, 0)
    }
}