package com.spm.battleship.internal

import android.content.Context

class Prefs(val context: Context) {
    val SHARED_NAME = "Datos"
    val SHARED_USER_NAME = "username"
    val SHARED_ID = "ID"
    val SHARED_HOST = "host"
    val storage = context.getSharedPreferences(SHARED_NAME,0)
    fun saveName(name:String){
        storage.edit().putString(SHARED_USER_NAME,name).apply()
    }
    fun saveID(ID:String){
        storage.edit().putString(SHARED_ID,ID).apply()
    }
    fun saveHost(Host:Int){
        storage.edit().putInt(SHARED_HOST,Host).apply()

    }
    fun getID():String{
        return storage.getString(SHARED_ID,"")!!
    }
    fun getUsername():String{
        return storage.getString(SHARED_USER_NAME,"")!!
    }
    fun wipe(){
        storage.edit().clear().apply()
    }

    fun getHost(): Int {
        return storage.getInt(SHARED_HOST,0)
    }
}