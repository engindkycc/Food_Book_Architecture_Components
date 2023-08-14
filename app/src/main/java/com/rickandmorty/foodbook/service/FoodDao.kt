package com.rickandmorty.foodbook.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rickandmorty.foodbook.model.Food

@Dao
interface FoodDao {

    //Data Access Object
    @Insert
    suspend fun insertAll(vararg food: Food) : List<Long>

    //Insert - > Room , insert into
    //suspend coroutine scope
    //vararg -< birden fazla ve istediğimiz sayıda besim
    //List<Long< long , id ler

    @Query("SELECT * FROM food")
    suspend fun getAllFood() : List<Food>

    @Query("SELECT * FROM food WHERE uuid = :foodId")
    suspend fun getFood(foodId : Int) : Food

    @Query("DELETE FROM food")
    suspend fun deleteAllFood( )

}