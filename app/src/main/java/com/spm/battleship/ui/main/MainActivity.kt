package com.spm.battleship.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.spm.battleship.R
import com.spm.battleship.internal.Prefs
import com.spm.battleship.internal.conection.EC2Source
import com.spm.battleship.internal.conection.SocketClient
import com.spm.battleship.internal.getViewModel
import com.spm.battleship.ui.login.LoginActivity
import com.spm.battleship.ui.rooms.RoomsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var client: SocketClient
        lateinit var prefs: Prefs
        const val ME_PLAYER = "ME_PLAYER"
        const val ROOM_NAME = "ROOM_NAME"
        const val ROLE_NAME = "ROLE_NAME"
        const val VS_PLAYER = "VS_PLAYER"
    }

    private val viewModel by lazy {
        getViewModel { MainViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs=Prefs(applicationContext)
        client= SocketClient(EC2Source.address,EC2Source.port)
        client.run()
        logoutButton.setOnClickListener {
            prefs.wipe()
        }

        selectRoomButton.setOnClickListener {
            val intent = Intent(this, RoomsActivity::class.java)
            intent.putExtra(ME_PLAYER, viewModel.getMyPlayer())
            startActivity(intent)
        }

        getStartedButton.setOnClickListener {
            launchLoginActivity()
        }
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(prefs.getUsername())
    }

    private fun updateUI(currentUser: String?) {

        if (currentUser != null) {

            welcomeView.text = String.format("Welcome - %s", currentUser)
            getStartedButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            selectRoomButton.visibility = View.VISIBLE

        } else {

            getStartedButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            selectRoomButton.visibility = View.GONE
        }
    }
}




