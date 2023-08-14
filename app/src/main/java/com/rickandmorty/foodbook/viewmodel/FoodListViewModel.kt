package com.rickandmorty.foodbook.viewmodel

import android.app.Application
import android.widget.Toast

import androidx.lifecycle.MutableLiveData
import com.rickandmorty.foodbook.model.Food
import com.rickandmorty.foodbook.service.FoodAPIService
import com.rickandmorty.foodbook.service.FoodDatabase
import com.rickandmorty.foodbook.util.PrivateSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FoodListViewModel(application: Application) : BaseViewModel(application){
    //Gözlemlenebilir yapılar
    val food = MutableLiveData<List<Food>>()
    val foodErrorMessage = MutableLiveData<Boolean>()
    val foodUploading = MutableLiveData<Boolean>()
    private val updateTime =  10 * 60 * 1000 * 1000 * 1000L

    private val foodAPIService = FoodAPIService()
    private  val disposable = CompositeDisposable()
    private  val privateSharedPreferences = PrivateSharedPreferences(getApplication())


    fun refreshData(){

        /*val muz = Food("Muz","100","10","5" , "1" , "wwww.test.com")
        val cilek = Food("Çilek","200","10","5" , "1" , "wwww.test.com")
        val elma = Food("Elma","300","10","5" , "1" , "wwww.test.com")

        val foodList = arrayListOf<Food>(muz,cilek,elma)

        food.value = foodList
        foodErrorMessage.value = false
        foodUploading.value = false*/

        val saveTime = privateSharedPreferences.timeGet()
        if(saveTime != null && saveTime != 0L && System.nanoTime() - saveTime < updateTime){
            //SQLite tan çek
            getDataFromSqlite()

        }else{

            getDataFromInternet()
        }

    }


    fun refreshFromInternet(){
        getDataFromInternet()
    }



    private  fun getDataFromSqlite(){

        foodUploading.value = true
        launch {

           val foodList = FoodDatabase(getApplication()).foodDao().getAllFood()
           foodShow(foodList)
           Toast.makeText(getApplication(),"Food Get Room",Toast.LENGTH_LONG).show()
        }

    }

    private fun getDataFromInternet(){

        //IO , Default , UI

         foodUploading.value = true
         disposable.add(
             foodAPIService.getData()
                 .subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeWith(object : DisposableSingleObserver<List<Food>>(){
                     override fun onSuccess(t: List<Food>) {
                         //Başarılı olursa
                         sqliteData(t)
                         Toast.makeText(getApplication(),"Food Get Internet",Toast.LENGTH_LONG).show()
                     }

                     override fun onError(e: Throwable) {
                         //Hata Alırsak
                         foodErrorMessage.value = true
                         foodUploading.value =false
                         e.printStackTrace()
                     }

                 })
        )

    }

    private fun foodShow(foodList: List<Food>){

        food.value = foodList
        foodErrorMessage.value = false
        foodUploading.value = false

    }

    private fun sqliteData(foodList: List<Food>){

        launch {

            val dao = FoodDatabase(getApplication()).foodDao()
            dao.deleteAllFood()
            val uuidList =  dao.insertAll(*foodList.toTypedArray())
            var i = 0

            while (i < foodList.size){

                foodList[i].uuid = uuidList[i].toInt()
                i = i + 1
            }

            foodShow(foodList)
        }

        privateSharedPreferences.timeSave(System.nanoTime())
    }

}