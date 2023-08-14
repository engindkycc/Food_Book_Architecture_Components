package com.rickandmorty.foodbook.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class PrivateSharedPreferences {

    companion object{
        private val TIME = "time"
        private  var sharedPreferences : SharedPreferences? = null

        @Volatile private  var instance : PrivateSharedPreferences? = null
        private val lock = Any()
        @OptIn(InternalCoroutinesApi::class)
        operator fun invoke(context: Context) : PrivateSharedPreferences = instance ?: synchronized(lock){

            instance ?:  privateSharedPreferencesMake(context).also {

                instance = it
            }
        }

        private  fun privateSharedPreferencesMake(context: Context) : PrivateSharedPreferences{

            sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return PrivateSharedPreferences()
        }
    }

    fun timeSave(time : Long){
        sharedPreferences?.edit(commit = true){
            putLong(TIME,time)
        }
    }

    fun timeGet() = sharedPreferences?.getLong(TIME,0)
}