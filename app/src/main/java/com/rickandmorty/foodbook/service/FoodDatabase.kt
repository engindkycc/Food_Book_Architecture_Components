package com.rickandmorty.foodbook.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rickandmorty.foodbook.model.Food

@Database(entities = [Food::class], version = 1)
abstract class FoodDatabase :  RoomDatabase()  {

    abstract fun  foodDao() : FoodDao

    //Singleton
    companion object{

        @Volatile private var instance : FoodDatabase? = null
        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock){
                instance ?: databaseCreate(context).also {

                    instance = it
                }
        }

        private fun databaseCreate(context : Context) = Room.databaseBuilder(context.applicationContext,
            FoodDatabase::class.java,
            "fooddatabase").build()

    }


}