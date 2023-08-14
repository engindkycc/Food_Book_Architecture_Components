package com.rickandmorty.foodbook.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.rickandmorty.foodbook.model.Food
import com.rickandmorty.foodbook.service.FoodDatabase
import kotlinx.coroutines.launch
import java.util.UUID


class FoodDetailViewModel(application: Application) : BaseViewModel(application){

    val foodLiveData = MutableLiveData<Food>()

    fun getRoomData(uuid: Int){

        /*val muz = Food("Muz","100","10","5" , "1" , "wwww.test.com")
        foodLiveData.value = muz*/

        launch {

            val dao = FoodDatabase(getApplication()).foodDao()
            val food = dao.getFood(uuid)
            foodLiveData.value = food
        }

    }

}