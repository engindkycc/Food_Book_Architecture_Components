package com.rickandmorty.foodbook.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
//import com.rickandmorty.foodbook.FoodDetailFragmentArgs
//import androidx.navigation.Navigation
import com.rickandmorty.foodbook.databinding.FragmentFoodDetailBinding
import com.rickandmorty.foodbook.util.imageDownload
import com.rickandmorty.foodbook.util.placeHolderDo
import com.rickandmorty.foodbook.viewmodel.FoodDetailViewModel


class FoodDetailFragment : Fragment() {
    private var foodId = 0
    private lateinit var viewModel : FoodDetailViewModel
    private lateinit var _binding : FragmentFoodDetailBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        _binding = FragmentFoodDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            foodId = FoodDetailFragmentArgs.fromBundle(it).foodId
            //println(foodId)

        }

        viewModel = ViewModelProviders.of(this).get(FoodDetailViewModel::class.java)
        viewModel.getRoomData(foodId)

        observeLiveData()
    }

        fun observeLiveData(){

            viewModel.foodLiveData.observe(viewLifecycleOwner, Observer { food ->

                food?.let {

                    binding.foodNameDetail.text = it.foodName
                    binding.foodCaloriDetail.text = it.foodCalorie
                    binding.foodCarbohydrateDetail.text = it.foodCarbohydrate
                    binding.foodProteinDetail.text = it.foodProtein
                    binding.foodOilDetail.text = it.foodOil
                    context?.let {

                        binding.foodImageDetail.imageDownload(food.foodImage, placeHolderDo(it))
                    }


                }

            })

        }



        /*binding.foodListButton.setOnClickListener {
            val action = FoodDetailFragmentDirections.actionFoodDetailFragmentToFoodListFragment(5)
            Navigation.findNavController(it).navigate(action)
        }*/





}