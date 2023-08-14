package com.rickandmorty.foodbook.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

import com.rickandmorty.foodbook.databinding.FoodReyclerRowBinding

import com.rickandmorty.foodbook.model.Food
import com.rickandmorty.foodbook.util.imageDownload
import com.rickandmorty.foodbook.util.placeHolderDo
import com.rickandmorty.foodbook.view.FoodListFragmentDirections

class FoodRecyclerAdapter(val foodList : ArrayList<Food>): RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder> () {

    class FoodViewHolder(var view: FoodReyclerRowBinding) : RecyclerView.ViewHolder(view.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(FoodReyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        //val view = DataBindingUtil.setContentView<FoodReyclerRowBinding>(this,R.layout.food_reycler_row)
        //return  view
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        //holder.view.food = foodList[position]
        holder.view.foodNameList.text = foodList[position].foodName
        holder.view.foodCaloriList.text = foodList[position].foodCalorie
        //görsel kısım eklenecek
        holder.view.foodImageList.imageDownload(foodList[position].foodImage, placeHolderDo(holder.itemView.context))

        holder.itemView.setOnClickListener {

            val action = FoodListFragmentDirections.actionFoodListFragmentToFoodDetailFragment(foodList.get(position).uuid)
            Navigation.findNavController(it).navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun foodListUpdate(newFoodList : List<Food>){
        foodList.clear()
        foodList.addAll(newFoodList)
        notifyDataSetChanged()

    }

}